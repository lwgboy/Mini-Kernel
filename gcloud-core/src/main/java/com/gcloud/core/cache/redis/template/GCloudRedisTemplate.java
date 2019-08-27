package com.gcloud.core.cache.redis.template;

import com.gcloud.core.util.SerializeUtils;
import org.springframework.data.redis.core.StringRedisTemplate;

public class GCloudRedisTemplate extends StringRedisTemplate {

    public Object getObject(String key){
        String str = opsForValue().get(key);
        return SerializeUtils.unSerialize(str);
    }

    public void setObject(String key, Object object){
        opsForValue().set(key, SerializeUtils.serialize(object));
    }

}
