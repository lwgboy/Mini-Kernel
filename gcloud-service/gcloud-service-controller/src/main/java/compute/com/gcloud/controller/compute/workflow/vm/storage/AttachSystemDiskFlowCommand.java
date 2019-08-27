package com.gcloud.controller.compute.workflow.vm.storage;

import com.gcloud.controller.compute.workflow.model.vm.AttachSystemDiskFlowCommandReq;
import com.gcloud.controller.compute.workflow.model.vm.AttachVolumeFlowCommandRes;
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
public class AttachSystemDiskFlowCommand extends BaseWorkFlowCommand {
	@Autowired
	private IVolumeService volumeService;

	@Autowired
	private VolumeDao volumeDao;

	@Override
	protected Object process() throws Exception {
		AttachSystemDiskFlowCommandReq req = (AttachSystemDiskFlowCommandReq) getReqParams();
		log.debug(String.format("虚拟机%s 挂载数据盘块设备开始", req.getInstanceId()));
		String attachmentId = volumeService.attachVolume(req.getVolumeId(), req.getInstanceId(), DiskUtil.getDeviceMountpoint(req.getTargetDev()), req.getCreateHost(), getTaskId());

		VmVolumeDetail volumeDetail = volumeService.volumeDetail(req.getVolumeId(), req.getInstanceId());

		AttachVolumeFlowCommandRes res = new AttachVolumeFlowCommandRes();
		res.setVolumeDetail(volumeDetail);
		res.setAttachmentId(attachmentId);
		log.debug(String.format("虚拟机%s 挂载数据盘块设备结束", req.getInstanceId()));
		return res;
	}

	@Override
	protected Object rollback() throws Exception {
		AttachVolumeFlowCommandRes res = (AttachVolumeFlowCommandRes)getResParams();
		String volumeId = res.getVolumeDetail().getVolumeId();
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
		return AttachSystemDiskFlowCommandReq.class;
	}

	@Override
	protected Class<?> getResParamClass() {
		return AttachVolumeFlowCommandRes.class;// AttachVolumeFlowCommandRes.class;
	}

}
