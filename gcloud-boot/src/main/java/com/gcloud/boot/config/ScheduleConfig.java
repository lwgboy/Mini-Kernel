package com.gcloud.boot.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.SchedulingConfigurer;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.scheduling.config.ScheduledTaskRegistrar;

/**
 * Created by yaowj on 2018/9/26.
 */
@Configuration
@EnableScheduling
@ConditionalOnExpression("${gcloud.schedule.pool.enable:false} == true")
public class ScheduleConfig implements SchedulingConfigurer {

    @Value("${gcloud.schedule.pool.poolSize:5}")
    private Integer poolSize;
    @Value("${gcloud.schedule.pool.threadNamePrefix:scheduled-task-pool-}")
    private String threadNamePrefix;

    @Override
    public void configureTasks(ScheduledTaskRegistrar scheduledTaskRegistrar) {
        ThreadPoolTaskScheduler threadPoolTaskScheduler = new ThreadPoolTaskScheduler();
        threadPoolTaskScheduler.setPoolSize(poolSize);
        threadPoolTaskScheduler.setThreadNamePrefix(threadNamePrefix);
        threadPoolTaskScheduler.initialize();
        scheduledTaskRegistrar.setTaskScheduler(threadPoolTaskScheduler);
    }

}
