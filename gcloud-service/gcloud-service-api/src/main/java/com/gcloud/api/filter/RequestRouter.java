package com.gcloud.api.filter;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.util.MultiValueMap;
import org.springframework.util.StreamUtils;

@Component
public class RequestRouter {
	public void route(String targetUrl,HttpServletRequest req, HttpServletResponse resp) {
		RequestEntity<byte[]> requestEntity = null;
		try {
			requestEntity = createRequestEntity(req, targetUrl);
		} catch (Exception e) {
			e.printStackTrace();
		} 
		
		MyRestTemplate restTemplate = new MyRestTemplate();
		ResponseEntity responseEntity = restTemplate.exchange(requestEntity, byte[].class);
		try {
			addResponseHeaders(resp,responseEntity);
			writeResponse(resp,responseEntity);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
	private RequestEntity createRequestEntity(HttpServletRequest request, String url)
			throws URISyntaxException, IOException {
		String method = request.getMethod();
		HttpMethod httpMethod = HttpMethod.resolve(method); 
		MultiValueMap<String, String> headers = createRequestHeaders(request); // 1、封装请求头
		byte[] body = createRequestBody(request); // 2、封装请求体
		String queryStr = request.getQueryString();//URL参数
		if(null != queryStr && queryStr.length()>0) {
			url +="?"+ queryStr;
		}
		RequestEntity requestEntity = new RequestEntity<byte[]>(body, headers, httpMethod, new URI(url));// 3、构造出RestTemplate能识别的RequestEntity
		return requestEntity;
	}

	private byte[] createRequestBody(HttpServletRequest request) throws IOException {
		InputStream inputStream = request.getInputStream();
		return StreamUtils.copyToByteArray(inputStream);
	}

	private MultiValueMap<String, String> createRequestHeaders(HttpServletRequest request) {
		HttpHeaders headers = new HttpHeaders();
		List<String> headerNames = Collections.list(request.getHeaderNames());
		for (String headerName : headerNames) {
			List<String> headerValues = Collections.list(request.getHeaders(headerName));
			for (String headerValue : headerValues) {
				headers.add(headerName, headerValue);
			}
		}
		return headers;
	}
	
	private void addResponseHeaders(HttpServletResponse servletResponse,ResponseEntity responseEntity) {
		HttpHeaders httpHeaders = responseEntity.getHeaders();
		for (Map.Entry<String, List<String>> entry : httpHeaders.entrySet()) {
			String headerName = entry.getKey();
			if(headerName.equals("Connection"))
				continue;
			List<String> headerValues = entry.getValue();
			for (String headerValue : headerValues) {
				servletResponse.addHeader(headerName, headerValue);
			}
		}
	}

	private void writeResponse(HttpServletResponse servletResponse,ResponseEntity responseEntity) throws Exception {
		if (servletResponse.getCharacterEncoding() == null) { 
			servletResponse.setCharacterEncoding("UTF-8");
		}
		if (responseEntity.hasBody()) {
			byte[] body = (byte[]) responseEntity.getBody();
			servletResponse.setStatus(responseEntity.getStatusCodeValue());
			ServletOutputStream outputStream = servletResponse.getOutputStream();
			outputStream.write(body);
			outputStream.flush();
		}
	}
}
