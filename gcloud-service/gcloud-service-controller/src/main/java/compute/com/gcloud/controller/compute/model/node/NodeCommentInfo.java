package com.gcloud.controller.compute.model.node;

public enum NodeCommentInfo {
	
	HYPERVISOR("hypervisor"),
    KERNEL_VERSION("kernelVersion"),
    CPU_TYPE("cpuType"),
    CLOUD_PLATFORM("cloudPlatform"),
    IS_FT("isFt"),
    LXC_CPU_USED("cpuUsed"),
    LXC_MEMORY_USED("memoryUsed");
	
    private String value;
    
    NodeCommentInfo(String value){
        this.value=value;
    }
    public String getValue(){
        return this.value;
    }
	
}
