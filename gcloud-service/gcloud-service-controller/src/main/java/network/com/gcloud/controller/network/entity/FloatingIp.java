package com.gcloud.controller.network.entity;

import com.gcloud.controller.ResourceProviderEntity;
import com.gcloud.framework.db.jdbc.annotation.ID;
import com.gcloud.framework.db.jdbc.annotation.Table;

import java.util.Date;

@Table(name = "gc_floating_ips", jdbc = "controllerJdbcTemplate")
public class FloatingIp extends ResourceProviderEntity {
    @ID
    private String id;
    private String routerId;
    private String floatingNetworkId;
    private String floatingPortId;
    private String floatingIpAddress;
    private String fixedPortId;
    private String instanceId;
    private String instanceType;
    private String status;
    private String userId;
    private String regionId;
    private Date createTime;
    private Date updatedAt;
//    private String bwQosPolicyId;
//    private Integer bandwidth;
    private String tenantId;

    public static final String ID = "id";
    public static final String ROUTER_ID = "routerId";
    public static final String FLOATING_NETWORK_ID = "floatingNetworkId";
    public static final String FLOATING_PORT_ID = "floatingPortId";
    public static final String FLOATING_IP_ADDRESS = "floatingIpAddress";
    public static final String FIXED_PORT_ID = "fixedPortId";
    public static final String INSTANCE_ID = "instanceId";
    public static final String INSTANCE_TYPE = "instanceType";
    public static final String STATUS = "status";
    public static final String USER_ID = "userId";
    public static final String REGION_ID = "regionId";
    public static final String CREATE_TIME = "createTime";
    public static final String UPDATED_AT = "updatedAt";
    public static final String BW_QOS_POLICY_ID = "bwQosPolicyId";
    public static final String BANDWIDTH = "bandwidth";

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getInstanceId() {
        return instanceId;
    }

    public void setInstanceId(String instanceId) {
        this.instanceId = instanceId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getRegionId() {
        return regionId;
    }

    public void setRegionId(String regionId) {
        this.regionId = regionId;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getRouterId() {
        return routerId;
    }

    public void setRouterId(String routerId) {
        this.routerId = routerId;
    }

    public String getFloatingNetworkId() {
        return floatingNetworkId;
    }

    public void setFloatingNetworkId(String floatingNetworkId) {
        this.floatingNetworkId = floatingNetworkId;
    }

    public String getFloatingPortId() {
        return floatingPortId;
    }

    public void setFloatingPortId(String floatingPortId) {
        this.floatingPortId = floatingPortId;
    }

    public String getFloatingIpAddress() {
        return floatingIpAddress;
    }

    public void setFloatingIpAddress(String floatingIpAddress) {
        this.floatingIpAddress = floatingIpAddress;
    }

    public String getFixedPortId() {
        return fixedPortId;
    }

    public void setFixedPortId(String fixedPortId) {
        this.fixedPortId = fixedPortId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getInstanceType() {
        return instanceType;
    }

    public void setInstanceType(String instanceType) {
        this.instanceType = instanceType;
    }

//    public String getBwQosPolicyId() {
//        return bwQosPolicyId;
//    }
//
//    public void setBwQosPolicyId(String bwQosPolicyId) {
//        this.bwQosPolicyId = bwQosPolicyId;
//    }

//    public Integer getBandwidth() {
//        return bandwidth;
//    }
//
//    public void setBandwidth(Integer bandwidth) {
//        this.bandwidth = bandwidth;
//    }

    public void setUpdatedAt(Date time) { this.updatedAt = time; }

    public Date getUpdatedAt() { return this.updatedAt; }

    public String updateId(String id) {
        this.setId(id);
        return ID;
    }

    public String updateRouterId(String routerId) {
        this.setRouterId(routerId);
        return ROUTER_ID;
    }

    public String updateFloatingNetworkId(String floatingNetworkId) {
        this.setFloatingNetworkId(floatingNetworkId);
        return FLOATING_NETWORK_ID;
    }

    public String updateFloatingPortId(String floatingPortId) {
        this.setFloatingPortId(floatingPortId);
        return FLOATING_PORT_ID;
    }

    public String updateFloatingIpAddress(String floatingIpAddress) {
        this.setFloatingIpAddress(floatingIpAddress);
        return FLOATING_IP_ADDRESS;
    }

    public String updateFixedPortId(String fixedPortId) {
        this.setFixedPortId(fixedPortId);
        return FIXED_PORT_ID;
    }

    public String updateInstanceId(String instanceId) {
        this.setInstanceId(instanceId);
        return INSTANCE_ID;
    }

    public String updateInstanceType(String instanceType) {
        this.setInstanceType(instanceType);
        return INSTANCE_TYPE;
    }

    public String updateStatus(String status) {
        this.setStatus(status);
        return STATUS;
    }

    public String updateUserId(String userId) {
        this.setUserId(userId);
        return USER_ID;
    }

    public String updateRegionId(String regionId) {
        this.setRegionId(regionId);
        return REGION_ID;
    }

    public String updateCreateTime(Date createTime) {
        this.setCreateTime(createTime);
        return CREATE_TIME;
    }

    public String updateUpdatedAt(Date updateTime) {
        this.setUpdatedAt(updateTime);
        return UPDATED_AT;
    }

//    public String updateBwQosPolicyId(String bwQosPolicyId) {
//        this.setBwQosPolicyId(bwQosPolicyId);
//        return BW_QOS_POLICY_ID;
//    }

//    public String updateBandwidth(Integer bandwidth) {
//        this.setBandwidth(bandwidth);
//        return BANDWIDTH;
//    }

	public String getTenantId() {
		return tenantId;
	}

	public void setTenantId(String tenantId) {
		this.tenantId = tenantId;
	}
}
