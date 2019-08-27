package com.gcloud.controller.network.provider;

import com.gcloud.controller.IResourceProvider;
import com.gcloud.controller.network.entity.SecurityGroup;
import com.gcloud.controller.network.model.AuthorizeSecurityGroupParams;
import com.gcloud.controller.network.model.CreateSecurityGroupParams;
import com.gcloud.controller.network.model.DescribeSecurityGroupAttributeResponse;
import com.gcloud.controller.network.model.ModifySecurityGroupAttributeParams;
import com.gcloud.header.api.model.CurrentUser;

import java.util.List;
import java.util.Map;

public interface ISecurityGroupProvider extends IResourceProvider {

    String createSecurityGroup(CreateSecurityGroupParams params, CurrentUser currentUser);

    void deleteSecurityGroup(String id);

    void deleteSecurityGroup(SecurityGroup securityGroup);

    void modifySecurityGroup(ModifySecurityGroupAttributeParams params);

    void modifySecurityGroup(ModifySecurityGroupAttributeParams params, SecurityGroup sg);

    String authorizeSecurityGroup(AuthorizeSecurityGroupParams params);

    void revokeSecurityGroup(String securityGroupId, String securityGroupRuleId);

//    DescribeSecurityGroupAttributeResponse describeSecurityGroupAttribute(String securityGroupId, String direction, String regionId);

    List<SecurityGroup> list(Map<String, String> filter);
}
