package com.gcloud.controller.compute.workflow.vm.storage;

import com.gcloud.controller.compute.workflow.model.vm.CreateSystemDiskFlowCommandReq;
import com.gcloud.controller.compute.workflow.model.vm.CreateVolumeFlowCommandRes;
import com.gcloud.controller.storage.model.CreateVolumeParams;
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
public class CreateSystemDiskFlowCommand extends BaseWorkFlowCommand {
	@Autowired
	private IVolumeService volumeService;
	
	@Override
	protected Object process() throws Exception {
		CreateSystemDiskFlowCommandReq req = (CreateSystemDiskFlowCommandReq) getReqParams();

		DiskInfo disk = req.getDataDisk();

		if (disk == null) {
			return null;
		}

		log.debug(String.format("创建块设备 %s 开始", disk.getDiskName()));

		CreateVolumeParams cdp = new CreateVolumeParams();
		cdp.setBootable(true);
		cdp.setDiskCategory(disk.getCategory());
		cdp.setDiskName(disk.getDiskName());
		cdp.setSize(disk.getSize());
		cdp.setTaskId(getTaskId());
		cdp.setDiskType(disk.getDiskType());
		cdp.setImageRef(disk.getImageRef());
		cdp.setZoneId(disk.getZoneId());
		
		String volumeId = volumeService.createVolume(cdp, req.getCreateUser());

		log.debug(String.format("创建块设备  %s 结束", disk.getDiskName()));

		CreateVolumeFlowCommandRes res = new CreateVolumeFlowCommandRes();
		res.setVolumeId(volumeId);
		res.setSize(disk.getSize());
		res.setCategory(disk.getCategory());
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
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected Class<?> getReqParamClass() {
		// TODO Auto-generated method stub
		return CreateSystemDiskFlowCommandReq.class;
	}

	@Override
	protected Class<?> getResParamClass() {
		// TODO Auto-generated method stub
		return CreateVolumeFlowCommandRes.class;
	}

	@Override
	public int getTimeOut() {
		//10分钟
		return 600;
	}
}
