package com.gcloud.header.compute.msg.api.vm.create;

import com.gcloud.header.ApiMessage;
import com.gcloud.header.api.ApiModel;
import com.gcloud.header.compute.msg.api.model.DiskInfo;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotBlank;

import java.util.List;

public class ApiCreateInstanceMsg extends ApiMessage {

    @Override
    public Class replyClazz() {
        return ApiCreateInstanceReplyMsg.class;
    }

    @ApiModel(description = "系统盘名称")
    private String systemDiskName;

    @ApiModel(description = "镜像ID", require = true)
    @NotBlank(message = "0010101::镜像ID不能为空")
    private String imageId;
    @ApiModel(description = "实例类型ID")
    //@NotBlank(message = "0010102::实例资源规格不能为空")
    private String instanceType;
    @ApiModel(description = "实例名称", require = true)
    @NotBlank(message = "0010103::实例名称不能为空")
    @Length(min = 4, max = 20, message = "0010104::名称长度为4-20")
    private String instanceName;
    @ApiModel(description = "实例主机名")
    @Length(min = 2, max = 15, message = "0010105::主机名长度为2-15")
    private String hostName;
    @ApiModel(description = "实例密码")
    @Length(min = 8, max = 20, message = "0010106::密码长度为8-20")
    private String password;
    @ApiModel(description = "虚拟交换机ID")
    private String vSwitchId;
    @ApiModel(description = "实例私网 IP 地址")
    private String privateIpAddress;
    @ApiModel(description = "数据盘")
    private List<DiskInfo> dataDisk;
    @ApiModel(description = "系统盘大小", require = true)
    private Integer systemDiskSize;
    @ApiModel(description = "系统盘的磁盘种类")
    private String systemDiskCategory;
    @ApiModel(description = "可用地域")
    private String zoneId;

    @ApiModel(description = "安全组")
    private String securityGroupId;

    // 常量
    private String systemDiskType = "system";
    private String DataDiskType = "data";

    public String getSystemDiskType() {
        return systemDiskType;
    }

    public void setSystemDiskType(String systemDiskType) {
        this.systemDiskType = systemDiskType;
    }

    public String getDataDiskType() {
        return DataDiskType;
    }

    public void setDataDiskType(String dataDiskType) {
        DataDiskType = dataDiskType;
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

    public String getInstanceName() {
        return instanceName;
    }

    public void setInstanceName(String instanceName) {
        this.instanceName = instanceName;
    }

    public String getHostName() {
        return hostName;
    }

    public void setHostName(String hostName) {
        this.hostName = hostName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getvSwitchId() {
        return vSwitchId;
    }

    public void setvSwitchId(String vSwitchId) {
        this.vSwitchId = vSwitchId;
    }

    public String getPrivateIpAddress() {
        return privateIpAddress;
    }

    public void setPrivateIpAddress(String privateIpAddress) {
        this.privateIpAddress = privateIpAddress;
    }

    public List<DiskInfo> getDataDisk() {
        return dataDisk;
    }

    public void setDataDisk(List<DiskInfo> dataDisk) {
        this.dataDisk = dataDisk;
    }

    public String getSystemDiskName() {
        return systemDiskName;
    }

    public void setSystemDiskName(String systemDiskName) {
        this.systemDiskName = systemDiskName;
    }

    public Integer getSystemDiskSize() {
        return systemDiskSize;
    }

    public void setSystemDiskSize(Integer systemDiskSize) {
        this.systemDiskSize = systemDiskSize;
    }

    public String getSystemDiskCategory() {
        return systemDiskCategory;
    }

    public void setSystemDiskCategory(String systemDiskCategory) {
        this.systemDiskCategory = systemDiskCategory;
    }

    public String getZoneId() {
        return zoneId;
    }

    public void setZoneId(String zoneId) {
        this.zoneId = zoneId;
    }


    public String getSecurityGroupId() {
        return securityGroupId;
    }

    public void setSecurityGroupId(String securityGroupId) {
        this.securityGroupId = securityGroupId;
    }

}
