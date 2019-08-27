package com.gcloud.header.compute.msg.api.model;

import java.io.Serializable;

import com.gcloud.header.compute.msg.api.vm.zone.AvailableResource;

public class DetailZone implements Serializable{
	private static final long serialVersionUID = 1L;
	
	private String id;
    private String zoneName;
    private AvailableResource availableResource;
    
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getZoneName() {
		return zoneName;
	}
	public void setZoneName(String zoneName) {
		this.zoneName = zoneName;
	}
	public AvailableResource getAvailableResource() {
		return availableResource;
	}
	public void setAvailableResource(AvailableResource availableResource) {
		this.availableResource = availableResource;
	}
}
