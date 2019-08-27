package com.gcloud.controller.compute.entity;

import com.gcloud.framework.db.jdbc.annotation.ID;
import com.gcloud.framework.db.jdbc.annotation.Table;

import java.util.Date;

@Table(name = "gc_instance_types")
public class InstanceType {

	@ID
	private String id;
	private String name;
	private Integer vcpus;
	private Integer memoryMb; // 单位MB
	private Date createdTime;
	private boolean deleted;

    public static final String ID = "id";
    public static final String NAME = "name";
    public static final String VCPUS = "vcpus";
    public static final String MEMORY_MB = "memoryMb";
    public static final String CREATED_TIME = "createdTime";
    public static final String DELETED = "deleted";

	public Integer getVcpus() {
		return vcpus;
	}

	public void setVcpus(Integer vcpus) {
		this.vcpus = vcpus;
	}

	public Date getCreatedTime() {
		return createdTime;
	}

	public void setCreatedTime(Date createdTime) {
		this.createdTime = createdTime;
	}

	public boolean isDeleted() {
		return deleted;
	}

	public void setDeleted(boolean deleted) {
		this.deleted = deleted;
	}

	public Integer getMemoryMb() {
		return memoryMb;
	}

	public void setMemoryMb(Integer memoryMb) {
		this.memoryMb = memoryMb;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

}
