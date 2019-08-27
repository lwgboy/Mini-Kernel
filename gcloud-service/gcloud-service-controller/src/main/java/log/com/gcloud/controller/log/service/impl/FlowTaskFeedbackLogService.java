package com.gcloud.controller.log.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gcloud.controller.log.model.LogFeedbackParams;
import com.gcloud.controller.log.service.ILogService;
import com.gcloud.core.workflow.service.IFlowTaskFeedbackLogService;
@Service
public class FlowTaskFeedbackLogService implements IFlowTaskFeedbackLogService{
	@Autowired
	ILogService logService;

	@Override
	public void flowTaskFeedbackLog(String taskId, String objectId, String status, String code) {
		LogFeedbackParams params = new LogFeedbackParams();
		params.setTaskId(taskId);
		params.setStatus(status);
		params.setCode(code);
		logService.feedback(params);
	}

}
