package com.gcloud.header.compute.msg.node.node;

import javax.validation.constraints.NotBlank;

import com.gcloud.header.ApiMessage;
import com.gcloud.header.ApiReplyMessage;
import com.gcloud.header.api.ApiModel;

public class ApiAttachNodeMsg extends ApiMessage{

	private static final long serialVersionUID = 1L;

	@Override
	public Class replyClazz() {
		return ApiReplyMessage.class;
	}
	
	@ApiModel(description = "节点名", require = true)
	@NotBlank(message = "1020201::HostName不能为空")
	private String hostname;
	
	@ApiModel(description = "可用区ID", require = true)
	@NotBlank(message = "1020202::可用区ID不能为空")
	private String zoneId;

	public String getHostname() {
		return hostname;
	}
	public void setHostname(String hostname) {
		this.hostname = hostname;
	}
	public String getZoneId() {
		return zoneId;
	}
	public void setZoneId(String zoneId) {
		this.zoneId = zoneId;
	}
}
