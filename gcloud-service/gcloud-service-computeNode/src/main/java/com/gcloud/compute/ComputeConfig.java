package com.gcloud.compute;

import org.springframework.amqp.core.Queue;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
@Configuration
public class ComputeConfig {
	@Value("${gcloud.service.computeNode}")
	public String syncQueueName;
	
	@Value("${gcloud.service.computeNode}"+"_async")
	public String asyncQueue;
    @Bean
    public Queue computeSyncQueue() {
    	System.out.println("init compute sync queue:"+syncQueueName);
        return new Queue(syncQueueName);
    }
    
    @Bean
    public Queue computeAsyncQueue() {
    	System.out.println("init compute async queue:"+asyncQueue);
        return new Queue(asyncQueue);
    }
    
}
