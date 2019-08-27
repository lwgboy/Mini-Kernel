package com.gcloud.api.region;

import java.util.HashMap;
import java.util.Map;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "gcloud.region-proxy")
public class Regions {
	private Map<String,Region> regions=new HashMap<>();

	public Map<String, Region> getRegions() {
		return regions;
	}

	public void setRegions(Map<String, Region> regions) {
		this.regions = regions;
	}

}
