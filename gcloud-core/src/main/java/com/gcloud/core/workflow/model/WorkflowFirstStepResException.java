package com.gcloud.core.workflow.model;

/**
 * 任务流的第一步抛出的异常信息，用于抛错给前端
 * @author dengyf
 *
 */
public class WorkflowFirstStepResException {
	private String  errorMsg;
	
	public WorkflowFirstStepResException() {}
			
	public WorkflowFirstStepResException(String  errorMsg){
		this.errorMsg = errorMsg;
	}

	public String getErrorMsg() {
		return errorMsg;
	}

	public void setErrorMsg(String errorMsg) {
		this.errorMsg = errorMsg;
	}
	
}
