package com.gcloud.core.cache.redis.config;

import com.gcloud.core.cache.redis.template.GCloudRedisTemplate;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;

import java.net.UnknownHostException;

/*
 * @Desccription RedisTemplate配置, 可以不要，这里是为了修改序列化，采用改用StringRedisSerializer
 * @Date 2018/3/6
 * @Created by yaowj
 */
@Configuration
@ConditionalOnExpression("${gcloud.redis.enable:false} == true")
public class RedisTemplateConfig {

    //改用StringRedisSerializer
    @Bean
    public GCloudRedisTemplate gcloudRedisTemplate(RedisConnectionFactory redisConnectionFactory) throws UnknownHostException {
        GCloudRedisTemplate template = new GCloudRedisTemplate();
        template.setConnectionFactory(redisConnectionFactory);
        return template;
    }


}
