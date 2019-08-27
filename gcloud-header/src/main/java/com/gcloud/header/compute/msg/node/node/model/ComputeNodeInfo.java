package com.gcloud.header.compute.msg.node.node.model;

import java.io.Serializable;

public class ComputeNodeInfo implements Serializable {

    private static final long serialVersionUID = 1L;

    private String nodeIp;
    private String hypervisor;
    private String cpuType;// cpu类型
    private String kernelVersion;// 内核版本
    private int maxCore;
    private int maxMemory;// MB
    private double maxDisk;// GB
    private int isFt;    //是否容错节点，0 ： 不是  1 ： 是
    private String cloudPlatform;
    private int physicalCpu;
    private String hostname;

    public String getNodeIp() {
        return nodeIp;
    }

    public void setNodeIp(String nodeIp) {
        this.nodeIp = nodeIp;
    }

    public int getMaxCore() {
        return maxCore;
    }

    public void setMaxCore(int maxCore) {
        this.maxCore = maxCore;
    }

    public int getMaxMemory() {
        return maxMemory;
    }

    public void setMaxMemory(int maxMemory) {
        this.maxMemory = maxMemory;
    }

    public String getHypervisor() {
        return hypervisor;
    }

    public void setHypervisor(String hypervisor) {
        this.hypervisor = hypervisor;
    }

    public String getCpuType() {
        return cpuType;
    }

    public void setCpuType(String cpuType) {
        this.cpuType = cpuType;
    }

    public String getKernelVersion() {
        return kernelVersion;
    }

    public void setKernelVersion(String kernelVersion) {
        this.kernelVersion = kernelVersion;
    }


    public int getIsFt() {
        return isFt;
    }

    public void setIsFt(int isFt) {
        this.isFt = isFt;
    }

    public String getCloudPlatform() {
        return cloudPlatform;
    }

    public void setCloudPlatform(String cloudPlatform) {
        this.cloudPlatform = cloudPlatform;
    }

    public ComputeNodeInfo() {
        super();
    }

    public double getMaxDisk() {
        return maxDisk;
    }

    public void setMaxDisk(double maxDisk) {
        this.maxDisk = maxDisk;
    }

    public int getPhysicalCpu() {
        return physicalCpu;
    }

    public void setPhysicalCpu(int physicalCpu) {
        this.physicalCpu = physicalCpu;
    }

    public String getHostname() {
        return hostname;
    }

    public void setHostname(String hostname) {
        this.hostname = hostname;
    }
}
