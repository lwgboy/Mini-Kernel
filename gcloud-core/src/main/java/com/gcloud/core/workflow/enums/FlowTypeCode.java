package com.gcloud.core.workflow.enums;

public enum FlowTypeCode {
	CLEAR_TRASH("ClearTrash"),
	DELETE_IMAGE("DeleteImage");
	
	private String flowTypeCode;
	FlowTypeCode(String flowType)
	{
		this.flowTypeCode = flowType;
	}
	public String getFlowTypeCode() {
		return flowTypeCode;
	}
	public void setFlowTypeCode(String flowTypeCode) {
		this.flowTypeCode = flowTypeCode;
	}
	
}
