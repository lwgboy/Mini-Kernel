package com.gcloud.controller.provider;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix="gcloud.provider.glance")
public class GlanceProvider extends Provider{

}
