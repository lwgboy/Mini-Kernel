package com.gcloud.controller.network.handler.api.router;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;

import com.gcloud.controller.network.model.DescribeVRoutersParams;
import com.gcloud.controller.network.service.IRouterService;
import com.gcloud.core.exception.GCloudException;
import com.gcloud.core.handle.ApiHandler;
import com.gcloud.core.handle.MessageHandler;
import com.gcloud.core.util.BeanUtil;
import com.gcloud.framework.db.PageResult;
import com.gcloud.header.Module;
import com.gcloud.header.SubModule;
import com.gcloud.header.network.model.VRouterSetType;
import com.gcloud.header.network.msg.api.DescribeVRoutersMsg;
import com.gcloud.header.network.msg.api.DescribeVRoutersReplyMsg;

@ApiHandler(module=Module.ECS,subModule=SubModule.VROUTER,action="DescribeVRouters")
public class ApiDescribeVRoutersHandler extends MessageHandler<DescribeVRoutersMsg, DescribeVRoutersReplyMsg>{
	@Autowired
	IRouterService vRouterService;
	
	@Override
	public DescribeVRoutersReplyMsg handle(DescribeVRoutersMsg msg) throws GCloudException {
		DescribeVRoutersParams params = BeanUtil.copyProperties(msg, DescribeVRoutersParams.class);
		PageResult<VRouterSetType> response = vRouterService.describeVRouters(params, msg.getCurrentUser());
		DescribeVRoutersReplyMsg replyMsg = new DescribeVRoutersReplyMsg();
        replyMsg.init(response);
        return replyMsg;
	}

}
