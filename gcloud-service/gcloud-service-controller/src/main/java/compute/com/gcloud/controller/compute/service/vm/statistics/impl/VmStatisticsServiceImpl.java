package com.gcloud.controller.compute.service.vm.statistics.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gcloud.controller.compute.dao.ComputeNodeDao;
import com.gcloud.controller.compute.dao.InstanceDao;
import com.gcloud.controller.compute.dao.InstanceTypeDao;
import com.gcloud.controller.compute.dao.ZoneDao;
import com.gcloud.controller.compute.entity.AvailableZoneEntity;
import com.gcloud.controller.compute.entity.InstanceType;
import com.gcloud.controller.compute.handler.api.model.ComputeNodeTotalResource;
import com.gcloud.controller.compute.handler.api.model.ComputeNodeUsedResource;
import com.gcloud.controller.compute.service.vm.statistics.IVmStatisticsService;
import com.gcloud.header.compute.msg.api.model.InstanceStatisticsByZoneStateItem;
import com.gcloud.header.compute.msg.api.model.InstanceStatisticsItemType;
import com.gcloud.header.compute.msg.api.model.InstanceStatisticsZoneItemType;
import com.gcloud.header.compute.msg.api.model.RedundantResourceAllocateItem;
@Service
public class VmStatisticsServiceImpl implements IVmStatisticsService {
	@Autowired
    private InstanceDao instanceDao;
	
	@Autowired
    private ZoneDao zoneDao;
	
	@Autowired
    private ComputeNodeDao computeNodeDao;
	
	@Autowired
    private InstanceTypeDao instanceTypeDao;

	@Override
	public List<InstanceStatisticsItemType> instanceStatistics() {
		return instanceDao.instanceStatistics(InstanceStatisticsItemType.class);
	}

	@Override
	public List<InstanceStatisticsZoneItemType> instanceStatisticsByZone() {
		List<InstanceStatisticsZoneItemType> results = new ArrayList<InstanceStatisticsZoneItemType>();
		List<InstanceStatisticsByZoneStateItem> items = instanceDao.instanceStatisticsByZoneState(InstanceStatisticsByZoneStateItem.class);
		List<AvailableZoneEntity> zones = zoneDao.findAll();
		for(AvailableZoneEntity zone:zones) {
			InstanceStatisticsZoneItemType item = new InstanceStatisticsZoneItemType();
			item.setZoneId(zone.getId());
			item.setZoneName(zone.getName());
			
			List<InstanceStatisticsByZoneStateItem> filters = items.stream().filter(a -> a.getZoneId().equals(zone.getId())).collect(Collectors.toList());
			List<InstanceStatisticsItemType> statisticsItems = filters.stream().map(filterItem -> {
				InstanceStatisticsItemType bean = new InstanceStatisticsItemType();
			    bean.setStatus(filterItem.getState());
			    bean.setCountNum(filterItem.getCountNum());
			    return bean;
			}).collect(Collectors.toList());
			
			item.setStatisticsItems(statisticsItems);
			results.add(item);
		}
		return results;
	}

	@Override
	public List<RedundantResourceAllocateItem> redundantResourceAllocate() {
		List<InstanceType> instanceTypes = instanceTypeDao.findByProperty("deleted", 0);
		List<ComputeNodeUsedResource> nodeUsedResources = instanceDao.computeNodeUsedResource(ComputeNodeUsedResource.class);
		List<ComputeNodeTotalResource> nodeTotalResources = computeNodeDao.computeNodeTotalResource(ComputeNodeTotalResource.class);
		
		int allNodeUsedCore = nodeUsedResources.stream().map(ComputeNodeUsedResource::getUsedCore).reduce(Integer::sum).get();
		int allNodeUsedMemory = nodeUsedResources.stream().map(ComputeNodeUsedResource::getUsedMemory).reduce(Integer::sum).get();
		int allNodeTotalCore = nodeTotalResources.stream().map(ComputeNodeTotalResource::getTotalCore).reduce(Integer::sum).get();
		int allNodeTotalMemory = nodeTotalResources.stream().map(ComputeNodeTotalResource::getTotalMemory).reduce(Integer::sum).get();
		int allNodeAvailCore = allNodeTotalCore - allNodeUsedCore;
		int allNodeAvailMemory = allNodeTotalMemory - allNodeUsedMemory;
		
		 Map<String, ComputeNodeUsedResource> nodeUsedResourceMap = nodeUsedResources.stream().collect(Collectors.toMap(h -> h.getHostname(), h -> h));
		List<RedundantResourceAllocateItem> items = new ArrayList<RedundantResourceAllocateItem>();
		for(InstanceType type:instanceTypes) {
			RedundantResourceAllocateItem item = new RedundantResourceAllocateItem();
			item.setInstanceType(type.getName());
			item.setConfig("CPU:" + type.getVcpus() + "核、内存：" + type.getMemoryMb()/1024 + "G");
			item.setMaxAllocate(getInstanceNum(allNodeAvailCore, allNodeAvailMemory, type));
			
			int actualAllocate = 0;
			for(ComputeNodeTotalResource nodeTotal:nodeTotalResources){
				ComputeNodeUsedResource used = nodeUsedResourceMap.get(nodeTotal.getHostname());
				if(used != null) {
					actualAllocate += getInstanceNum(nodeTotal.getTotalCore() - used.getUsedCore(), nodeTotal.getTotalMemory() - used.getUsedMemory(), type);
				} else {
					actualAllocate += getInstanceNum(nodeTotal.getTotalCore(), nodeTotal.getTotalMemory(), type);
				}
			}
			item.setActualAllocate(actualAllocate);
			items.add(item);
		}
				
		return items;
	}
	
	private int getInstanceNum(int availCore, int availMemory, InstanceType type) {
		int num1 = availCore/type.getVcpus();
		int num2 = availMemory/type.getMemoryMb();
		return num1 > num2?num2:num1;
	}
}
