package com.gcloud.controller.security.entity;


import com.gcloud.framework.db.jdbc.annotation.ID;
import com.gcloud.framework.db.jdbc.annotation.Table;

import java.util.Date;

/**
 * Created by yaowj on 2018/7/13.
 */
@Table(name = "gc_security_cluster_component")
public class SecurityClusterComponent {

    @ID
    private String id;
    private String clusterId;
    private String objectId;
    private String objectType;
    private String type;
    private Boolean ha;
    private Date createTime;
    private String createUser;
    private Date updateTime;
    private String stateInfo;
    private String state;
    private String createConfig;

    public static final String ID = "id";
    public static final String CLUSTER_ID = "clusterId";
    public static final String OBJECT_ID = "objectId";
    public static final String OBJECT_TYPE = "objectType";
    public static final String TYPE = "type";
    public static final String HA = "ha";
    public static final String CREATE_TIME = "createTime";
    public static final String CREATE_USER = "createUser";
    public static final String UPDATE_TIME = "updateTime";
    public static final String STATE_INFO = "stateInfo";
    public static final String STATE = "state";
    public static final String CREATE_CONFIG = "createConfig";


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

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getCreateUser() {
        return createUser;
    }

    public void setCreateUser(String createUser) {
        this.createUser = createUser;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public Boolean getHa() {
        return ha;
    }

    public void setHa(Boolean ha) {
        this.ha = ha;
    }

    public String getStateInfo() {
        return stateInfo;
    }

    public void setStateInfo(String stateInfo) {
        this.stateInfo = stateInfo;
    }

    public String getCreateConfig() {
        return createConfig;
    }

    public void setCreateConfig(String createConfig) {
        this.createConfig = createConfig;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
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

    public String updateId(String id) {
        this.setId(id);
        return ID;
    }

    public String updateClusterId(String clusterId) {
        this.setClusterId(clusterId);
        return CLUSTER_ID;
    }

    public String updateObjectId(String objectId) {
        this.setObjectId(objectId);
        return OBJECT_ID;
    }

    public String updateObjectType(String objectType) {
        this.setObjectType(objectType);
        return OBJECT_TYPE;
    }

    public String updateType(String type) {
        this.setType(type);
        return TYPE;
    }

    public String updateHa(Boolean ha) {
        this.setHa(ha);
        return HA;
    }

    public String updateCreateTime(Date createTime) {
        this.setCreateTime(createTime);
        return CREATE_TIME;
    }

    public String updateCreateUser(String createUser) {
        this.setCreateUser(createUser);
        return CREATE_USER;
    }

    public String updateUpdateTime(Date updateTime) {
        this.setUpdateTime(updateTime);
        return UPDATE_TIME;
    }

    public String updateStateInfo(String stateInfo) {
        this.setStateInfo(stateInfo);
        return STATE_INFO;
    }

    public String updateState(String state) {
        this.setState(state);
        return STATE;
    }

    public String updateCreateConfig(String createConfig) {
        this.setCreateConfig(createConfig);
        return CREATE_CONFIG;
    }

}
