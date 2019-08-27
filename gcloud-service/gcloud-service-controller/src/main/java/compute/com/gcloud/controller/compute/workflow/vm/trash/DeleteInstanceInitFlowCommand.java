package com.gcloud.controller.compute.workflow.vm.trash;

import com.gcloud.controller.compute.dao.InstanceDao;
import com.gcloud.controller.compute.entity.VmInstance;
import com.gcloud.controller.compute.service.vm.base.IVmBaseService;
import com.gcloud.controller.compute.service.vm.trash.IVmTrashService;
import com.gcloud.controller.compute.workflow.model.trash.DeleteInstanceInitFlowCommandReq;
import com.gcloud.controller.compute.workflow.model.trash.DeleteInstanceInitFlowCommandRes;
import com.gcloud.controller.compute.workflow.model.trash.DetachAndDeleteDiskInfo;
import com.gcloud.controller.compute.workflow.model.trash.DetachAndDeleteNetcardInfo;
import com.gcloud.controller.network.dao.PortDao;
import com.gcloud.controller.network.entity.Port;
import com.gcloud.controller.storage.dao.VolumeAttachmentDao;
import com.gcloud.controller.storage.entity.VolumeAttachment;
import com.gcloud.core.workflow.core.BaseWorkFlowCommand;
import com.gcloud.header.compute.enums.Device;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * Created by yaowj on 2018/11/26.
 */
@Component
@Scope("prototype")
@Slf4j
public class DeleteInstanceInitFlowCommand extends BaseWorkFlowCommand {

    @Autowired
    private VolumeAttachmentDao volumeAttachmentDao;

    @Autowired
    private PortDao portDao;

    @Autowired
    private InstanceDao instanceDao;

    @Autowired
    private IVmBaseService vmBaseService;

    @Autowired
    private IVmTrashService vmTrashService;


    @Override
    protected Object process() throws Exception {

        DeleteInstanceInitFlowCommandReq req = (DeleteInstanceInitFlowCommandReq)getReqParams();
        Map<String, VmInstance> insMap = new HashMap<>();

        vmTrashService.delete(req.getInstanceId(), req.getInTask(), getTaskId());

        List<VolumeAttachment> attachments = volumeAttachmentDao.getInstanceRefAttach(req.getInstanceId());
        List<Port> ports = portDao.findByProperty(Port.DEVICE_ID, req.getInstanceId());

        VmInstance ins = instanceDao.getById(req.getInstanceId());

        List<DetachAndDeleteDiskInfo> disks = null;
        List<DetachAndDeleteNetcardInfo> netcards = null;

        if(attachments != null && attachments.size() > 0){
            disks = new ArrayList<>();
            for(VolumeAttachment attachment : attachments){
                DetachAndDeleteDiskInfo disk = new DetachAndDeleteDiskInfo();

                boolean deleteDisk = req.getInstanceId().equals(attachment.getInstanceUuid())
                        && (Device.VDA.getMountPath().equals(attachment.getMountpoint()) || req.getDeleteDataDisk() == null || req.getDeleteDataDisk());

                disk.setDelete(deleteDisk);
                disk.setDiskId(attachment.getVolumeId());
                disk.setInstanceId(attachment.getInstanceUuid());
                disk.setNode(attachment.getAttachedHost());
                disk.setAttachmentId(attachment.getId());
                disks.add(disk);
            }
        }

        if(ports != null && ports.size() > 0){
            netcards = new ArrayList<>();
            for(Port port : ports){
                DetachAndDeleteNetcardInfo detachPort = new DetachAndDeleteNetcardInfo();
                detachPort.setNetcardId(port.getId());
                detachPort.setDelete(req.getDeleteNetcard() != null && req.getDeleteNetcard());
                netcards.add(detachPort);
            }
        }

        DeleteInstanceInitFlowCommandRes res = new DeleteInstanceInitFlowCommandRes();
        res.setDisks(disks);
        res.setNetcards(netcards);
        res.setVmUserId(ins.getUserId());
        res.setTaskId(UUID.randomUUID().toString());


        return res;
    }

    @Override
    protected Object rollback() throws Exception {

        DeleteInstanceInitFlowCommandReq req = (DeleteInstanceInitFlowCommandReq)getReqParams();
        vmBaseService.cleanState(req.getInstanceId(), false);

        return null;
    }

    @Override
    protected Object timeout() throws Exception {
        return null;
    }

    @Override
    protected Class<?> getReqParamClass() {
        return DeleteInstanceInitFlowCommandReq.class;
    }

    @Override
    protected Class<?> getResParamClass() {
        return DeleteInstanceInitFlowCommandRes.class;
    }
}
