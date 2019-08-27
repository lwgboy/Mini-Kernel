
package com.gcloud.controller.compute.handler.api.model;

import com.gcloud.common.model.PageParams;

public class DescribeZonesParams extends PageParams {

    private String regionId;

    public String getRegionId() {
        return regionId;
    }

    public void setRegionId(String regionId) {
        this.regionId = regionId;
    }

}
