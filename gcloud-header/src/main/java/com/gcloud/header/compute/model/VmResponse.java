package com.gcloud.header.compute.model;

/*
 * @Date 2015-4-20
 * 
 * @Author chenyu1@g-cloud.com.cn
 * 
 * @Copyright 2015 www.g-cloud.com.cn Inc. All rights reserved.
 * 
 * @Description 返回信息类
 */

import java.io.Serializable;

public class VmResponse implements Serializable {

	private static final long serialVersionUID = 1L;

	private String instanceId;
	private String taskId;
	private Boolean result;//true/false 该对象操作是否成功
	private String code;//错误码
	private String alias;
	private String message;//英文错误码显示
	private Object[] params;//参数
	private String increBackupId;
	public String getInstanceId() {
		return instanceId;
	}
	public void setInstanceId(String instanceId) {
		this.instanceId = instanceId;
	}
	public String getTaskId() {
		return taskId;
	}
	public void setTaskId(String taskId) {
		this.taskId = taskId;
	}
	public Boolean getResult() {
		return result;
	}
	public void setResult(Boolean result) {
		this.result = result;
	}
    public String getCode() {
        return code;
    }
    public void setCode(String code) {
        this.code = code;
    }
    public String getAlias() {
        return alias;
    }
    public void setAlias(String alias) {
        this.alias = alias;
    }
    public String getMessage() {
        return message;
    }
    public void setMessage(String message) {
        this.message = message;
    }
    public Object[] getParams() {
        return params;
    }
    public void setParams(Object[] params) {
        this.params = params;
    }
	public String getIncreBackupId() {
		return increBackupId;
	}
	public void setIncreBackupId(String increBackupId) {
		this.increBackupId = increBackupId;
	}
	

}
