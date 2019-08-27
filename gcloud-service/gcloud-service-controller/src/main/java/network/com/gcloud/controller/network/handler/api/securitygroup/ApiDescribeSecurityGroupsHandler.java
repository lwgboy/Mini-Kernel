package com.gcloud.controller.network.handler.api.securitygroup;

import org.springframework.beans.factory.annotation.Autowired;

import com.gcloud.common.model.PageParams;
import com.gcloud.controller.network.service.ISecurityGroupService;
import com.gcloud.core.exception.GCloudException;
import com.gcloud.core.handle.ApiHandler;
import com.gcloud.core.handle.MessageHandler;
import com.gcloud.core.util.BeanUtil;
import com.gcloud.framework.db.PageResult;
import com.gcloud.header.Module;
import com.gcloud.header.SubModule;
import com.gcloud.header.network.model.SecurityGroupItemType;
import com.gcloud.header.network.msg.api.DescribeSecurityGroupsMsg;
import com.gcloud.header.network.msg.api.DescribeSecurityGroupsReplyMsg;

@ApiHandler(module=Module.ECS,subModule=SubModule.SECURITYGROUP,action="DescribeSecurityGroups")
public class ApiDescribeSecurityGroupsHandler extends MessageHandler<DescribeSecurityGroupsMsg, DescribeSecurityGroupsReplyMsg> {
	@Autowired
	ISecurityGroupService securityGroupService;
	
	@Override
	public DescribeSecurityGroupsReplyMsg handle(DescribeSecurityGroupsMsg msg) throws GCloudException {
		PageParams params = BeanUtil.copyProperties(msg, PageParams.class);
        PageResult<SecurityGroupItemType> response = securityGroupService.describeSecurityGroups(params, msg.getCurrentUser());
        DescribeSecurityGroupsReplyMsg replyMsg = new DescribeSecurityGroupsReplyMsg();
        replyMsg.init(response);
        return replyMsg;
	}

}
