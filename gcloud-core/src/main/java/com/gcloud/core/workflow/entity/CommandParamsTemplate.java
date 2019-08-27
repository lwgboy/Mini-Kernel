package com.gcloud.core.workflow.entity;

import java.io.Serializable;

import com.gcloud.framework.db.jdbc.annotation.ID;
import com.gcloud.framework.db.jdbc.annotation.Table;

@Table(name="gc_work_flow_command_params_template")
public class CommandParamsTemplate implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@ID
	private Integer id;
	private String execCommand;
	private String paramType;//req、res
	private String fieldName;
	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getExecCommand() {
		return execCommand;
	}
	public void setExecCommand(String execCommand) {
		this.execCommand = execCommand;
	}
	public String getParamType() {
		return paramType;
	}
	public void setParamType(String paramType) {
		this.paramType = paramType;
	}
	public String getFieldName() {
		return fieldName;
	}
	public void setFieldName(String fieldName) {
		this.fieldName = fieldName;
	}
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	
	

}
