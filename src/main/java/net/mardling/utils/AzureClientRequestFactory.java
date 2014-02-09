package net.mardling.utils;

import java.io.IOException;
import java.net.URI;

import org.springframework.http.HttpMethod;
import org.springframework.http.client.ClientHttpRequest;
import org.springframework.http.client.ClientHttpRequestFactory;

public class AzureClientRequestFactory implements ClientHttpRequestFactory {

	@Override
	public ClientHttpRequest createRequest(URI uri, HttpMethod method) throws IOException {
		
		return new AzureHttpClientRequest(uri, method);
	}

}