package com.gcloud.header.compute.msg.api.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.gcloud.framework.db.jdbc.annotation.TableField;
import com.gcloud.header.GcloudConstants;
import com.gcloud.header.api.ApiModel;
import com.gcloud.header.controller.ControllerProperty;
import com.gcloud.header.network.model.IpAddressSetType;
import com.gcloud.header.network.model.NetworkInterfaces;

import java.io.Serializable;
import java.util.Date;

public class InstanceAttributesType implements Serializable {
    @ApiModel(description = "CPU核数")
    @TableField("core")
    private Integer cpu;
    @ApiModel(description = "内存大小，单位为 MB")
    private Integer memory;
    @ApiModel(description = "实例的显示名称")
    @TableField("alias")
    private String instanceName;
    @ApiModel(description = "镜像ID")
    private String imageId;
    @ApiModel(description = "实例规格类型")
    private String instanceType;
    @ApiModel(description = "地域ID")
    private String regionId = ControllerProperty.REGION_ID;
    @ApiModel(description = "创建时间")
    @TableField("launch_time")
    @JsonFormat(timezone = GcloudConstants.DEFAULT_TIMEZONE, pattern = GcloudConstants.DEFAULT_DATEFORMAT)
    private Date creationTime;
    @ApiModel(description = "实例状态")
    @TableField("state")
    private String status;
    @ApiModel(description = "实例ID")
    @TableField("id")
    private String instanceId;
    @ApiModel(description = "可用区ID")
    private String zoneId;
    @ApiModel(description = "弹性公网ip集合")
    private IpAddressSetType eipAddress;
    @ApiModel(description = "内网ip集合")
    private IpAddressSetType innerIpAddress;
    @ApiModel(description = "实例的网卡集合")
    private NetworkInterfaces networkInterfaces;

    private String stepState;
    private String taskState;

    public Integer getCpu() {
        return cpu;
    }

    public void setCpu(Integer cpu) {
        this.cpu = cpu;
    }

    public Integer getMemory() {
        return memory;
    }

    public void setMemory(Integer memory) {
        this.memory = memory;
    }

    public String getInstanceName() {
        return instanceName;
    }

    public void setInstanceName(String instanceName) {
        this.instanceName = instanceName;
    }

    public String getImageId() {
        return imageId;
    }

    public void setImageId(String imageId) {
        this.imageId = imageId;
    }

    public String getInstanceType() {
        return instanceType;
    }

    public void setInstanceType(String instanceType) {
        this.instanceType = instanceType;
    }

    public String getRegionId() {
        return regionId;
    }

    public void setRegionId(String regionId) {
        this.regionId = regionId;
    }

    public Date getCreationTime() {
        return creationTime;
    }

    public void setCreationTime(Date creationTime) {
        this.creationTime = creationTime;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getInstanceId() {
        return instanceId;
    }

    public void setInstanceId(String instanceId) {
        this.instanceId = instanceId;
    }

    public String getZoneId() {
        return zoneId;
    }

    public void setZoneId(String zoneId) {
        this.zoneId = zoneId;
    }

    public IpAddressSetType getEipAddress() {
        return eipAddress;
    }

    public void setEipAddress(IpAddressSetType eipAddress) {
        this.eipAddress = eipAddress;
    }

    public NetworkInterfaces getNetworkInterfaces() {
        return networkInterfaces;
    }

    public void setNetworkInterfaces(NetworkInterfaces networkInterfaces) {
        this.networkInterfaces = networkInterfaces;
    }

    public String getTaskState() {
        return taskState;
    }

    public void setTaskState(String taskState) {
        this.taskState = taskState;
    }

    public String getStepState() {
        return stepState;
    }

    public void setStepState(String stepState) {
        this.stepState = stepState;
    }

	public IpAddressSetType getInnerIpAddress() {
		return innerIpAddress;
	}

	public void setInnerIpAddress(IpAddressSetType innerIpAddress) {
		this.innerIpAddress = innerIpAddress;
	}
}
