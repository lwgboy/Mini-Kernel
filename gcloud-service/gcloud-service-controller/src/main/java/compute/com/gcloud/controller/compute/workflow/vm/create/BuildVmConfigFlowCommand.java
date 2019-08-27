package com.gcloud.controller.compute.workflow.vm.create;

import com.gcloud.controller.compute.model.vm.VmImageInfo;
import com.gcloud.controller.compute.workflow.model.vm.BuildVmConfigFlowCommandReq;
import com.gcloud.core.messagebus.MessageBus;
import com.gcloud.core.util.MessageUtil;
import com.gcloud.core.workflow.core.BaseWorkFlowCommand;
import com.gcloud.header.compute.enums.CloudDiskTargetDev;
import com.gcloud.header.compute.msg.node.vm.create.BuildVmConfigMsg;
import com.gcloud.header.compute.msg.node.vm.model.VmDetail;
import com.gcloud.header.compute.msg.node.vm.trash.CleanInstanceInfoMsg;
import com.gcloud.header.storage.model.VmVolumeDetail;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
@Scope("prototype")
@Slf4j
public class BuildVmConfigFlowCommand extends BaseWorkFlowCommand {

	@Autowired
    private MessageBus bus;

	@Override
	protected Object process() throws Exception {
		BuildVmConfigFlowCommandReq req = (BuildVmConfigFlowCommandReq) getReqParams();

		// 封装VmDetail
		VmDetail vmDetail = new VmDetail();
		vmDetail.setAutoStart(req.getAutoStart());
		vmDetail.setCore(req.getCore());
		vmDetail.setCpuCore(req.getCpuCore());
		vmDetail.setCpuSocket(req.getCpuSocket());
		vmDetail.setCpuThread(req.getCpuThread());
		vmDetail.setDisk(req.getDisk());
		if(req.getImageInfo() != null){
            VmImageInfo imageInfo = req.getImageInfo();
		    vmDetail.setImageId(imageInfo.getImageId());
		    vmDetail.setImagePath(imageInfo.getImagePath());
		    vmDetail.setImagePoolId(imageInfo.getImagePoolId());
		    vmDetail.setImageStorageType(imageInfo.getImageStorageType());

		}
		vmDetail.setId(req.getInstanceId());
		vmDetail.setMemory(req.getMemory());// 传MB下去
		vmDetail.setPoolId(req.getPoolId());
		vmDetail.setStorageType(req.getStorageType());
		vmDetail.setUserId(req.getCreateUser().getId());
		vmDetail.setIsUsbRedir(0);
		vmDetail.setZxAuth(req.getZxAuth());

		vmDetail.setVmNetwork(req.getVmNetworks());

		List<VmVolumeDetail> vmDisks = new ArrayList<VmVolumeDetail>();
		vmDisks.add(req.getVmSysDisk());
		if (null != req.getVmDataDisks() && req.getVmDataDisks().size() > 0) {
			List<VmVolumeDetail> dataDisks = req.getVmDataDisks();
			for (int i = 0; i < dataDisks.size(); i++) {
				VmVolumeDetail disk = dataDisks.get(i);
				disk.setTargetDev(CloudDiskTargetDev.getDev(i + 1));
			}
			vmDisks.addAll(dataDisks);
		}

		vmDetail.setVmDisks(vmDisks);

		BuildVmConfigMsg msg = new BuildVmConfigMsg();
		msg.setTaskId(getTaskId());
		msg.setUserId(req.getCreateUser().getId());
		msg.setVmDetail(vmDetail);

		msg.setServiceId(MessageUtil.computeServiceId(req.getCreateHost()));

		bus.send(msg);

		return null;
	}

	/*
	 * @Override protected Object postProcess(Map<String, Object> params) throws
	 * Exception { // 根据执行结果修改数据库 return null; }
	 */

	@Override
	protected Object rollback() throws Exception {

		BuildVmConfigFlowCommandReq req = (BuildVmConfigFlowCommandReq) getReqParams();

		CleanInstanceInfoMsg msg = new CleanInstanceInfoMsg();
		msg.setInstanceId(req.getInstanceId());
		msg.setTaskId(getTaskId());
		msg.setServiceId(MessageUtil.computeServiceId(req.getCreateHost()));

		bus.send(msg);

		return null;
	}

	@Override
	protected Object timeout() throws Exception {
		return null;
	}

	@Override
	protected Class<?> getReqParamClass() {
		return BuildVmConfigFlowCommandReq.class;
	}

	@Override
	protected Class<?> getResParamClass() {
		return null;
	}

}
