package com.gcloud.controller.network.entity;

import com.gcloud.framework.db.jdbc.annotation.ID;
import com.gcloud.framework.db.jdbc.annotation.Table;

import java.util.Date;

@Table(name = "gc_ovs_bridges_usage", jdbc = "controllerJdbcTemplate")
public class OvsBridgeUsage {

    @ID
    private Long id;
    private String bridgeId;
    private String refType;
    private String refId;
    private Date createTime;

    public static final String ID = "id";
    public static final String BRIDGE_ID = "bridgeId";
    public static final String REF_TYPE = "refType";
    public static final String REF_ID = "refId";
    public static final String CREATE_TIME = "createTime";


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getBridgeId() {
        return bridgeId;
    }

    public void setBridgeId(String bridgeId) {
        this.bridgeId = bridgeId;
    }

    public String getRefType() {
        return refType;
    }

    public void setRefType(String refType) {
        this.refType = refType;
    }

    public String getRefId() {
        return refId;
    }

    public void setRefId(String refId) {
        this.refId = refId;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String updateId(Long id) {
        this.setId(id);
        return ID;
    }

    public String updateBridgeId(String bridgeId) {
        this.setBridgeId(bridgeId);
        return BRIDGE_ID;
    }

    public String updateRefType(String refType) {
        this.setRefType(refType);
        return REF_TYPE;
    }

    public String updateRefId(String refId) {
        this.setRefId(refId);
        return REF_ID;
    }

    public String updateCreateTime(Date createTime) {
        this.setCreateTime(createTime);
        return CREATE_TIME;
    }


}
