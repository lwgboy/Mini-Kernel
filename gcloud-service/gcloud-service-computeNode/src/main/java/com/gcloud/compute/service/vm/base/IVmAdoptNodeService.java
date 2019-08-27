package com.gcloud.compute.service.vm.base;

import com.gcloud.header.compute.msg.node.vm.model.VmStateInfo;

import java.util.Map;

/**
 * Created by yaowj on 2018/12/10.
 */
public interface IVmAdoptNodeService {

    void scanVmByDirs(String instanceConfigurePath);
    boolean adoptVms(boolean init);
    Map<String, VmStateInfo> stateInfo();

}
