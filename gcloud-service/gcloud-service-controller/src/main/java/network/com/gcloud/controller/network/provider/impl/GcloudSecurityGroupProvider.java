package com.gcloud.controller.network.provider.impl;

import com.gcloud.controller.network.entity.SecurityGroup;
import com.gcloud.controller.network.model.AuthorizeSecurityGroupParams;
import com.gcloud.controller.network.model.CreateSecurityGroupParams;
import com.gcloud.controller.network.model.DescribeSecurityGroupAttributeResponse;
import com.gcloud.controller.network.model.ModifySecurityGroupAttributeParams;
import com.gcloud.controller.network.provider.ISecurityGroupProvider;
import com.gcloud.header.api.model.CurrentUser;
import com.gcloud.header.enums.ProviderType;
import com.gcloud.header.enums.ResourceType;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Component
public class GcloudSecurityGroupProvider implements ISecurityGroupProvider {

    @Override
    public ResourceType resourceType() {
        return ResourceType.SECURITY_GROUP;
    }

    @Override
    public ProviderType providerType() {
        return ProviderType.GCLOUD;
    }

    @Override
    public String createSecurityGroup(CreateSecurityGroupParams params, CurrentUser currentUser) {
        return null;
    }

    @Override
    public void deleteSecurityGroup(String id) {

    }

    @Override
    public void deleteSecurityGroup(SecurityGroup securityGroup) {

    }

    @Override
    public void modifySecurityGroup(ModifySecurityGroupAttributeParams params) {

    }

    @Override
    public void modifySecurityGroup(ModifySecurityGroupAttributeParams params, SecurityGroup sg) {

    }

    @Override
    public String authorizeSecurityGroup(AuthorizeSecurityGroupParams params) {
        // TODO Auto-generated method stub
    	return null;
    }
    
    @Override
    public void revokeSecurityGroup(String securityGroupId, String securityGroupRuleId) {
        // TODO Auto-generated method stub
        
    }
    
    /*@Override
    public DescribeSecurityGroupAttributeResponse describeSecurityGroupAttribute(String securityGroupId, String direction, String regionId) {
        // TODO Auto-generated method stub
        return null;
    }*/

    @Override
    public List<SecurityGroup> list(Map<String, String> filter) {
        return null;
    }
}
