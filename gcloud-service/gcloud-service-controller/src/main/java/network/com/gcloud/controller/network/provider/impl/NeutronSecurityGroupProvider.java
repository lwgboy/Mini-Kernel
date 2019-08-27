package com.gcloud.controller.network.provider.impl;

import com.gcloud.common.util.StringUtils;
import com.gcloud.controller.network.dao.SecurityGroupDao;
import com.gcloud.controller.network.entity.SecurityGroup;
import com.gcloud.controller.network.model.AuthorizeSecurityGroupParams;
import com.gcloud.controller.network.model.CreateSecurityGroupParams;
import com.gcloud.controller.network.model.DescribeSecurityGroupAttributeResponse;
import com.gcloud.controller.network.model.ModifySecurityGroupAttributeParams;
import com.gcloud.controller.network.provider.ISecurityGroupProvider;
import com.gcloud.controller.network.service.ISecurityGroupService;
import com.gcloud.controller.provider.NeutronProviderProxy;
import com.gcloud.core.exception.GCloudException;
import com.gcloud.core.simpleflow.Flow;
import com.gcloud.core.simpleflow.NoRollbackFlow;
import com.gcloud.core.simpleflow.SimpleFlowChain;
import com.gcloud.header.api.model.CurrentUser;
import com.gcloud.header.enums.ProviderType;
import com.gcloud.header.enums.ResourceType;

import org.openstack4j.model.network.SecurityGroupRule;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Component
public class NeutronSecurityGroupProvider implements ISecurityGroupProvider {

    @Autowired
    private NeutronProviderProxy proxy;

    @Autowired
    private ISecurityGroupService securityGroupService;

    @Autowired
    private SecurityGroupDao securityGroupDao;

    @Override
    public ResourceType resourceType() {
        return ResourceType.SECURITY_GROUP;
    }

    @Override
    public ProviderType providerType() {
        return ProviderType.NEUTRON;
    }

    @Override
    public String createSecurityGroup(CreateSecurityGroupParams params, CurrentUser currentUser){
        SimpleFlowChain<String, String> chain = new SimpleFlowChain<>("createSecurityGroup");
        chain.then(new Flow<String>("createSecurityGroup") {
            @Override
            public void run(SimpleFlowChain chain, String data) {
                String id = proxy.createSecurityGroup(params.getSecurityGroupName(), params.getDescription());
                chain.data(id);
                chain.next();
            }

            @Override
            public void rollback(SimpleFlowChain chain, String data) {
                proxy.removeSecurityCroup(data);
            }

        }).then(new NoRollbackFlow<String>("save to db") {
            @Override
            public void run(SimpleFlowChain chain, String data) {
                SecurityGroup sg = securityGroupService.createSecurityGroupData(params, providerType(), data, currentUser);
                chain.setResult(sg.getId());
                chain.next();
            }
        }).start();

        if(StringUtils.isNotBlank(chain.getErrorCode())){
            throw new GCloudException(chain.getErrorCode());
        }

        return chain.getResult();
    }

    @Override
    public void deleteSecurityGroup(String id) {
        SecurityGroup sg = securityGroupDao.getById(id);
        if(sg != null){
            deleteSecurityGroup(sg);
        }
    }

    @Override
    public void deleteSecurityGroup(SecurityGroup securityGroup) {
        securityGroupService.cleanSecurityGroupData(securityGroup.getId());
        proxy.removeSecurityCroup(securityGroup.getProviderRefId());
    }

    @Override
    public void modifySecurityGroup(ModifySecurityGroupAttributeParams params) {
        SecurityGroup sg = securityGroupDao.getById(params.getSecurityGroupId());
        if(sg != null){
            modifySecurityGroup(params, sg);
        }
    }

    @Override
    public void modifySecurityGroup(ModifySecurityGroupAttributeParams params, SecurityGroup sg) {
        securityGroupService.modifySecurityGroupData(params);
        proxy.modifySecurityGroupAttribute(sg.getProviderRefId(), params.getSecurityGroupName(), params.getDescription());
    }

    @Override
    public String authorizeSecurityGroup(AuthorizeSecurityGroupParams params) {
        return proxy.authorizeSecurityGroup(params);
    }
    
    @Override
    public void revokeSecurityGroup(String securityGroupId, String securityGroupRuleId) {
        proxy.revokeSecurityGroup(securityGroupRuleId);
    }
    
    /*@Override
    public DescribeSecurityGroupAttributeResponse describeSecurityGroupAttribute(String securityGroupId, String direction, String regionId) {
        return proxy.describeSecurityGroupAttribute(securityGroupId, direction, regionId);
    }*/

    @Override
    public List<SecurityGroup> list(Map<String, String> filter) {
        List<org.openstack4j.model.network.SecurityGroup> sg = proxy.listSecurityGroup(filter);
        List<SecurityGroup> retList = new ArrayList<>();
        for (org.openstack4j.model.network.SecurityGroup s : sg) {
            SecurityGroup secGroup = new SecurityGroup();
            secGroup.setUpdatedAt(s.getUpdatedAt());
            secGroup.setDescription(s.getDescription());
            secGroup.setId(s.getId());
            secGroup.setName(s.getName());
            // does not have status.
//            secGroup.setUserId();
            retList.add(secGroup);
        }

        return retList;
    }
}
