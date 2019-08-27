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
import org.springframework.stereotype.Component;

import com.gcloud.api.region.Region;
import com.gcloud.api.region.Regions;

@Component
public class RegionRouterFilter implements Filter{
	@Autowired
	private Regions regions;
	@Autowired
	private RequestRouter router;
	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		// TODO Auto-generated method stub
		String r=request.getParameter("regionId");
		if(r!=null) {
			Region region=regions.getRegions().get(r);
			if(!region.isLocal()) {
				HttpServletRequest httpRequest=(HttpServletRequest) request;
				HttpServletResponse httpResponse=(HttpServletResponse)response;
				String targetUrl=region.getRemoteAddress()+httpRequest.getRequestURI();
				router.route(targetUrl, httpRequest, httpResponse);
				return;
			}
		}
		chain.doFilter(request, response);
	}

	@Override
	public void destroy() {
		// TODO Auto-generated method stub
		
	}
	
}
