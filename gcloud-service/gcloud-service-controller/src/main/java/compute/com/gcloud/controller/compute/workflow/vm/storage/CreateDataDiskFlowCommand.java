package com.gcloud.controller.compute.workflow.vm.storage;

import com.gcloud.controller.compute.workflow.model.storage.CreateDataDiskFlowCommandReq;
import com.gcloud.controller.compute.workflow.model.vm.CreateVolumeFlowCommandRes;
import com.gcloud.controller.storage.model.CreateDiskParams;
import com.gcloud.controller.storage.service.IVolumeService;
import com.gcloud.core.workflow.core.BaseWorkFlowCommand;
import com.gcloud.header.compute.msg.api.model.DiskInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
@Scope("prototype")
@Slf4j
public class CreateDataDiskFlowCommand  extends BaseWorkFlowCommand{
	@Autowired
	IVolumeService volumeService;
	
	@Override
	protected Object process() throws Exception {
		CreateDataDiskFlowCommandReq req = (CreateDataDiskFlowCommandReq) getReqParams();
		CreateDiskParams params = new CreateDiskParams();
		DiskInfo disk = req.getDataDisk();
		params.setDescription(disk.getDescription());
		params.setDiskCategory(disk.getCategory());
		params.setDiskName(disk.getDiskName());
		params.setSize(disk.getSize());
		params.setTaskId(getTaskId());
		params.setZoneId(disk.getZoneId());
		
		String volumeId = volumeService.create(params, req.getCreateUser());
		
		CreateVolumeFlowCommandRes res = new CreateVolumeFlowCommandRes();
		res.setVolumeId(volumeId);
		res.setSize(params.getSize());
		res.setCategory(params.getDiskCategory());
		return res;
	}

	@Override
	protected Object rollback() throws Exception {
		CreateVolumeFlowCommandRes res = (CreateVolumeFlowCommandRes)getResParams();

		volumeService.deleteVolume(res.getVolumeId(), getTaskId());

		return null;
	}

	@Override
	protected Object timeout() throws Exception {
		return null;
	}

	@Override
	protected Class<?> getReqParamClass() {
		return CreateDataDiskFlowCommandReq.class;
	}

	@Override
	protected Class<?> getResParamClass() {
		return CreateVolumeFlowCommandRes.class;
	}

	@Override
	public int getTimeOut() {
		//10分钟
		return 600;
	}
}
