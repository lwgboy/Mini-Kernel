package com.gcloud.boot.config;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.env.EnvironmentPostProcessor;
import org.springframework.core.env.ConfigurableEnvironment;

public class MyEnvironmentPostProcessor implements EnvironmentPostProcessor{

	@Override
	public void postProcessEnvironment(ConfigurableEnvironment environment, SpringApplication application) {
		// TODO Auto-generated method stub
		 RandomUUIDPropertySource propertySource=new RandomUUIDPropertySource("RandUUID");
		 HostInfoPropertySource hostInfoPropertySource=new HostInfoPropertySource("HostInfo");
		 environment.getPropertySources().addLast(propertySource);
		 environment.getPropertySources().addLast(hostInfoPropertySource);
	}

}
