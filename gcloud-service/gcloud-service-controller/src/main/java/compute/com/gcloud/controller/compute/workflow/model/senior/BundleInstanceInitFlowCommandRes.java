package com.gcloud.controller.compute.workflow.model.senior;

import com.gcloud.core.workflow.model.WorkflowFirstStepResException;
import com.gcloud.header.storage.model.VmVolumeDetail;

/**
 * Created by yaowj on 2018/11/30.
 */
public class BundleInstanceInitFlowCommandRes extends WorkflowFirstStepResException{

    private String imageFilePath;
    private String osType;
    private String architecture;
    private String description;
    private String beginningState;

    private String nodeIp;
    private String tenantId;

    private String taskId;

    private String imageId;
    private String imageName;

    private VmVolumeDetail volumeDetail;

    public String getImageFilePath() {
        return imageFilePath;
    }

    public void setImageFilePath(String imageFilePath) {
        this.imageFilePath = imageFilePath;
    }

    public String getOsType() {
        return osType;
    }

    public void setOsType(String osType) {
        this.osType = osType;
    }

    public String getArchitecture() {
        return architecture;
    }

    public void setArchitecture(String architecture) {
        this.architecture = architecture;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getBeginningState() {
        return beginningState;
    }

    public void setBeginningState(String beginningState) {
        this.beginningState = beginningState;
    }

    public String getNodeIp() {
        return nodeIp;
    }

    public void setNodeIp(String nodeIp) {
        this.nodeIp = nodeIp;
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

	public String getTenantId() {
		return tenantId;
	}

	public void setTenantId(String tenantId) {
		this.tenantId = tenantId;
	}

    public String getImageId() {
        return imageId;
    }

    public void setImageId(String imageId) {
        this.imageId = imageId;
    }

    public String getImageName() {
        return imageName;
    }

    public void setImageName(String imageName) {
        this.imageName = imageName;
    }
}
