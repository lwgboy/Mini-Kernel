package com.gcloud.core.quartz;

import java.util.Map;

import javax.annotation.PostConstruct;

import org.apache.commons.lang.StringUtils;
import org.quartz.CronScheduleBuilder;
import org.quartz.JobBuilder;
import org.quartz.JobDetail;
import org.quartz.JobKey;
import org.quartz.ScheduleBuilder;
import org.quartz.Scheduler;
import org.quartz.SimpleScheduleBuilder;
import org.quartz.SimpleTrigger;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;
import org.quartz.TriggerKey;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import com.gcloud.core.quartz.annotation.QuartzTimer;
import com.gcloud.core.quartz.model.JobDataConst;
import com.gcloud.core.quartz.model.JobFixedType;
import com.gcloud.core.quartz.model.QuartzJobFixedData;
import com.gcloud.core.util.SerializeUtils;

public class QuartzInitialize implements ApplicationContextAware {

    private static ApplicationContext context;

    @Autowired
    private Scheduler scheduler;

    @Value("${gcloud.quartz.mode:cluster}")
    private String mode;

    private final String SINGLE_NODE = "singleNode";

    @PostConstruct
    public void register() throws Exception {
        Map<String, Object> timers = context.getBeansWithAnnotation(QuartzTimer.class);
        for (Object timer : timers.values()) {
            addJob(timer);
        }
        scheduler.getListenerManager().addJobListener(new FixedJobListener());
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        context = applicationContext;
    }

    public void addJob(Object timer) throws Exception {

        Class<?> clazz = timer.getClass();
        if (!GcloudJobBean.class.isAssignableFrom(clazz) || !clazz.isAnnotationPresent(QuartzTimer.class)) {
            return;
        }

        GcloudJobBean jobTimer = (GcloudJobBean) timer;
        ScheduleBuilder<? extends Trigger> builder = jobTimer.scheduleBuilder();

        String key = clazz.getName();//MD5Util.encrypt(clazz.getName());
        String jobKeyStr = String.format("%s_%s", key, clazz.getSimpleName());
        String trigerKeyStr = String.format("%s_trigger", jobKeyStr);

        JobKey jobKey = new JobKey(jobKeyStr, Scheduler.DEFAULT_GROUP);
        TriggerKey triggerKey = new TriggerKey(trigerKeyStr, Scheduler.DEFAULT_GROUP);

        if (scheduler.checkExists(jobKey)) {
            if (mode != null && SINGLE_NODE.equals(mode)) {
                scheduler.deleteJob(jobKey);
            } else {
                return;
            }
        }

        JobFixedType jobFixedType = null;

        QuartzTimer timerAno = (QuartzTimer) clazz.getAnnotation(QuartzTimer.class);

        JobDetail job = JobBuilder.newJob(jobTimer.getClass()).withIdentity(jobKey).build();
        Trigger trigger = null;

        if (builder == null) {
            if (StringUtils.isNotBlank(timerAno.cron())) {
                builder = CronScheduleBuilder.cronSchedule(timerAno.cron()).withMisfireHandlingInstructionIgnoreMisfires();
            } else if (timerAno.fixedRate() > 0) {
                jobFixedType = JobFixedType.FIXED_RATE;
                builder = SimpleScheduleBuilder.simpleSchedule().repeatForever().withIntervalInMilliseconds(timerAno.fixedRate());//.withMisfireHandlingInstructionIgnoreMisfires()
            } else if (timerAno.fixedDelay() > 0) {
                jobFixedType = JobFixedType.FIXED_DELAY;
                builder = SimpleScheduleBuilder.simpleSchedule().repeatForever().withIntervalInMilliseconds(timerAno.fixedDelay());//.withMisfireHandlingInstructionIgnoreMisfires()
            } else {
//	            log.error(String.format("定时器[%s]时间设置不正确", clazz.getName()));
                return;
            }
        }
        trigger = TriggerBuilder.newTrigger().withIdentity(triggerKey).withSchedule(builder).build();

        if (null != jobFixedType && trigger instanceof SimpleTrigger) {
            QuartzJobFixedData fixedData = new QuartzJobFixedData(jobFixedType.toString(), ((SimpleTrigger) trigger).getRepeatInterval());
            job.getJobDataMap().put(JobDataConst.QUARTZ_JOB_FIXED_DATA, SerializeUtils.serialize(fixedData));
        }

        scheduler.scheduleJob(job, trigger);

    }

}
