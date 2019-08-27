package com.gcloud.controller.network.service.impl;

import com.gcloud.controller.ResourceProviders;
import com.gcloud.controller.network.dao.RouterDao;
import com.gcloud.controller.network.entity.Router;
import com.gcloud.controller.network.model.DescribeVRoutersParams;
import com.gcloud.controller.network.provider.IRouterProvider;
import com.gcloud.controller.network.service.IRouterService;
import com.gcloud.controller.network.service.IVpcService;
import com.gcloud.core.cache.container.CacheContainer;
import com.gcloud.core.cache.enums.CacheType;
import com.gcloud.core.exception.GCloudException;
import com.gcloud.core.util.BeanUtil;
import com.gcloud.framework.db.PageResult;
import com.gcloud.header.enums.ResourceType;
import com.gcloud.header.network.model.VRouterSetType;
import com.gcloud.header.network.model.VpcsItemType;
import com.gcloud.header.network.msg.api.CreateVRouterMsg;
import com.gcloud.header.network.msg.api.CreateVpcMsg;
import com.gcloud.header.network.msg.api.DescribeVpcsMsg;
import com.gcloud.header.network.msg.api.ModifyVpcAttributeMsg;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
@Transactional(propagation = Propagation.REQUIRED)
public class VpcServiceImpl implements IVpcService{
	
	@Autowired
    private RouterDao routerDao;

	@Autowired
	private IRouterService routerService;

	@Override
	public PageResult<VpcsItemType> describeVpcs(DescribeVpcsMsg msg) {
		// TODO Auto-generated method stub
		DescribeVRoutersParams param = BeanUtil.copyProperties(msg, DescribeVRoutersParams.class);
		PageResult<VRouterSetType> pageVRouter = routerDao.describeVRouters(param, VRouterSetType.class, msg.getCurrentUser());
		PageResult<VpcsItemType> result = new PageResult<VpcsItemType>();
		List<VpcsItemType> list = new ArrayList<VpcsItemType>();
		for(VRouterSetType router: pageVRouter.getList()) {
			VpcsItemType vpc = new VpcsItemType();
			vpc.setVpcId(router.getvRouterId());
			vpc.setVpcName(router.getvRouterName());
			vpc.setStatus(router.getStatus());
			vpc.setCnStatus(router.getStatus());
			vpc.setRegionId(router.getRegionId());		
			Map<String, List> vSwitchIds = new HashMap<String, List>();
			List<String> subnetIds = null;
			if(router.getSubnets() != null) {
				subnetIds = Arrays.asList(router.getSubnets().split(",")); 
			}else {
				subnetIds = new ArrayList<String>();
			}
			vSwitchIds.put("VSwitchId", subnetIds);
			vpc.setvSwitchIds(vSwitchIds);
			list.add(vpc);
		}
		
		result.setPageNo(pageVRouter.getPageNo());
		result.setPageSize(pageVRouter.getPageSize());
		result.setTotalCount(pageVRouter.getTotalCount());
		result.setList(list);

		return result;
	}

	@Override
	public String createVpc(CreateVpcMsg msg) {
		// TODO Auto-generated method stub
		CreateVRouterMsg routerMsg = BeanUtil.copyProperties(msg, CreateVRouterMsg.class);
		routerMsg.setvRouterName(msg.getVpcName());
		return this.getProviderOrDefault().createVRouter(routerMsg);
	}

	@Override
	public void removeVpc(String vpcId) {
		// TODO Auto-generated method stub
		routerService.deleteVRouter(vpcId);

	}

	@Override
	public void updateVpc(ModifyVpcAttributeMsg msg) {
		// TODO Auto-generated method stub
		Router router = routerDao.getById(msg.getVpcId());
		if (router == null) {
			log.debug("专有网络不存在");
            throw new GCloudException("0100403::专有网络不存在");
		}
		List<String> updatedField = new ArrayList<String>();
		updatedField.add(router.updateName(msg.getVpcName()));
		routerDao.update(router, updatedField);
		CacheContainer.getInstance().put(CacheType.ROUTER_NAME, msg.getVpcId(), msg.getVpcName());
		this.getProviderOrDefault().modifyVRouterAttribute(msg.getVpcId(), msg.getVpcName());
	}
	
	private IRouterProvider getProviderOrDefault() {
        IRouterProvider provider = ResourceProviders.getDefault(ResourceType.ROUTER);
        return provider;
    }

    private IRouterProvider checkAndGetProvider(Integer providerType) {
        IRouterProvider provider = ResourceProviders.checkAndGet(ResourceType.ROUTER, providerType);
        return provider;
    }

}
