package net.mardling.utils;

import java.io.IOException;
import java.io.InputStream;

import org.springframework.http.HttpHeaders;
import org.springframework.http.client.AbstractClientHttpResponse;

public class AzureHttpClientResponse extends AbstractClientHttpResponse {
	
	HttpHeaders headers;
	int statusCode;
	String statusText;

	@Override
	public void close() {
		// TODO Auto-generated method stub

	}

	@Override
	public int getRawStatusCode() throws IOException {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public String getStatusText() throws IOException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public InputStream getBody() throws IOException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public HttpHeaders getHeaders() {
		// TODO Auto-generated method stub
		return null;
	}

}
