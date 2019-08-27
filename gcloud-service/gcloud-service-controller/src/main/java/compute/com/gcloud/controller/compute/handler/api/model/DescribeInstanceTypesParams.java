package com.gcloud.controller.compute.handler.api.model;

import com.gcloud.common.model.PageParams;

public class DescribeInstanceTypesParams extends PageParams{

    private String zoneId;

    public String getZoneId() {
        return zoneId;
    }

    public void setZoneId(String zoneId) {
        this.zoneId = zoneId;
    }
    
}
