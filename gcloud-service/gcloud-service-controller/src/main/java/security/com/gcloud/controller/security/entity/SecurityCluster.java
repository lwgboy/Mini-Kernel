package com.gcloud.controller.security.entity;

import com.gcloud.framework.db.jdbc.annotation.ID;
import com.gcloud.framework.db.jdbc.annotation.Table;

import java.util.Date;

/**
 * Created by yaowj on 2018/7/6.
 */
@Table(name = "gc_security_cluster")
public class SecurityCluster {

    @ID
    private String id;
    private String name;
    private String description;
    private String state;
    private Date createTime;
    private String createUser;
    private Date updateTime;
    private Boolean ha;
    private String zoneId;

    public static final String ID = "id";
    public static final String NAME = "name";
    public static final String DESCRIPTION = "description";
    public static final String STATE = "state";
    public static final String CREATE_TIME = "createTime";
    public static final String CREATE_USER = "createUser";
    public static final String UPDATE_TIME = "updateTime";
    public static final String HA = "ha";
    public static final String ZONE_ID = "zoneId";


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
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

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public Boolean getHa() {
        return ha;
    }

    public void setHa(Boolean ha) {
        this.ha = ha;
    }

    public String getZoneId() {
        return zoneId;
    }

    public void setZoneId(String zoneId) {
        this.zoneId = zoneId;
    }

    public String updateId(String id) {
        this.setId(id);
        return ID;
    }

    public String updateName(String name) {
        this.setName(name);
        return NAME;
    }

    public String updateDescription(String description) {
        this.setDescription(description);
        return DESCRIPTION;
    }

    public String updateState(String state) {
        this.setState(state);
        return STATE;
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

    public String updateHa(Boolean ha) {
        this.setHa(ha);
        return HA;
    }

    public String updateZoneId(String zoneId) {
        this.setZoneId(zoneId);
        return ZONE_ID;
    }
}
