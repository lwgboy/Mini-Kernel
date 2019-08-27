package com.gcloud.controller.compute.service.vm.trash.impl;

import com.gcloud.controller.compute.dao.InstanceDao;
import com.gcloud.controller.compute.entity.VmInstance;
import com.gcloud.controller.compute.service.vm.trash.IVmTrashService;
import com.gcloud.core.exception.GCloudException;
import com.gcloud.header.compute.enums.VmTaskState;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by yaowj on 2018/12/3.
 */
@Service
@Slf4j
public class VmTrashServiceImpl implements IVmTrashService {

    @Autowired
    private InstanceDao instanceDao;

    @Override
    public void delete(String instanceId, boolean inTask, String taskId) {

        VmInstance vm = instanceDao.getById(instanceId);
        if (vm == null) {
            throw new GCloudException("0010802::云服务器不存在");
        }

        if(!inTask){
            if (!instanceDao.updateInstanceTaskState(instanceId, VmTaskState.DELETING)) {
                throw new GCloudException("0010803::云服务器当前状态不能删除");
            }
        }

    }
}
