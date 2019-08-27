package com.gcloud.core.quartz;

import org.quartz.ScheduleBuilder;
import org.quartz.Trigger;
import org.springframework.scheduling.quartz.QuartzJobBean;

public abstract class GcloudJobBean extends QuartzJobBean{
	public ScheduleBuilder<? extends Trigger> scheduleBuilder(){
		return null;
	}
}
