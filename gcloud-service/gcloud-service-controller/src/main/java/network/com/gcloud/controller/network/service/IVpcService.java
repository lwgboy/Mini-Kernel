package com.gcloud.controller.network.service;

import com.gcloud.framework.db.PageResult;
import com.gcloud.header.network.model.VpcsItemType;
import com.gcloud.header.network.msg.api.CreateVpcMsg;
import com.gcloud.header.network.msg.api.DescribeVpcsMsg;
import com.gcloud.header.network.msg.api.ModifyVpcAttributeMsg;

public interface IVpcService {

	PageResult<VpcsItemType> describeVpcs(DescribeVpcsMsg msg);
	String createVpc(CreateVpcMsg msg);
	void removeVpc(String vpcId);
	void updateVpc(ModifyVpcAttributeMsg msg);

}
