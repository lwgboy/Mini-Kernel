package com.gcloud.controller.network.model;

public class AllocateEipAddressResponse {
	private String allocationId;
    private String allocationRefId;
	private String eipAddress;
	private String routerId;
	private String portId;
	private String status;
	
	public String getAllocationId() {
		return allocationId;
	}
	public void setAllocationId(String allocationId) {
		this.allocationId = allocationId;
	}
	public String getAllocationRefId() {
        return allocationRefId;
    }
    public void setAllocationRefId(String allocationRefId) {
        this.allocationRefId = allocationRefId;
    }
    public String getEipAddress() {
		return eipAddress;
	}
	public void setEipAddress(String eipAddress) {
		this.eipAddress = eipAddress;
	}
	public String getRouterId() {
		return routerId;
	}
	public void setRouterId(String routerId) {
		this.routerId = routerId;
	}
	public String getPortId() {
		return portId;
	}
	public void setPortId(String portId) {
		this.portId = portId;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
}
