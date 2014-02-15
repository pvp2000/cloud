package net.mardling.utils;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;

public class CustomHeaderInterceptor implements ClientHttpRequestInterceptor {
	
	Map<String, String> headers;
	
	CustomHeaderInterceptor() {
		headers = new HashMap<String, String>();
	}

    @Override
    public ClientHttpResponse intercept(HttpRequest request, byte[] data, ClientHttpRequestExecution execution) throws IOException {
    	
    	for(String key : headers.keySet()) {
    		request.getHeaders().add(key, headers.get(key));
    	}
        request.getHeaders().add("myHeader", "value");
        return execution.execute(request, data);
    }

	public Map<String, String> getHeaders() {
		return headers;
	}

	public void setHeaders(Map<String, String> headers) {
		this.headers = headers;
	}
    
    public void addHeader(String name, String value) {
    	headers.put(name, value);
    }

}
