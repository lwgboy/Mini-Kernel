
package com.gcloud.header.compute.msg.api.vm.zone;

import java.io.Serializable;
import java.util.List;

import com.gcloud.header.compute.msg.api.model.AvailableZone;

public class ApiDescribeZonesReplyWrapper implements Serializable {

    private static final long serialVersionUID = 1L;
    private List<AvailableZone> zone;

    public List<AvailableZone> getZone() {
        return zone;
    }

    public void setZone(List<AvailableZone> zone) {
        this.zone = zone;
    }

}
