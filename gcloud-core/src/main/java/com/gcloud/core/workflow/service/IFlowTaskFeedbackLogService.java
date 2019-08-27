package com.gcloud.core.workflow.service;

public interface IFlowTaskFeedbackLogService {
	public void flowTaskFeedbackLog(String taskId, String objectId, String status, String code);
}
