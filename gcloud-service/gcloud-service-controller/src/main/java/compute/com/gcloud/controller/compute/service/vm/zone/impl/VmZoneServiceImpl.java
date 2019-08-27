
package com.gcloud.controller.compute.service.vm.zone.impl;

import com.gcloud.common.util.StringUtils;
import com.gcloud.controller.compute.dao.ComputeNodeDao;
import com.gcloud.controller.compute.dao.ZoneDao;
import com.gcloud.controller.compute.entity.AvailableZoneEntity;
import com.gcloud.controller.compute.entity.ComputeNode;
import com.gcloud.controller.compute.handler.api.model.DescribeInstanceTypesParams;
import com.gcloud.controller.compute.model.vm.DeleteZoneParams;
import com.gcloud.controller.compute.model.vm.DetailZoneParams;
import com.gcloud.controller.compute.service.vm.base.IVmBaseService;
import com.gcloud.controller.compute.service.vm.zone.IVmZoneService;
import com.gcloud.controller.compute.utils.RedisNodesUtil;
import com.gcloud.controller.storage.service.IStoragePoolService;
import com.gcloud.core.exception.GCloudException;
import com.gcloud.framework.db.PageResult;
import com.gcloud.header.api.model.CurrentUser;
import com.gcloud.header.compute.msg.api.model.AvailableZone;
import com.gcloud.header.compute.msg.api.model.DetailZone;
import com.gcloud.header.compute.msg.api.model.InstanceTypeItemType;
import com.gcloud.header.compute.msg.api.vm.zone.AvailableResource;
import com.gcloud.header.compute.msg.api.vm.zone.DataDiskCategories;
import com.gcloud.header.compute.msg.api.vm.zone.DiskCategory;
import com.gcloud.header.compute.msg.api.vm.zone.InstanceType;
import com.gcloud.header.compute.msg.api.vm.zone.InstanceTypes;
import com.gcloud.header.compute.msg.api.vm.zone.SystemDiskCategories;
import com.gcloud.header.storage.model.DiskCategoryModel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class VmZoneServiceImpl implements IVmZoneService {

    @Autowired
    private ZoneDao zoneDao;

    @Autowired
    private ComputeNodeDao nodeDao;
    
    @Autowired
    private IVmBaseService vmBaseService;
    
    @Autowired
    private IStoragePoolService poolService;
    
    @Override
    public PageResult<AvailableZone> describeZones(Integer pageNumber, Integer pageSize) {
        PageResult page = this.zoneDao.page(pageNumber, pageSize);
        List<AvailableZone> vos = new ArrayList<>();
        for (Object obj : page.getList()) {
            AvailableZoneEntity entity = (AvailableZoneEntity)obj;
            AvailableZone vo = new AvailableZone();
            vo.setZoneId(entity.getId());
            vo.setLocalName(entity.getName());
            vos.add(vo);
        }
        page.setList(vos);
        return page;
    }

    @Override
    public void createZone(String zoneName) {
        synchronized (VmZoneServiceImpl.class) {
            if (this.zoneDao.findUniqueByProperty("name", zoneName) != null) {
            	log.error("0180102::可用区名称已存在");
                throw new GCloudException("0180102::可用区名称已存在");
            }
            AvailableZoneEntity zone = new AvailableZoneEntity();
            zone.setId(StringUtils.genUuid());
            zone.setName(zoneName);
            try {
            	this.zoneDao.save(zone);
            } catch(Exception e) {
            	log.error("创建可用区失败，原因：【"+ e.getCause() + "::" + e.getMessage() +"】");
            	throw new GCloudException("0180103::创建可用区失败");
            }
            
        }
    }

    @Override
    public void updateComputeNodeZone(String zoneId, List<Integer> nodeIds) {
        AvailableZoneEntity zone = this.zoneDao.getById(zoneId);
        if (zone == null) {
        	log.error("0180502::不存在该可用区");
            throw new GCloudException("0180502::不存在该可用区");
        }
        if (nodeIds != null) {
            for (Integer nodeId : nodeIds) {
                ComputeNode node = this.nodeDao.getById(nodeId);
                if (node != null) {
                    node.setZoneId(zoneId);
                    List<String> updateField = new ArrayList<>();
                    updateField.add("zoneId");
                    this.nodeDao.update(node, updateField);
                    RedisNodesUtil.updateComputeNodeZone(node.getHostname(), zoneId);
                }
            }
        }
    }

	@Override
	public void deleteZone(DeleteZoneParams params, CurrentUser currentUser) {
		AvailableZoneEntity zone = zoneDao.getById(params.getId());
		if(zone == null) {
			log.error("0180202::不存在该可用区");
			throw new GCloudException("0180202::不存在该可用区");
		}
		
		try{
			zoneDao.deleteById(params.getId());
		}catch(Exception e) {
			log.error("删除可用区失败，原因：【"+ e.getCause() + "::" + e.getMessage() +"】");
			throw new GCloudException("0180203::删除可用区失败");
		}
	}

	@Override
	public DetailZone detailZone(DetailZoneParams params, CurrentUser currentUser) {
		AvailableZoneEntity zone = zoneDao.getById(params.getId());
		
		DetailZone response = new DetailZone();
		if(zone == null) {
			return response;
		}
		
		AvailableResource resource = new AvailableResource();
		resource.setInstanceTypes(new InstanceTypes());
		resource.getInstanceTypes().setSupportedInstanceType(new ArrayList<>());
		
		DescribeInstanceTypesParams instanceTypeParams = new DescribeInstanceTypesParams();
		instanceTypeParams.setZoneId(zone.getId());
		for (InstanceTypeItemType type : this.vmBaseService.describeInstanceTypes(instanceTypeParams)) {
			InstanceType resType = new InstanceType();
			resType.setInstanceTypeId(type.getInstanceTypeId());
			resType.setInstanceTypeName(type.getInstanceTypeName());
			resource.getInstanceTypes().getSupportedInstanceType().add(resType);
		}
		
		resource.setSystemDiskCategories(new SystemDiskCategories());
        resource.getSystemDiskCategories().setSupportedSystemDiskCategory(new ArrayList<>());
        resource.setDataDiskCategories(new DataDiskCategories());
        resource.getDataDiskCategories().setSupportedDataDiskCategory(new ArrayList<>());
        for (DiskCategoryModel item : poolService.describeDiskCategories(zone.getId())) {
            DiskCategory diskCategory = new DiskCategory();
            diskCategory.setDiskTypeId(item.getId());
            diskCategory.setDiskTypeName(item.getName());
            diskCategory.setDiskTypeCnName(item.getName());
            diskCategory.setMin(item.getMinSize());
            diskCategory.setMax(item.getMaxSize());
            resource.getSystemDiskCategories().getSupportedSystemDiskCategory().add(diskCategory);
            resource.getDataDiskCategories().getSupportedDataDiskCategory().add(diskCategory);
        }
        
        response.setZoneName(zone.getName());
        response.setAvailableResource(resource);
		
		return response;
	}

}
