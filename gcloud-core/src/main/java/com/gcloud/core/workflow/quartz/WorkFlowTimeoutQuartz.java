package com.gcloud.core.workflow.quartz;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.stereotype.Component;

import com.gcloud.core.quartz.GcloudJobBean;
import com.gcloud.core.quartz.annotation.QuartzTimer;
import com.gcloud.core.service.SpringUtil;
import com.gcloud.core.workflow.core.BaseWorkFlowCommand;
import com.gcloud.core.workflow.entity.WorkFlowInstanceStep;
import com.gcloud.core.workflow.mng.IWorkFlowInstanceStepMng;

/**工作流超时处理
 * @author dengyf
 *
 */
@Component
@QuartzTimer(fixedRate = 60 * 1000L)
public class WorkFlowTimeoutQuartz extends GcloudJobBean{

	@Override
	protected void executeInternal(JobExecutionContext arg0) throws JobExecutionException {
		IWorkFlowInstanceStepMng mng = (IWorkFlowInstanceStepMng)SpringUtil.getBean("workFlowInstanceStepMng");
	    List<WorkFlowInstanceStep> steps = mng.getAllNotFinishedStep();
	    for(WorkFlowInstanceStep step:steps)
	    {
	    	if(step.getTimeOut()!= 0 && addSecOfDate(step.getStartTime(), step.getTimeOut()).before(new Date())) {
		    	BaseWorkFlowCommand command = (BaseWorkFlowCommand)SpringUtil.getBean(step.getExecCommand());
				command.setWorkFlowStepId(step.getId());
				command.timeoutHandler();
	    	}
	    }
	}
	
	private Date addSecOfDate(Date date,int i)
	{
		Calendar c = Calendar.getInstance();
	    c.setTime(date);
	    c.add(Calendar.SECOND, i);
	    Date newDate = c.getTime();
	    return newDate;
	}

}
