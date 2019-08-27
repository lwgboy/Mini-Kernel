package com.gcloud.boot.config;

import java.util.Random;
import java.util.UUID;

import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.PropertySource;
import org.springframework.core.env.StandardEnvironment;

public class RandomUUIDPropertySource extends PropertySource<Random>{
	public static final String RANDOM_PROPERTY_SOURCE_NAME = "randomUUID";
	private static final String PREFIX = "randomUUID";
	private static String uuid;
	public RandomUUIDPropertySource(String name) {
		super(name, new Random());
		// TODO Auto-generated constructor stub
	}
	public RandomUUIDPropertySource() {
		this(RANDOM_PROPERTY_SOURCE_NAME);
	}
	
	@Override
	public Object getProperty(String name) {
		// TODO Auto-generated method stub
		if (!name.startsWith(PREFIX)) {
			return null;
		}
		if(uuid==null)
			uuid=UUID.randomUUID().toString().replaceAll("-", "");
		return uuid;
	}
	
	public static void addToEnvironment(ConfigurableEnvironment environment) {
		environment.getPropertySources().addAfter(
				StandardEnvironment.SYSTEM_ENVIRONMENT_PROPERTY_SOURCE_NAME,
				new RandomUUIDPropertySource(RANDOM_PROPERTY_SOURCE_NAME));
	}

}
