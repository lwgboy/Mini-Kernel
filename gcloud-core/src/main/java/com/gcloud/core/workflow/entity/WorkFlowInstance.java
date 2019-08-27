package com.gcloud.core.workflow.entity;

import java.io.Serializable;
import java.util.Date;

import com.gcloud.framework.db.jdbc.annotation.ID;
import com.gcloud.framework.db.jdbc.annotation.Table;

@Table(name="gc_work_flow_instance")
public class WorkFlowInstance implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@ID
	private Long id;
	private String flowTypeCode;
	private String taskId;
	private String paramsJson;
	private String errorCode;
	private String state;
	private Date startTime;
	private Date endTime;
	private Long parentFlowId;
	private int parentFlowStepId;
	private String batchParams;
	private String topestFlowTaskId;
	
	public static String STATE = "state";
	public static String END_TIME = "endTime";
	public static String ERROR_CODE = "errorCode";
	
	public String updateErrorCode(String errorCode) {
		this.setErrorCode(errorCode);
		return ERROR_CODE;
	}
	
	public String updateEndTime(Date endTime) {
		this.setEndTime(endTime);
		return END_TIME;
	}
	
	public String updateState(String state) {
		this.setState(state);
		return STATE;
	}
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getFlowTypeCode() {
		return flowTypeCode;
	}
	public void setFlowTypeCode(String flowTypeCode) {
		this.flowTypeCode = flowTypeCode;
	}
	public String getTaskId() {
		return taskId;
	}
	public void setTaskId(String taskId) {
		this.taskId = taskId;
	}
	public String getParamsJson() {
		return paramsJson;
	}
	public void setParamsJson(String paramsJson) {
		this.paramsJson = paramsJson;
	}
	public String getErrorCode() {
		return errorCode;
	}
	public void setErrorCode(String errorCode) {
		this.errorCode = errorCode;
	}
	public String getState() {
		return state;
	}
	public void setState(String state) {
		this.state = state;
	}
	public Date getStartTime() {
		return startTime;
	}
	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}
	public Date getEndTime() {
		return endTime;
	}
	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}
	public Long getParentFlowId() {
		return parentFlowId;
	}
	public void setParentFlowId(Long parentFlowId) {
		this.parentFlowId = parentFlowId;
	}
	public int getParentFlowStepId() {
		return parentFlowStepId;
	}
	public void setParentFlowStepId(int parentFlowStepId) {
		this.parentFlowStepId = parentFlowStepId;
	}
	public String getBatchParams() {
		return batchParams;
	}
	public void setBatchParams(String batchParams) {
		this.batchParams = batchParams;
	}

	public String getTopestFlowTaskId() {
		return topestFlowTaskId;
	}

	public void setTopestFlowTaskId(String topestFlowTaskId) {
		this.topestFlowTaskId = topestFlowTaskId;
	}
	
}
