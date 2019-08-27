package com.gcloud.core.workflow.entity;

import java.io.Serializable;

import com.gcloud.framework.db.jdbc.annotation.ID;
import com.gcloud.framework.db.jdbc.annotation.Table;

@Table(name="gc_work_flow_batch")
public class BatchWorkFlow implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@ID
	private Long id;
	private String ptaskId;
	private Long flowId;
	private String state;
	
	public static String STATE = "state";
	
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
	public String getPtaskId() {
		return ptaskId;
	}
	public void setPtaskId(String ptaskId) {
		this.ptaskId = ptaskId;
	}
	public Long getFlowId() {
		return flowId;
	}
	public void setFlowId(Long flowId) {
		this.flowId = flowId;
	}
	public String getState() {
		return state;
	}
	public void setState(String state) {
		this.state = state;
	}
	
}
