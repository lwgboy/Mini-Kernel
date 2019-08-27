
package com.gcloud.controller.compute.handler.api.vm.zone;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;

import com.gcloud.controller.compute.handler.api.model.DescribeInstanceTypesParams;
import com.gcloud.controller.compute.handler.api.model.DescribeZonesParams;
import com.gcloud.controller.compute.service.vm.base.IVmBaseService;
import com.gcloud.controller.compute.service.vm.zone.IVmZoneService;
import com.gcloud.controller.storage.service.IStoragePoolService;
import com.gcloud.core.exception.GCloudException;
import com.gcloud.core.handle.ApiHandler;
import com.gcloud.core.handle.MessageHandler;
import com.gcloud.core.util.BeanUtil;
import com.gcloud.framework.db.PageResult;
import com.gcloud.header.Module;
import com.gcloud.header.SubModule;
import com.gcloud.header.compute.msg.api.model.AvailableZone;
import com.gcloud.header.compute.msg.api.model.InstanceTypeItemType;
import com.gcloud.header.compute.msg.api.vm.zone.ApiDescribeZonesMsg;
import com.gcloud.header.compute.msg.api.vm.zone.ApiDescribeZonesReplyMsg;
import com.gcloud.header.compute.msg.api.vm.zone.AvailableResource;
import com.gcloud.header.compute.msg.api.vm.zone.AvailableResources;
import com.gcloud.header.compute.msg.api.vm.zone.DataDiskCategories;
import com.gcloud.header.compute.msg.api.vm.zone.DiskCategory;
import com.gcloud.header.compute.msg.api.vm.zone.InstanceType;
import com.gcloud.header.compute.msg.api.vm.zone.InstanceTypes;
import com.gcloud.header.compute.msg.api.vm.zone.SystemDiskCategories;
import com.gcloud.header.storage.model.DiskCategoryModel;

@ApiHandler(module = Module.ECS, subModule = SubModule.VM, action = "DescribeZones")
public class ApiDescribeZonesHandler extends MessageHandler<ApiDescribeZonesMsg, ApiDescribeZonesReplyMsg> {

    @Autowired
    private IVmZoneService zoneService;

    @Autowired
    private IVmBaseService vmBaseService;

    @Autowired
    private IStoragePoolService poolService;

    @Override
    public ApiDescribeZonesReplyMsg handle(ApiDescribeZonesMsg msg) throws GCloudException {
        DescribeZonesParams params = BeanUtil.copyProperties(msg, DescribeZonesParams.class);
        PageResult<AvailableZone> page = this.zoneService.describeZones(params.getPageNumber(), params.getPageSize());
        for (AvailableZone zone : page.getList()) {
            zone.setAvailableResources(new AvailableResources());
            zone.getAvailableResources().setResourcesInfo(new ArrayList<>());
            AvailableResource resource = new AvailableResource();
            {
                resource.setInstanceTypes(new InstanceTypes());
                resource.getInstanceTypes().setSupportedInstanceType(new ArrayList<>());
                DescribeInstanceTypesParams tmp = new DescribeInstanceTypesParams();
                tmp.setZoneId(zone.getZoneId());
                for (InstanceTypeItemType type : this.vmBaseService.describeInstanceTypes(tmp)) {
                    InstanceType resType = new InstanceType();
                    resType.setInstanceTypeId(type.getInstanceTypeId());
                    resType.setInstanceTypeName(type.getInstanceTypeName());
                    resource.getInstanceTypes().getSupportedInstanceType().add(resType);
                }
            }
            {
                resource.setSystemDiskCategories(new SystemDiskCategories());
                resource.getSystemDiskCategories().setSupportedSystemDiskCategory(new ArrayList<>());
                resource.setDataDiskCategories(new DataDiskCategories());
                resource.getDataDiskCategories().setSupportedDataDiskCategory(new ArrayList<>());
                for (DiskCategoryModel tmp : this.poolService.describeDiskCategories(zone.getZoneId())) {
                    DiskCategory diskCategory = new DiskCategory();
                    diskCategory.setDiskTypeId(tmp.getId());
                    diskCategory.setDiskTypeName(tmp.getName());
                    diskCategory.setDiskTypeCnName(tmp.getName());
                    diskCategory.setMin(tmp.getMinSize());
                    diskCategory.setMax(tmp.getMaxSize());
                    resource.getSystemDiskCategories().getSupportedSystemDiskCategory().add(diskCategory);
                    resource.getDataDiskCategories().getSupportedDataDiskCategory().add(diskCategory);
                }
            }
            zone.getAvailableResources().getResourcesInfo().add(resource);
        }
        ApiDescribeZonesReplyMsg replyMsg = new ApiDescribeZonesReplyMsg();
        replyMsg.init(page);
        return replyMsg;
    }

}
