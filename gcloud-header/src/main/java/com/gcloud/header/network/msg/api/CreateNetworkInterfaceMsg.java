package com.gcloud.header.network.msg.api;

import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotBlank;

import com.gcloud.header.common.RegExp;
import com.gcloud.header.ApiMessage;
import com.gcloud.header.api.ApiModel;

import javax.validation.constraints.Pattern;

public class CreateNetworkInterfaceMsg extends ApiMessage {

	private static final long serialVersionUID = 1L;

	@ApiModel(description = "子网ID", require = true)
	@NotBlank(message = "0080101::子网ID不能为空")
	private String vSwitchId;
	@ApiModel(description = "IP地址")
	@Pattern(regexp = RegExp.IPV4, message = "0080105::请输入正确的IP")
	private String primaryIpAddress;
	@ApiModel(description = "安全组ID", require = true)
	@NotBlank(message = "0080102::安全组ID不能为空")
	private String securityGroupId;
	@ApiModel(description = "网卡名称")
    @Length(max = 255, message = "0080106::名称长度不能大于255")
	private String networkInterfaceName;
	@ApiModel(description = "描述")
    @Length(max = 255, message = "0080107::描述长度不能大于255")
	private String description;


	public String getvSwitchId() {
		return vSwitchId;
	}


	public void setvSwitchId(String vSwitchId) {
		this.vSwitchId = vSwitchId;
	}


	public String getPrimaryIpAddress() {
		return primaryIpAddress;
	}


	public void setPrimaryIpAddress(String primaryIpAddress) {
		this.primaryIpAddress = primaryIpAddress;
	}


	public String getSecurityGroupId() {
		return securityGroupId;
	}


	public void setSecurityGroupId(String securityGroupId) {
		this.securityGroupId = securityGroupId;
	}


	public String getNetworkInterfaceName() {
		return networkInterfaceName;
	}


	public void setNetworkInterfaceName(String networkInterfaceName) {
		this.networkInterfaceName = networkInterfaceName;
	}


	public String getDescription() {
		return description;
	}


	public void setDescription(String description) {
		this.description = description;
	}


	@Override
	public Class replyClazz() {
		// TODO Auto-generated method stub
		return CreateNetworkInterfaceReplyMsg.class;
	}

}
