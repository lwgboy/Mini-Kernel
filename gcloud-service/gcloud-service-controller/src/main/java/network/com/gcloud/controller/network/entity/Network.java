package com.gcloud.controller.network.entity;

import com.gcloud.controller.ResourceProviderEntity;
import com.gcloud.framework.db.jdbc.annotation.ID;
import com.gcloud.framework.db.jdbc.annotation.Table;

import java.util.Date;

@Table(name = "gc_networks", jdbc = "controllerJdbcTemplate")
public class Network extends ResourceProviderEntity {
    @ID
    private String id;
    private String name;
    private String status;
    private String regionId;
    private Date updatedAt;
    private String tenantId;
    private Date createTime;
    private Integer type;

    public static final String ID = "id";
    public static final String NAME = "name";
    public static final String STATUS = "status";
    public static final String REGION_ID = "regionId";
    public static final String UPDATED_AT = "updatedAt";
    public static final String TYPE = "type";

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getRegionId() {
        return regionId;
    }

    public void setRegionId(String regionId) {
        this.regionId = regionId;
    }

    public Date getUpdatedAt() {
        return this.updatedAt;
    }

    public void setUpdatedAt(Date time) {
        this.updatedAt = time;
    }

    public String getTenantId() {
        return tenantId;
    }

    public void setTenantId(String tenantId) {
        this.tenantId = tenantId;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public String updateName(String name) {
        this.setName(name);
        return NAME;
    }

    public String updateUpdatedAt(Date updatedAt) {
        this.setUpdatedAt(updatedAt);
        return UPDATED_AT;
    }

    public String updateStatus(String status) {
        this.setStatus(status);
        return STATUS;
    }

    public String updateType(Integer type) {
        this.setType(type);
        return TYPE;
    }
}
