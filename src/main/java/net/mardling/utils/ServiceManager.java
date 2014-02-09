package net.mardling.utils;

import java.io.*;
import java.net.URI;
import java.net.URL;

import javax.net.ssl.*;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.util.encoders.Base64;
import org.springframework.http.HttpMethod;
import org.springframework.http.client.ClientHttpRequest;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;
import org.w3c.dom.*;
import org.xml.sax.SAXException;

import java.security.*;

public class ServiceManager {

	// holds the name of the store which will be used to build the output
	private String outStore;
	// holds the name of the publishSettingsFile
	private String publishSettingsFile;
	// The value of the subscription id that is being used
	private String subscriptionId;
	// the name of the cloud service to check for
	private String name;

	/*
	 * Used to create the PKCS#12 store - important to note that the store is
	 * created on the fly so is in fact passwordless - the JSSE fails with
	 * masqueraded exceptions so the BC provider is used instead - since the
	 * PKCS#12 import structure does not have a password it has to be done this
	 * way otherwise BC can be used to load the cert into a keystore in advance
	 * and password
	 */
	private KeyStore createKeyStorePKCS12(String base64Certificate)
			throws Exception {
		Security.addProvider(new BouncyCastleProvider());
		KeyStore store = KeyStore.getInstance("PKCS12",
				BouncyCastleProvider.PROVIDER_NAME);
		store.load(null, null);

		// read in the value of the base 64 cert without a password (PBE can be
		// applied afterwards if this is needed
		InputStream sslInputStream = new ByteArrayInputStream(
				Base64.decode(base64Certificate));
		store.load(sslInputStream, "".toCharArray());

		// we need to a create a physical keystore as well here
		OutputStream out = new FileOutputStream(getOutStore());
		store.store(out, "".toCharArray());
		out.close();
		return store;
	}
	
	
	private void restTemplate() {
		RestTemplate rest = new RestTemplate();
	}
	

	/*
	 * Used to get an SSL factory from the keystore on the fly - this is then
	 * used in the request to the service management which will match the
	 * .publishsettings imported certificate
	 */
	private SSLSocketFactory getFactory(String base64Certificate)
			throws Exception {
		KeyManagerFactory keyManagerFactory = KeyManagerFactory
				.getInstance("SunX509");
		KeyStore keyStore = createKeyStorePKCS12(base64Certificate);

		// gets the TLS context so that it can use client certs attached to the
		SSLContext context = SSLContext.getInstance("TLS");
		keyManagerFactory.init(keyStore, "".toCharArray());
		context.init(keyManagerFactory.getKeyManagers(), null, null);

		return context.getSocketFactory();
	}

	public static String doCall(ServiceManager manager) {
		String output = "";
		try {
			// manager.parseArgs(args);
			manager.setPublishSettingsFile("src/main/resources/AzureCredentials.xml");
			manager.setOutStore("output.txt");
			manager.setSubscriptionId("72e280a7-f53d-4199-be45-3063afec8240");
			manager.setName("Testjkfdlareqrewqrew");
			// Step 1: Read in the .publishsettings file

			File file = new File(manager.getPublishSettingsFile());
			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
			DocumentBuilder db = dbf.newDocumentBuilder();
			Document doc = db.parse(file);
			doc.getDocumentElement().normalize();
			// Step 2: Get the PublishProfile
			NodeList ndPublishProfile = doc
					.getElementsByTagName("PublishProfile");
			Element publishProfileElement = (Element) ndPublishProfile.item(0);
			// Step 3: Get the PublishProfile
			String certificate = publishProfileElement
					.getAttribute("ManagementCertificate");
			System.out.println("Base 64 cert value: " + certificate);
			// Step 4: Load certificate into keystore
			SSLSocketFactory factory = manager.getFactory(certificate);
			// Step 5: Make HTTP request -
			// https://management.core.windows.net/[subscriptionid]/services/hostedservices/operations/isavailable/javacloudservicetest
			// URL url = new URL("https://management.core.windows.net/" +
			// manager.getSubscriptionId() +
			// "/services/hostedservices/operations/isavailable/" +
			// manager.getName());
			URL url = new URL("https://management.core.windows.net/"
					+ manager.getSubscriptionId() + "/operatingsystems");
			System.out.println("Service Management request: " + url.toString());
			HttpsURLConnection connection = (HttpsURLConnection) url
					.openConnection();
			// Step 6: Add certificate to request
			connection.setSSLSocketFactory(factory);
			// Step 7: Generate response
			connection.setRequestMethod("GET");
			connection.setRequestProperty("x-ms-version", "2012-03-01");
			int responseCode = connection.getResponseCode();
			// response code should be a 200 OK - other likely code is a 403
			// forbidden if the certificate has not been added to the
			// subscription for any reason
			InputStream responseStream = null;
			if (responseCode == 200) {
				responseStream = connection.getInputStream();
			} else {
				responseStream = connection.getErrorStream();
			}
			BufferedReader buffer = new BufferedReader(new InputStreamReader(
					responseStream));
			// response will come back on a single line
			String inputLine = buffer.readLine();
			buffer.close();
			// get the availability flag
			// boolean availability =
			// manager.parseAvailablilityResponse(inputLine);
			// System.out.println("The name " + manager.getName() +
			// " is available: " + availability);
			output = inputLine;
		} catch (Exception ex) {
			System.out.println(ex.getMessage());
		} finally {
			manager.deleteOutStoreFile();
		}

		return output;
	}

	/*
	 * <AvailabilityResponse xmlns="http://schemas.microsoft.com/windowsazure"
	 * xmlns:i="http://www.w3.org/2001/XMLSchema-instance">
	 * <Result>true</Result> </AvailabilityResponse> Parses the value of the
	 * result from the returning XML
	 */
	private boolean parseAvailablilityResponse(String response)
			throws ParserConfigurationException, SAXException, IOException {
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		DocumentBuilder db = dbf.newDocumentBuilder();

		// read this into an input stream first and then load into xml document
		@SuppressWarnings("deprecation")
		StringBufferInputStream stream = new StringBufferInputStream(response);
		Document doc = db.parse(stream);
		doc.getDocumentElement().normalize();
		// pull the value from the Result and get the text content
		NodeList nodeResult = doc.getElementsByTagName("Result");
		Element elementResult = (Element) nodeResult.item(0);
		// use the text value to return a boolean value
		return Boolean.parseBoolean(elementResult.getTextContent());
	}

	private Document parseResponse(String response)
			throws ParserConfigurationException, SAXException, IOException {
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		DocumentBuilder db = dbf.newDocumentBuilder();

		// read this into an input stream first and then load into xml document
		@SuppressWarnings("deprecation")
		StringBufferInputStream stream = new StringBufferInputStream(response);
		Document doc = db.parse(stream);
		doc.getDocumentElement().normalize();

		return doc;
	}

	// Parses the string arguments into the class to set the details for the
	// request
	private void parseArgs(String args[]) throws Exception {
		String usage = "Usage: ServiceManager -ps [.publishsettings file] -store [out file store] -subscription [subscription id] -name [name]";
		if (args.length != 8)
			throw new Exception("Invalid number of arguments:\n" + usage);
		for (int i = 0; i < args.length; i++) {
			switch (args[i]) {
			case "-store":
				setOutStore(args[i + 1]);
				break;
			case "-ps":
				setPublishSettingsFile(args[i + 1]);
				break;
			case "-subscription":
				setSubscriptionId(args[i + 1]);
				break;
			case "-name":
				setName(args[i + 1]);
				break;
			}
		}
		// make sure that all of the details are present before we begin the
		// request
		if (getOutStore() == null || getPublishSettingsFile() == null
				|| getSubscriptionId() == null || getName() == null)
			throw new Exception("Missing values\n" + usage);
	}

	// gets the name of the java keystore
	public String getOutStore() {
		return outStore;
	}

	// sets the name of the java keystore
	public void setOutStore(String outStore) {
		this.outStore = outStore;
	}

	// gets the name of the publishsettings file
	public String getPublishSettingsFile() {
		return publishSettingsFile;
	}

	// sets the name of the java publishsettings file
	public void setPublishSettingsFile(String publishSettingsFile) {
		this.publishSettingsFile = publishSettingsFile;
	}

	// get the value of the subscription id
	public String getSubscriptionId() {
		return subscriptionId;
	}

	// sets the value of the subscription id
	public void setSubscriptionId(String subscriptionId) {
		this.subscriptionId = subscriptionId;
	}

	// get the value of the subscription id
	public String getName() {
		return name;
	}

	// sets the value of the subscription id
	public void setName(String name) {
		this.name = name;
	}

	// deletes the outstore keystore when it has finished with it
	private void deleteOutStoreFile() {
		// the file will exist if we reach this point
		try {
			java.io.File file = new java.io.File(getOutStore());
			file.delete();
		} catch (Exception ex) {
		}
	}


}
