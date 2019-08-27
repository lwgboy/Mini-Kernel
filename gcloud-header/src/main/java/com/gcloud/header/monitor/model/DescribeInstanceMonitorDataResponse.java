package com.gcloud.header.monitor.model;

import java.io.Serializable;
import java.util.List;

public class DescribeInstanceMonitorDataResponse implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private List<InstanceMonitorDataType> instanceMonitorData;

	public List<InstanceMonitorDataType> getInstanceMonitorData() {
		return instanceMonitorData;
	}

	public void setInstanceMonitorData(List<InstanceMonitorDataType> instanceMonitorData) {
		this.instanceMonitorData = instanceMonitorData;
	}
	
}
