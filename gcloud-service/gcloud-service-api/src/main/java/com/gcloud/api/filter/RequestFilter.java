package com.gcloud.api.filter;


import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import com.gcloud.api.security.HttpRequestConstant;

import java.io.IOException;
import java.util.UUID;

/**
 * Created by yaowj on 2018/9/17.
 */
public class RequestFilter implements Filter {

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        servletRequest.setAttribute(HttpRequestConstant.ATTR_REQUEST_ID, UUID.randomUUID().toString());
        filterChain.doFilter(servletRequest, servletResponse);

    }

    @Override
    public void destroy() {

    }
}
