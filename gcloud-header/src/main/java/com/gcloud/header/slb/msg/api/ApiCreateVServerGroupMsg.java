package com.gcloud.header.slb.msg.api;

import org.hibernate.validator.constraints.NotBlank;

import com.gcloud.header.ApiMessage;
import com.gcloud.header.api.ApiModel;

public class ApiCreateVServerGroupMsg extends ApiMessage{
	@ApiModel(description = "负载均衡器ID", require = true)
	@NotBlank(message = "0140101::负载均衡器ID不能为空")
	private String loadBalancerId;
	@ApiModel(description = "后端服务器组名称", require = true)
	@NotBlank(message = "0140102::后端服务器组名称不能为空")
	private String vServerGroupName;
	@ApiModel(description = "服务器组访问协议，取值HTTP、TCP", require = true)
	@NotBlank(message = "0140103::后端服务组协议不能为空")
	private String vServerGroupProtocol;
	@Override
	public Class replyClazz() {
		// TODO Auto-generated method stub
		return ApiCreateVServerGroupReplyMsg.class;
	}
	public String getLoadBalancerId() {
		return loadBalancerId;
	}
	public void setLoadBalancerId(String loadBalancerId) {
		this.loadBalancerId = loadBalancerId;
	}
	public String getvServerGroupName() {
		return vServerGroupName;
	}
	public void setvServerGroupName(String vServerGroupName) {
		this.vServerGroupName = vServerGroupName;
	}
	public String getvServerGroupProtocol() {
		return vServerGroupProtocol;
	}
	public void setvServerGroupProtocol(String vServerGroupProtocol) {
		this.vServerGroupProtocol = vServerGroupProtocol;
	}

}
