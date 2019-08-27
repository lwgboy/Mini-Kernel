package com.gcloud.header.monitor.model;

import java.io.Serializable;

public class DiskMonitorDataType implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private String diskId;
	private Integer bpsRead;
	private Integer bpsWrite;
	private Integer bpsTotal;
	private String timeStamp;
	
	public String getDiskId() {
		return diskId;
	}
	public void setDiskId(String diskId) {
		this.diskId = diskId;
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
	public Integer getBpsTotal() {
		return bpsTotal;
	}
	public void setBpsTotal(Integer bpsTotal) {
		this.bpsTotal = bpsTotal;
	}
	public String getTimeStamp() {
		return timeStamp;
	}
	public void setTimeStamp(String timeStamp) {
		this.timeStamp = timeStamp;
	}
	
}
