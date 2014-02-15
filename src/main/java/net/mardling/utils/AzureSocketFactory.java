package net.mardling.utils;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.Security;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;

import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.apache.http.client.HttpClient;
import org.apache.http.conn.ssl.AllowAllHostnameVerifier;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.X509HostnameVerifier;
import org.apache.http.impl.client.HttpClients;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.util.encoders.Base64;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

public class AzureSocketFactory {

	public ClientHttpRequestFactory getRequestFactory() throws ConnectionFactoryCreationException {

		ClientHttpRequestFactory connFactory=null;
		try {
			SSLSocketFactory factory = getFactory(
					getBase64Cert("src/main/resources/AzureCredentials.xml"),
					"output.txt");
			X509HostnameVerifier verifier = new AllowAllHostnameVerifier();

			SSLConnectionSocketFactory sslsf = new SSLConnectionSocketFactory(
					factory, verifier);

			HttpClient httpClient = HttpClients.custom()
					.setSSLSocketFactory(sslsf).build();
			connFactory = new HttpComponentsClientHttpRequestFactory(
					httpClient);
			
			return connFactory;
		} catch (UnrecoverableKeyException | KeyManagementException
				| NoSuchAlgorithmException | KeyStoreException
				| NoSuchProviderException | CertificateException | IOException e) {
			
			e.printStackTrace();
			throw new ConnectionFactoryCreationException();
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
			String outstore) throws KeyStoreException, NoSuchProviderException,
			NoSuchAlgorithmException, CertificateException, IOException {
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

	/*
	 * Used to get an SSL factory from the keystore on the fly - this is then
	 * used in the request to the service management which will match the
	 * .publishsettings imported certificate
	 */
	private static SSLSocketFactory getFactory(String base64Certificate,
			String outStore) throws NoSuchAlgorithmException,
			UnrecoverableKeyException, KeyStoreException,
			KeyManagementException, NoSuchProviderException,
			CertificateException, IOException {
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
