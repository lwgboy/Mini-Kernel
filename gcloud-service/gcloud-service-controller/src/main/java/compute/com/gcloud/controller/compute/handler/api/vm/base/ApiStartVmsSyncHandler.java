package com.gcloud.controller.compute.handler.api.vm.base;

import com.gcloud.controller.compute.model.vm.StartupParams;
import com.gcloud.controller.compute.model.vm.StartupResponse;
import com.gcloud.controller.compute.service.vm.base.IVmBaseService;
import com.gcloud.core.handle.ApiHandler;
import com.gcloud.core.handle.MessageHandler;
import com.gcloud.header.Module;
import com.gcloud.header.SubModule;
import com.gcloud.header.compute.model.VmResponse;
import com.gcloud.header.compute.msg.api.vm.base.ApiDemoStartVmsSyncMsg;
import com.gcloud.header.compute.msg.api.vm.base.ApiDemoStartVmsSyncMsgReply;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;

@ApiHandler(module= Module.ECS,subModule=SubModule.VM, action="start_sync")
@Slf4j
public class ApiStartVmsSyncHandler extends MessageHandler<ApiDemoStartVmsSyncMsg, ApiDemoStartVmsSyncMsgReply> {
	@Autowired
	private IVmBaseService vmBaseService;
	@Override
	public ApiDemoStartVmsSyncMsgReply handle(ApiDemoStartVmsSyncMsg msg) {
		// TODO Auto-generated method stub
		ApiDemoStartVmsSyncMsgReply reply = new ApiDemoStartVmsSyncMsgReply();
//		log.info(String.format("云服务器开机开始, 基本信息:%s", msg.getInstanceIds()));
//		StartupParams sp = new StartupParams();
//		sp.setInstanceIds(msg.getInstanceIds());
//		List<StartupResponse> startupResponses = vmBaseService.syncStart(sp);
//		List<VmResponse> msgResponse = new ArrayList<VmResponse>();
//		if(startupResponses != null && startupResponses.size() > 0){
//
//			for(StartupResponse startupRes : startupResponses){
//				VmResponse res = new VmResponse();
//				res.setAlias(startupRes.getAlias());
//				res.setCode(startupRes.getCode());
//				res.setIncreBackupId(startupRes.getIncreBackupId());
//				res.setInstanceId(startupRes.getInstanceId());
//				res.setMessage(startupRes.getMessage());
//				res.setParams(startupRes.getParams());
//				res.setResult(startupRes.getResult());
//				res.setTaskId(startupRes.getTaskId());
//				msgResponse.add(res);
//			}
//
//		}
//
//		reply.setResponses(msgResponse);

		log.info(String.format("云服务器开机结束, 基本信息:%s", msg.getInstanceIds()));
		return reply;
	}

}
