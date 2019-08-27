package com.gcloud.core.cache.quartz;

import java.util.Iterator;
import java.util.Map;

import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.stereotype.Component;

import com.gcloud.core.cache.Cache;
import com.gcloud.core.cache.enums.CacheType;
import com.gcloud.core.quartz.GcloudJobBean;
import com.gcloud.core.quartz.annotation.QuartzTimer;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@QuartzTimer(fixedRate = 24 * 60 * 60 * 1000L)//闲时重新初始化
public class CacheReInitQuartz extends GcloudJobBean{
	@Override
	protected void executeInternal(JobExecutionContext arg0) throws JobExecutionException {
		Map<CacheType,Cache> caches= Cache.getMember();
		Iterator it=caches.values().iterator();
		while(it.hasNext()) {
			Cache c = (Cache)it.next();
			if(c.reInit()||!c.isHasInit()) {
				c.init();
			}
		}
	}

}
