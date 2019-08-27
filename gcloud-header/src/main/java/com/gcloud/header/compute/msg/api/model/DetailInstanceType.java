package com.gcloud.header.compute.msg.api.model;

import java.io.Serializable;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

public class DetailInstanceType implements Serializable{
	private static final long serialVersionUID = 1L;
	
	private String id;
	private String name;
	@JsonProperty(value = "CpuCoreCount")
	private Integer vcpus;
	@JsonProperty(value = "MemorySize")
	private Double memoryMb; // 单位MB
	private List<ZoneInfo> availableZones;
	
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
	public Integer getVcpus() {
		return vcpus;
	}
	public void setVcpus(Integer vcpus) {
		this.vcpus = vcpus;
	}
	public Double getMemoryMb() {
		return memoryMb;
	}
	public void setMemoryMb(Double memoryMb) {
		this.memoryMb = memoryMb;
	}
	public List<ZoneInfo> getAvailableZones() {
		return availableZones;
	}
	public void setAvailableZones(List<ZoneInfo> availableZones) {
		this.availableZones = availableZones;
	}
}
