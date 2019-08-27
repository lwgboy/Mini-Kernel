package com.gcloud.header.monitor.model;

import java.io.Serializable;

public class InstanceMonitorDataType implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private String instanceId;
	private Integer cpu;
	private Integer intranetRX;
	private Integer intranetTX;
	private Integer bpsRead;
	private Integer bpsWrite;
	private String timeStamp;

	public String getInstanceId() {
		return instanceId;
	}

	public void setInstanceId(String instanceId) {
		this.instanceId = instanceId;
	}

	public Integer getCpu() {
		return cpu;
	}

	public void setCpu(Integer cpu) {
		this.cpu = cpu;
	}

	public Integer getIntranetRX() {
		return intranetRX;
	}

	public void setIntranetRX(Integer intranetRX) {
		this.intranetRX = intranetRX;
	}

	public Integer getIntranetTX() {
		return intranetTX;
	}

	public void setIntranetTX(Integer intranetTX) {
		this.intranetTX = intranetTX;
	}

	public Integer getBpsRead() {
		return bpsRead;
	}

	public void setBpsRead(Integer bpsRead) {
		this.bpsRead = bpsRead;
	}

	public Integer getBpsWrite() {
		return bpsWrite;
	}

	public void setBpsWrite(Integer bpsWrite) {
		this.bpsWrite = bpsWrite;
	}

	public String getTimeStamp() {
		return timeStamp;
	}

	public void setTimeStamp(String timeStamp) {
		this.timeStamp = timeStamp;
	}

}
