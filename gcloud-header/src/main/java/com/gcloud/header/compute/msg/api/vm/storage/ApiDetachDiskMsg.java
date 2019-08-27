package com.gcloud.header.compute.msg.api.vm.storage;

import com.gcloud.header.ApiMessage;
import com.gcloud.header.api.ApiModel;

import javax.validation.constraints.NotNull;

public class ApiDetachDiskMsg extends ApiMessage {
	@ApiModel(description = "云服务器ID", require = true)
	@NotNull(message = "0011001::云服务器ID不能为空")
	private String instanceId;
	@ApiModel(description = "磁盘ID", require = true)
	@NotNull(message = "0011002::磁盘ID不能为空")
	private String diskId;

	@Override
	public Class replyClazz() {
		return ApiDetachDiskReplyMsg.class;
	}

	public String getInstanceId() {
		return instanceId;
	}

	public void setInstanceId(String instanceId) {
		this.instanceId = instanceId;
	}

	public String getDiskId() {
		return diskId;
	}

	public void setDiskId(String diskId) {
		this.diskId = diskId;
	}
}
