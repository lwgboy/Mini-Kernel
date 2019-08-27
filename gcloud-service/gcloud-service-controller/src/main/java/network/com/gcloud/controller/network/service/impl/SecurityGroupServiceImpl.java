package com.gcloud.controller.network.service.impl;

import com.gcloud.common.model.PageParams;
import com.gcloud.common.util.StringUtils;
import com.gcloud.controller.ResourceProviders;
import com.gcloud.controller.network.dao.SecurityGroupDao;
import com.gcloud.controller.network.dao.SecurityGroupPortBindingDao;
import com.gcloud.controller.network.dao.SecurityGroupRuleDao;
import com.gcloud.controller.network.entity.SecurityGroup;
import com.gcloud.controller.network.entity.SecurityGroupRule;
import com.gcloud.controller.network.enums.Ethertype;
import com.gcloud.controller.network.enums.SecurityDirection;
import com.gcloud.controller.network.model.AuthorizeSecurityGroupParams;
import com.gcloud.controller.network.model.CreateSecurityGroupParams;
import com.gcloud.controller.network.model.DescribeSecurityGroupAttributeResponse;
import com.gcloud.controller.network.model.ModifySecurityGroupAttributeParams;
import com.gcloud.controller.network.provider.ISecurityGroupProvider;
import com.gcloud.controller.network.service.ISecurityGroupService;
import com.gcloud.core.cache.container.CacheContainer;
import com.gcloud.core.cache.enums.CacheType;
import com.gcloud.core.exception.GCloudException;
import com.gcloud.framework.db.PageResult;
import com.gcloud.header.api.model.CurrentUser;
import com.gcloud.header.enums.ProviderType;
import com.gcloud.header.enums.ResourceType;
import com.gcloud.header.network.model.PermissionType;
import com.gcloud.header.network.model.PermissionTypes;
import com.gcloud.header.network.model.SecurityGroupItemType;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
@Transactional(propagation = Propagation.REQUIRES_NEW)
public class SecurityGroupServiceImpl implements ISecurityGroupService {
    @Autowired
    private SecurityGroupDao securityGroupDao;

    @Autowired
    private SecurityGroupPortBindingDao securityGroupPortBindingDao;
    
    @Autowired
    private SecurityGroupRuleDao securityGroupRuleDao;

    @Override
    public PageResult<SecurityGroupItemType> describeSecurityGroups(PageParams params, CurrentUser currentUser) {
        return securityGroupDao.describeSecurityGroups(params, SecurityGroupItemType.class, currentUser);
    }

    @Override
    public String createSecurityGroup(CreateSecurityGroupParams params, CurrentUser currentUser) throws GCloudException {
        return this.getProviderOrDefault(params.getProvider()).createSecurityGroup(params, currentUser);
    }

    @Override
    public void deleteSecurityGroup(String id) throws GCloudException {
        SecurityGroup sg = securityGroupDao.getById(id);
        if (null == sg) {
            throw new GCloudException("0040402::找不到安全组");
        }

        if(sg.getIsDefault() != null && sg.getIsDefault() && StringUtils.isBlank(sg.getTenantId())){
            throw new GCloudException("0040403::默认安全组不能删除");
        }

        this.checkAndGetProvider(sg.getProvider()).deleteSecurityGroup(sg);
    }

    @Override
    public void modifySecurityGroupAttribute(ModifySecurityGroupAttributeParams params) {
        SecurityGroup sg = securityGroupDao.getById(params.getSecurityGroupId());
        if (null == sg) {
            throw new GCloudException("0040202::找不到安全组");
        }

        if(sg.getIsDefault() != null && sg.getIsDefault()){
            throw new GCloudException("0040205::默认安全组不能修改");
        }

        this.checkAndGetProvider(sg.getProvider()).modifySecurityGroup(params, sg);
        
        CacheContainer.getInstance().put(CacheType.SECURITYGROUP_NAME, params.getSecurityGroupId(), params.getSecurityGroupName());
    }

    @Override
    public void authorizeSecurityGroup(AuthorizeSecurityGroupParams params, CurrentUser currentUser) {
    	AuthorizeSecurityGroupParams refParams = new AuthorizeSecurityGroupParams();
    	BeanUtils.copyProperties(params, refParams);
        SecurityGroup sg = securityGroupDao.getById(params.getSecurityGroupId());
        if (null == sg) {
            throw new GCloudException("0040504::找不到安全组");
        }
        refParams.setSecurityGroupId(sg.getProviderRefId());
        if (StringUtils.isNotBlank(params.getSourceGroupId())) {
            SecurityGroup source = securityGroupDao.getById(params.getSourceGroupId());
            if (null == source) {
                throw new GCloudException("0040505::找不到源安全组");
            }
            refParams.setSourceGroupId(source.getProviderRefId());
        }
        if (StringUtils.isNotBlank(params.getDestGroupId())) {
            SecurityGroup dest = securityGroupDao.getById(params.getDestGroupId());
            if (null == dest) {
                throw new GCloudException("0040506::找不到目标安全组");
            }
            refParams.setDestGroupId(dest.getProviderRefId());
        }
        
        String ruleId = createSecurityGroupRule(params, currentUser, sg);
        
        String ruleProviderRefId = this.checkAndGetProvider(sg.getProvider()).authorizeSecurityGroup(refParams);
        
        SecurityGroupRule rule = new SecurityGroupRule();
        rule.setId(ruleId);
        List<String> updateField = new ArrayList<>();
        updateField.add(rule.updateProviderRefId(ruleProviderRefId));
        securityGroupRuleDao.update(rule, updateField);
    }
    
    private String createSecurityGroupRule(AuthorizeSecurityGroupParams params, CurrentUser currentUser, SecurityGroup sg) {
    	SecurityGroupRule rule = new SecurityGroupRule();
    	rule.setCreateTime(new Date());
    	rule.setDirection(params.getDirection());
    	rule.setEthertype(Ethertype.IPv4.getValue());
    	rule.setId(UUID.randomUUID().toString());
    	rule.setPortRange(params.getPortRange());
    	rule.setProtocol(params.getIpProtocol());
    	rule.setProvider(sg.getProvider());
    	if(params.getDirection().equals(SecurityDirection.EGRESS.getValue())) {
    		if(com.gcloud.common.util.StringUtils.isNotBlank(params.getDestGroupId())) {
    			rule.setRemoteGroupId(params.getDestGroupId());
			}
			if(com.gcloud.common.util.StringUtils.isNotBlank(params.getDestCidrIp())) {
				rule.setRemoteIpPrefix(params.getDestCidrIp());
			}
    	} else {
    		if(com.gcloud.common.util.StringUtils.isNotBlank(params.getSourceGroupId())) {
    			rule.setRemoteGroupId(params.getSourceGroupId());
			}
			if(com.gcloud.common.util.StringUtils.isNotBlank(params.getSourceCidrIp())) {
				rule.setRemoteIpPrefix(params.getSourceCidrIp());
			}
    	}
    	rule.setSecurityGroupId(sg.getId());
    	rule.setTenantId(currentUser.getDefaultTenant());
    	rule.setUserId(currentUser.getId());
    	
    	securityGroupRuleDao.save(rule);
    	
    	return rule.getId();
    }

    @Override
    public void revokeSecurityGroup(String securityGroupRuleId) {
        SecurityGroupRule sgRule = securityGroupRuleDao.getById(securityGroupRuleId);
        if (null == sgRule) {
            throw new GCloudException("0040602::找不到安全组规则");
        }
        
        SecurityGroup sg = securityGroupDao.getById(sgRule.getSecurityGroupId());
        if (null == sg) {
            throw new GCloudException("0040603::找不到安全组");
        }
        securityGroupRuleDao.deleteById(securityGroupRuleId);
        
        this.checkAndGetProvider(sg.getProvider()).revokeSecurityGroup(sg.getProviderRefId(), sgRule.getProviderRefId());
    }

    @Override
    public DescribeSecurityGroupAttributeResponse describeSecurityGroupAttribute(String securityGroupId, String direction, String regionId) {
        SecurityGroup sGroup = securityGroupDao.getById(securityGroupId);
        if (null == sGroup) {
            throw new GCloudException("0040702::找不到安全组");
        }
        
		DescribeSecurityGroupAttributeResponse response = new DescribeSecurityGroupAttributeResponse();
		PermissionTypes permissionTypes = new PermissionTypes();

		response.setDescription(sGroup.getDescription());
		response.setRegionId(regionId);
		response.setSecurityGroupId(sGroup.getId());
		response.setSecurityGroupName(sGroup.getName());
		
		Map<String, Object> pars = new HashMap<String, Object>();
		pars.put("security_group_id", securityGroupId);
		if(null != direction) {
			pars.put("direction", direction);
		}
		
		List<SecurityGroupRule> rules = securityGroupRuleDao.findByProperties(pars);

		for (SecurityGroupRule rule : rules) {
			PermissionType type = new PermissionType();
			type.setSecurityGroupRuleId(rule.getId());
			type.setIpProtocol(rule.getProtocol());
			type.setDirection(rule.getDirection());
			type.setPortRange(rule.getPortRange());

			if (rule.getDirection() != null) {
				if(rule.getDirection().equals(SecurityDirection.EGRESS.getValue())) {
					type.setDestGroupId(rule.getRemoteGroupId());
					type.setDestCidrIp(rule.getRemoteIpPrefix());
				} else if(rule.getDirection().equals(SecurityDirection.INGRESS.getValue())) {
					type.setSourceGroupId(rule.getRemoteGroupId());
					type.setSourceCidrIp(rule.getRemoteIpPrefix());
				}
			}

			permissionTypes.getPermission().add(type);
		}

		response.setPermissions(permissionTypes);
		
		return response;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public void cleanSecurityGroupData(String id) {
        securityGroupDao.deleteById(id);
        securityGroupPortBindingDao.deleteBySecurityGroup(id);
        securityGroupRuleDao.deleteBySecurityGroup(id);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public SecurityGroup createSecurityGroupData(CreateSecurityGroupParams params, ProviderType provider, String refSecurityGroupId, CurrentUser currentUser) {
        SecurityGroup sg = new SecurityGroup();
        sg.setId(UUID.randomUUID().toString());
        sg.setCreateTime(new Date());
        sg.setDescription(params.getDescription());
        sg.setName(params.getSecurityGroupName());
        sg.setUserId(currentUser.getId());
        sg.setTenantId(currentUser.getDefaultTenant());
        sg.setProvider(provider.getValue());
        sg.setProviderRefId(refSecurityGroupId);
        if(params.isDefaultSg()) {
        	sg.setIsDefault(true);//租户默认安全组
        } else {
        	sg.setIsDefault(false);
        }
        securityGroupDao.save(sg);

        return sg;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public void modifySecurityGroupData(ModifySecurityGroupAttributeParams params) {
        SecurityGroup sg = new SecurityGroup();
        sg.setId(params.getSecurityGroupId());

        List<String> updateField = new ArrayList<>();
        if (params.getSecurityGroupName() != null) {
            updateField.add(sg.updateName(params.getSecurityGroupName()));
        }

        if (params.getDescription() != null) {
            updateField.add(sg.updateDescription(params.getDescription()));
        }

        if (updateField.size() > 0) {
            securityGroupDao.update(sg, updateField);
        }
    }

    private ISecurityGroupProvider getProviderOrDefault(Integer providerType) {
        ISecurityGroupProvider provider = ResourceProviders.getOrDefault(ResourceType.SECURITY_GROUP, providerType);
        return provider;
    }

    private ISecurityGroupProvider checkAndGetProvider(Integer providerType) {
        ISecurityGroupProvider provider = ResourceProviders.checkAndGet(ResourceType.SECURITY_GROUP, providerType);
        return provider;
    }

}
