package com.gcloud.controller.provider;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix="gcloud.provider.neutron")
public class NeutronProvider extends Provider{
	
}
