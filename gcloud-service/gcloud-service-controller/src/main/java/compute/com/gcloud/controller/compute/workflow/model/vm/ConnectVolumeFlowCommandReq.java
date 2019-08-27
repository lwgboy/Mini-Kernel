package com.gcloud.controller.compute.workflow.model.vm;

import com.gcloud.header.compute.msg.api.model.DiskInfo;

public class ConnectVolumeFlowCommandReq {
	private DiskInfo dataDisk;
	private String hostName;
	private String zoneId;
	public DiskInfo getDataDisk() {
		return dataDisk;
	}
	public void setDataDisk(DiskInfo dataDisk) {
		this.dataDisk = dataDisk;
	}
	public String getHostName() {
		return hostName;
	}
	public void setHostName(String hostName) {
		this.hostName = hostName;
	}
	public String getZoneId() {
		return zoneId;
	}
	public void setZoneId(String zoneId) {
		this.zoneId = zoneId;
	}
	
}
