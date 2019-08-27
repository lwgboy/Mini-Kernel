package com.gcloud.core.workflow.entity;

import java.io.Serializable;

import com.gcloud.framework.db.jdbc.annotation.ID;
import com.gcloud.framework.db.jdbc.annotation.Table;

@Table(name="gc_work_flow_type")
public class WorkFlowType implements Serializable{
	private static final long serialVersionUID = 1L;
	@ID
	private Long id;
	private String flowTypeCode;
	private String flowTypeName;
	private String description;
	private boolean faultRollbackContinue;//回滚出错时是否继续rollback剩下的，一般对于创建的场景为true，删除的场景为false
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
	public String getFlowTypeName() {
		return flowTypeName;
	}
	public void setFlowTypeName(String flowTypeName) {
		this.flowTypeName = flowTypeName;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public boolean isFaultRollbackContinue() {
		return faultRollbackContinue;
	}
	public void setFaultRollbackContinue(boolean faultRollbackContinue) {
		this.faultRollbackContinue = faultRollbackContinue;
	}
	
}
