
package com.gcloud.header.compute.msg.api.model;

import java.io.Serializable;

import com.gcloud.header.compute.msg.api.vm.zone.AvailableResources;

public class AvailableZone implements Serializable {

    private String zoneId;
    private String localName;
    private Boolean status;
    private AvailableResources availableResources;

    public String getZoneId() {
        return zoneId;
    }

    public void setZoneId(String zoneId) {
        this.zoneId = zoneId;
    }

    public String getLocalName() {
        return localName;
    }

    public void setLocalName(String localName) {
        this.localName = localName;
    }

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }

    public AvailableResources getAvailableResources() {
        return availableResources;
    }

    public void setAvailableResources(AvailableResources availableResources) {
        this.availableResources = availableResources;
    }

}
