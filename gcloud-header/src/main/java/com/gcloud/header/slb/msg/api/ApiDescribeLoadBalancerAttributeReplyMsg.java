package com.gcloud.header.slb.msg.api;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.gcloud.header.ApiReplyMessage;
import com.gcloud.header.GcloudConstants;
import com.gcloud.header.api.ApiModel;

public class ApiDescribeLoadBalancerAttributeReplyMsg extends ApiReplyMessage {
	
	private static final long serialVersionUID = 1L;
	
	@ApiModel(description = "负载均衡ID")
	private String loadBalancerId;
	@ApiModel(description = "名称")
	private String loadBalancerName;
	@ApiModel(description = "状态")
	private String loadBalancerStatus;
	@ApiModel(description = "地址")
	private String address;
	@ApiModel(description = "VPC ID")
	private String vpcId;
	@ApiModel(description = "子网ID")
	private String vSwitchId;
	@ApiModel(description = "创建时间")
	@JsonFormat(timezone = GcloudConstants.DEFAULT_TIMEZONE, pattern = GcloudConstants.DEFAULT_DATEFORMAT)
	private Date createTime;
	@ApiModel(description = "端口号列表")
	private List<Integer> ListenerPorts;
	@ApiModel(description = "端口号协议列表")
	private List<ListenerPortAndProtocol> ListenerPortsAndProtocol;
	
	public String getLoadBalancerId() {
		return loadBalancerId;
	}
	public void setLoadBalancerId(String loadBalancerId) {
		this.loadBalancerId = loadBalancerId;
	}
	public String getLoadBalancerName() {
		return loadBalancerName;
	}
	public void setLoadBalancerName(String loadBalancerName) {
		this.loadBalancerName = loadBalancerName;
	}
	public String getLoadBalancerStatus() {
		return loadBalancerStatus;
	}
	public void setLoadBalancerStatus(String loadBalancerStatus) {
		this.loadBalancerStatus = loadBalancerStatus;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public String getVpcId() {
		return vpcId;
	}
	public void setVpcId(String vpcId) {
		this.vpcId = vpcId;
	}
	public String getvSwitchId() {
		return vSwitchId;
	}
	public void setvSwitchId(String vSwitchId) {
		this.vSwitchId = vSwitchId;
	}
	public Date getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	public List<Integer> getListenerPorts() {
		return ListenerPorts;
	}
	public void setListenerPorts(List<Integer> listenerPorts) {
		ListenerPorts = listenerPorts;
	}
	public List<ListenerPortAndProtocol> getListenerPortsAndProtocol() {
		return ListenerPortsAndProtocol;
	}
	public void setListenerPortsAndProtocol(List<ListenerPortAndProtocol> listenerPortsAndProtocol) {
		ListenerPortsAndProtocol = listenerPortsAndProtocol;
	}
	
	

}
