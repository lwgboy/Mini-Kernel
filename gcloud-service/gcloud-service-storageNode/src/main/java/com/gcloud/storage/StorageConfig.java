
package com.gcloud.storage;

import org.springframework.amqp.core.Queue;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import lombok.extern.slf4j.Slf4j;

@Configuration
@Slf4j
public class StorageConfig {

    @Value("${gcloud.service.storageNode}")
    public String syncQueue;

    @Value("${gcloud.service.storageNode}" + "_async")
    public String asyncQueue;

    @Bean
    public Queue syncQueue() {
        log.info("init storage sync queue:" + syncQueue);
        return new Queue(syncQueue);
    }

    @Bean
    public Queue asyncQueue() {
        log.info("init storage async queue:" + asyncQueue);
        return new Queue(asyncQueue);
    }

}
