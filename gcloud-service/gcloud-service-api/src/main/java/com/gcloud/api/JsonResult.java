package com.gcloud.api;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.gcloud.core.error.ErrorInfo;

public class JsonResult {

	private boolean success;
	private Object data;
	private List<ErrorInfo> errors = new ArrayList<ErrorInfo>();
	
	public JsonResult() {}
	
	public JsonResult(Object data) {
		this.success = true;
		if(data!=null) {
			this.data = data;
		}else{
			this.data="请求成功";
		}
		
	}
	
	public JsonResult(boolean success, Object data) {
		this.success = success;
		this.data = data;
	}

	public boolean isSuccess() {
		return success;
	}
	public void setSuccess(boolean success) {
		this.success = success;
	}
	@JsonInclude(JsonInclude.Include.NON_EMPTY)
	public Object getData() {
		return data;
	}
	public void setData(Object data) {
		this.data = data;
	}
	@JsonInclude(JsonInclude.Include.NON_EMPTY)
	public List<ErrorInfo> getErrors() {
		return errors;
	}

	public void setErrors(List<ErrorInfo> errors) {
		this.errors = errors;
	}
	
}
