package com.gcloud.boot.config;

import com.alibaba.druid.pool.DruidDataSource;
import com.gcloud.controller.ControllerConfig;
import com.gcloud.framework.db.jdbc.JdbcTemplateConfig;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;


@Configuration
public class DatasourceConfiguration {
	@Bean(name = "controllerDataSource")
    @ConfigurationProperties(prefix = "gcloud.datasource.controller")
	@ConditionalOnBean(ControllerConfig.class)
	@Primary
    public DataSource controllerDataSource() {
        return DataSourceBuilder.create().type(DruidDataSource.class).build();
    }
	
	@ConditionalOnBean(ControllerConfig.class)
	@Bean(name = "controllerJdbcTemplate")
    public JdbcTemplate controllerJdbcTemplate(@Qualifier("controllerDataSource") DataSource dataSource) {
        return new JdbcTemplate(dataSource);
    }
	
	
	
	@Bean
	public JdbcTemplateConfig jdbcTemplateConfig(){
		JdbcTemplateConfig config=new JdbcTemplateConfig();
		config.addPatternPath("controllerJdbcTemplate", "com.gcloud.controller.dao.*");
		config.addPatternPath("controllerJdbcTemplate", "com.gcloud.core.workflow.dao.*");
		config.addPatternPath("controllerJdbcTemplate", "com.gcloud.log.dao.*");
		config.addPatternPath("controllerJdbcTemplate", "com.gcloud.controller.image.dao.*");
		config.addPatternPath("identityJdbcTemplate", "com.gcloud.identity.user.dao.*");
		return config;
	}
}	
