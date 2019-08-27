package com.gcloud.header.network.msg.api;

import com.gcloud.header.ApiPageMessage;
import com.gcloud.header.api.ApiModel;

import java.util.List;

public class DescribeNetworkInterfacesMsg extends ApiPageMessage {

	private static final long serialVersionUID = 1L;

	@ApiModel(description = "VPC 的虚拟交换机 ID")
	private String vSwitchId;
	@ApiModel(description = "网卡IP")
	private String primaryIpAddress;
	@ApiModel(description = "安全组ID")
	private String securityGroupId;
	@ApiModel(description = "网卡的名称")
	private String networkInterfaceName;
	@ApiModel(description = "实例 ID")
	private String instanceId;
	@ApiModel(description = "网卡ID列表")
	private List<String> networkInterfaceIds;
//	private String regionId;

	@ApiModel(description = "端口的拥有者")
	private List<String> deviceOwners;

	@ApiModel(description = "是否包含没有拥有者")
	private Boolean includeOwnerless;
	
	@Override
	public Class replyClazz() {
		// TODO Auto-generated method stub
		return DescribeNetworkInterfacesReplyMsg.class;
	}

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

	public String getInstanceId() {
		return instanceId;
	}

	public void setInstanceId(String instanceId) {
		this.instanceId = instanceId;
	}

	public List<String> getNetworkInterfaceIds() {
		return networkInterfaceIds;
	}

	public void setNetworkInterfaceIds(List<String> networkInterfaceIds) {
		this.networkInterfaceIds = networkInterfaceIds;
	}

	public List<String> getDeviceOwners() {
		return deviceOwners;
	}

	public void setDeviceOwners(List<String> deviceOwners) {
		this.deviceOwners = deviceOwners;
	}

	public Boolean getIncludeOwnerless() {
		return includeOwnerless;
	}

	public void setIncludeOwnerless(Boolean includeOwnerless) {
		this.includeOwnerless = includeOwnerless;
	}
}
