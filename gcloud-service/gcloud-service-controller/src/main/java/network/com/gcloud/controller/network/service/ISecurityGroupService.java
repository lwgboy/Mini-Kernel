package com.gcloud.controller.network.service;

import com.gcloud.common.model.PageParams;
import com.gcloud.controller.network.entity.SecurityGroup;
import com.gcloud.controller.network.model.AuthorizeSecurityGroupParams;
import com.gcloud.controller.network.model.CreateSecurityGroupParams;
import com.gcloud.controller.network.model.DescribeSecurityGroupAttributeResponse;
import com.gcloud.controller.network.model.ModifySecurityGroupAttributeParams;
import com.gcloud.framework.db.PageResult;
import com.gcloud.header.api.model.CurrentUser;
import com.gcloud.header.enums.ProviderType;
import com.gcloud.header.network.model.SecurityGroupItemType;

public interface ISecurityGroupService {
	String createSecurityGroup(CreateSecurityGroupParams params, CurrentUser currentUser);
    void deleteSecurityGroup(String id);
    void modifySecurityGroupAttribute(ModifySecurityGroupAttributeParams params);
    PageResult<SecurityGroupItemType> describeSecurityGroups(PageParams params, CurrentUser currentUser);
    void authorizeSecurityGroup(AuthorizeSecurityGroupParams params, CurrentUser currentUser);
    void revokeSecurityGroup(String securityGroupRuleId);
    DescribeSecurityGroupAttributeResponse describeSecurityGroupAttribute(String securityGroupId, String direction, String regionId);

    void cleanSecurityGroupData(String id);
    SecurityGroup createSecurityGroupData(CreateSecurityGroupParams params, ProviderType provider, String refSecurityGroupId, CurrentUser currentUser);
    void modifySecurityGroupData(ModifySecurityGroupAttributeParams params);
}
