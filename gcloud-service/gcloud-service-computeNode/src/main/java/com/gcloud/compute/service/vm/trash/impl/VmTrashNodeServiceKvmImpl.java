package com.gcloud.compute.service.vm.trash.impl;

import com.gcloud.compute.cache.cache.VmInstancesCache;
import com.gcloud.compute.prop.ComputeNodeProp;
import com.gcloud.compute.service.vm.trash.IVmTrashNodeService;
import com.gcloud.compute.util.VmNodeUtil;
import com.gcloud.compute.virtual.IVmVirtual;
import com.gcloud.core.condition.ConditionalHypervisor;
import com.gcloud.core.exception.GCloudException;
import com.gcloud.service.common.compute.model.DomainInfo;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.File;

/**
 * Created by yaowj on 2018/11/28.
 */
@ConditionalHypervisor
@Slf4j
public class VmTrashNodeServiceKvmImpl implements IVmTrashNodeService {

    @Autowired
    private ComputeNodeProp prop;

    @Autowired
    private IVmVirtual vmVirtual;

    @Override
    public void cleanInstanceFile(String instanceId) {
        String configPath = VmNodeUtil.getInstanceConfigPath(prop.getNodeIp(), instanceId);
        File cfgPahFile = new File(configPath);
        if(cfgPahFile.exists()){
            try{
                FileUtils.deleteDirectory(cfgPahFile);
            }catch (Exception ex){
                log.error("删除文件失败", ex);
                throw new GCloudException("::删除文件失败");
            }

        }

    }

    @Override
    public void cleanInstanceInfo(String instanceId) {
        DomainInfo domInfo = VmNodeUtil.checkVm(instanceId);
        if(domInfo != null){
            vmVirtual.undefine(instanceId);
        }

        VmInstancesCache.remove(instanceId);
    }
}
