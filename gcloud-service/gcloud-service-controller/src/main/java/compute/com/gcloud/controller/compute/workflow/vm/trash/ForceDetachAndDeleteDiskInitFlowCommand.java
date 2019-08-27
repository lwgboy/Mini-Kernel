package com.gcloud.controller.compute.workflow.vm.trash;

import com.gcloud.common.util.StringUtils;
import com.gcloud.controller.compute.dao.InstanceDao;
import com.gcloud.controller.compute.entity.VmInstance;
import com.gcloud.controller.compute.service.vm.storage.IVmDiskService;
import com.gcloud.controller.compute.workflow.model.trash.ForceDetachAndDeleteDiskInitFlowCommandReq;
import com.gcloud.controller.compute.workflow.model.trash.ForceDetachAndDeleteDiskInitFlowCommandRes;
import com.gcloud.controller.storage.dao.VolumeDao;
import com.gcloud.controller.storage.entity.Volume;
import com.gcloud.controller.storage.service.IVolumeService;
import com.gcloud.core.exception.GCloudException;
import com.gcloud.core.workflow.core.BaseWorkFlowCommand;
import com.gcloud.header.storage.enums.VolumeStatus;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 * Created by yaowj on 2018/12/4.
 */
@Component
@Scope("prototype")
@Slf4j
public class ForceDetachAndDeleteDiskInitFlowCommand extends BaseWorkFlowCommand {

    @Autowired
    private InstanceDao instanceDao;

    @Autowired
    private IVmDiskService vmDiskService;

    @Autowired
    private IVolumeService volumeService;

    @Autowired
    private VolumeDao volumeDao;


    @Override
    protected Object process() throws Exception {

        ForceDetachAndDeleteDiskInitFlowCommandReq req = (ForceDetachAndDeleteDiskInitFlowCommandReq)getReqParams();

        ForceDetachAndDeleteDiskInitFlowCommandRes res = new ForceDetachAndDeleteDiskInitFlowCommandRes();
        VmInstance ins = instanceDao.getById(req.getDisk().getInstanceId());
        if(ins == null || StringUtils.isBlank(ins.getUserId())){
            throw new GCloudException("0010804::获取云服务器用户信息失败");
        }


        String node = req.getDisk().getNode();
        if(StringUtils.isBlank(node)){
            node = ins.getHostname();
        }

        if(StringUtils.isBlank(node)){
            throw new GCloudException("0010805::云服务器所在节点失去连接");
        }

        boolean delete = req.getDisk() == null || req.getDisk().isDelete();
        res.setDelete(delete);
        res.setDiskId(req.getDisk().getDiskId());
        res.setInstanceId(req.getDisk().getInstanceId());
        res.setNode(node);
        res.setAttachmentId(req.getDisk().getAttachmentId());
        res.setUserId(ins.getUserId());

        vmDiskService.detachDataDiskInit(req.getDisk().getInstanceId(), req.getDisk().getDiskId(), true);

        return res;
    }

    @Override
    protected Object rollback() throws Exception {
        ForceDetachAndDeleteDiskInitFlowCommandReq req = (ForceDetachAndDeleteDiskInitFlowCommandReq)getReqParams();
        Volume volume = volumeDao.getById(req.getDisk().getDiskId());
        if(volume != null && VolumeStatus.DETACHING.value().equals(volume.getStatus())){
            volumeService.rollDetachingVolume(req.getDisk().getDiskId());
        }
        return null;
    }


    @Override
    protected Object timeout() throws Exception {
        return null;
    }

    @Override
    protected Class<?> getReqParamClass() {
        return ForceDetachAndDeleteDiskInitFlowCommandReq.class;
    }

    @Override
    protected Class<?> getResParamClass() {
        return ForceDetachAndDeleteDiskInitFlowCommandRes.class;
    }

}
