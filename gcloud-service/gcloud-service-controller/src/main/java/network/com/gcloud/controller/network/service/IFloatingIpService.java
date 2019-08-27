package com.gcloud.controller.network.service;

import com.gcloud.controller.network.model.AllocateEipAddressResponse;
import com.gcloud.controller.network.model.AssociateEipAddressParams;
import com.gcloud.controller.network.model.ModifyEipAddressAttributeParams;
import com.gcloud.framework.db.PageResult;
import com.gcloud.header.api.model.CurrentUser;
import com.gcloud.header.network.model.EipAddressSetType;
import com.gcloud.header.network.msg.api.DescribeEipAddressesMsg;

public interface IFloatingIpService {
	AllocateEipAddressResponse allocateEipAddress(String networkId, String regionId, CurrentUser currentUser);
	void associateEipAddress(AssociateEipAddressParams params);
	void unAssociateEipAddress(String allocationId);
	void releaseEipAddress(String allocationId);
	PageResult<EipAddressSetType> describeEipAddresses(DescribeEipAddressesMsg params);
	void modifyEipAddressAttribute(ModifyEipAddressAttributeParams param);
}
