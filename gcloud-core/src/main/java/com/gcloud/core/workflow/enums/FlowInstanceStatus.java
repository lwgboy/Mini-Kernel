package com.gcloud.core.workflow.enums;

public enum FlowInstanceStatus {
	WAITING,
	EXECUTING,
	SUCCESS,
	FAILURE,
	ROLLBACKING,
	ROLLBACKING_FAIL_NOT_CONTINUE,//回滚中途失败不继续回滚（直接回滚第一步）
	ROLLBACKING_FAIL,
	ROLLBACKED_FAIL,
	ROLLBACKED;
}
