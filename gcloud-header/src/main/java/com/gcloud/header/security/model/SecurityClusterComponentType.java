package com.gcloud.header.security.model;

import java.io.Serializable;

public class SecurityClusterComponentType implements Serializable {

    private static final long serialVersionUID = 1L;

    private String id;
    private String clusterId;
    private String type;
    private String objectId;
    private String objectType;
    private String state;
    private String stateCnName;
    private String stateInfo;
    private String createUser;
    private String createConfig;
    private String createTime;
    private String updateTime;
    private Boolean ha;
    
    private SecurityClusterInstanceType vmInfo;
    private SecurityClusterDcType dcInfo;
    
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getClusterId() {
		return clusterId;
	}
	public void setClusterId(String clusterId) {
		this.clusterId = clusterId;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getObjectId() {
		return objectId;
	}
	public void setObjectId(String objectId) {
		this.objectId = objectId;
	}
	public String getObjectType() {
		return objectType;
	}
	public void setObjectType(String objectType) {
		this.objectType = objectType;
	}
	public String getState() {
		return state;
	}
	public void setState(String state) {
		this.state = state;
	}
	public String getStateCnName() {
		return stateCnName;
	}
	public void setStateCnName(String stateCnName) {
		this.stateCnName = stateCnName;
	}
	public String getStateInfo() {
		return stateInfo;
	}
	public void setStateInfo(String stateInfo) {
		this.stateInfo = stateInfo;
	}
	public String getCreateUser() {
		return createUser;
	}
	public void setCreateUser(String createUser) {
		this.createUser = createUser;
	}
	public String getCreateConfig() {
		return createConfig;
	}
	public void setCreateConfig(String createConfig) {
		this.createConfig = createConfig;
	}
	public String getCreateTime() {
		return createTime;
	}
	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}
	public String getUpdateTime() {
		return updateTime;
	}
	public void setUpdateTime(String updateTime) {
		this.updateTime = updateTime;
	}
	public Boolean getHa() {
		return ha;
	}
	public void setHa(Boolean ha) {
		this.ha = ha;
	}
	public SecurityClusterInstanceType getVmInfo() {
		return vmInfo;
	}
	public void setVmInfo(SecurityClusterInstanceType vmInfo) {
		this.vmInfo = vmInfo;
	}
	public SecurityClusterDcType getDcInfo() {
		return dcInfo;
	}
	public void setDcInfo(SecurityClusterDcType dcInfo) {
		this.dcInfo = dcInfo;
	}
}
