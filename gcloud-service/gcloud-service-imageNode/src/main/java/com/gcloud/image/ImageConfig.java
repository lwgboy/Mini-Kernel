package com.gcloud.image;

import org.springframework.amqp.core.Queue;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ImageConfig {
	@Value("${gcloud.service.imageNode}")
	public String syncQueueName;
	
	@Value("${gcloud.service.imageNode}"+"_async")
	public String asyncQueue;
    @Bean
    public Queue imageSyncQueue() {
    	System.out.println("init image sync queue:"+syncQueueName);
        return new Queue(syncQueueName);
    }
    
    @Bean
    public Queue imageAsyncQueue() {
    	System.out.println("init image async queue:"+asyncQueue);
        return new Queue(asyncQueue);
    }
}
