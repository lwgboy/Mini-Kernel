package com.gcloud.core.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
@Configuration
public class ServiceName {
	@Value("${gcloud.service.api}")
	private String api;
	@Value("${gcloud.service.identity}")
	private String identity;
	@Value("${gcloud.service.controller}")
	private String controller;
	@Value("${gcloud.service.computeNode}")
	private String computeNode;
    @Value("${gcloud.service.storageNode}")
    private String storageNode;
	public String getApi() {
		return api;
	}
	public String getIdentity() {
		return identity;
	}
	public String getController() {
		return controller;
	}
	public String getComputeNode() {
		return computeNode;
	}
    public String getStorageNode() {
        return storageNode;
    }
	
}
