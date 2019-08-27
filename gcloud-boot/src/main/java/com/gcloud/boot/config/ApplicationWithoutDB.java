package com.gcloud.boot.config;

import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceTransactionManagerAutoConfiguration;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration;
import org.springframework.boot.autoconfigure.session.SessionAutoConfiguration;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.ComponentScan.Filter;
import org.springframework.context.annotation.FilterType;
import org.springframework.scheduling.annotation.EnableScheduling;

//import springfox.documentation.swagger2.annotations.EnableSwagger2;
@ComponentScan(basePackages = "com.gcloud", excludeFilters = {
        @Filter(type = FilterType.CUSTOM, classes = {FilterCustom.class})})
@EnableScheduling
//@EnableSwagger2
@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class, HibernateJpaAutoConfiguration.class, DataSourceTransactionManagerAutoConfiguration.class, SessionAutoConfiguration.class})
@ConditionalOnProperty(name = "gcloud.withDb", havingValue = "false")
public class ApplicationWithoutDB extends SpringBootServletInitializer {

    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
        return builder.web(WebApplicationType.NONE).sources(ApplicationWithoutDB.class);
    }

}
