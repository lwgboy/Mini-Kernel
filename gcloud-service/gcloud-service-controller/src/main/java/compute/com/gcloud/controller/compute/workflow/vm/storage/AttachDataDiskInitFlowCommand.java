package com.gcloud.controller.compute.workflow.vm.storage;

import com.gcloud.controller.compute.dao.InstanceDao;
import com.gcloud.controller.compute.entity.VmInstance;
import com.gcloud.controller.compute.service.vm.base.IVmBaseService;
import com.gcloud.controller.compute.service.vm.storage.IVmDiskService;
import com.gcloud.controller.compute.workflow.model.storage.AttachDataDiskInitFlowCommandReq;
import com.gcloud.controller.compute.workflow.model.storage.AttachDataDiskInitFlowCommandRes;
import com.gcloud.controller.storage.dao.VolumeDao;
import com.gcloud.controller.storage.entity.Volume;
import com.gcloud.controller.storage.service.IVolumeService;
import com.gcloud.core.workflow.core.BaseWorkFlowCommand;
import com.gcloud.header.storage.enums.VolumeStatus;
import com.gcloud.header.storage.model.VmVolumeDetail;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.UUID;

/**
 * Created by yaowj on 2018/11/15.
 */
@Component
@Scope("prototype")
@Slf4j
public class AttachDataDiskInitFlowCommand extends BaseWorkFlowCommand {

	@Autowired
	private VolumeDao volumeDao;

	@Autowired
	private InstanceDao instanceDao;

	@Autowired
	private IVmBaseService vmBaseService;

	@Autowired
	private IVolumeService volumeService;

	@Autowired
	private IVmDiskService vmDiskService;

	@Override
	protected Object process() throws Exception {

		AttachDataDiskInitFlowCommandReq req = (AttachDataDiskInitFlowCommandReq) getReqParams();
		vmDiskService.attachDataDiskInit(req.getInstanceId(), req.getVolumeId(), req.getInTask());

		AttachDataDiskInitFlowCommandRes res = new AttachDataDiskInitFlowCommandRes();
		try {
			Volume volume = volumeDao.getById(req.getVolumeId());
			VmInstance instance = instanceDao.getById(req.getInstanceId());

			VmVolumeDetail volumeDetail = volumeService.volumeDetail(req.getVolumeId(), req.getInstanceId());
			res.setVmHostName(instance.getHostname());
			res.setVolumeDetail(volumeDetail);
			res.setVolumeId(volume.getId());
			res.setVolumeName(volume.getDisplayName());
			res.setInstanceId(req.getInstanceId());
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

	public void errorRollback() {
		AttachDataDiskInitFlowCommandReq req = (AttachDataDiskInitFlowCommandReq) getReqParams();
		Volume volume = volumeDao.getById(req.getVolumeId());
		if(VolumeStatus.ATTACHING.value().equals(volume.getStatus())){
			volumeService.unreserveVolume(req.getVolumeId());
		}

		vmBaseService.cleanState(req.getInstanceId(), req.getInTask());

	}

	@Override
	protected Object timeout() throws Exception {
		return null;
	}

	@Override
	protected Class<?> getReqParamClass() {
		return AttachDataDiskInitFlowCommandReq.class;
	}

	@Override
	protected Class<?> getResParamClass() {
		return AttachDataDiskInitFlowCommandRes.class;
	}
}
