package com.gcloud.core.quartz;

import java.util.Date;

import org.quartz.JobDataMap;
import org.quartz.JobDetail;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.Scheduler;
import org.quartz.Trigger;
import org.quartz.impl.triggers.SimpleTriggerImpl;
import org.quartz.listeners.JobListenerSupport;
import org.quartz.utils.DirtyFlagMap;

import com.gcloud.core.quartz.model.JobDataConst;
import com.gcloud.core.quartz.model.JobFixedType;
import com.gcloud.core.quartz.model.QuartzJobFixedData;
import com.gcloud.core.util.SerializeUtils;

public class FixedJobListener extends JobListenerSupport{

	@Override
	public String getName() {
		return "fixedjob";
	}
	
	@Override
    public void jobWasExecuted(final JobExecutionContext context, final JobExecutionException exception) {
        JobDetail jobdetail = context.getJobDetail();
        JobDataMap data = (JobDataMap)jobdetail.getJobDataMap();

        Trigger trigger = context.getTrigger();

        if(!trigger.mayFireAgain()) {
            return;
        }
        QuartzJobFixedData fixedData = data == null ? null : (QuartzJobFixedData)SerializeUtils.unSerialize(data.get(JobDataConst.QUARTZ_JOB_FIXED_DATA).toString());

        if(fixedData == null || fixedData.getInterval() <= 0 || !(trigger instanceof SimpleTriggerImpl)) {
            return;
        }

        try {

            Date nextTime = null;
            if(JobFixedType.FIXED_DELAY.toString().equals(fixedData.getJobFixedType())) {
                nextTime = new Date((new Date()).getTime() + fixedData.getInterval());
            } else if(JobFixedType.FIXED_RATE.toString().equals(fixedData.getJobFixedType())) {
                nextTime = new Date(context.getFireTime().getTime() + fixedData.getInterval());
            }

            if(trigger instanceof SimpleTriggerImpl) {
                ((SimpleTriggerImpl) trigger).setStartTime(nextTime);
            }

            Scheduler scheduler = context.getScheduler();

            if(!scheduler.isShutdown()) {
                scheduler.rescheduleJob(trigger.getKey(), trigger);
            }

        } catch (Exception ex) {

        }

    }
}
