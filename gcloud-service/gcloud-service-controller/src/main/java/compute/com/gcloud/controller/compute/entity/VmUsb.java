package com.gcloud.controller.compute.entity;

import java.util.Date;

import com.gcloud.framework.db.jdbc.annotation.ID;
import com.gcloud.framework.db.jdbc.annotation.Table;


/*
 * @Date 2015-5-30
 * 
 * @Author yaowj@g-cloud.com.cn
 * 
 * @Copyright 2015 www.g-cloud.com.cn Inc. All rights reserved.
 * 
 * @Description gc_vm_usb 实体类
 */
@Table(name = "gc_vm_usb",jdbc="controllerJdbcTemplate")
public class VmUsb {
	
	@ID
	private Integer id;
	private String instanceId;
	private String sn;
	private String name;
	private String state;
	private String manufacturer;
	private Date startTime;
	private Integer maxSize;
	private String hostName;
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getInstanceId() {
		return instanceId;
	}
	public void setInstanceId(String instanceId) {
		this.instanceId = instanceId;
	}
	public String getSn() {
		return sn;
	}
	public void setSn(String sn) {
		this.sn = sn;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getState() {
		return state;
	}
	public void setState(String state) {
		this.state = state;
	}
	public String getManufacturer() {
		return manufacturer;
	}
	public void setManufacturer(String manufacturer) {
		this.manufacturer = manufacturer;
	}
	public Date getStartTime() {
		return startTime;
	}
	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}
	public Integer getMaxSize() {
		return maxSize;
	}
	public void setMaxSize(Integer maxSize) {
		this.maxSize = maxSize;
	}
	public String getHostName() {
		return hostName;
	}
	public void setHostName(String hostName) {
		this.hostName = hostName;
	} 

	
	
}
