
package com.gcloud.header.compute.msg.api.vm.zone;

import java.util.List;

import com.gcloud.header.PageReplyMessage;
import com.gcloud.header.compute.msg.api.model.AvailableZone;

public class ApiDescribeZonesReplyMsg extends PageReplyMessage<AvailableZone> {

    private static final long serialVersionUID = 1L;

    private ApiDescribeZonesReplyWrapper zones;

    @Override
    public void setList(List<AvailableZone> list) {
        this.zones = new ApiDescribeZonesReplyWrapper();
        this.zones.setZone(list);
    }

    public ApiDescribeZonesReplyWrapper getZones() {
        return zones;
    }

    public void setZones(ApiDescribeZonesReplyWrapper zones) {
        this.zones = zones;
    }

}
