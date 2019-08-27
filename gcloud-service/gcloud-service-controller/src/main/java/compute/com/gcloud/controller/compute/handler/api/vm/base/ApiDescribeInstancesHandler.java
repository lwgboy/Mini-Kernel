package com.gcloud.controller.compute.handler.api.vm.base;

import org.springframework.beans.factory.annotation.Autowired;

import com.gcloud.controller.compute.handler.api.model.DescribeInstancesParams;
import com.gcloud.controller.compute.service.vm.base.IVmBaseService;
import com.gcloud.core.exception.GCloudException;
import com.gcloud.core.handle.ApiHandler;
import com.gcloud.core.handle.MessageHandler;
import com.gcloud.core.util.BeanUtil;
import com.gcloud.framework.db.PageResult;
import com.gcloud.header.Module;
import com.gcloud.header.SubModule;
import com.gcloud.header.compute.msg.api.model.InstanceAttributesType;
import com.gcloud.header.compute.msg.api.vm.base.ApiDescribeInstancesMsg;
import com.gcloud.header.compute.msg.api.vm.base.ApiDescribeInstancesReplyMsg;

@ApiHandler(module = Module.ECS,subModule=SubModule.VM,action = "DescribeInstances")
public class ApiDescribeInstancesHandler extends MessageHandler<ApiDescribeInstancesMsg, ApiDescribeInstancesReplyMsg> {
	@Autowired
	private IVmBaseService vmBaseService;
	
	@Override
	public ApiDescribeInstancesReplyMsg handle(ApiDescribeInstancesMsg msg) throws GCloudException {
		DescribeInstancesParams params = BeanUtil.copyProperties(msg, DescribeInstancesParams.class);
		PageResult<InstanceAttributesType> response = vmBaseService.describeInstances(params, msg.getCurrentUser());
		ApiDescribeInstancesReplyMsg replyMsg = new ApiDescribeInstancesReplyMsg();
		replyMsg.init(response);
		return replyMsg;
	}

}
