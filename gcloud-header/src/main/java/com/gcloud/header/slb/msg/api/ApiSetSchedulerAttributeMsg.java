package com.gcloud.header.slb.msg.api;

import org.hibernate.validator.constraints.NotBlank;

import com.gcloud.header.ApiMessage;
import com.gcloud.header.api.ApiModel;

public class ApiSetSchedulerAttributeMsg extends ApiMessage {

	@ApiModel(description = "监听器ID或后端服务器组ID", require = true)
	@NotBlank(message = "0130101::ID不能为空")
	private String resourceId;
	@ApiModel(description = "协议")
	private String protocol;
	@ApiModel(description = "调度策略名称", require = true)
	@NotBlank(message = "0130102::调度策略名称不能为空")
	private String scheduler;
	/**
	 * 调度策略 <br>
	 * 阿里：取值：wrr | wlc | rr <br>
	 * openstack：取值：ROUND_ROBIN | LEAST_CONNECTIONS | SOURCE_IP
	 */

	public String getResourceId() {
		return resourceId;
	}



	public void setResourceId(String resourceId) {
		this.resourceId = resourceId;
	}



	public String getProtocol() {
		return protocol;
	}



	public void setProtocol(String protocol) {
		this.protocol = protocol;
	}



	public String getScheduler() {
		return scheduler;
	}



	public void setScheduler(String scheduler) {
		this.scheduler = scheduler;
	}



	@Override
	public Class replyClazz() {
		// TODO Auto-generated method stub
		return ApiSetSchedulerAttributeReplyMsg.class;
	}

}
