package com.gcloud.header.network.msg.api;

import org.hibernate.validator.constraints.NotBlank;

import com.gcloud.header.ApiMessage;
import com.gcloud.header.ApiReplyMessage;
import com.gcloud.header.api.ApiModel;

public class ModifyExternalNetworkAttributeMsg extends ApiMessage {

	private static final long serialVersionUID = 1L;

	@ApiModel(description = "外部网络ID", require = true)
    @NotBlank(message = "0080301::外部网络ID不能为空")
	private String networkId;
	@ApiModel(description = "外部网络名称", require = true)
    @NotBlank(message = "0080301::外部网络名称不能为空")
	private String networkName;
	
	
	public String getNetworkId() {
		return networkId;
	}


	public void setNetworkId(String networkId) {
		this.networkId = networkId;
	}


	public String getNetworkName() {
		return networkName;
	}


	public void setNetworkName(String networkName) {
		this.networkName = networkName;
	}


	@Override
	public Class replyClazz() {
		// TODO Auto-generated method stub
		return ApiReplyMessage.class;
	}
	
	

}
