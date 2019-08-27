/*
 * @Date 2019年2月12日
 * 
 * @Author kongmq@g-cloud.com.cn
 * 
 * @Copyright 2019 www.g-cloud.com.cn Inc. All rights reserved.
 * 
 * @Description
 */

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
public class MonitorRouterFilter implements Filter {

	@Value("${gcloud.api.monitor.url:http://127.0.0.1}")
	private String mointorUrl;

	@Value("${gcloud.api.monitor.token:}")
	private String token;

	@Autowired
	private RequestRouter router;

	@Override
	public void init(FilterConfig filterConfig) throws ServletException {

	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		HttpServletRequest httpRequest = (HttpServletRequest) request;
		HttpServletResponse httpResponse = (HttpServletResponse) response;
		String targetUrl = mointorUrl + httpRequest.getRequestURI();
		if (httpRequest.getRequestURI().startsWith("/monitor/")) {
			ModifiableHttpServletRequest modifyReq = new ModifiableHttpServletRequest(httpRequest);
			if (StringUtils.isNotBlank(token)) {
				modifyReq.putHeader("Authorization", token);
			}
			if (mointorUrl.startsWith("https")) {
				try {
					SslUtils.ignoreSsl();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			router.route(targetUrl, modifyReq, httpResponse);
			return;
		}
		chain.doFilter(request, response);
	}

	@Override
	public void destroy() {

	}

}
