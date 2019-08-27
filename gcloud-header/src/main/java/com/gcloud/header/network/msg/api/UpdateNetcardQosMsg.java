package com.gcloud.header.network.msg.api;

import com.gcloud.header.ApiMessage;
import com.gcloud.header.ApiReplyMessage;
import com.gcloud.header.api.ApiModel;

import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

public class UpdateNetcardQosMsg extends ApiMessage {
	
	@ApiModel(description = "网卡ID", require = true)
	@NotBlank(message = "0080501::端口ID不能为空")
	private String networkInterfaceId;
	@ApiModel(description = "入口带宽")
	@NotNull(message = "0080503::入口速度不能为空")
	@Min(value = 0, message = "0080504::入口速度要大于0")
	private Integer ingress;//单位mb/s
	@ApiModel(description = "出口带宽")
	@NotNull(message = "0080505::出口速度不能为空")
	@Min(value = 0, message = "0080506::出口速度要大于0")
	private Integer outgress;
	
	@Override
	public Class replyClazz() {
		// TODO Auto-generated method stub
		return ApiReplyMessage.class;
	}

	public String getNetworkInterfaceId() {
		return networkInterfaceId;
	}

	public void setNetworkInterfaceId(String networkInterfaceId) {
		this.networkInterfaceId = networkInterfaceId;
	}

	public Integer getIngress() {
		return ingress;
	}

	public void setIngress(Integer ingress) {
		this.ingress = ingress;
	}

	public Integer getOutgress() {
		return outgress;
	}

	public void setOutgress(Integer outgress) {
		this.outgress = outgress;
	}
	
}
