package com.gcloud.core.workflow.enums;

public enum FeedbackState {
	SKIP,
	SUCCESS,
	SUCCESS_Y,
    SUCCESS_N,
	FAILURE;
	
	public static boolean include(String state) {
		for(FeedbackState item:FeedbackState.values()) {
			if(item.name().equals(state)) {
				return true;
			}
		}
		return false;
	}
	
}
