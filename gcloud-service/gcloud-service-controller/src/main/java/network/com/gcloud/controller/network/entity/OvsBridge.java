package com.gcloud.controller.network.entity;

import com.gcloud.controller.ResourceProviderEntity;
import com.gcloud.framework.db.jdbc.annotation.ID;
import com.gcloud.framework.db.jdbc.annotation.Table;

import java.util.Date;

@Table(name = "gc_ovs_bridges", jdbc = "controllerJdbcTemplate")
public class OvsBridge extends ResourceProviderEntity {

    @ID
    private String id;
    private String name;
    private String bridge;
    private String state;
    private String hostname;
    private Date createTime;

    public static final String ID = "id";
    public static final String NAME = "name";
    public static final String BRIDGE = "bridge";
    public static final String STATE = "state";
    public static final String CREATE_TIME = "createTime";
    public static final String HOSTNAME = "hostname";

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

    public String getBridge() {
        return bridge;
    }

    public void setBridge(String bridge) {
        this.bridge = bridge;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getHostname() {
        return hostname;
    }

    public void setHostname(String hostname) {
        this.hostname = hostname;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String updateId(String id) {
        this.setId(id);
        return ID;
    }

    public String updateName(String name) {
        this.setName(name);
        return NAME;
    }

    public String updateBridge(String bridge) {
        this.setBridge(bridge);
        return BRIDGE;
    }

    public String updateState(String state) {
        this.setState(state);
        return STATE;
    }

    public String updateCreateTime (Date createTime){
        this.setCreateTime(createTime);
        return CREATE_TIME;
    }

    public String updateHostname (String hostname){
        this.setHostname(hostname);
        return HOSTNAME;
    }
}
