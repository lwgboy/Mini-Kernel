package com.gcloud.compute.service.vm.network;

import com.gcloud.header.compute.msg.node.vm.model.VmNetworkDetail;

/**
 * Created by yaowj on 2018/11/13.
 */
public interface IVmNetworkNodeService {

    void detachPort(String instanceId, VmNetworkDetail networkDetail);
    void cleanNetEnvConfig(String portId, VmNetworkDetail networkDetail);
    void forceCleanNetEnvConfig(String portId, VmNetworkDetail networkDetail);
    void cleanNetConfigFile(String instanceId, VmNetworkDetail networkDetail);
    void forceCleanNetConfigFile(String instanceId, VmNetworkDetail networkDetail);
    void forceDetach(String instanceId, String portId, String macAddress);

    void configNetEnv(String instanceUuid, VmNetworkDetail vmNetworkDetail);
    void configNetFile(String instanceUuid, VmNetworkDetail vmNetworkDetail);
    void attachPort(String instanceId, VmNetworkDetail vmNetworkDetail);



}
