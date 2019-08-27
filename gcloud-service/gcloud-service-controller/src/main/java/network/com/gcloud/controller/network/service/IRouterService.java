package com.gcloud.controller.network.service;

import com.gcloud.controller.network.entity.Router;
import com.gcloud.controller.network.model.DescribeVRoutersParams;
import com.gcloud.framework.db.PageResult;
import com.gcloud.header.api.model.CurrentUser;
import com.gcloud.header.network.model.VRouterSetType;
import com.gcloud.header.network.msg.api.CreateVRouterMsg;
import com.gcloud.header.network.msg.api.ModifyVRouterAttributeMsg;

public interface IRouterService {
	String createVRouter(CreateVRouterMsg msg);
	Router getVRouterById(String vRouterId);
	void deleteVRouter(String routerId);
	PageResult<VRouterSetType> describeVRouters(DescribeVRoutersParams params, CurrentUser currentUser);
	void modifyVRouterAttribute(ModifyVRouterAttributeMsg msg);
	void setVRouterGateway(String vRouterId, String networkId, CurrentUser currentUser);
	void cleanVRouterGateway(String vRouterId);
	void attachVSwitchVRouter(String routerId, String subnetId, CurrentUser currentUser);
	void detachVSwitchVRouter(String routerId, String subnetId, String portId);
	void detachVSwitchVRouter(String routerId, String subnetId);
	int routerStatistics();
}
