package com.gcloud.core.cache;

import javax.annotation.PostConstruct;

import org.springframework.context.annotation.DependsOn;
import org.springframework.stereotype.Component;

import com.gcloud.core.service.SpringUtil;

import lombok.extern.slf4j.Slf4j;

@DependsOn("springUtil")
@Component
@Slf4j
public class ResourceNameCacheReg {
	@PostConstruct
	public void init() {
		log.debug("ResourceNameCacheReg init... ");
        for (Cache cache : SpringUtil.getBeans(Cache.class)) {
            cache.register(cache.getClass());
        }
    }
}
