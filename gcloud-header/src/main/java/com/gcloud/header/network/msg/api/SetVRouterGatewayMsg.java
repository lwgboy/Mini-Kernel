package com.gcloud.header.network.msg.api;

import com.gcloud.header.ApiMessage;
import com.gcloud.header.ApiReplyMessage;
import com.gcloud.header.api.ApiModel;
import org.hibernate.validator.constraints.NotBlank;

public class SetVRouterGatewayMsg extends ApiMessage {
	@NotBlank(message = "0020501::路由ID不能为空")
	@ApiModel(description="路由ID", require=true)
	private String vRouterId;
	@NotBlank(message = "0020502::外网ID不能为空")
	@ApiModel(description="外部网络ID", require=true)
	private String externalNetworkId;
	@Override
	public Class replyClazz() {
		// TODO Auto-generated method stub
		return ApiReplyMessage.class;
	}
	public String getvRouterId() {
		return vRouterId;
	}
	public void setvRouterId(String vRouterId) {
		this.vRouterId = vRouterId;
	}
	public String getExternalNetworkId() {
		return externalNetworkId;
	}
	public void setExternalNetworkId(String externalNetworkId) {
		this.externalNetworkId = externalNetworkId;
	}
}
