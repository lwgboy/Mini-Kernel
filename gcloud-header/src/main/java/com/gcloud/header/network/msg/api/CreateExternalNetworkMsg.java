package com.gcloud.header.network.msg.api;

import org.hibernate.validator.constraints.Length;

import com.gcloud.header.ApiCreateMessage;
import com.gcloud.header.api.ApiModel;

public class CreateExternalNetworkMsg extends ApiCreateMessage{

	@ApiModel(description="网络名称", require=true)
	@Length(min=2, max = 20, message = "0160101::外部网络名称不能为空")
	private String networkName;
	
	public String getNetworkName() {
		return networkName;
	}

	public void setNetworkName(String networkName) {
		this.networkName = networkName;
	}

	@Override
	public Class replyClazz() {
		// TODO Auto-generated method stub
		return CreateExternalNetworkReplyMsg.class;
	}
	
	
}
