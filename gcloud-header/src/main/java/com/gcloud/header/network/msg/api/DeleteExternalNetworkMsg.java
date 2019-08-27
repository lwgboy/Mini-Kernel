package com.gcloud.header.network.msg.api;

import org.hibernate.validator.constraints.NotBlank;

import com.gcloud.header.ApiMessage;
import com.gcloud.header.ApiReplyMessage;

public class DeleteExternalNetworkMsg extends ApiMessage{
	
	private static final long serialVersionUID = 1L;

    @NotBlank(message = "0160201::外部网络ID不能为空")
	private String networkId;

	public String getNetworkId() {
		return networkId;
	}

	public void setNetworkId(String networkId) {
		this.networkId = networkId;
	}

	@Override
	public Class replyClazz() {
		// TODO Auto-generated method stub
		return ApiReplyMessage.class;
	}

}
