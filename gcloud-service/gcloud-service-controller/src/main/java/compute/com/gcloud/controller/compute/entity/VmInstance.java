package com.gcloud.controller.compute.entity;

import com.gcloud.framework.db.jdbc.annotation.ID;
import com.gcloud.framework.db.jdbc.annotation.Table;

import java.io.Serializable;
import java.util.Date;


/*
 * @Date 2015-4-14
 *
 * @Author yaowj@g-cloud.com.cn
 *
 * @Copyright 2015 www.g-cloud.com.cn Inc. All rights reserved.
 *
 * @Description gc_instances 实体类
 */
@Table(name = "gc_instances")
public class VmInstance implements Serializable, Cloneable {

    /**
     *
     */
    private static final long serialVersionUID = -5370731326807626628L;
    @ID
    private String id;
    private String alias;
    private String userId;
    private String imageId;
    private Integer core;
    private Integer cpuCore;
    private Integer cpuThread;
    private Integer cpuSocket;
    private Integer memory;
    private Integer disk;
    private String hostname;
    private String state;
    private String lastState;
    private String stepState; //步骤状态 或者 简单的任务状态。 例如 开机， 或者是创建虚拟机中的开机步骤。
    private String taskState; //任务流的任务状态 例如 删除虚拟机
    private String architecture;
    private String platform;
    private String createType;
    private String createSourceId;
    private String storageType;
    private Date launchTime;
    private Date startTime;
    private String trashed;
    private String isoId;
    private Integer autostart;
    private String appName;
    private String appType;
    private String useDepartment;
    private Integer isHa;
    private Integer isFt;
    private Integer isLock;
    private String poolId;
    private String domain;
    private String appId;
    private String ip;
    private String floatingip;
    private String creator;
    private String userdata;
    private Integer restoreType;
    private Integer ieCache;
    private Integer autoLogin;
    private Integer vmDisableType;
    private String imagePoolId;
    private String imageStorageType;
    private Integer usbRedir;
    private String remotePort;
    private Integer usbType;
    private String instanceType;
    private String zoneId;
    private String tenantId;

    public static final String ID = "id";
    public static final String ALIAS = "alias";
    public static final String USER_ID = "userId";
    public static final String IMAGE_ID = "imageId";
    public static final String CORE = "core";
    public static final String CPU_CORE = "cpuCore";
    public static final String CPU_THREAD = "cpuThread";
    public static final String CPU_SOCKET = "cpuSocket";
    public static final String MEMORY = "memory";
    public static final String DISK = "disk";
    public static final String HOSTNAME = "hostname";
    public static final String STATE = "state";
    public static final String LAST_STATE = "lastState";
    public static final String STEP_STATE = "stepState";
    public static final String TASK_STATE = "taskState";
    public static final String ARCHITECTURE = "architecture";
    public static final String PLATFORM = "platform";
    public static final String CREATE_TYPE = "createType";
    public static final String CREATE_SOURCE_ID = "createSourceId";
    public static final String STORAGE_TYPE = "storageType";
    public static final String LAUNCH_TIME = "launchTime";
    public static final String START_TIME = "startTime";
    public static final String TRASHED = "trashed";
    public static final String ISO_ID = "isoId";
    public static final String AUTOSTART = "autostart";
    public static final String APP_NAME = "appName";
    public static final String APP_TYPE = "appType";
    public static final String USE_DEPARTMENT = "useDepartment";
    public static final String IS_HA = "isHa";
    public static final String IS_FT = "isFt";
    public static final String IS_LOCK = "isLock";
    public static final String POOL_ID = "poolId";
    public static final String DOMAIN = "domain";
    public static final String APP_ID = "appId";
    public static final String IP = "ip";
    public static final String FLOATINGIP = "floatingip";
    public static final String CREATOR = "creator";
    public static final String USERDATA = "userdata";
    public static final String RESTORE_TYPE = "restoreType";
    public static final String IE_CACHE = "ieCache";
    public static final String AUTO_LOGIN = "autoLogin";
    public static final String VM_DISABLE_TYPE = "vmDisableType";
    public static final String IMAGE_POOL_ID = "imagePoolId";
    public static final String IMAGE_STORAGE_TYPE = "imageStorageType";
    public static final String USB_REDIR = "usbRedir";
    public static final String REMOTE_PORT = "remotePort";
    public static final String USB_TYPE = "usbType";
    public static final String INSTANCE_TYPE = "instanceType";
    public static final String ZONE_ID = "zoneId";

    public String updateId(String id) {
        this.setId(id);
        return ID;
    }

    public String updateAlias(String alias) {
        this.setAlias(alias);
        return ALIAS;
    }

    public String updateUserId(String userId) {
        this.setUserId(userId);
        return USER_ID;
    }

    public String updateImageId(String imageId) {
        this.setImageId(imageId);
        return IMAGE_ID;
    }

    public String updateCore(Integer core) {
        this.setCore(core);
        return CORE;
    }

    public String updateCpuCore(Integer cpuCore) {
        this.setCpuCore(cpuCore);
        return CPU_CORE;
    }

    public String updateCpuThread(Integer cpuThread) {
        this.setCpuThread(cpuThread);
        return CPU_THREAD;
    }

    public String updateCpuSocket(Integer cpuSocket) {
        this.setCpuSocket(cpuSocket);
        return CPU_SOCKET;
    }

    public String updateMemory(Integer memory) {
        this.setMemory(memory);
        return MEMORY;
    }

    public String updateDisk(Integer disk) {
        this.setDisk(disk);
        return DISK;
    }

    public String updateHostname(String hostname) {
        this.setHostname(hostname);
        return HOSTNAME;
    }

    public String updateState(String state) {
        this.setState(state);
        return STATE;
    }

    public String updateLastState(String lastState) {
        this.setLastState(lastState);
        return LAST_STATE;
    }

    public String updateStepState(String stepState) {
        this.setStepState(stepState);
        return STEP_STATE;
    }

    public String updateTaskState(String taskState) {
        this.setTaskState(taskState);
        return TASK_STATE;
    }

    public String updateArchitecture(String architecture) {
        this.setArchitecture(architecture);
        return ARCHITECTURE;
    }

    public String updatePlatform(String platform) {
        this.setPlatform(platform);
        return PLATFORM;
    }

    public String updateCreateType(String createType) {
        this.setCreateType(createType);
        return CREATE_TYPE;
    }

    public String updateCreateSourceId(String createSourceId) {
        this.setCreateSourceId(createSourceId);
        return CREATE_SOURCE_ID;
    }

    public String updateStorageType(String storageType) {
        this.setStorageType(storageType);
        return STORAGE_TYPE;
    }

    public String updateLaunchTime(Date launchTime) {
        this.setLaunchTime(launchTime);
        return LAUNCH_TIME;
    }

    public String updateStartTime(Date startTime) {
        this.setStartTime(startTime);
        return START_TIME;
    }

    public String updateTrashed(String trashed) {
        this.setTrashed(trashed);
        return TRASHED;
    }

    public String updateIsoId(String isoId) {
        this.setIsoId(isoId);
        return ISO_ID;
    }

    public String updateAutostart(Integer autostart) {
        this.setAutostart(autostart);
        return AUTOSTART;
    }

    public String updateAppName(String appName) {
        this.setAppName(appName);
        return APP_NAME;
    }

    public String updateAppType(String appType) {
        this.setAppType(appType);
        return APP_TYPE;
    }

    public String updateUseDepartment(String useDepartment) {
        this.setUseDepartment(useDepartment);
        return USE_DEPARTMENT;
    }

    public String updateIsHa(Integer isHa) {
        this.setIsHa(isHa);
        return IS_HA;
    }

    public String updateIsFt(Integer isFt) {
        this.setIsFt(isFt);
        return IS_FT;
    }

    public String updateIsLock(Integer isLock) {
        this.setIsLock(isLock);
        return IS_LOCK;
    }

    public String updatePoolId(String poolId) {
        this.setPoolId(poolId);
        return POOL_ID;
    }

    public String updateDomain(String domain) {
        this.setDomain(domain);
        return DOMAIN;
    }

    public String updateAppId(String appId) {
        this.setAppId(appId);
        return APP_ID;
    }

    public String updateIp(String ip) {
        this.setIp(ip);
        return IP;
    }

    public String updateFloatingip(String floatingip) {
        this.setFloatingip(floatingip);
        return FLOATINGIP;
    }

    public String updateCreator(String creator) {
        this.setCreator(creator);
        return CREATOR;
    }

    public String updateUserdata(String userdata) {
        this.setUserdata(userdata);
        return USERDATA;
    }

    public String updateRestoreType(Integer restoreType) {
        this.setRestoreType(restoreType);
        return RESTORE_TYPE;
    }

    public String updateIeCache(Integer ieCache) {
        this.setIeCache(ieCache);
        return IE_CACHE;
    }

    public String updateAutoLogin(Integer autoLogin) {
        this.setAutoLogin(autoLogin);
        return AUTO_LOGIN;
    }

    public String updateVmDisableType(Integer vmDisableType) {
        this.setVmDisableType(vmDisableType);
        return VM_DISABLE_TYPE;
    }

    public String updateImagePoolId(String imagePoolId) {
        this.setImagePoolId(imagePoolId);
        return IMAGE_POOL_ID;
    }

    public String updateImageStorageType(String imageStorageType) {
        this.setImageStorageType(imageStorageType);
        return IMAGE_STORAGE_TYPE;
    }

    public String updateUsbRedir(Integer usbRedir) {
        this.setUsbRedir(usbRedir);
        return USB_REDIR;
    }

    public String updateRemotePort(String remotePort) {
        this.setRemotePort(remotePort);
        return REMOTE_PORT;
    }

    public String updateUsbType(Integer usbType) {
        this.setUsbType(usbType);
        return USB_TYPE;
    }

    public String updateInstanceType(String instanceType) {
        this.setInstanceType(instanceType);
        return INSTANCE_TYPE;
    }

    public String updateZoneId(String zoneId) {
        this.setZoneId(zoneId);
        return ZONE_ID;
    }


    public String getInstanceType() {
        return instanceType;
    }

    public void setInstanceType(String instanceType) {
        this.instanceType = instanceType;
    }

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

    public String getIsoId() {
        return isoId;
    }

    public void setIsoId(String isoId) {
        this.isoId = isoId;
    }

    public String getPoolId() {
        return poolId;
    }

    public void setPoolId(String poolId) {
        this.poolId = poolId;
    }

    public String getAppId() {
        return appId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
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

    public Date getLaunchTime() {
        return launchTime;
    }

    public void setLaunchTime(Date launchTime) {
        this.launchTime = launchTime;
    }

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
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

    public String getDomain() {
        return domain;
    }

    public void setDomain(String domain) {
        this.domain = domain;
    }

    public String getTrashed() {
        return trashed;
    }

    public void setTrashed(String trashed) {
        this.trashed = trashed;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public Integer getAutostart() {
        return autostart;
    }

    public void setAutostart(Integer autostart) {
        this.autostart = autostart;
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

    public Integer getRestoreType() {
        return restoreType;
    }

    public void setRestoreType(Integer restoreType) {
        this.restoreType = restoreType;
    }

    public Integer getIeCache() {
        return ieCache;
    }

    public void setIeCache(Integer ieCache) {
        this.ieCache = ieCache;
    }

    public Integer getAutoLogin() {
        return autoLogin;
    }

    public void setAutoLogin(Integer autoLogin) {
        this.autoLogin = autoLogin;
    }

    public Integer getVmDisableType() {
        return vmDisableType;
    }

    public void setVmDisableType(Integer vmDisableType) {
        this.vmDisableType = vmDisableType;
    }

    public String getFloatingip() {
        return floatingip;
    }

    public void setFloatingip(String floatingip) {
        this.floatingip = floatingip;
    }

    public Integer getIsLock() {
        return isLock;
    }

    public void setIsLock(Integer isLock) {
        this.isLock = isLock;
    }

    public String getImagePoolId() {
        return imagePoolId;
    }

    public void setImagePoolId(String imagePoolId) {
        this.imagePoolId = imagePoolId;
    }

    public Integer getUsbRedir() {
        return usbRedir;
    }

    public void setUsbRedir(Integer usbRedir) {
        this.usbRedir = usbRedir;
    }

    public String getRemotePort() {
        return remotePort;
    }

    public void setRemotePort(String remotePort) {
        this.remotePort = remotePort;
    }

    public Integer getCpuCore() {
        return cpuCore;
    }

    public void setCpuCore(Integer cpuCore) {
        this.cpuCore = cpuCore;
    }

    public Integer getCpuThread() {
        return cpuThread;
    }

    public void setCpuThread(Integer cpuThread) {
        this.cpuThread = cpuThread;
    }

    public Integer getCpuSocket() {
        return cpuSocket;
    }

    public void setCpuSocket(Integer cpuSocket) {
        this.cpuSocket = cpuSocket;
    }

    public String getStepState() {
        return stepState;
    }

    public void setStepState(String stepState) {
        this.stepState = stepState;
    }

    public Integer getUsbType() {
        return usbType;
    }

    public void setUsbType(Integer usbType) {
        this.usbType = usbType;
    }

    public String getZoneId() {
        return zoneId;
    }

    public void setZoneId(String zoneId) {
        this.zoneId = zoneId;
    }

    public String getStorageType() {
        return storageType;
    }

    public void setStorageType(String storageType) {
        this.storageType = storageType;
    }

    public String getImageStorageType() {
        return imageStorageType;
    }

    public void setImageStorageType(String imageStorageType) {
        this.imageStorageType = imageStorageType;
    }
    
    public String getTenantId() {
		return tenantId;
	}

	public void setTenantId(String tenantId) {
		this.tenantId = tenantId;
	}

	public VmInstance clone() {
        VmInstance o = null;
        try {
            o = (VmInstance) super.clone();
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }
        return o;
    }
}
