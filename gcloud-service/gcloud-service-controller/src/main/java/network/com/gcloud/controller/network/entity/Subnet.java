package com.gcloud.controller.network.entity;

import com.gcloud.controller.ResourceProviderEntity;
import com.gcloud.framework.db.jdbc.annotation.ID;
import com.gcloud.framework.db.jdbc.annotation.Table;

import java.util.Date;

/**
 * Created by yaowj on 2018/10/25.
 */
@Table(name = "gc_subnets", jdbc = "controllerJdbcTemplate")
public class Subnet extends ResourceProviderEntity {

    @ID
    private String id;
    private String name;
    private String cidr;//交换机的地址
    private String networkId;
    private String zoneId;
    private String userId;
    private Date createTime;
    private Date updatedAt;
    private String tenantId;
    private Integer provider;
    private String providerRefId;

    public static final String ID = "id";
    public static final String NAME = "name";
    public static final String CIDR = "cidr";
    public static final String NETWORK_ID = "networkId";
    public static final String ROUTER_ID = "routerId";
    public static final String ZONE_ID = "zoneId";
    public static final String USER_ID = "userId";
    public static final String CREATE_TIME = "createTime";
    public static final String UPDATED_AT = "updatedAt";
    public static final String TENANT_ID = "tenantId";
    public static final String PROVIDER = "provider";
    public static final String PROVIDER_REF_ID = "providerRefId";

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

    public String getCidr() {
        return cidr;
    }

    public void setCidr(String cidr) {
        this.cidr = cidr;
    }

    public String getNetworkId() {
        return networkId;
    }

    public void setNetworkId(String networkId) {
        this.networkId = networkId;
    }

    public String getZoneId() {
        return zoneId;
    }

    public void setZoneId(String zoneId) {
        this.zoneId = zoneId;
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

    public Date getUpdatedAt() {
        return updatedAt;
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

    public Integer getProvider() {
        return provider;
    }

    public void setProvider(Integer provider) {
        this.provider = provider;
    }

    public String getProviderRefId() {
        return providerRefId;
    }

    public void setProviderRefId(String providerRefId) {
        this.providerRefId = providerRefId;
    }

    public String updateId(String id) {
        this.setId(id);
        return ID;
    }

    public String updateName(String name) {
        this.setName(name);
        return NAME;
    }

    public String updateCidr(String cidr) {
        this.setCidr(cidr);
        return CIDR;
    }

    public String updateNetworkId(String networkId) {
        this.setNetworkId(networkId);
        return NETWORK_ID;
    }

    public String updateUserId(String userId) {
        this.setUserId(userId);
        return USER_ID;
    }

    public String updateTenantId(String id) {
        this.setTenantId(tenantId);
        return TENANT_ID;
    }

    public String updateCreateTime(Date createTime) {
        this.setCreateTime(createTime);
        return CREATE_TIME;
    }

    public String updateProvider(Integer provider) {
        this.setProvider(provider);
        return PROVIDER;
    }

    public String updateProviderRefId(String providerRefId) {
        this.setProviderRefId(providerRefId);
        return PROVIDER_REF_ID;
    }

    public String updateUpdatedAt(Date updatedAt) {
        this.setUpdatedAt(updatedAt);
        return UPDATED_AT;
    }


}
