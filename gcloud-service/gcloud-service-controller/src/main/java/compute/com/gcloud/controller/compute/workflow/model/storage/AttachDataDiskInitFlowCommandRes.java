package com.gcloud.controller.compute.workflow.model.storage;

import com.gcloud.core.workflow.model.WorkflowFirstStepResException;
import com.gcloud.header.storage.model.VmVolumeDetail;

public class AttachDataDiskInitFlowCommandRes extends WorkflowFirstStepResException{

	private String taskId;
	private String instanceId;
	private String vmHostName;
	private VmVolumeDetail volumeDetail;
	private String volumeId;
	private String volumeName;

	public String getVolumeId() {
		return volumeId;
	}

	public void setVolumeId(String volumeId) {
		this.volumeId = volumeId;
	}

	public String getVolumeName() {
		return volumeName;
	}

	public void setVolumeName(String volumeName) {
		this.volumeName = volumeName;
	}

	public String getVmHostName() {
		return vmHostName;
	}

	public void setVmHostName(String vmHostName) {
		this.vmHostName = vmHostName;
	}

	public String getTaskId() {
		return taskId;
	}

	public void setTaskId(String taskId) {
		this.taskId = taskId;
	}

	public VmVolumeDetail getVolumeDetail() {
		return volumeDetail;
	}

	public void setVolumeDetail(VmVolumeDetail volumeDetail) {
		this.volumeDetail = volumeDetail;
	}

	public String getInstanceId() {
		return instanceId;
	}

	public void setInstanceId(String instanceId) {
		this.instanceId = instanceId;
	}

}
