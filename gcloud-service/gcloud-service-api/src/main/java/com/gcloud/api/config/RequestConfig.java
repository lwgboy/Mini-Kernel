package com.gcloud.api.config;

import com.gcloud.api.filter.AuthFilter;
import com.gcloud.api.filter.ErrorFilter;
import com.gcloud.api.filter.HttpSignatureFilter;
import com.gcloud.api.filter.K8sRouterFilter;
import com.gcloud.api.filter.RegionRouterFilter;
import com.gcloud.api.filter.RequestFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.servlet.Filter;

/**
 * Created by yaowj on 2018/9/17.
 */
@Configuration
public class RequestConfig {
	@Autowired
	RegionRouterFilter regionRouterFilter;
	@Autowired
	K8sRouterFilter k8sRouterFilter;
	@Autowired
	AuthFilter authFilter;
	@Autowired
	ErrorFilter errorFilter;
	@Autowired
	HttpSignatureFilter httpSignatureFilter;

    @Bean
    public FilterRegistrationBean sessionOnlineFilter(Filter requestFilter) {
        FilterRegistrationBean filterRegistration = new FilterRegistrationBean();
        filterRegistration.setFilter(requestFilter);
        filterRegistration.addUrlPatterns("*.do");
        filterRegistration.setOrder(1);
        return filterRegistration;
    }

	/** 过滤器统一异常处理
	 * 此过滤器需要设置为第一个执行
	 * @author dengyf
	 *
	 */
	@Bean
	public FilterRegistrationBean errorFilterRegistration() {
		FilterRegistrationBean registration = new FilterRegistrationBean(errorFilter);
		registration.addUrlPatterns("*");
		registration.setOrder(2);
		return registration;
	}

	@Bean
	public FilterRegistrationBean authTokenFilterRegistration() {
		FilterRegistrationBean registration = new FilterRegistrationBean(authFilter);
		registration.addUrlPatterns("*");
		registration.setOrder(3);
		return registration;
	}

	@Bean
	public FilterRegistrationBean httpSignatureFilterRegistration() {
		FilterRegistrationBean registration = new FilterRegistrationBean(httpSignatureFilter);
		registration.addUrlPatterns("*");
		registration.setOrder(4);
		return registration;
	}

    @Bean
    public FilterRegistrationBean authFilterRegistration() {
    	FilterRegistrationBean registration = new FilterRegistrationBean(regionRouterFilter);
    	registration.addUrlPatterns("*.do");
    	registration.setOrder(5);
    	return registration;
    }
    
    @Bean
    public FilterRegistrationBean k8sRouterFilterRegistration() {
    	FilterRegistrationBean registration = new FilterRegistrationBean(k8sRouterFilter);
    	registration.addUrlPatterns("/api/*");
    	registration.addUrlPatterns("/apis/*");
    	registration.setOrder(6);
    	return registration;
    }


    
    @Bean
    public Filter requestFilter() {
        return new RequestFilter();
    }

}
