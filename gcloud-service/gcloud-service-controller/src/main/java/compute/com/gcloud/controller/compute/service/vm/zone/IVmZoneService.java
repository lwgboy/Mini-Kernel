
package com.gcloud.controller.compute.service.vm.zone;

import java.util.List;

import com.gcloud.controller.compute.model.vm.DeleteZoneParams;
import com.gcloud.controller.compute.model.vm.DetailZoneParams;
import com.gcloud.framework.db.PageResult;
import com.gcloud.header.api.model.CurrentUser;
import com.gcloud.header.compute.msg.api.model.AvailableZone;
import com.gcloud.header.compute.msg.api.model.DetailZone;

public interface IVmZoneService {

    PageResult<AvailableZone> describeZones(Integer pageNumber, Integer pageSize);

    void createZone(String zoneName);

    void updateComputeNodeZone(String zoneId, List<Integer> nodeIds);

    void deleteZone(DeleteZoneParams params, CurrentUser currentUser);
    
    DetailZone detailZone(DetailZoneParams params, CurrentUser currentUser);
}
