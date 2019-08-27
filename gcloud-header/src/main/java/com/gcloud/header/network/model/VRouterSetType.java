package com.gcloud.header.network.model;

import com.gcloud.framework.db.jdbc.annotation.TableField;
import com.gcloud.header.api.ApiModel;
import com.gcloud.header.controller.ControllerProperty;

import java.io.Serializable;

public class VRouterSetType implements Serializable {
    @ApiModel(description = "路由器Id")
    @TableField("id")
    private String vRouterId;
    @ApiModel(description = "路由器名称")
    @TableField("name")
    private String vRouterName;
    @ApiModel(description = "区域Id")
    private String regionId = ControllerProperty.REGION_ID;
    private String status;
    private String subnets;

    @TableField("external_gateway_network_id")
    private String networkId;

    public String getvRouterId() {
        return vRouterId;
    }

    public void setvRouterId(String vRouterId) {
        this.vRouterId = vRouterId;
    }

    public String getvRouterName() {
        return vRouterName;
    }

    public void setvRouterName(String vRouterName) {
        this.vRouterName = vRouterName;
    }

    public String getRegionId() {
        return regionId;
    }

    public void setRegionId(String regionId) {
        this.regionId = regionId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getSubnets() {
        return subnets;
    }

    public void setSubnets(String subnets) {
        this.subnets = subnets;
    }

    public String getNetworkId() {
        return networkId;
    }

    public void setNetworkId(String networkId) {
        this.networkId = networkId;
    }
}
