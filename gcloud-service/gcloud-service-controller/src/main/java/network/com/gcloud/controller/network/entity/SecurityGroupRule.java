package com.gcloud.controller.network.entity;

import java.util.Date;

import com.gcloud.controller.ResourceProviderEntity;
import com.gcloud.framework.db.jdbc.annotation.ID;
import com.gcloud.framework.db.jdbc.annotation.Table;

@Table(name = "gc_security_group_rules", jdbc = "controllerJdbcTemplate")
public class SecurityGroupRule extends ResourceProviderEntity {//gc_security_group_rules
	@ID
    private String id;
    private String securityGroupId;
    private String protocol;
    private String portRange;
    private String remoteIpPrefix;
    private String remoteGroupId;
    private String direction;
    private String ethertype;
    private Date createTime;
    private Date updatedAt;
    private String userId;
    private String description;
    private String tenantId;

    public static final String ID = "id";
    public static final String SECURITY_GROUP_ID = "securityGroupId";
    public static final String CREATE_TIME = "createTime";
    public static final String USER_ID = "userId";
    public static final String DESCRIPTION = "description";
    public static final String UPDATED_AT = "updatedAt";
    
    
    
    public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getSecurityGroupId() {
		return securityGroupId;
	}

	public void setSecurityGroupId(String securityGroupId) {
		this.securityGroupId = securityGroupId;
	}

	public String getPortRange() {
		return portRange;
	}

	public String getProtocol() {
		return protocol;
	}

	public void setProtocol(String protocol) {
		this.protocol = protocol;
	}

	public String getRemoteIpPrefix() {
		return remoteIpPrefix;
	}

	public void setRemoteIpPrefix(String remoteIpPrefix) {
		this.remoteIpPrefix = remoteIpPrefix;
	}

	public String getRemoteGroupId() {
		return remoteGroupId;
	}

	public void setRemoteGroupId(String remoteGroupId) {
		this.remoteGroupId = remoteGroupId;
	}

	public void setPortRange(String portRange) {
		this.portRange = portRange;
	}

	public String getDirection() {
		return direction;
	}

	public void setDirection(String direction) {
		this.direction = direction;
	}

	public String getEthertype() {
		return ethertype;
	}

	public void setEthertype(String ethertype) {
		this.ethertype = ethertype;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getTenantId() {
		return tenantId;
	}

	public void setTenantId(String tenantId) {
		this.tenantId = tenantId;
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

    public String updateSecurityGroupId(String securityGroupId) {
        this.setSecurityGroupId(securityGroupId);
        return SECURITY_GROUP_ID;
    }

    public String updateCreateTime(Date createTime) {
        this.setCreateTime(createTime);
        return CREATE_TIME;
    }

    public String updateUserId(String userId) {
        this.setUserId(userId);
        return USER_ID;
    }

    public String updateDescription(String description) {
        this.setDescription(description);
        return DESCRIPTION;
    }
}
