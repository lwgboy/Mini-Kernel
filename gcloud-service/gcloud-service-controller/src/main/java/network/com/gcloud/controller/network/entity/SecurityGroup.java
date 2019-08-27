package com.gcloud.controller.network.entity;

import com.gcloud.controller.ResourceProviderEntity;
import com.gcloud.framework.db.jdbc.annotation.ID;
import com.gcloud.framework.db.jdbc.annotation.Table;

import java.util.Date;

/**
 * Created by yaowj on 2018/10/25.
 */
@Table(name = "gc_security_groups", jdbc = "controllerJdbcTemplate")
public class SecurityGroup extends ResourceProviderEntity {
    @ID
    private String id;
    private String name;
    private Date createTime;
    private Date updatedAt;
    private String userId;
    private String description;
    private String tenantId;
    private Boolean isDefault;

    public static final String ID = "id";
    public static final String NAME = "name";
    public static final String CREATE_TIME = "createTime";
    public static final String USER_ID = "userId";
    public static final String DESCRIPTION = "description";
    public static final String UPDATED_AT = "updatedAt";
    public static final String IS_DEFAULT = "isDefault";
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

    public Date getUpdatedAt() { return updatedAt; }

    public void setUpdatedAt(Date time) { this.updatedAt = time; }

    public String getTenantId() {
        return tenantId;
    }

    public void setTenantId(String tenantId) {
        this.tenantId = tenantId;
    }

    public Boolean getIsDefault() {
        return isDefault;
    }

    public void setIsDefault(Boolean isDefault) {
        this.isDefault = isDefault;
    }

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

    public String updateIsDefault(Boolean isDefault){
        this.setIsDefault(isDefault);
        return IS_DEFAULT;
    }

    public String updateTenantId(String tenantId){
        this.setTenantId(tenantId);
        return TENANT_ID;
    }

}
