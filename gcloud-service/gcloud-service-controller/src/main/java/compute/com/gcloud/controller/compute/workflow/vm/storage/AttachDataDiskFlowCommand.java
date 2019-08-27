package com.gcloud.controller.compute.workflow.vm.storage;

import com.gcloud.controller.compute.workflow.model.storage.AttachDataDiskFlowCommandReq;
import com.gcloud.controller.compute.workflow.model.storage.AttachDataDiskFlowCommandRes;
import com.gcloud.controller.storage.dao.VolumeDao;
import com.gcloud.controller.storage.entity.Volume;
import com.gcloud.controller.storage.service.IVolumeService;
import com.gcloud.core.workflow.core.BaseWorkFlowCommand;
import com.gcloud.header.storage.enums.VolumeStatus;
import com.gcloud.header.storage.model.VmVolumeDetail;
import com.gcloud.service.common.compute.uitls.DiskUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
@Scope("prototype")
@Slf4j
public class AttachDataDiskFlowCommand extends BaseWorkFlowCommand {

	@Autowired
	private IVolumeService volumeService;

	@Autowired
	private VolumeDao volumeDao;

	@Override
	protected Object process() throws Exception {
		AttachDataDiskFlowCommandReq req = (AttachDataDiskFlowCommandReq) getReqParams();
		log.debug(String.format("虚拟机%s 挂载数据盘块设备开始", req.getInstanceId()));
		
		VmVolumeDetail volumeDetail = req.getVolumeDetail();
		
		String attachmentId = volumeService.attachVolume(volumeDetail.getVolumeId(), req.getInstanceId(), DiskUtil.getDeviceMountpoint(volumeDetail.getTargetDev()), req.getVmHostName(), getTaskId());
		AttachDataDiskFlowCommandRes res = new AttachDataDiskFlowCommandRes();
		res.setAttachmentId(attachmentId);
		log.debug(String.format("虚拟机%s 挂载数据盘块设备结束", req.getInstanceId()));
		return res;
	}

	@Override
	protected Object rollback() throws Exception {

		AttachDataDiskFlowCommandReq req = (AttachDataDiskFlowCommandReq) getReqParams();
		AttachDataDiskFlowCommandRes res = (AttachDataDiskFlowCommandRes) getResParams();

		String volumeId = req.getVolumeDetail().getVolumeId();
		Volume volume = volumeDao.getById(volumeId);
		if(VolumeStatus.IN_USE.value().equals(volume.getStatus())){
			volumeService.beginDetachingVolume(volumeId);
			volumeService.detachVolume(volumeId, res.getAttachmentId());
		}

		return null;
	}

	@Override
	protected Object timeout() throws Exception {
		return null;
	}

	@Override
	protected Class<?> getReqParamClass() {
		return AttachDataDiskFlowCommandReq.class;
	}

	@Override
	protected Class<?> getResParamClass() {
		return AttachDataDiskFlowCommandRes.class;
	}
}
