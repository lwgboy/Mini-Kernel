package com.gcloud.header.network.msg.api;

import org.hibernate.validator.constraints.NotBlank;

import com.gcloud.header.ApiMessage;
import com.gcloud.header.api.ApiModel;

public class AllocateEipAddressMsg extends ApiMessage {
	@NotBlank(message = "0050101::弹性公网实例Id不能为空")
	@ApiModel(description = "公网网络ID", require = true)
	private String networkId;
//	//仅支持阿里的，默认值为5
//	private String bandwidth;
	
	@Override
	public Class replyClazz() {
		return AllocateEipAddressReplyMsg.class;
	}

	public String getNetworkId() {
		return networkId;
	}

	public void setNetworkId(String networkId) {
		this.networkId = networkId;
	}

//	public String getBandwidth() {
//		return bandwidth;
//	}
//
//	public void setBandwidth(String bandwidth) {
//		this.bandwidth = bandwidth;
//	}
	
}
