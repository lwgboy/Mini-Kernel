package com.gcloud.header.security.model;

import java.io.Serializable;

public class SecurityClusterInstanceType implements Serializable {

    private static final long serialVersionUID = 1L;
    
    private String id;
    private String alias;
    private String userId;
    private String imageId;
    private Integer core;
    private Integer memory;
    private Integer disk;
    private String hostname;
    private String state;
    private String lastState;
    private String taskState; //任务流的任务状态 例如 删除虚拟机
    private String architecture;
    private String platform;
    private String createType;
    private String createSourceId;
    private String storageType;
    private String launchTime;
    private String startTime;
    private String trashed;
    private String isoId;
    private Integer autostart;
    private String appName;
    private String appType;
    private String useDepartment;
    private Integer isHa;
    private Integer isFt;
    private String poolId;
    private String domain;
    private String appId;
    private String ip;
    private String creator;
    private String userdata;
    
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getAlias() {
		return alias;
	}
	public void setAlias(String alias) {
		this.alias = alias;
	}
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public String getImageId() {
		return imageId;
	}
	public void setImageId(String imageId) {
		this.imageId = imageId;
	}
	public Integer getCore() {
		return core;
	}
	public void setCore(Integer core) {
		this.core = core;
	}
	public Integer getMemory() {
		return memory;
	}
	public void setMemory(Integer memory) {
		this.memory = memory;
	}
	public Integer getDisk() {
		return disk;
	}
	public void setDisk(Integer disk) {
		this.disk = disk;
	}
	public String getHostname() {
		return hostname;
	}
	public void setHostname(String hostname) {
		this.hostname = hostname;
	}
	public String getState() {
		return state;
	}
	public void setState(String state) {
		this.state = state;
	}
	public String getLastState() {
		return lastState;
	}
	public void setLastState(String lastState) {
		this.lastState = lastState;
	}
	public String getTaskState() {
		return taskState;
	}
	public void setTaskState(String taskState) {
		this.taskState = taskState;
	}
	public String getArchitecture() {
		return architecture;
	}
	public void setArchitecture(String architecture) {
		this.architecture = architecture;
	}
	public String getPlatform() {
		return platform;
	}
	public void setPlatform(String platform) {
		this.platform = platform;
	}
	public String getCreateType() {
		return createType;
	}
	public void setCreateType(String createType) {
		this.createType = createType;
	}
	public String getCreateSourceId() {
		return createSourceId;
	}
	public void setCreateSourceId(String createSourceId) {
		this.createSourceId = createSourceId;
	}
	public String getStorageType() {
		return storageType;
	}
	public void setStorageType(String storageType) {
		this.storageType = storageType;
	}
	public String getLaunchTime() {
		return launchTime;
	}
	public void setLaunchTime(String launchTime) {
		this.launchTime = launchTime;
	}
	public String getStartTime() {
		return startTime;
	}
	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}
	public String getTrashed() {
		return trashed;
	}
	public void setTrashed(String trashed) {
		this.trashed = trashed;
	}
	public String getIsoId() {
		return isoId;
	}
	public void setIsoId(String isoId) {
		this.isoId = isoId;
	}
	public Integer getAutostart() {
		return autostart;
	}
	public void setAutostart(Integer autostart) {
		this.autostart = autostart;
	}
	public String getAppName() {
		return appName;
	}
	public void setAppName(String appName) {
		this.appName = appName;
	}
	public String getAppType() {
		return appType;
	}
	public void setAppType(String appType) {
		this.appType = appType;
	}
	public String getUseDepartment() {
		return useDepartment;
	}
	public void setUseDepartment(String useDepartment) {
		this.useDepartment = useDepartment;
	}
	public Integer getIsHa() {
		return isHa;
	}
	public void setIsHa(Integer isHa) {
		this.isHa = isHa;
	}
	public Integer getIsFt() {
		return isFt;
	}
	public void setIsFt(Integer isFt) {
		this.isFt = isFt;
	}
	public String getPoolId() {
		return poolId;
	}
	public void setPoolId(String poolId) {
		this.poolId = poolId;
	}
	public String getDomain() {
		return domain;
	}
	public void setDomain(String domain) {
		this.domain = domain;
	}
	public String getAppId() {
		return appId;
	}
	public void setAppId(String appId) {
		this.appId = appId;
	}
	public String getIp() {
		return ip;
	}
	public void setIp(String ip) {
		this.ip = ip;
	}
	public String getCreator() {
		return creator;
	}
	public void setCreator(String creator) {
		this.creator = creator;
	}
	public String getUserdata() {
		return userdata;
	}
	public void setUserdata(String userdata) {
		this.userdata = userdata;
	}

//    private String id;
//    private String clusterId;
//    private String instanceId;
//    private String alias;
//    private String userId;
//    private String imageId;
//    private Integer core;
//    private Integer memory;
//    private Integer disk;
//    private String hostName;
//    private String state;
//    private String stateCnName;
//    private String platform;
//    private String ip;
//    private String floatingip;
//    private String launchTime;
//    private String creator;
//    
////  缺少下面的参数
////	authFuns: null
////	imageName: null
////	netcardIds: ["05576916-ccb3-4b9c-8e99-13502e32b171", "c3e9dcbf-89d7-4404-be87-3146415e353d"]
////	vmDisableReason: null
//    
//	public String getId() {
//		return id;
//	}
//	public void setId(String id) {
//		this.id = id;
//	}
//	public String getClusterId() {
//		return clusterId;
//	}
//	public void setClusterId(String clusterId) {
//		this.clusterId = clusterId;
//	}
//	public String getInstanceId() {
//		return instanceId;
//	}
//	public void setInstanceId(String instanceId) {
//		this.instanceId = instanceId;
//	}
//	public String getAlias() {
//		return alias;
//	}
//	public void setAlias(String alias) {
//		this.alias = alias;
//	}
//	public String getUserId() {
//		return userId;
//	}
//	public void setUserId(String userId) {
//		this.userId = userId;
//	}
//	public String getImageId() {
//		return imageId;
//	}
//	public void setImageId(String imageId) {
//		this.imageId = imageId;
//	}
//	public Integer getCore() {
//		return core;
//	}
//	public void setCore(Integer core) {
//		this.core = core;
//	}
//	public Integer getMemory() {
//		return memory;
//	}
//	public void setMemory(Integer memory) {
//		this.memory = memory;
//	}
//	public Integer getDisk() {
//		return disk;
//	}
//	public void setDisk(Integer disk) {
//		this.disk = disk;
//	}
//	public String getHostName() {
//		return hostName;
//	}
//	public void setHostName(String hostName) {
//		this.hostName = hostName;
//	}
//	public String getState() {
//		return state;
//	}
//	public void setState(String state) {
//		this.state = state;
//	}
//	public String getStateCnName() {
//		return stateCnName;
//	}
//	public void setStateCnName(String stateCnName) {
//		this.stateCnName = stateCnName;
//	}
//	public String getPlatform() {
//		return platform;
//	}
//	public void setPlatform(String platform) {
//		this.platform = platform;
//	}
//	public String getIp() {
//		return ip;
//	}
//	public void setIp(String ip) {
//		this.ip = ip;
//	}
//	public String getFloatingip() {
//		return floatingip;
//	}
//	public void setFloatingip(String floatingip) {
//		this.floatingip = floatingip;
//	}
//	public String getLaunchTime() {
//		return launchTime;
//	}
//	public void setLaunchTime(String launchTime) {
//		this.launchTime = launchTime;
//	}
//	public String getCreator() {
//		return creator;
//	}
//	public void setCreator(String creator) {
//		this.creator = creator;
//	}
}
