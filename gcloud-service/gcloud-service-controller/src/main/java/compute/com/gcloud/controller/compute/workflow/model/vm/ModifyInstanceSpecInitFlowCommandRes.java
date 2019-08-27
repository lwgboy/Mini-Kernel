package com.gcloud.controller.compute.workflow.model.vm;

import com.gcloud.core.workflow.model.WorkflowFirstStepResException;

public class ModifyInstanceSpecInitFlowCommandRes extends WorkflowFirstStepResException{

	private String taskId;
	private String instanceId;
	private Integer cpu;
	private Integer memory;
	private String hostName;
    private Integer orgCpu;
    private Integer orgMemory;

	public String getHostName() {
		return hostName;
	}

	public void setHostName(String hostName) {
		this.hostName = hostName;
	}

	public String getTaskId() {
		return taskId;
	}

	public void setTaskId(String taskId) {
		this.taskId = taskId;
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
