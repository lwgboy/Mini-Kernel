package com.gcloud.api.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
@Component
public class IdentityRouterFilter implements Filter{
	@Value("${gcloud.api.identity.local}")
	private boolean local;
	@Value("${gcloud.api.identity.remoteAddress}")
	private String remoteAddress;
	
	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		// TODO Auto-generated method stub
		HttpServletRequest httpRequest=(HttpServletRequest) request;
		String uri=httpRequest.getRequestURI();
		if(uri.indexOf("/user/")==0||uri.indexOf("/role/")==0||uri.indexOf("/department/")==0){
			
		}
		chain.doFilter(request, response);
	}

	@Override
	public void destroy() {
		// TODO Auto-generated method stub
		
	}

}
