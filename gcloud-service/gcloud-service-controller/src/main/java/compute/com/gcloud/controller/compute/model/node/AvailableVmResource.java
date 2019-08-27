package com.gcloud.controller.compute.model.node;

import java.io.Serializable;

/*
 * @Date 2015-4-14
 *
 * @Author zhangzj@g-cloud.com.cn
 *
 * @Copyright 2015 www.g-cloud.com.cn Inc. All rights reserved.
 *
 * @Description 可用计算资源实体类
 */
public class AvailableVmResource extends AvailableResource implements Serializable {
    private static final long serialVersionUID = 1L;
    private int maxCore;
    private int maxMemory;
    private int physicalCpu;
    private double maxDisk;
    private int availableCore = 0;
    private int availableMemory = 0;
    private double availableDisk = 0d;

    private float cpuUtil = 100f;
    private float memoryUtil = 100f;

    public int getMaxCore() {
        return maxCore;
    }

    public int getMaxMemory() {
        return maxMemory;
    }

    public double getMaxDisk() {
        return maxDisk;
    }

    public int getAvailableCore() {
        return availableCore;
    }

    public int getAvailableMemory() {
        return availableMemory;
    }

    public double getAvailableDisk() {
        return availableDisk;
    }

    public void setMaxCore(int maxCore) {
        this.maxCore = maxCore;
    }

    public void setMaxMemory(int maxMemory) {
        this.maxMemory = maxMemory;
    }

    public void setMaxDisk(double maxDisk) {
        this.maxDisk = maxDisk;
    }

    public void setAvailableCore(int availableCore) {
        synchronized (this) {
            // 防止可用资源大于总数资源
            if (this.maxCore < availableCore) {
                availableCore = this.maxCore;
            }
            this.availableCore = availableCore;
        }
    }

    public void setAvailableMemory(int availableMemory) {
        synchronized (this) {
            // 防止可用资源大于总数资源
            if (this.maxMemory < availableMemory) {
                availableMemory = this.maxMemory;
            }
            this.availableMemory = availableMemory;
        }
    }

    public void setAvailableDisk(double availableDisk) {
        synchronized (this) {
            // 防止可用资源大于总数资源
            if (this.maxDisk < availableDisk) {
                availableDisk = this.maxDisk;
            }
            this.availableDisk = availableDisk;
        }
    }

    public float getCpuUtil() {
        return cpuUtil;
    }

    public void setCpuUtil(float cpuUtil) {
        this.cpuUtil = cpuUtil;
    }

    public float getMemoryUtil() {
        return memoryUtil;
    }

    public void setMemoryUtil(float memoryUtil) {
        this.memoryUtil = memoryUtil;
    }

    public int getPhysicalCpu() {
        return physicalCpu;
    }

    public void setPhysicalCpu(int physicalCpu) {
        this.physicalCpu = physicalCpu;
    }

}
