/*
 * @Date 2015-5-13
 * 
 * @Author chenyu1@g-cloud.com.cn
 * 
 * @Copyright 2015 www.g-cloud.com.cn Inc. All rights reserved.
 * 
 * 
 */
package com.gcloud.header.compute.msg.node.vm.model;

import com.gcloud.header.storage.model.VmVolumeDetail;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class VmDetail implements Cloneable,Serializable{
	private String id; // uuid
	private String state;
	// 对cpu的配置
	private int core;
	private Integer cpuThread;
	private Integer cpuCore;
	private Integer cpuSocket;
	
	private int memory;
	private int disk;  // 系统盘和数据盘的qcow2大小除以1024，再四舍五入后乘以1024，单位：MB
	private String storageType;
	private String userId;
	private String platform;
	private String architecture;
	private Integer autoStart;
	private String imagePath;
	private String imageId;
	private String imageRefId;
	private String isoPath;
	private String isoId;
	private Integer vmDisableType;
	private String imagePoolId;
	private String imageStorageType;
	private String isoPoolId;
	private String isoStorageType;
	private String poolId;
	private Integer isUsbRedir;
	private String remotePort;
	private Boolean zxAuth;
	
	private List<VmVolumeDetail> vmDisks = new ArrayList<VmVolumeDetail>();

	private List<VmNetworkDetail> vmNetwork = new ArrayList<VmNetworkDetail>();

	private List<VmCdromDetail> vmCdrom = new ArrayList<VmCdromDetail>();

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public int getCore() {
		return core;
	}

	public void setCore(int core) {
		this.core = core;
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

	public String getPlatform() {
		return platform;
	}

	public void setPlatform(String platform) {
		this.platform = platform;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	@Override
	public String toString() {
		return "Instance [id=" + id + ", state=" + state + ", core=" + core + ", memory=" + memory + ", disk=" + disk + ", storageType=" + storageType + ", userId=" + userId + ", platform=" + platform + ", autoStart=" + autoStart + ", vmDisableType=" + vmDisableType + "]";
	}

	public Integer getAutoStart() {
		return autoStart;
	}

	public List<VmVolumeDetail> getVmDisks() {
		return vmDisks;
	}

	public void setVmDisks(List<VmVolumeDetail> vmDisks) {
		this.vmDisks = vmDisks;
	}

	public void setAutoStart(Integer autoStart) {
		this.autoStart = autoStart;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getImagePath() {
		return imagePath;
	}

	public void setImagePath(String imagePath) {
		this.imagePath = imagePath;
	}

	public String getImageId() {
		return imageId;
	}

	public void setImageId(String imageId) {
		this.imageId = imageId;
	}

	public String getIsoPath() {
		return isoPath;
	}

	public void setIsoPath(String isoPath) {
		this.isoPath = isoPath;
	}

	public String getIsoId() {
		return isoId;
	}

	public void setIsoId(String isoId) {
		this.isoId = isoId;
	}

	public List<VmNetworkDetail> getVmNetwork() {
		return vmNetwork;
	}

	public void setVmNetwork(List<VmNetworkDetail> vmNetwork) {
		this.vmNetwork = vmNetwork;
	}

	public Integer getVmDisableType() {
		return vmDisableType;
	}

	public void setVmDisableType(Integer vmDisableType) {
		this.vmDisableType = vmDisableType;
	}

	public String getImagePoolId() {
		return imagePoolId;
	}

	public void setImagePoolId(String imagePoolId) {
		this.imagePoolId = imagePoolId;
	}

	public String getIsoPoolId() {
		return isoPoolId;
	}

	public void setIsoPoolId(String isoPoolId) {
		this.isoPoolId = isoPoolId;
	}

	public String getPoolId() {
		return poolId;
	}

	public void setPoolId(String poolId) {
		this.poolId = poolId;
	}

	public String getArchitecture() {
		return architecture;
	}

	public void setArchitecture(String architecture) {
		this.architecture = architecture;
	}
	public List<VmCdromDetail> getVmCdrom() {
		return vmCdrom;
	}

	public void setVmCdrom(List<VmCdromDetail> vmCdrom) {
		this.vmCdrom = vmCdrom;
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

	public String getImageStorageType() {
		return imageStorageType;
	}

	public void setImageStorageType(String imageStorageType) {
		this.imageStorageType = imageStorageType;
	}

	public String getIsoStorageType() {
		return isoStorageType;
	}

	public void setIsoStorageType(String isoStorageType) {
		this.isoStorageType = isoStorageType;
	}

	public Boolean getZxAuth() {
		return zxAuth;
	}

	public void setZxAuth(Boolean zxAuth) {
		this.zxAuth = zxAuth;
	}

	public String getImageRefId() {
		return imageRefId;
	}

	public void setImageRefId(String imageRefId) {
		this.imageRefId = imageRefId;
	}

	public VmDetail clone() {
		VmDetail o = null;  
        try {  
            o = (VmDetail) super.clone();  
        } catch (CloneNotSupportedException e) {  
            e.printStackTrace();  
        }  
        return o;  
	}
}
