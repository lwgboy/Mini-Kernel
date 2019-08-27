package com.gcloud.header.compute.msg.node.vm.base;

import com.gcloud.header.NodeMessage;

public class ConfigInstanceResourceMsg extends NodeMessage {

	private String instanceId;
	private String vmUserId;
	private Integer cpu;
	private Integer memory;
	private Integer orgCpu;
	private Integer orgMemory;

	public String getVmUserId() {
		return vmUserId;
	}

	public void setVmUserId(String vmUserId) {
		this.vmUserId = vmUserId;
	}

	public String getInstanceId() {
		return instanceId;
	}

	public void setInstanceId(String instanceId) {
		this.instanceId = instanceId;
	}

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

    public Integer getOrgCpu() {
        return orgCpu;
    }

    public void setOrgCpu(Integer orgCpu) {
        this.orgCpu = orgCpu;
    }

    public Integer getOrgMemory() {
        return orgMemory;
    }

    public void setOrgMemory(Integer orgMemory) {
        this.orgMemory = orgMemory;
    }
}
