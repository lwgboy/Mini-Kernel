package com.gcloud.controller.security.entity;


import com.gcloud.framework.db.jdbc.annotation.ID;
import com.gcloud.framework.db.jdbc.annotation.Table;

import java.util.Date;

/**
 * Created by yaowj on 2018/7/13.
 */
@Table(name = "gc_security_cluster_securitygroup")
public class SecurityClusterSecurityGroup {

    @ID
    private Integer id;
    private String clusterId;
    private String securityGroupId;
    private Date createTime;
    private String createUser;
    private Date updateTime;
    private String networkType;

    public static final String ID = "id";
    public static final String CLUSTER_ID = "clusterId";
    public static final String SECURITY_GROUP_ID = "securityGroupId";
    public static final String CREATE_TIME = "createTime";
    public static final String CREATE_USER = "createUser";
    public static final String UPDATE_TIME = "updateTime";
    public static final String NETWORK_TYPE = "networkType";


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getClusterId() {
        return clusterId;
    }

    public void setClusterId(String clusterId) {
        this.clusterId = clusterId;
    }

    public String getSecurityGroupId() {
        return securityGroupId;
    }

    public void setSecurityGroupId(String securityGroupId) {
        this.securityGroupId = securityGroupId;
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

    public String getNetworkType() {
        return networkType;
    }

    public void setNetworkType(String networkType) {
        this.networkType = networkType;
    }

    public String updateId(Integer id) {
        this.setId(id);
        return ID;
    }

    public String updateClusterId(String clusterId) {
        this.setClusterId(clusterId);
        return CLUSTER_ID;
    }

    public String updateSecurityGroupId(String securityGroupId) {
        this.setSecurityGroupId(securityGroupId);
        return SECURITY_GROUP_ID;
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

    public String updateNetworkType(String networkType) {
        this.setNetworkType(networkType);
        return NETWORK_TYPE;
    }
}
