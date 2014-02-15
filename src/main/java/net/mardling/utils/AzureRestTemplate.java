package net.mardling.utils;

import java.io.*;
import java.net.URI;
import java.net.URISyntaxException;

import javax.net.ssl.*;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import net.mardling.azure.OperatingSystem;
import net.mardling.azure.OperatingSystems;
import net.mardling.azure.Subscription;

import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.HttpClients;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.util.encoders.Base64;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.apache.http.conn.ssl.AllowAllHostnameVerifier;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.X509HostnameVerifier;
import org.springframework.web.client.RestTemplate;
import org.w3c.dom.*;

import java.security.*;
import java.util.Collections;

// TODO: Refactor cert store creation
public class AzureRestTemplate {

	private ClientHttpRequestFactory connFactory;

	public AzureRestTemplate() {
		try {

			SSLSocketFactory factory = AzureRestTemplate
					.getFactory(
							AzureRestTemplate
									.getBase64Cert("src/main/resources/AzureCredentials.xml"),
							"output.txt");
			X509HostnameVerifier verifier = new AllowAllHostnameVerifier();

			SSLConnectionSocketFactory sslsf = new SSLConnectionSocketFactory(
					factory, verifier);

			HttpClient httpClient = HttpClients.custom()
					.setSSLSocketFactory(sslsf).build();
			connFactory = new HttpComponentsClientHttpRequestFactory(httpClient);

		} catch (Exception e) {

		}

	}

	/*
	 * Used to create the PKCS#12 store - important to note that the store is
	 * created on the fly so is in fact passwordless - the JSSE fails with
	 * masqueraded exceptions so the BC provider is used instead - since the
	 * PKCS#12 import structure does not have a password it has to be done this
	 * way otherwise BC can be used to load the cert into a keystore in advance
	 * and password
	 */
	private static KeyStore createKeyStorePKCS12(String base64Certificate,
			String outstore) throws Exception {
		Security.addProvider(new BouncyCastleProvider());
		KeyStore store = KeyStore.getInstance("PKCS12",
				BouncyCastleProvider.PROVIDER_NAME);
		store.load(null, null);

		System.out.println(base64Certificate);
		// read in the value of the base 64 cert without a password (PBE can be
		// applied afterwards if this is needed
		InputStream sslInputStream = new ByteArrayInputStream(
				Base64.decode(base64Certificate));
		store.load(sslInputStream, "".toCharArray());

		// we need to a create a physical keystore as well here
		OutputStream out = new FileOutputStream("output.txt");
		store.store(out, "".toCharArray());
		out.close();
		return store;
	}

	private RestTemplate getAzureTemplate(ClientHttpRequestFactory factory) {
		RestTemplate rest = new RestTemplate(connFactory);

		CustomHeaderInterceptor addHeaders = new CustomHeaderInterceptor();
		addHeaders.addHeader("x-ms-version", "2012-03-01");

		rest.setInterceptors(Collections
				.singletonList((ClientHttpRequestInterceptor) addHeaders));
		
		return rest;

	}

	
	public Subscription getSubscription(String subsID) throws URISyntaxException {
		URI uri = new URI("https://management.core.windows.net/" + subsID);


		RestTemplate rest = getAzureTemplate(connFactory);

		Subscription sub = (Subscription) rest.getForObject(uri,
				Subscription.class);
		
		return sub;
	}
	
	public OperatingSystems getOperatingSystems(String subsID) throws URISyntaxException {
		URI uri = new URI("https://management.core.windows.net/" + subsID + "/operatingsystems");

		RestTemplate rest = getAzureTemplate(connFactory);
		
		OperatingSystems os = (OperatingSystems) rest.getForObject(uri,OperatingSystems.class);
		
		return os;
		
	}
	
	public void restTemplate() {
		try {

			URI uri = new URI(
					"https://management.core.windows.net/72e280a7-f53d-4199-be45-3063afec8240");


			RestTemplate rest = getAzureTemplate(connFactory);

			Subscription sub = (Subscription) rest.getForObject(uri,
					Subscription.class);

			System.out
					.println("Subscription Name:" + sub.getSubscriptionName());
			System.out.println("Subscription ID:" + sub.getSubscriptionID());

			uri = new URI(
					"https://management.core.windows.net/72e280a7-f53d-4199-be45-3063afec8240/operatingsystems");

			OperatingSystems os = (OperatingSystems) rest.getForObject(uri,
					OperatingSystems.class);

			System.out.println("OS " + os.getOperatingSystems().size());

			for (OperatingSystem myOs : os.getOperatingSystems()) {
				System.out.println(myOs.getVersion());
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/*
	 * Used to get an SSL factory from the keystore on the fly - this is then
	 * used in the request to the service management which will match the
	 * .publishsettings imported certificate
	 */
	private static SSLSocketFactory getFactory(String base64Certificate,
			String outStore) throws Exception {
		KeyManagerFactory keyManagerFactory = KeyManagerFactory
				.getInstance("SunX509");
		KeyStore keyStore = createKeyStorePKCS12(base64Certificate, outStore);

		// gets the TLS context so that it can use client certs attached to the
		SSLContext context = SSLContext.getInstance("TLS");
		keyManagerFactory.init(keyStore, "".toCharArray());
		context.init(keyManagerFactory.getKeyManagers(), null, null);

		return context.getSocketFactory();
	}

	private static String getBase64Cert(String publishSettingsFile) {
		String certificate = null;
		try {
			// this.publishSettingsFile="src/main/resources/AzureCredentials.xml";

			// Step 1: Read in the .publishsettings file

			File file = new File(publishSettingsFile);
			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
			DocumentBuilder db = dbf.newDocumentBuilder();
			Document doc = db.parse(file);
			doc.getDocumentElement().normalize();
			// Step 2: Get the PublishProfile
			NodeList ndPublishProfile = doc
					.getElementsByTagName("PublishProfile");
			Element publishProfileElement = (Element) ndPublishProfile.item(0);
			// Step 3: Get the PublishProfile
			certificate = publishProfileElement
					.getAttribute("ManagementCertificate");

		} catch (Exception e) {
			e.printStackTrace();
		}

		return certificate;
	}

}
