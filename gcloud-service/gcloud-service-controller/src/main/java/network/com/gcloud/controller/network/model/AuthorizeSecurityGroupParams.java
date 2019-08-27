package com.gcloud.controller.network.model;

public class AuthorizeSecurityGroupParams {
	private String direction;
	private String securityGroupId;
	private String ipProtocol;
	private String portRange;
	private String sourceGroupId;
	private String sourceCidrIp;
	private String destGroupId;
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
	
}
