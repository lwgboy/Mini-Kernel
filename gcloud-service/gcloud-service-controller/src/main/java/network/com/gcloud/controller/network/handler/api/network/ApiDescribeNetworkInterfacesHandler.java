package com.gcloud.controller.network.handler.api.network;

import com.gcloud.controller.network.model.DescribeNetworkInterfacesParams;
import com.gcloud.controller.network.service.IPortService;
import com.gcloud.core.exception.GCloudException;
import com.gcloud.core.handle.ApiHandler;
import com.gcloud.core.handle.MessageHandler;
import com.gcloud.core.util.BeanUtil;
import com.gcloud.framework.db.PageResult;
import com.gcloud.header.Module;
import com.gcloud.header.SubModule;
import com.gcloud.header.network.model.NetworkInterfaceSet;
import com.gcloud.header.network.msg.api.DescribeNetworkInterfacesMsg;
import com.gcloud.header.network.msg.api.DescribeNetworkInterfacesReplyMsg;
import com.gcloud.header.storage.model.DiskItemType;
import org.springframework.beans.factory.annotation.Autowired;

@ApiHandler(module=Module.ECS,subModule=SubModule.NETWORKINTERFACE,action="DescribeNetworkInterfaces")
public class ApiDescribeNetworkInterfacesHandler extends MessageHandler<DescribeNetworkInterfacesMsg, DescribeNetworkInterfacesReplyMsg> {

	@Autowired
	private IPortService portService;

	@Override
	public DescribeNetworkInterfacesReplyMsg handle(DescribeNetworkInterfacesMsg msg) throws GCloudException {

		DescribeNetworkInterfacesParams params = BeanUtil.copyProperties(msg, DescribeNetworkInterfacesParams.class);
		PageResult<NetworkInterfaceSet> response = portService.describe(params, msg.getCurrentUser());
		DescribeNetworkInterfacesReplyMsg replyMsg = new DescribeNetworkInterfacesReplyMsg();
		replyMsg.init(response);
		return replyMsg;
	}

}
