package com.gcloud.core.workflow.enums;

public enum FlowStepStatus {
	SKIP,
	NOT_EXECUTE,
	EXECUTING,
	SUCCESS,
	FAILURE,
	ROLLBACKING,
	ROLLBACKED,
	ROLLBACKED_SKIP,
	ROLLBACKED_FAIL;
}
