package com.gcloud.controller.provider;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.gcloud.common.util.HttpClientUtil;
import com.gcloud.controller.monitor.model.Statistics;
import com.gcloud.controller.monitor.model.StatisticsPoint;
import com.gcloud.controller.monitor.model.StatisticsResource;
import com.gcloud.core.exception.GCloudException;

import lombok.extern.slf4j.Slf4j;
import net.sf.json.JSONObject;

@Slf4j
@Component
public class MonitorProviderProxy {
	
	@Autowired
	private MonitorProvider provider;
	
	public Statistics statistics(Map<String, Object> params){
		String result = null;
		
		try{
			result = HttpClientUtil.doGet(this.provider.getUrl() +  "/monitor/Statistics", params);
        }catch (Exception ex){
            log.error("获取监控信息失败" + ex, ex);
            throw new GCloudException("::获取监控信息失败");
        }

		JSONObject json = JSONObject.fromObject(result);
		
		Map<String, Class> classMap = new HashMap<String, Class>();
		classMap.put("list", StatisticsResource.class);
		classMap.put("points", StatisticsPoint.class);
		Statistics data = (Statistics) JSONObject.toBean(json.getJSONObject("data"), Statistics.class, classMap);
		return data;
	}
	
}
