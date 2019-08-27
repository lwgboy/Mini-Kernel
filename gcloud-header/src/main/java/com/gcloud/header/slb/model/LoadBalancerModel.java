package com.gcloud.header.slb.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.gcloud.framework.db.jdbc.annotation.TableField;
import com.gcloud.header.GcloudConstants;
import com.gcloud.header.api.ApiModel;

import java.io.Serializable;
import java.util.Date;

public class LoadBalancerModel implements Serializable{
	
	private static final long serialVersionUID = 1L;
	
	@ApiModel(description = "负载均衡ID")
	@TableField("id")
	private String loadBalancerId;
	@ApiModel(description = "负载均衡实例别名")
	@TableField("name")
	private String loadBalancerName;
	@ApiModel(description = "负载均衡实例状态")
	@TableField("status")
	private String loadBalancerStatus;
	@ApiModel(description = "负载均衡实例服务地址")
	private String address;
	@ApiModel(description = "负载均衡实例Vpcid")
	private String vpcId;
	@ApiModel(description = "负载均衡实例Vswitchid")
	@TableField("vip_subnet_id")
	private String vSwitchId;
	@ApiModel(description = "创建时间")
	@JsonFormat(timezone = GcloudConstants.DEFAULT_TIMEZONE, pattern = GcloudConstants.DEFAULT_DATEFORMAT)
	private Date createTime;
	@ApiModel(description = "负载均衡监听器ID")
	private String Listener;
	
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
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	public String getListener() {
		return Listener;
	}
	public void setListener(String listener) {
		Listener = listener;
	}
	
	
}
