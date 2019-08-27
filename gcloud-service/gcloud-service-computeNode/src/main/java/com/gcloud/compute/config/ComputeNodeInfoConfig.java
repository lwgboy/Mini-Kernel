package com.gcloud.compute.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.gcloud.common.util.StringUtils;
import com.gcloud.compute.prop.ComputeNodeProp;
import com.gcloud.compute.util.EnvironmentUtils;
import com.gcloud.compute.virtual.IVmVirtual;
import com.gcloud.header.compute.msg.node.node.model.ComputeNodeInfo;
import com.gcloud.service.common.compute.model.NodeInfo;
import com.gcloud.service.common.compute.uitls.VmUtil;

/**
 * Created by yaowj on 2018/10/18.
 */
@Configuration
public class ComputeNodeInfoConfig {

    @Autowired
    private ComputeNodeProp computeNodeProp;

    @Autowired
    private IVmVirtual vmVirtual;

    @Bean
    public ComputeNodeInfo computeNodeInfo(){

        NodeInfo nodeInfo = vmVirtual.info();

        int core = nodeInfo.getCpus();
        int memory = nodeInfo.getMemorys() / 1024;// mb

        ComputeNodeInfo computeNodeInfo = new ComputeNodeInfo();

        computeNodeInfo.setNodeIp(computeNodeProp.getNodeIp());
        computeNodeInfo.setHypervisor(computeNodeProp.getHypervisor());
        computeNodeInfo.setIsFt(computeNodeProp.getFtNode());
        computeNodeInfo.setCloudPlatform(computeNodeProp.getCloudPlatform());

        String cpuType = EnvironmentUtils.getCpuInfo();
        computeNodeInfo.setCpuType(cpuType);
        String kernelVersion = EnvironmentUtils.getKernelVersion();
        computeNodeInfo.setKernelVersion(kernelVersion);
        int physicalCpuNum = EnvironmentUtils.getPhysicalCpu() == null ? -1 : EnvironmentUtils.getPhysicalCpu();
        computeNodeInfo.setPhysicalCpu(physicalCpuNum);

        if (computeNodeProp.getCpuBinding()) {
            if (computeNodeProp.getMaxCores() > 0 && computeNodeProp.getMaxCores() < core)
                core = computeNodeProp.getMaxCores();
        } else {
            if (computeNodeProp.getMaxCores() > 0){
                core = computeNodeProp.getMaxCores();
            }

        }

        computeNodeInfo.setMaxCore(core);

        if (computeNodeProp.getMaxMem() > 0) {
            memory = computeNodeProp.getMaxMem();
        }

        computeNodeInfo.setMaxMemory(memory);

        String hostname = computeNodeProp.getHostname();
        if(StringUtils.isBlank(hostname)){
            hostname = VmUtil.getHostName();
        }
        computeNodeInfo.setHostname(hostname);

        return computeNodeInfo;
    }

}
