package com.gcloud.core.workflow.entity;

import java.io.Serializable;

import com.gcloud.framework.db.jdbc.annotation.ID;
import com.gcloud.framework.db.jdbc.annotation.Table;

@Table(name="gc_work_flow_command_value_template")
public class FlowCommandValueTemplate  implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@ID
	private Integer id;
	private String flowTypeCode;//任务流类型代码
	private Integer stepId; //步骤ID
	private String fieldName;
	private Integer fromStepId;
	private String fromParamType;//res、req
	private String fromFieldName;
//	private String fromFieldType;
	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getFlowTypeCode() {
		return flowTypeCode;
	}
	public void setFlowTypeCode(String flowTypeCode) {
		this.flowTypeCode = flowTypeCode;
	}
	public Integer getStepId() {
		return stepId;
	}
	public void setStepId(Integer stepId) {
		this.stepId = stepId;
	}
	public String getFieldName() {
		return fieldName;
	}
	public void setFieldName(String fieldName) {
		this.fieldName = fieldName;
	}
	public Integer getFromStepId() {
		return fromStepId;
	}
	public void setFromStepId(Integer fromStepId) {
		this.fromStepId = fromStepId;
	}
	public String getFromParamType() {
		return fromParamType;
	}
	public void setFromParamType(String fromParamType) {
		this.fromParamType = fromParamType;
	}
	public String getFromFieldName() {
		return fromFieldName;
	}
	public void setFromFieldName(String fromFieldName) {
		this.fromFieldName = fromFieldName;
	}
//	public String getFromFieldType() {
//		return fromFieldType;
//	}
//	public void setFromFieldType(String fromFieldType) {
//		this.fromFieldType = fromFieldType;
//	}
	
}
