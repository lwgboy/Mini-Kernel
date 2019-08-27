package com.gcloud.controller.compute.handler.api.vm.base;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.gcloud.controller.compute.handler.api.model.DescribeInstanceTypesParams;
import com.gcloud.controller.compute.service.vm.base.IVmBaseService;
import com.gcloud.core.exception.GCloudException;
import com.gcloud.core.handle.ApiHandler;
import com.gcloud.core.handle.MessageHandler;
import com.gcloud.core.util.BeanUtil;
import com.gcloud.framework.db.PageResult;
import com.gcloud.header.Module;
import com.gcloud.header.SubModule;
import com.gcloud.header.compute.msg.api.model.InstanceTypeItemType;
import com.gcloud.header.compute.msg.api.vm.base.ApiDescribeInstanceTypesMsg;
import com.gcloud.header.compute.msg.api.vm.base.ApiDescribeInstanceTypesReplyMsg;

/*
 * @Date Nov 7, 2018
 * 
 * @Author zhangzj
 * 
 * @Description TODO
 */
@ApiHandler(module = Module.ECS,subModule=SubModule.VM,action = "DescribeInstanceTypes")
public class ApiDescribeInstanceTypesHandler extends MessageHandler<ApiDescribeInstanceTypesMsg, ApiDescribeInstanceTypesReplyMsg> {

	@Autowired
	private IVmBaseService vmBaseService;

	@Override
	public ApiDescribeInstanceTypesReplyMsg handle(ApiDescribeInstanceTypesMsg msg) throws GCloudException {

		DescribeInstanceTypesParams params = BeanUtil.copyProperties(msg, DescribeInstanceTypesParams.class);
		List<InstanceTypeItemType> response = vmBaseService.describeInstanceTypes(params);
        for (InstanceTypeItemType type : response) {
            type.setMemorySize(Math.ceil(type.getMemorySize() / 1024));
        }
		ApiDescribeInstanceTypesReplyMsg replyMsg = new ApiDescribeInstanceTypesReplyMsg();
		replyMsg.init(response);
		return replyMsg;
	}
}
