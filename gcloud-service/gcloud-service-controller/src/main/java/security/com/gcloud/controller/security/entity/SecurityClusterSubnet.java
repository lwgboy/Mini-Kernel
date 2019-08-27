package com.gcloud.controller.security.entity;

import com.gcloud.framework.db.jdbc.annotation.ID;
import com.gcloud.framework.db.jdbc.annotation.Table;

import java.util.Date;

/**
 * Created by yaowj on 2018/7/13.
 */
@Table(name = "gc_security_cluster_subnet")
public class SecurityClusterSubnet {

    @ID
    private Integer id;
    private String clusterId;
    private String subnetId;
    private String networkId;
    private Date createTime;
    private String createUser;
    private Date updateTime;
    private String networkType;
    private String cidr;

    public static final String ID = "id";
    public static final String CLUSTER_ID = "clusterId";
    public static final String SUBNET_ID = "subnetId";
    public static final String NETWORK_ID = "networkId";
    public static final String CREATE_TIME = "createTime";
    public static final String CREATE_USER = "createUser";
    public static final String UPDATE_TIME = "updateTime";
    public static final String NETWORK_TYPE = "networkType";
    public static final String CIDR = "cidr";

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

    public String getSubnetId() {
        return subnetId;
    }

    public void setSubnetId(String subnetId) {
        this.subnetId = subnetId;
    }

    public String getNetworkId() {
        return networkId;
    }

    public void setNetworkId(String networkId) {
        this.networkId = networkId;
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

    public String getCidr() {
        return cidr;
    }

    public void setCidr(String cidr) {
        this.cidr = cidr;
    }

    public String updateId (Integer id){
        this.setId(id);
        return ID;
    }
    public String updateClusterId (String clusterId){
        this.setClusterId(clusterId);
        return CLUSTER_ID;
    }
    public String updateSubnetId (String subnetId){
        this.setSubnetId(subnetId);
        return SUBNET_ID;
    }
    public String updateNetworkId (String networkId){
        this.setNetworkId(networkId);
        return NETWORK_ID;
    }
    public String updateCreateTime (Date createTime){
        this.setCreateTime(createTime);
        return CREATE_TIME;
    }
    public String updateCreateUser (String createUser){
        this.setCreateUser(createUser);
        return CREATE_USER;
    }
    public String updateUpdateTime (Date updateTime){
        this.setUpdateTime(updateTime);
        return UPDATE_TIME;
    }
    public String updateNetworkType (String networkType){
        this.setNetworkType(networkType);
        return NETWORK_TYPE;
    }

    public String updateCidr(String cidr){
        this.setCidr(cidr);
        return CIDR;
    }
}
