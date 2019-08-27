
package com.gcloud.header.compute.msg.api.vm.zone;

import java.io.Serializable;
import java.util.List;

public class AvailableResources implements Serializable {

    private static final long serialVersionUID = 1L;
    private List<AvailableResource> resourcesInfo;

    public List<AvailableResource> getResourcesInfo() {
        return resourcesInfo;
    }

    public void setResourcesInfo(List<AvailableResource> resourcesInfo) {
        this.resourcesInfo = resourcesInfo;
    }

}
