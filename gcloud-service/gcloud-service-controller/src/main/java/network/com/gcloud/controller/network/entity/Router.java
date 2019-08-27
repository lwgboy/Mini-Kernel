package com.gcloud.controller.network.entity;

import com.gcloud.controller.ResourceProviderEntity;
import com.gcloud.framework.db.jdbc.annotation.ID;
import com.gcloud.framework.db.jdbc.annotation.Table;

import java.util.Date;

@Table(name = "gc_routers", jdbc = "controllerJdbcTemplate")
public class Router extends ResourceProviderEntity {
    @ID
    private String id;
    private String name;
    //冗余字段，一个路由只有一个网关
    private String externalGatewayNetworkId;
    private String userId;
    private Date createTime;
    private Date updatedAt;
    private String status;
    private String regionId;
    private String tenantId;

    public static final String ID = "id";
    public static final String NAME = "name";
    public static final String EXTERNAL_GATEWAY_NETWORK_ID = "externalGatewayNetworkId";
    public static final String USER_ID = "userId";
    public static final String CREATE_TIME = "createTime";
    public static final String UPDATED_AT = "updatedAt";
    public static final String STATUS = "status";
    public static final String REGION_ID = "regionId";
    public static final String TENANT_ID = "tenantId";

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

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getExternalGatewayNetworkId() {
        return externalGatewayNetworkId;
    }

    public void setExternalGatewayNetworkId(String externalGatewayNetworkId) {
        this.externalGatewayNetworkId = externalGatewayNetworkId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getRegionId() {
        return regionId;
    }

    public void setRegionId(String regionId) {
        this.regionId = regionId;
    }

    public Date getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Date updatedAt) {
        this.updatedAt = updatedAt;
    }

    public String getTenantId() {
        return tenantId;
    }

    public void setTenantId(String tenantId) {
        this.tenantId = tenantId;
    }

    public String updateName(String name) {
        this.setName(name);
        return NAME;
    }

    public String updateUpdatedAt(Date updatedAt) {
        this.setUpdatedAt(updatedAt);
        return UPDATED_AT;
    }

    public String updateExternalGatewayNetworkId(String externalGatewayNetworkId) {
        this.setExternalGatewayNetworkId(externalGatewayNetworkId);
        return EXTERNAL_GATEWAY_NETWORK_ID;
    }
}
