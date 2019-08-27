package com.gcloud.header.compute.msg.api.model;

import java.io.Serializable;

public class RegionType implements Serializable {

    private static final long serialVersionUID = 1L;

    private String regionId;

    public String getRegionId() {
        return regionId;
    }

    public void setRegionId(String regionId) {
        this.regionId = regionId;
    }
}
