package com.gcloud.controller.network.handler.api.floatingip;

import org.springframework.beans.factory.annotation.Autowired;

import com.gcloud.controller.network.service.IFloatingIpService;
import com.gcloud.core.exception.GCloudException;
import com.gcloud.core.handle.ApiHandler;
import com.gcloud.core.handle.MessageHandler;
import com.gcloud.framework.db.PageResult;
import com.gcloud.header.Module;
import com.gcloud.header.SubModule;
import com.gcloud.header.network.model.EipAddressSetType;
import com.gcloud.header.network.msg.api.DescribeEipAddressesMsg;
import com.gcloud.header.network.msg.api.DescribeEipAddressesReplyMsg;
@ApiHandler(module=Module.ECS,subModule=SubModule.EIPADDRSS,action="DescribeEipAddresses")
public class ApiDescribeEipAddressesHandler extends MessageHandler<DescribeEipAddressesMsg, DescribeEipAddressesReplyMsg> {
	@Autowired
	IFloatingIpService eipService;
	
	@Override
	public DescribeEipAddressesReplyMsg handle(DescribeEipAddressesMsg msg) throws GCloudException {
		PageResult<EipAddressSetType> response = eipService.describeEipAddresses(msg);
		DescribeEipAddressesReplyMsg replyMsg = new DescribeEipAddressesReplyMsg();
        replyMsg.init(response);
        return replyMsg;
	}

}
