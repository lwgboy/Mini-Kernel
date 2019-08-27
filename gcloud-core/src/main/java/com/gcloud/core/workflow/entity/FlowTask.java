package com.gcloud.core.workflow.entity;

import java.io.Serializable;
import java.util.Date;

import com.gcloud.framework.db.jdbc.annotation.ID;
import com.gcloud.framework.db.jdbc.annotation.Table;

@Table(name="gc_work_flow_task")
public class FlowTask implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@ID
	private Long id;
	private String taskId;
	private String flowTypeCode;
	private String state;
	private int batchSize;
	private Date startTime;
	private Date endTime;
	private String regionId;
	private String userId;
	
	private Long parentFlowId;
	private Integer parentFlowStepId;
	private boolean needFeedbackLog;
	
	public static String END_TIME = "endTime";
	public static String STATE = "state";
	
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
	public String getTaskId() {
		return taskId;
	}
	public void setTaskId(String taskId) {
		this.taskId = taskId;
	}
	public String getFlowTypeCode() {
		return flowTypeCode;
	}
	public void setFlowTypeCode(String flowTypeCode) {
		this.flowTypeCode = flowTypeCode;
	}
	public String getState() {
		return state;
	}
	public void setState(String state) {
		this.state = state;
	}
	public int getBatchSize() {
		return batchSize;
	}
	public void setBatchSize(int batchSize) {
		this.batchSize = batchSize;
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
	public Integer getParentFlowStepId() {
		return parentFlowStepId;
	}
	public void setParentFlowStepId(Integer parentFlowStepId) {
		this.parentFlowStepId = parentFlowStepId;
	}
	public String getRegionId() {
		return regionId;
	}
	public void setRegionId(String regionId) {
		this.regionId = regionId;
	}
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public boolean isNeedFeedbackLog() {
		return needFeedbackLog;
	}

	public void setNeedFeedbackLog(boolean needFeedbackLog) {
		this.needFeedbackLog = needFeedbackLog;
	}
	
}
