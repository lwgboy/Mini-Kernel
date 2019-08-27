package com.gcloud.header.slb.msg.api;

import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotBlank;

import com.gcloud.header.ApiMessage;
import com.gcloud.header.api.ApiModel;

public class CreateLoadBalancerMsg extends ApiMessage {
	
	@ApiModel(description = "名称", require = true)
	@Length(min=2, max = 20, message = "0110101::名称长度为[2,20]")
	private String  loadBalancerName;
	@ApiModel(description = "子网ID", require = true)
	@NotBlank(message = "0110102::子网ID不能为空")
	private String   vSwitchId;

	public String getLoadBalancerName() {
		return loadBalancerName;
	}

	public void setLoadBalancerName(String loadBalancerName) {
		this.loadBalancerName = loadBalancerName;
	}

	public String getvSwitchId() {
		return vSwitchId;
	}

	public void setvSwitchId(String vSwitchId) {
		this.vSwitchId = vSwitchId;
	}

	@Override
	public Class replyClazz() {
		// TODO Auto-generated method stub
		return CreateLoadBalancerReplyMsg.class;
	}

}
