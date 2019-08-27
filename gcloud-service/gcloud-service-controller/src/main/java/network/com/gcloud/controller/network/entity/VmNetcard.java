package com.gcloud.controller.network.entity;

import java.util.Date;

import com.gcloud.framework.db.jdbc.annotation.ID;
import com.gcloud.framework.db.jdbc.annotation.Table;

@Table(name="gc_vm_netcard",jdbc="controllerJdbcTemplate")
public class VmNetcard {
	private static final long serialVersionUID = 1L;
	@ID
	private Long id;
	private String instanceId;
	private String netcardId;
	private String ip;
	private String sufId;
	private String brName;
	private String aftName;
	private String preName;
	private Date createTime;
	private String subnetId;
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getInstanceId() {
		return instanceId;
	}
	public void setInstanceId(String instanceId) {
		this.instanceId = instanceId;
	}
	public String getNetcardId() {
		return netcardId;
	}
	public void setNetcardId(String netcardId) {
		this.netcardId = netcardId;
	}
	public String getIp() {
		return ip;
	}
	public void setIp(String ip) {
		this.ip = ip;
	}
	public String getSufId() {
		return sufId;
	}
	public void setSufId(String sufId) {
		this.sufId = sufId;
	}
	public String getBrName() {
		return brName;
	}
	public void setBrName(String brName) {
		this.brName = brName;
	}
	public String getAftName() {
		return aftName;
	}
	public void setAftName(String aftName) {
		this.aftName = aftName;
	}
	public String getPreName() {
		return preName;
	}
	public void setPreName(String preName) {
		this.preName = preName;
	}
	public Date getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	public String getSubnetId() {
		return subnetId;
	}
	public void setSubnetId(String subnetId) {
		this.subnetId = subnetId;
	}
}
