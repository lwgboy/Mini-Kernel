package com.gcloud.controller;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.amqp.core.Queue;
import org.springframework.beans.factory.annotation.Value;

@Configuration
public class ControllerConfig {
	@Value("${gcloud.service.controller}")
	public String queueName;
	
	@Value("${gcloud.service.controller}"+"_async")
	public String asyncQueue;
    @Bean
    public Queue controllerQueue() {
    	System.out.println("init contoller queue:"+queueName);
        return new Queue(queueName);
    }
    
    @Bean
    public Queue controller2Queue() {
    	System.out.println("init contoller async queue:"+asyncQueue);
        return new Queue(asyncQueue);
    }
    
}