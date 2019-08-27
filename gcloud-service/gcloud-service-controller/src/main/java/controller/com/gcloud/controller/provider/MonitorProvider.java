package com.gcloud.controller.provider;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix="gcloud.provider.monitor")
public class MonitorProvider extends Provider {

}
