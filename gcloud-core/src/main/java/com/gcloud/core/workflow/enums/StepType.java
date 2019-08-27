package com.gcloud.core.workflow.enums;

public enum StepType {
	COMMAND("command"),
	FLOW("flow"),
	FLOW_TASK("flow_task");
	
	String type;
	StepType(String type)
	{
		this.type = type;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	
	
}
