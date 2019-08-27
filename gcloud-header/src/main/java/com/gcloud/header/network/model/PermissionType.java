package com.gcloud.header.network.model;

import java.io.Serializable;

public class PermissionType  implements Serializable{
	private String securityGroupRuleId;
	private String ipProtocol;
	private String portRange;
	private String sourceCidrIp;
	private String sourceGroupId;
	private String destCidrIp;
	private String destGroupId;
	private String direction;
	private String description;
	
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
	
	public String getSourceCidrIp() {
		return sourceCidrIp;
	}
	
	public void setSourceCidrIp(String sourceCidrIp) {
		this.sourceCidrIp = sourceCidrIp;
	}
	
	public String getSourceGroupId() {
		return sourceGroupId;
	}
	
	public void setSourceGroupId(String sourceGroupId) {
		this.sourceGroupId = sourceGroupId;
	}
	
	public String getDestCidrIp() {
		return destCidrIp;
	}
	
	public void setDestCidrIp(String destCidrIp) {
		this.destCidrIp = destCidrIp;
	}
	
	public String getDestGroupId() {
		return destGroupId;
	}
	
	public void setDestGroupId(String destGroupId) {
		this.destGroupId = destGroupId;
	}
	
	public String getDirection() {
		return direction;
	}
	
	public void setDirection(String direction) {
		this.direction = direction;
	}
	
	public String getDescription() {
		return description;
	}
	
	public void setDescription(String description) {
		this.description = description;
	}
	public String getSecurityGroupRuleId() {
		return securityGroupRuleId;
	}

	public void setSecurityGroupRuleId(String securityGroupRuleId) {
		this.securityGroupRuleId = securityGroupRuleId;
	}
}
