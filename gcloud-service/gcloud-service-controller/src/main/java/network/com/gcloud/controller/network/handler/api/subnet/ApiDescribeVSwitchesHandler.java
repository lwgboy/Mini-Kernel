package com.gcloud.controller.network.handler.api.subnet;

import com.gcloud.controller.ResourceIsolationCheck;
import com.gcloud.controller.enums.ResourceIsolationCheckType;
import com.gcloud.controller.network.model.DescribeVSwitchesParams;
import com.gcloud.controller.network.service.ISwitchService;
import com.gcloud.core.exception.GCloudException;
import com.gcloud.core.handle.ApiHandler;
import com.gcloud.core.handle.MessageHandler;
import com.gcloud.core.util.BeanUtil;
import com.gcloud.framework.db.PageResult;
import com.gcloud.header.Module;
import com.gcloud.header.SubModule;
import com.gcloud.header.network.model.VSwitchSetType;
import com.gcloud.header.network.msg.api.DescribeVSwitchesMsg;
import com.gcloud.header.network.msg.api.DescribeVSwitchesReplyMsg;
import org.springframework.beans.factory.annotation.Autowired;

@ApiHandler(module=Module.ECS,subModule=SubModule.VSWITCH,action="DescribeVSwitches")
@ResourceIsolationCheck(resourceIsolationCheckType = ResourceIsolationCheckType.NETWORK, resourceIdField = "vpcId")
public class ApiDescribeVSwitchesHandler extends MessageHandler<DescribeVSwitchesMsg, DescribeVSwitchesReplyMsg> {
	@Autowired
	private ISwitchService subnetService;
	
	@Override
	public DescribeVSwitchesReplyMsg handle(DescribeVSwitchesMsg msg) throws GCloudException {
		DescribeVSwitchesParams params = BeanUtil.copyProperties(msg, DescribeVSwitchesParams.class);
		PageResult<VSwitchSetType> response = subnetService.describeVSwitches(params, msg.getCurrentUser());
		DescribeVSwitchesReplyMsg replyMsg = new DescribeVSwitchesReplyMsg();
        replyMsg.init(response);
        return replyMsg;
	}

}
