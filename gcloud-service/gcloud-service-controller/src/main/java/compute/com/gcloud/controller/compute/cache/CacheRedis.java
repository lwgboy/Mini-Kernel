package com.gcloud.controller.compute.cache;

import com.gcloud.core.cache.redis.template.GCloudRedisTemplate;
import com.gcloud.core.exception.GCloudException;
import com.gcloud.service.common.Consts;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.text.MessageFormat;

/**
 * Created by yaowj on 2018/10/19.
 */
@Component
public class CacheRedis {

    @Autowired
    private GCloudRedisTemplate redisTemplate;

    public void delUnAdoptObject(String hostName, String id) throws GCloudException {
        String setName = MessageFormat.format(Consts.RedisKey.GCLOUD_CONTROLLER_COMPUTE_ADOPT_SET, hostName);
        if (id != null) {
            redisTemplate.opsForHash().delete(setName, id);
        } else {
            redisTemplate.delete(setName);
        }
    }

}
