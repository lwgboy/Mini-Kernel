package com.gcloud.api.filter;

import java.io.IOException;
import java.net.URI;

import org.springframework.http.HttpMethod;
import org.springframework.http.client.ClientHttpRequest;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.util.Assert;
import org.springframework.web.client.RequestCallback;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.ResponseExtractor;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

public class MyRestTemplate extends RestTemplate {
	
	protected <T> T doExecute(URI url, HttpMethod method, RequestCallback requestCallback,
			ResponseExtractor<T> responseExtractor) throws RestClientException {

		Assert.notNull(url, "'url' must not be null");
		Assert.notNull(method, "'method' must not be null");
		ClientHttpResponse response = null;
		try {
			ClientHttpRequest request = createRequest(url, method);
			if (requestCallback != null) {
				requestCallback.doWithRequest(request);
			}
			response = request.execute();
			//handleResponse(url, method, response);
			if (responseExtractor != null) {
				return responseExtractor.extractData(response);
			}
			else {
				return null;
			}
		}
		catch (IOException ex) {
			String resource = url.toString();
			String query = url.getRawQuery();
			resource = (query != null ? resource.substring(0, resource.indexOf('?')) : resource);
			throw new ResourceAccessException("I/O error on " + method.name() +
					" request for \"" + resource + "\": " + ex.getMessage(), ex);
		}
		finally {
			if (response != null) {
				response.close();
			}
		}
	}
}
