package com.gcloud.controller.compute.service.vm.statistics;

import java.util.List;

import com.gcloud.header.compute.msg.api.model.InstanceStatisticsItemType;
import com.gcloud.header.compute.msg.api.model.InstanceStatisticsZoneItemType;
import com.gcloud.header.compute.msg.api.model.RedundantResourceAllocateItem;

public interface IVmStatisticsService {
	List<InstanceStatisticsItemType>  instanceStatistics();
	
	List<InstanceStatisticsZoneItemType> instanceStatisticsByZone();
	
	List<RedundantResourceAllocateItem> redundantResourceAllocate();
}
