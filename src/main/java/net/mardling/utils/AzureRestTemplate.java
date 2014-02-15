package net.mardling.utils;

import java.net.URI;
import java.net.URISyntaxException;

import net.mardling.azure.OperatingSystems;
import net.mardling.azure.Subscription;

import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.web.client.RestTemplate;
import java.util.Collections;


public class AzureRestTemplate {

	private ClientHttpRequestFactory connFactory;

	public AzureRestTemplate() {
		try {
			AzureSocketFactory factory = new AzureSocketFactory();
			connFactory = factory.getRequestFactory();

		} catch (Exception | ConnectionFactoryCreationException e) {
               e.printStackTrace();
		}

	}

	private RestTemplate getAzureTemplate(ClientHttpRequestFactory factory) {
		RestTemplate rest = new RestTemplate(connFactory);

		CustomHeaderInterceptor addHeaders = new CustomHeaderInterceptor();
		addHeaders.addHeader("x-ms-version", "2012-03-01");

		rest.setInterceptors(Collections
				.singletonList((ClientHttpRequestInterceptor) addHeaders));
		
		return rest;

	}

	
	public Subscription getSubscription(String subsID) {
		

		URI uri=null;
		try {
			uri = new URI("https://management.core.windows.net/" + subsID);
		} catch (URISyntaxException e) {
			e.printStackTrace();
		}

		RestTemplate rest = getAzureTemplate(connFactory);

		Subscription sub = (Subscription) rest.getForObject(uri,
				Subscription.class);
		
		return sub;
	}
	
	public OperatingSystems getOperatingSystems(String subsID) {
		URI uri=null;
		try {
			uri = new URI("https://management.core.windows.net/" + subsID + "/operatingsystems");
		} catch (URISyntaxException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		RestTemplate rest = getAzureTemplate(connFactory);
		
		OperatingSystems os = (OperatingSystems) rest.getForObject(uri,OperatingSystems.class);
		
		return os;
		
	}

}
