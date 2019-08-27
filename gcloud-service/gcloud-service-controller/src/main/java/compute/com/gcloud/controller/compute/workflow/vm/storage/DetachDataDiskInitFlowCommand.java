package com.gcloud.controller.compute.workflow.vm.storage;

import com.gcloud.controller.compute.dao.InstanceDao;
import com.gcloud.controller.compute.entity.VmInstance;
import com.gcloud.controller.compute.service.vm.base.IVmBaseService;
import com.gcloud.controller.compute.service.vm.storage.IVmDiskService;
import com.gcloud.controller.compute.workflow.model.storage.DetachDataDiskInitFlowCommandReq;
import com.gcloud.controller.compute.workflow.model.storage.DetachDataDiskInitFlowCommandRes;
import com.gcloud.controller.storage.dao.VolumeAttachmentDao;
import com.gcloud.controller.storage.dao.VolumeDao;
import com.gcloud.controller.storage.entity.Volume;
import com.gcloud.controller.storage.entity.VolumeAttachment;
import com.gcloud.controller.storage.service.IVolumeService;
import com.gcloud.core.exception.GCloudException;
import com.gcloud.core.workflow.core.BaseWorkFlowCommand;
import com.gcloud.header.storage.enums.VolumeStatus;
import com.gcloud.header.storage.model.VmVolumeDetail;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.UUID;

/**
 * Created by yaowj on 2018/11/20.
 */
@Component
@Scope("prototype")
@Slf4j
public class DetachDataDiskInitFlowCommand extends BaseWorkFlowCommand {

    @Autowired
    private VolumeAttachmentDao volumeAttachmentDao;

    @Autowired
    private InstanceDao instanceDao;

    @Autowired
    private IVmBaseService vmBaseService;

    @Autowired
    private VolumeDao volumeDao;

    @Autowired
    private IVolumeService volumeService;

    @Autowired
    private IVmDiskService vmDiskService;

    @Override
    protected Object process() throws Exception {
        DetachDataDiskInitFlowCommandReq req = (DetachDataDiskInitFlowCommandReq) getReqParams();
        DetachDataDiskInitFlowCommandRes res = new DetachDataDiskInitFlowCommandRes();

        vmDiskService.detachDataDiskInit(req.getInstanceId(), req.getVolumeId(), req.getInTask());

        try {
            VmInstance instance = instanceDao.getById(req.getInstanceId());

            List<VolumeAttachment> volumeAttachments = volumeAttachmentDao.findByVolumeIdAndInstanceId(req.getVolumeId(), instance.getId());
            if (volumeAttachments == null || volumeAttachments.size() == 0) {
                throw new GCloudException("0011008::云服务器没有挂载此磁盘");
            }

            Volume volume = volumeDao.getById(req.getVolumeId());
            if (volume == null) {
                throw new GCloudException("0011009::磁盘不存在");
            }

            VmVolumeDetail detail = volumeService.volumeDetail(req.getVolumeId(), req.getInstanceId());

            res.setAttachmentId(volumeAttachments.get(0).getId());
            res.setVmUserId(instance.getUserId());
            res.setVmHostName(instance.getHostname());
            res.setInstanceId(req.getInstanceId());
            res.setVolumeId(volume.getId());
            res.setVolumeName(volume.getDisplayName());
            res.setVolumeDetail(detail);
            res.setTaskId(UUID.randomUUID().toString());
        } catch (Exception ex) {
            errorRollback();
            throw ex;
        }
        return res;
    }

    @Override
    protected Object rollback() throws Exception {

        errorRollback();

        return null;
    }

    private void errorRollback() {
        DetachDataDiskInitFlowCommandReq req = (DetachDataDiskInitFlowCommandReq) getReqParams();

        Volume volume = volumeDao.getById(req.getVolumeId());
        if(VolumeStatus.DETACHING.value().equals(volume.getStatus())){
            volumeService.rollDetachingVolume(req.getVolumeId());
        }

        vmBaseService.cleanState(req.getInstanceId(), req.getInTask());
    }

    @Override
    protected Object timeout() throws Exception {
        return null;
    }

    @Override
    protected Class<?> getReqParamClass() {
        return DetachDataDiskInitFlowCommandReq.class;
    }

    @Override
    protected Class<?> getResParamClass() {
        return DetachDataDiskInitFlowCommandRes.class;
    }
}
