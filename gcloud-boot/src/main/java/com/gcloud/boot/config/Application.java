package com.gcloud.boot.config;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.ComponentScan.Filter;
import org.springframework.context.annotation.FilterType;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;


//import springfox.documentation.swagger2.annotations.EnableSwagger2;

//@ComponentScan(basePackages = "com.gcloud")
@ComponentScan(basePackages = "com.gcloud",
excludeFilters={@Filter(type = FilterType.CUSTOM, classes = {FilterCustom.class})})

@EnableScheduling
//@EnableSwagger2
@SpringBootApplication
@EnableCaching
@EnableAsync
@ConditionalOnProperty(name = "gcloud.withDb", havingValue = "true")
public class Application extends SpringBootServletInitializer {


	@Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
        return builder.sources(Application.class);
    }

}
