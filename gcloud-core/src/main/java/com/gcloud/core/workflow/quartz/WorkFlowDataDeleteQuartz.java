package com.gcloud.core.workflow.quartz;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import org.apache.commons.lang.time.DateUtils;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.gcloud.core.quartz.GcloudJobBean;
import com.gcloud.core.quartz.annotation.QuartzTimer;
import com.gcloud.core.workflow.mng.IFlowTaskMng;

import lombok.extern.slf4j.Slf4j;

/*
 * @Date 2019年7月1日
 * 
 * @Author dengyf@g-cloud.com.cn
 * 
 * @Copyright 2019 www.g-cloud.com.cn Inc. All rights reserved. 
 * 
 * @Description 定时删除任务流产生的过期数据，默认删除30天前的数据
 */
@Slf4j
@Component
@QuartzTimer(fixedRate = 10 * 60 * 1000L)
public class WorkFlowDataDeleteQuartz extends GcloudJobBean {
	 @Value("${gcloud.controller.workflow.expireDay:30}")
	 private Integer expireDay;
	 
	@Autowired
	IFlowTaskMng flowTaskMng;

	@Override
	protected void executeInternal(JobExecutionContext context) throws JobExecutionException {
		log.info("WorkFlowDataDeleteQuartz start");
		try {
			SimpleDateFormat sdf = new SimpleDateFormat("YYYY-MM-dd HH:mm:ss");
			flowTaskMng.deleteWorkflowExpireData(sdf.format(DateUtils.round(DateUtils.addDays(new Date(), -expireDay), Calendar.DATE)));
		}catch(Exception ex) {
			log.error("WorkFlowDataDeleteQuartz error," + ex.getMessage());
		}
		log.info("WorkFlowDataDeleteQuartz end");
	}

}
