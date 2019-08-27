package com.gcloud.controller.compute.workflow.model.vm;

import com.gcloud.controller.compute.model.vm.VmImageInfo;
import com.gcloud.header.api.model.CurrentUser;
import com.gcloud.header.compute.msg.node.vm.model.VmNetworkDetail;
import com.gcloud.header.storage.model.VmVolumeDetail;

import java.util.List;

public class BuildVmConfigFlowCommandReq {
	private String createHost;
	
	private String instanceId;
//	private String instanceUuid; // uuid
	// 对cpu的配置
	private int core;
	private Integer cpuThread = 1;
	private Integer cpuCore = 1;
	private Integer cpuSocket;
	
	private int memory;
	private int disk;  // 系统盘和数据盘的qcow2大小除以1024，再四舍五入后乘以1024，单位：MB
	private String storageType;
	private CurrentUser createUser;
	private Integer autoStart = 1;
	private String poolId;
	private Integer isUsbRedir = 0;
	private String remotePort;

	private VmImageInfo imageInfo;

	private Boolean zxAuth;

	
	private VmVolumeDetail vmSysDisk;
	
	private List<VmVolumeDetail> vmDataDisks;
	
	private List<VmNetworkDetail> vmNetworks;

	public String getCreateHost() {
		return createHost;
	}

	public void setCreateHost(String createHost) {
		this.createHost = createHost;
	}

	public String getInstanceId() {
		return instanceId;
	}

	public void setInstanceId(String instanceId) {
		this.instanceId = instanceId;
	}

	/*public String getInstanceUuid() {
		return instanceUuid;
	}

	public void setInstanceUuid(String instanceUuid) {
		this.instanceUuid = instanceUuid;
	}*/

	public int getCore() {
		return core;
	}

	public void setCore(int core) {
		this.core = core;
	}

	public Integer getCpuThread() {
		return cpuThread;
	}

	public void setCpuThread(Integer cpuThread) {
		this.cpuThread = cpuThread;
	}

	public Integer getCpuCore() {
		return cpuCore;
	}

	public void setCpuCore(Integer cpuCore) {
		this.cpuCore = cpuCore;
	}

	public Integer getCpuSocket() {
		return cpuSocket;
	}

	public void setCpuSocket(Integer cpuSocket) {
		this.cpuSocket = cpuSocket;
	}

	public int getMemory() {
		return memory;
	}

	public void setMemory(int memory) {
		this.memory = memory;
	}

	public int getDisk() {
		return disk;
	}

	public void setDisk(int disk) {
		this.disk = disk;
	}

	public String getStorageType() {
		return storageType;
	}

	public void setStorageType(String storageType) {
		this.storageType = storageType;
	}

	public CurrentUser getCreateUser() {
		return createUser;
	}

	public void setCreateUser(CurrentUser createUser) {
		this.createUser = createUser;
	}

	public Integer getAutoStart() {
		return autoStart;
	}

	public void setAutoStart(Integer autoStart) {
		this.autoStart = autoStart;
	}

	public String getPoolId() {
		return poolId;
	}

	public void setPoolId(String poolId) {
		this.poolId = poolId;
	}

	public Integer getIsUsbRedir() {
		return isUsbRedir;
	}

	public void setIsUsbRedir(Integer isUsbRedir) {
		this.isUsbRedir = isUsbRedir;
	}

	public String getRemotePort() {
		return remotePort;
	}

	public void setRemotePort(String remotePort) {
		this.remotePort = remotePort;
	}

	public VmVolumeDetail getVmSysDisk() {
		return vmSysDisk;
	}

	public void setVmSysDisk(VmVolumeDetail vmSysDisk) {
		this.vmSysDisk = vmSysDisk;
	}

	public List<VmVolumeDetail> getVmDataDisks() {
		return vmDataDisks;
	}

	public void setVmDataDisks(List<VmVolumeDetail> vmDataDisks) {
		this.vmDataDisks = vmDataDisks;
	}

	public List<VmNetworkDetail> getVmNetworks() {
		return vmNetworks;
	}

	public void setVmNetworks(List<VmNetworkDetail> vmNetworks) {
		this.vmNetworks = vmNetworks;
	}

    public VmImageInfo getImageInfo() {
        return imageInfo;
    }

    public void setImageInfo(VmImageInfo imageInfo) {
        this.imageInfo = imageInfo;
    }

	public Boolean getZxAuth() {
		return zxAuth;
	}

	public void setZxAuth(Boolean zxAuth) {
		this.zxAuth = zxAuth;
	}
}
