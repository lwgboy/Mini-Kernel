package com.gcloud.controller.network.entity;

import com.gcloud.controller.ResourceProviderEntity;
import com.gcloud.framework.db.jdbc.annotation.ID;
import com.gcloud.framework.db.jdbc.annotation.Table;

import java.util.Date;

/**
 * Created by yaowj on 2018/10/24.
 */
@Table(name = "gc_ports", jdbc = "controllerJdbcTemplate")
public class Port extends ResourceProviderEntity {

    @ID
    private String id;
    private String name;
    private String networkId;
    private String macAddress;
    private String deviceId;
    private String deviceOwner;
    private String status;
    private String description;
    private String userId;
    private Date createTime;
    private Date updatedAt;

    private String sufId;
    private String brName;
    private String aftName;
    private String preName;
    private String ovsBridgeId;
    private String customOvsBr;
    private Boolean noArpLimit;
    
    private String tenantId;

    public static final String ID = "id";
    public static final String NAME = "name";
    public static final String NETWORK_ID = "networkId";
    public static final String MAC_ADDRESS = "macAddress";
    public static final String DEVICE_ID = "deviceId";
    public static final String DEVICE_OWNER = "deviceOwner";
    public static final String STATUS = "status";
    public static final String DESCRIPTION = "description";
    public static final String USER_ID = "userId";
    public static final String CREATE_TIME = "createTime";
    public static final String UPDATED_AT = "updatedAt";
    public static final String SUF_ID = "sufId";
    public static final String BR_NAME = "brName";
    public static final String AFT_NAME = "aftName";
    public static final String PRE_NAME = "preName";
    public static final String OVS_BRIDGE_ID = "ovsBridgeId";
    public static final String NO_ARP_LIMIT = "noArpLimit";

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

    public String getNetworkId() {
        return networkId;
    }

    public void setNetworkId(String networkId) {
        this.networkId = networkId;
    }

    public String getMacAddress() {
        return macAddress;
    }

    public void setMacAddress(String macAddress) {
        this.macAddress = macAddress;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public String getDeviceOwner() {
        return deviceOwner;
    }

    public void setDeviceOwner(String deviceOwner) {
        this.deviceOwner = deviceOwner;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getSufId() {
        return sufId;
    }

    public void setSufId(String sufId) {
        this.sufId = sufId;
    }

    public String getBrName() {
        return brName;
    }

    public void setBrName(String brName) {
        this.brName = brName;
    }

    public String getAftName() {
        return aftName;
    }

    public void setAftName(String aftName) {
        this.aftName = aftName;
    }

    public String getPreName() {
        return preName;
    }

    public void setPreName(String preName) {
        this.preName = preName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
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

    public String getOvsBridgeId() {
        return ovsBridgeId;
    }

    public void setOvsBridgeId(String ovsBridgeId) {
        this.ovsBridgeId = ovsBridgeId;
    }

    public Boolean getNoArpLimit() {
        return noArpLimit;
    }

    public void setNoArpLimit(Boolean noArpLimit) {
        this.noArpLimit = noArpLimit;
    }

    public Date getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(Date time) { this.updatedAt = time; }

    public String updateUpdatedAt(Date time) {
        this.setUpdatedAt(time);
        return UPDATED_AT;
    }

    public String updateId(String id) {
        this.setId(id);
        return ID;
    }

    public String updateName(String name) {
        this.setName(name);
        return NAME;
    }

    public String updateNetworkId(String networkId) {
        this.setNetworkId(networkId);
        return NETWORK_ID;
    }

    public String updateMacAddress(String macAddress) {
        this.setMacAddress(macAddress);
        return MAC_ADDRESS;
    }

    public String updateDeviceId(String deviceId) {
        this.setDeviceId(deviceId);
        return DEVICE_ID;
    }

    public String updateDeviceOwner(String deviceOwner) {
        this.setDeviceOwner(deviceOwner);
        return DEVICE_OWNER;
    }

    public String updateStatus(String status) {
        this.setStatus(status);
        return STATUS;
    }

    public String updateDescription(String description) {
        this.setDescription(description);
        return DESCRIPTION;
    }

    public String updateUserId(String userId) {
        this.setUserId(userId);
        return USER_ID;
    }

    public String updateCreateTime(Date createTime) {
        this.setCreateTime(createTime);
        return CREATE_TIME;
    }

    public String updateSufId(String sufId) {
        this.setSufId(sufId);
        return SUF_ID;
    }

    public String updateBrName(String brName) {
        this.setBrName(brName);
        return BR_NAME;
    }

    public String updateAftName(String aftName) {
        this.setAftName(aftName);
        return AFT_NAME;
    }

    public String updatePreName(String preName) {
        this.setPreName(preName);
        return PRE_NAME;
    }

    public String updateOvsBridgeId(String ovsBridgeId) {
        this.setOvsBridgeId(ovsBridgeId);
        return OVS_BRIDGE_ID;
    }

    public String updateNoArpLimit(Boolean noArpLimit) {
        this.setNoArpLimit(noArpLimit);
        return NO_ARP_LIMIT;
    }
    
	public String getTenantId() {
		return tenantId;
	}

	public void setTenantId(String tenantId) {
		this.tenantId = tenantId;
	}


}
