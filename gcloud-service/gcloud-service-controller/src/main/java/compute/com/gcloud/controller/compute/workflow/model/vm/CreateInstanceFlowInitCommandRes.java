package com.gcloud.controller.compute.workflow.model.vm;

import com.gcloud.controller.compute.model.vm.VmImageInfo;
import com.gcloud.core.workflow.model.WorkflowFirstStepResException;
import com.gcloud.header.api.model.CurrentUser;
import com.gcloud.header.compute.msg.api.model.DiskInfo;

import java.util.List;

public class CreateInstanceFlowInitCommandRes extends WorkflowFirstStepResException{
	private String instanceId;
	private String createHost;
	private String storageType;

	private String instancePath;

	private int remotePort;

	private List<DiskInfo> repeatParams;

	private DiskInfo systemDisk;

	private VmImageInfo imageInfo;

	private Integer cpu;
	private Integer memory;
	private Integer systemDiskSize;
	private List<DiskInfo> dataDisk;

	// 用于记录日志
	private String taskId;
	private String instanceName;
	private CurrentUser createUser;

	public DiskInfo getSystemDisk() {
		return systemDisk;
	}

	public void setSystemDisk(DiskInfo systemDisk) {
		this.systemDisk = systemDisk;
	}

	public String getInstanceId() {
		return instanceId;
	}

	public void setInstanceId(String instanceId) {
		this.instanceId = instanceId;
	}

	public String getCreateHost() {
		return createHost;
	}

	public void setCreateHost(String createHost) {
		this.createHost = createHost;
	}

	public String getInstancePath() {
		return instancePath;
	}

	public void setInstancePath(String instancePath) {
		this.instancePath = instancePath;
	}

	public int getRemotePort() {
		return remotePort;
	}

	public void setRemotePort(int remotePort) {
		this.remotePort = remotePort;
	}

	public List<DiskInfo> getRepeatParams() {
		return repeatParams;
	}

	public void setRepeatParams(List<DiskInfo> repeatParams) {
		this.repeatParams = repeatParams;
	}

	public String getTaskId() {
		return taskId;
	}

	public void setTaskId(String taskId) {
		this.taskId = taskId;
	}

	public String getInstanceName() {
		return instanceName;
	}

	public void setInstanceName(String instanceName) {
		this.instanceName = instanceName;
	}

    public String getStorageType() {
        return storageType;
    }

    public void setStorageType(String storageType) {
        this.storageType = storageType;
    }

    public VmImageInfo getImageInfo() {
        return imageInfo;
    }

    public void setImageInfo(VmImageInfo imageInfo) {
        this.imageInfo = imageInfo;
    }

	public Integer getCpu() {
		return cpu;
	}

	public void setCpu(Integer cpu) {
		this.cpu = cpu;
	}

	public Integer getMemory() {
		return memory;
	}

	public void setMemory(Integer memory) {
		this.memory = memory;
	}

	public Integer getSystemDiskSize() {
		return systemDiskSize;
	}

	public void setSystemDiskSize(Integer systemDiskSize) {
		this.systemDiskSize = systemDiskSize;
	}

	public List<DiskInfo> getDataDisk() {
		return dataDisk;
	}

	public void setDataDisk(List<DiskInfo> dataDisk) {
		this.dataDisk = dataDisk;
	}

	public CurrentUser getCreateUser() {
		return createUser;
	}

	public void setCreateUser(CurrentUser createUser) {
		this.createUser = createUser;
	}
}
