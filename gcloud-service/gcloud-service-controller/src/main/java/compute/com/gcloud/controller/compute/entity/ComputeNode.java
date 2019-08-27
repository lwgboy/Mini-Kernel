package com.gcloud.controller.compute.entity;

import com.gcloud.framework.db.jdbc.annotation.ID;
import com.gcloud.framework.db.jdbc.annotation.Table;

import java.util.Date;


/*
 * @Date 2015-4-16
 *
 * @Author yaowj@g-cloud.com.cn
 *
 * @Copyright 2015 www.g-cloud.com.cn Inc. All rights reserved.
 *
 * @Description gc_compute_nodes实体类
 */
@Table(name = "gc_compute_nodes", jdbc = "controllerJdbcTemplate")
public class ComputeNode {

    @ID
    private Integer id;
    private String hostname;
    private String nodeIp;
    private Integer memoryTotal;
    private Double diskTotal;
    private Integer cpuTotal;
    private String hypervisorType;
    private Integer state;
    private Integer ftNode;
    private String cloudPlatform;
    private Date createTime;
    private Integer energyNode;
    private String type;
    private Integer physicalCpu;
    private String zoneId;

    public static final String ID = "id";
    public static final String HOSTNAME = "hostname";
    public static final String NODE_IP = "nodeIp";
    public static final String MEMORY_TOTAL = "memoryTotal";
    public static final String DISK_TOTAL = "diskTotal";
    public static final String CPU_TOTAL = "cpuTotal";
    public static final String HYPERVISOR_TYPE = "hypervisorType";
    public static final String STATE = "state";
    public static final String FT_NODE = "ftNode";
    public static final String CLOUD_PLATFORM = "cloudPlatform";
    public static final String CREATE_TIME = "createTime";
    public static final String ENERGY_NODE = "energyNode";
    public static final String TYPE = "type";
    public static final String PHYSICAL_CPU = "physicalCpu";
    public static final String ZONE_ID = "zoneId";


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getHostname() {
        return hostname;
    }

    public void setHostname(String hostname) {
        this.hostname = hostname;
    }

    public String getNodeIp() {
        return nodeIp;
    }

    public void setNodeIp(String nodeIp) {
        this.nodeIp = nodeIp;
    }

    public Integer getMemoryTotal() {
        return memoryTotal;
    }

    public void setMemoryTotal(Integer memoryTotal) {
        this.memoryTotal = memoryTotal;
    }

    public Integer getCpuTotal() {
        return cpuTotal;
    }

    public void setCpuTotal(Integer cpuTotal) {
        this.cpuTotal = cpuTotal;
    }

    public String getHypervisorType() {
        return hypervisorType;
    }

    public void setHypervisorType(String hypervisorType) {
        this.hypervisorType = hypervisorType;
    }

    public Integer getState() {
        return state;
    }

    public void setState(Integer state) {
        this.state = state;
    }

    public Integer getFtNode() {
        return ftNode;
    }

    public void setFtNode(Integer ftNode) {
        this.ftNode = ftNode;
    }

    public String getCloudPlatform() {
        return cloudPlatform;
    }

    public void setCloudPlatform(String cloudPlatform) {
        this.cloudPlatform = cloudPlatform;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Integer getEnergyNode() {
        return energyNode;
    }

    public void setEnergyNode(Integer energyNode) {
        this.energyNode = energyNode;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Double getDiskTotal() {
        return diskTotal;
    }

    public void setDiskTotal(Double diskTotal) {
        this.diskTotal = diskTotal;
    }

    public Integer getPhysicalCpu() {
        return physicalCpu;
    }

    public void setPhysicalCpu(Integer physicalCpu) {
        this.physicalCpu = physicalCpu;
    }

    public String getZoneId() {
        return zoneId;
    }

    public void setZoneId(String zoneId) {
        this.zoneId = zoneId;
    }

    public String updateId(Integer id) {
        this.setId(id);
        return ID;
    }

    public String updateHostname(String hostname) {
        this.setHostname(hostname);
        return HOSTNAME;
    }

    public String updateNodeIp(String nodeIp) {
        this.setNodeIp(nodeIp);
        return NODE_IP;
    }

    public String updateMemoryTotal(Integer memoryTotal) {
        this.setMemoryTotal(memoryTotal);
        return MEMORY_TOTAL;
    }

    public String updateDiskTotal(Double diskTotal) {
        this.setDiskTotal(diskTotal);
        return DISK_TOTAL;
    }

    public String updateCpuTotal(Integer cpuTotal) {
        this.setCpuTotal(cpuTotal);
        return CPU_TOTAL;
    }

    public String updateHypervisorType(String hypervisorType) {
        this.setHypervisorType(hypervisorType);
        return HYPERVISOR_TYPE;
    }

    public String updateState(Integer state) {
        this.setState(state);
        return STATE;
    }

    public String updateFtNode(Integer ftNode) {
        this.setFtNode(ftNode);
        return FT_NODE;
    }

    public String updateCloudPlatform(String cloudPlatform) {
        this.setCloudPlatform(cloudPlatform);
        return CLOUD_PLATFORM;
    }

    public String updateCreateTime(Date createTime) {
        this.setCreateTime(createTime);
        return CREATE_TIME;
    }

    public String updateEnergyNode(Integer energyNode) {
        this.setEnergyNode(energyNode);
        return ENERGY_NODE;
    }

    public String updateType(String type) {
        this.setType(type);
        return TYPE;
    }

    public String updatePhysicalCpu(Integer physicalCpu) {
        this.setPhysicalCpu(physicalCpu);
        return PHYSICAL_CPU;
    }

    public String updateZoneId(String zoneId) {
        this.setZoneId(zoneId);
        return ZONE_ID;
    }
}
