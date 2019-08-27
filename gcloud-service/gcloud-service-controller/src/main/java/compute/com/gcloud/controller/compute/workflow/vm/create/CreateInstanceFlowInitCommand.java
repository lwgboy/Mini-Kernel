package com.gcloud.controller.compute.workflow.vm.create;

import com.gcloud.controller.compute.dao.InstanceDao;
import com.gcloud.controller.compute.dispatcher.Dispatcher;
import com.gcloud.controller.compute.model.vm.CreateInstanceByImageInitParams;
import com.gcloud.controller.compute.model.vm.CreateInstanceByImageInitResponse;
import com.gcloud.controller.compute.service.vm.create.IVmCreateService;
import com.gcloud.controller.compute.workflow.model.vm.CreateInstanceFlowInitCommandReq;
import com.gcloud.controller.compute.workflow.model.vm.CreateInstanceFlowInitCommandRes;
import com.gcloud.core.service.SpringUtil;
import com.gcloud.core.util.BeanUtil;
import com.gcloud.core.workflow.core.BaseWorkFlowCommand;
import com.gcloud.header.compute.msg.api.model.DiskInfo;
import com.gcloud.header.storage.enums.DiskType;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
@Scope("prototype")
@Slf4j
public class CreateInstanceFlowInitCommand extends BaseWorkFlowCommand {

	@Autowired
	private IVmCreateService vmCreateService;

	@Override
	protected Object process() throws Exception {
		CreateInstanceFlowInitCommandReq req = (CreateInstanceFlowInitCommandReq) getReqParams();
		CreateInstanceByImageInitParams params = BeanUtil.copyProperties(req, CreateInstanceByImageInitParams.class);
		CreateInstanceByImageInitResponse createInitRes = vmCreateService.createInstanceByImageInit(params, req.getCurrentUser());


		DiskInfo systemDisk = new DiskInfo();
		systemDisk.setDiskName(req.getSystemDiskName());
		systemDisk.setCategory(req.getSystemDiskCategory());
		systemDisk.setDiskType(DiskType.SYSTEM);
		systemDisk.setImageRef(req.getImageId());
		systemDisk.setZoneId(req.getZoneId());

		CreateInstanceFlowInitCommandRes res = new CreateInstanceFlowInitCommandRes();
		systemDisk.setSize(createInitRes.getSystemDiskSize());
		res.setStorageType(createInitRes.getStorageType());
		res.setImageInfo(createInitRes.getImageInfo());
		res.setCreateHost(createInitRes.getCreateHost());
		res.setInstanceId(createInitRes.getId());
		res.setCpu(createInitRes.getCpu());
		res.setMemory(createInitRes.getMemory());
		res.setSystemDiskSize(createInitRes.getSystemDiskSize());
		res.setCreateUser(createInitRes.getCreateUser());

		res.setInstanceName(req.getInstanceName());
		res.setSystemDisk(systemDisk);

		if(req.getDataDisk() != null && req.getDataDisk().size() > 0){
			for(DiskInfo diskInfo : req.getDataDisk()){
				if(StringUtils.isBlank(diskInfo.getZoneId())){
					diskInfo.setZoneId(req.getZoneId());
				}

			}
		}
		res.setDataDisk(req.getDataDisk());



		//如果有其他业务逻辑，跑错时需要调用rollback

		return res;
	}

	@Override
	protected Object rollback() throws Exception {
		CreateInstanceFlowInitCommandRes res = (CreateInstanceFlowInitCommandRes)getResParams();
	    try{
            InstanceDao instanceDao = (InstanceDao) SpringUtil.getBean("instanceDao");
            instanceDao.deleteById(res.getInstanceId());
        }catch (Exception ex){
            log.error("创建云服务器回滚，删除虚拟机失败", ex);
        }


        try{
            Dispatcher.dispatcher().release(res.getCreateHost(), res.getCpu(), res.getMemory());
        }catch (Exception ex){
            log.error("创建云服务器回滚，释放资源失败", ex);
        }

		return null;
	}


	@Override
	protected Object timeout() throws Exception {
		CreateInstanceFlowInitCommandRes res = (CreateInstanceFlowInitCommandRes) getResParams();
		InstanceDao instanceDao = (InstanceDao) SpringUtil.getBean("instanceDao");
		/*
		 * if(vm.getState().equals(VmState)) {
		 * 
		 * }
		 */
		return null;
	}

	@Override
	protected Class<?> getReqParamClass() {
		return CreateInstanceFlowInitCommandReq.class;
	}

	@Override
	protected Class<?> getResParamClass() {
		return CreateInstanceFlowInitCommandRes.class;
	}

}
