package com.gcloud.api.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.gcloud.common.util.StringUtils;
import com.gcloud.core.util.SslUtils;

@Component
public class K8sRouterFilter implements Filter{
	@Value("${gcloud.api.k8s.url:http://127.0.0.1}")
	private String k8sUrl;
	
	@Value("${gcloud.api.k8s.token:}")
	private String token;
	
	@Autowired
	private RequestRouter router;

	@Override
	public void destroy() {
		
	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		HttpServletRequest httpRequest = (HttpServletRequest) request;
		HttpServletResponse httpResponse = (HttpServletResponse)response;
		String targetUrl = k8sUrl + httpRequest.getRequestURI();
		ModifiableHttpServletRequest modifyReq = new ModifiableHttpServletRequest(httpRequest);
		if(StringUtils.isNotBlank(token)) {
			modifyReq.putHeader("Authorization", token);
		}
		if(k8sUrl.startsWith("https")) {
			try {
				SslUtils.ignoreSsl();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		router.route(targetUrl, modifyReq, httpResponse);
		return;
	}

	@Override
	public void init(FilterConfig arg0) throws ServletException {
		
	}

}
