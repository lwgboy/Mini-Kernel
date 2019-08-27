package com.gcloud.header.compute.msg.api.vm.base;

import java.util.List;

import com.gcloud.header.ApiPageMessage;
import com.gcloud.header.api.ApiModel;

public class ApiDescribeInstancesMsg extends ApiPageMessage{
	@ApiModel(description="实例名称")
	private String instanceName;
	@ApiModel(description="实例状态")
	private String status;
	@ApiModel(description="可用区ID")
	private String zoneId;
	@ApiModel(description="实例ID列表")
	private List<String> instanceIds;

	@Override
	public Class replyClazz() {
		return ApiDescribeInstancesReplyMsg.class;
	}

	public String getInstanceName() {
		return instanceName;
	}

	public void setInstanceName(String instanceName) {
		this.instanceName = instanceName;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getZoneId() {
		return zoneId;
	}

	public void setZoneId(String zoneId) {
		this.zoneId = zoneId;
	}

	public List<String> getInstanceIds() {
		return instanceIds;
	}

	public void setInstanceIds(List<String> instanceIds) {
		this.instanceIds = instanceIds;
	}
	
	
}
