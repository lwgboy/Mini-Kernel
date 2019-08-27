package com.gcloud.core.service;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.gcloud.header.Module;
@Component
public class ServiceModuleMapping {
	@Autowired
	private ServiceName serverName;
	
	private Map<String,String> uriServiceMap=new HashMap<>();
	
	@PostConstruct
	public void init() {
		uriServiceMap.put(Module.USER.name(), serverName.getIdentity());
		uriServiceMap.put(Module.TENANT.name(), serverName.getIdentity());
	}
	public String getService(String module) {
		String service=uriServiceMap.get(module);
		if(service==null)
			return serverName.getController();
		return service;
	}
}
