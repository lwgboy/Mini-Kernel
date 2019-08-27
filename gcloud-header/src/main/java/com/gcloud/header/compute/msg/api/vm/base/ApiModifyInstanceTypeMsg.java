package com.gcloud.header.compute.msg.api.vm.base;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import com.gcloud.header.ApiMessage;
import com.gcloud.header.ApiReplyMessage;
import com.gcloud.header.api.ApiModel;

public class ApiModifyInstanceTypeMsg extends ApiMessage{

	private static final long serialVersionUID = 1L;

	@Override
	public Class replyClazz() {
		return ApiReplyMessage.class;
	}
	
	@ApiModel(description = "实例类型ID", require = true)
	@NotBlank(message = "0011601::实例类型ID不能为空")
	private String id;
	
	@ApiModel(description = "内存大小，单位MB", require = true)
	@NotNull(message = "0011602::内存大小不能为空")
	private Integer memory;
	
	@ApiModel(description ="cpu核数", require = true)
	@NotNull(message = "0011603::cpu核数不能为空")
	private Integer cpu;

	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public Integer getMemory() {
		return memory;
	}
	public void setMemory(Integer memory) {
		this.memory = memory;
	}
	public Integer getCpu() {
		return cpu;
	}
	public void setCpu(Integer cpu) {
		this.cpu = cpu;
	}
}
