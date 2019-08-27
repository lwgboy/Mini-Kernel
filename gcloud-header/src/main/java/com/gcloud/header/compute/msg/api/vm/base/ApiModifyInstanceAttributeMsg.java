package com.gcloud.header.compute.msg.api.vm.base;

import javax.validation.constraints.Pattern;

import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotBlank;

import com.gcloud.header.ApiMessage;
import com.gcloud.header.ApiReplyMessage;
import com.gcloud.header.api.ApiModel;

public class ApiModifyInstanceAttributeMsg extends ApiMessage{

	@Override
	public Class replyClazz() {
		return ApiReplyMessage.class;
	}
	@ApiModel(description="实例 ID",require=true)
	@NotBlank(message = "0010501::实例 ID不能为空")
	private String instanceId;
	@ApiModel(description="实例名称")
//	@Pattern(regexp="^[\\p{Han}A-Za-z][\\p{Han}A-Za-z0-9_] {3,19})*$",message="实例名称长度为[4,20] 英文、中文字符、数字或_，以大小字母或中文开头")
	private String instanceName;
	@ApiModel(description="实例密码")
	@Length(min=8,max=20, message="0010502::密码长度[8,20]")
	private String password;
	public String getInstanceId() {
		return instanceId;
	}
	public void setInstanceId(String instanceId) {
		this.instanceId = instanceId;
	}
	public String getInstanceName() {
		return instanceName;
	}
	public void setInstanceName(String instanceName) {
		this.instanceName = instanceName;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	
	
}
