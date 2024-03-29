package com.gcloud.api;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;



@Configuration
public class SpringMVCConfig extends WebMvcConfigurerAdapter{
	@Autowired
	ApiHandlerMessageMethodArgumentResolver apiHandlerMessageMethodArgumentResolver;
	@Override
	public void addArgumentResolvers(List<HandlerMethodArgumentResolver> argumentResolvers) {
        super.addArgumentResolvers(argumentResolvers);
        //argumentResolvers.add(apiHandlerMethodArgumentResolver);
        argumentResolvers.add(apiHandlerMessageMethodArgumentResolver);
    }
	
	@Override  
    public void addInterceptors(InterceptorRegistry registry) {  
        //注册自定义拦截器，添加拦截路径和排除拦截路径  
    }
}
