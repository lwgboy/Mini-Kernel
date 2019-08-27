package com.gcloud.header.monitor.model;

import java.io.Serializable;
import java.util.List;

public class DescribeDiskMonitorDataResponse implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private List<DiskMonitorDataType> diskMonitorDataType;

	public List<DiskMonitorDataType> getDiskMonitorDataType() {
		return diskMonitorDataType;
	}

	public void setDiskMonitorDataType(List<DiskMonitorDataType> diskMonitorDataType) {
		this.diskMonitorDataType = diskMonitorDataType;
	}
}
