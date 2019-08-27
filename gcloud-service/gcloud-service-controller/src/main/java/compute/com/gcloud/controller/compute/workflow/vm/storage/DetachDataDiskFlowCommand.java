package com.gcloud.controller.compute.workflow.vm.storage;

import com.gcloud.controller.compute.workflow.model.storage.DetachDataDiskFlowCommandReq;
import com.gcloud.controller.compute.workflow.model.storage.DetachDataDiskFlowCommandRes;
import com.gcloud.controller.storage.dao.VolumeAttachmentDao;
import com.gcloud.controller.storage.dao.VolumeDao;
import com.gcloud.controller.storage.entity.Volume;
import com.gcloud.controller.storage.entity.VolumeAttachment;
import com.gcloud.controller.storage.service.IVolumeService;
import com.gcloud.core.workflow.core.BaseWorkFlowCommand;
import com.gcloud.header.storage.enums.VolumeStatus;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
@Scope("prototype")
@Slf4j
public class DetachDataDiskFlowCommand extends BaseWorkFlowCommand {

	@Autowired
	private IVolumeService volumeService;

	@Autowired
	private VolumeAttachmentDao attachmentDao;

	@Autowired
	private VolumeDao volumeDao;

	@Override
	protected Object process() throws Exception {
		DetachDataDiskFlowCommandReq req = (DetachDataDiskFlowCommandReq) getReqParams();
		log.debug(String.format("磁盘%s 挂载点：%s 卸载块设备开始", req.getVolumeId(), req.getAttachmentId()));

		VolumeAttachment volumeAttachment = attachmentDao.getById(req.getAttachmentId());
		DetachDataDiskFlowCommandRes res = new DetachDataDiskFlowCommandRes();
		res.setAttachedHost(volumeAttachment.getAttachedHost());
		res.setInstanceUuid(volumeAttachment.getInstanceUuid());
		res.setMountPoint(volumeAttachment.getMountpoint());
		res.setVolumeId(volumeAttachment.getVolumeId());

		volumeService.detachVolume(req.getVolumeId(), req.getAttachmentId());

		log.debug(String.format("磁盘%s 挂载点：%s 卸载块设备结束", req.getVolumeId(), req.getAttachmentId()));
		return res;
	}

	@Override
	protected Object rollback() throws Exception {
		DetachDataDiskFlowCommandReq req = (DetachDataDiskFlowCommandReq) getReqParams();
		DetachDataDiskFlowCommandRes res = (DetachDataDiskFlowCommandRes) getResParams();

		Volume volume = volumeDao.getById(req.getVolumeId());
		if(volume != null && (VolumeStatus.AVAILABLE.value().equals(volume.getStatus()) || VolumeStatus.IN_USE.value().equals(volume.getStatus()))){
			volumeService.attachVolume(req.getVolumeId(), res.getInstanceUuid(), res.getMountPoint(), res.getAttachedHost(), getTaskId());
		}

		return null;
	}

	@Override
	protected Object timeout() throws Exception {
		return null;
	}

	@Override
	protected Class<?> getReqParamClass() {
		return DetachDataDiskFlowCommandReq.class;
	}

	@Override
	protected Class<?> getResParamClass() {
		return DetachDataDiskFlowCommandRes.class;
	}
}
