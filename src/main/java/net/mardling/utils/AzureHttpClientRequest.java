package net.mardling.utils;

import java.io.IOException;
import java.io.OutputStream;
import java.net.URI;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.client.ClientHttpRequest;
import org.springframework.http.client.ClientHttpResponse;

public class AzureHttpClientRequest implements ClientHttpRequest {
	
	HttpMethod method;
	URI uri;
	
	public AzureHttpClientRequest(URI uri, HttpMethod method) {
		this.method=method;
		this.uri = uri;
		
	}

	@Override
	public HttpMethod getMethod() {
		return method;
	}

	@Override
	public URI getURI() {
		return uri;
	}

	@Override
	public HttpHeaders getHeaders() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public OutputStream getBody() throws IOException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ClientHttpResponse execute() throws IOException {
		// TODO Auto-generated method stub
		return null;
	}

}
