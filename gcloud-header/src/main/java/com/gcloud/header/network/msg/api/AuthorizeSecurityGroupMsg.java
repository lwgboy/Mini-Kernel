package com.gcloud.header.network.msg.api;

import org.hibernate.validator.constraints.NotBlank;

import com.gcloud.header.ApiMessage;
import com.gcloud.header.ApiReplyMessage;
import com.gcloud.header.api.ApiModel;

public class AuthorizeSecurityGroupMsg extends ApiMessage {
	@NotBlank(message = "0040501")
	@ApiModel(description = "规则方向入：ingress和出：egress", require = true)
	private String direction;
	@NotBlank(message = "0040502")
	@ApiModel(description = "安全组Id", require = true)
	private String securityGroupId;
	@NotBlank(message = "0040503")
	@ApiModel(description = "安全组协议", require = true)
	private String ipProtocol;
	//@NotBlank(message = "0040504")
	@ApiModel(description = "端口范围")
	private String portRange;
	@ApiModel(description = "源安全组Id")
	private String sourceGroupId;
	@ApiModel(description = "源ip段")
	private String sourceCidrIp;
	@ApiModel(description = "目标安全组Id")
	private String destGroupId;
	@ApiModel(description = "目标安全组Ip")
	private String destCidrIp;
	
	public String getDirection() {
		return direction;
	}


	public void setDirection(String direction) {
		this.direction = direction;
	}


	public String getSecurityGroupId() {
		return securityGroupId;
	}


	public void setSecurityGroupId(String securityGroupId) {
		this.securityGroupId = securityGroupId;
	}


	public String getIpProtocol() {
		return ipProtocol;
	}


	public void setIpProtocol(String ipProtocol) {
		this.ipProtocol = ipProtocol;
	}


	public String getPortRange() {
		return portRange;
	}


	public void setPortRange(String portRange) {
		this.portRange = portRange;
	}


	public String getSourceGroupId() {
		return sourceGroupId;
	}


	public void setSourceGroupId(String sourceGroupId) {
		this.sourceGroupId = sourceGroupId;
	}


	public String getSourceCidrIp() {
		return sourceCidrIp;
	}


	public void setSourceCidrIp(String sourceCidrIp) {
		this.sourceCidrIp = sourceCidrIp;
	}


	public String getDestGroupId() {
		return destGroupId;
	}


	public void setDestGroupId(String destGroupId) {
		this.destGroupId = destGroupId;
	}


	public String getDestCidrIp() {
		return destCidrIp;
	}


	public void setDestCidrIp(String destCidrIp) {
		this.destCidrIp = destCidrIp;
	}


	@Override
	public Class replyClazz() {
		return ApiReplyMessage.class;
	}

}
