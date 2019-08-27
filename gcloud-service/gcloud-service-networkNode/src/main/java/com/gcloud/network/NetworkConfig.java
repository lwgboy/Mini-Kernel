package com.gcloud.network;

import org.springframework.amqp.core.Queue;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class NetworkConfig {
	@Value("${gcloud.service.networkNode}")
	public String syncQueueName;
	
	@Value("${gcloud.service.networkNode}"+"_async")
	public String asyncQueue;
    @Bean
    public Queue networkSyncQueue() {
    	System.out.println("init network sync queue:"+syncQueueName);
        return new Queue(syncQueueName);
    }
    
    @Bean
    public Queue networkAsyncQueue() {
    	System.out.println("init network async queue:"+asyncQueue);
        return new Queue(asyncQueue);
    }
    
}
