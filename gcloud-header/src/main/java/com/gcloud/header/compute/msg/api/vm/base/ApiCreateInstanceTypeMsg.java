package com.gcloud.header.compute.msg.api.vm.base;

import javax.validation.constraints.NotBlank;

import com.gcloud.header.ApiMessage;
import com.gcloud.header.ApiReplyMessage;
import com.gcloud.header.api.ApiModel;

public class ApiCreateInstanceTypeMsg extends ApiMessage{

	private static final long serialVersionUID = 1L;

	@Override
	public Class replyClazz() {
		return ApiReplyMessage.class;
	}

	@ApiModel(description = "实例类型名称", require = true)
	@NotBlank(message = "0011401::实例类型名字不能为空")
	private String name;
	
	@ApiModel(description = "内存大小， 默认2048MB， 单位MB", require = false)
	private Integer memory = 2048;
	
	@ApiModel(description = "cpu核数， 默认为1核", require = false)
	private Integer cpu = 1;

	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
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
