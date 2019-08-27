package com.gcloud.controller.compute.workflow.model.storage;

import com.gcloud.core.workflow.model.WorkflowFirstStepResException;
import com.gcloud.header.storage.model.VmVolumeDetail;

public class DetachDataDiskInitFlowCommandRes extends WorkflowFirstStepResException{

	private String taskId;
	private String instanceId;
	private String vmHostName;
	private String vmUserId;
	private String attachmentId;
	private String volumeId;
	private String volumeName;
	private VmVolumeDetail volumeDetail;

	public String getVolumeName() {
		return volumeName;
	}

	public void setVolumeName(String volumeName) {
		this.volumeName = volumeName;
	}

	public String getAttachmentId() {
		return attachmentId;
	}

	public void setAttachmentId(String attachmentId) {
		this.attachmentId = attachmentId;
	}

	public String getVmHostName() {
		return vmHostName;
	}

	public void setVmHostName(String vmHostName) {
		this.vmHostName = vmHostName;
	}

	public String getVmUserId() {
		return vmUserId;
	}

	public void setVmUserId(String vmUserId) {
		this.vmUserId = vmUserId;
	}

	public String getTaskId() {
		return taskId;
	}

	public void setTaskId(String taskId) {
		this.taskId = taskId;
	}

	public String getVolumeId() {
		return volumeId;
	}

	public void setVolumeId(String volumeId) {
		this.volumeId = volumeId;
	}

	public String getInstanceId() {
		return instanceId;
	}

	public void setInstanceId(String instanceId) {
		this.instanceId = instanceId;
	}

    public VmVolumeDetail getVolumeDetail() {
        return volumeDetail;
    }

    public void setVolumeDetail(VmVolumeDetail volumeDetail) {
        this.volumeDetail = volumeDetail;
    }
}
