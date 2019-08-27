package com.gcloud.controller.network.entity;

import com.gcloud.framework.db.jdbc.annotation.ID;
import com.gcloud.framework.db.jdbc.annotation.Table;

/**
 * Created by yaowj on 2018/11/6.
 */
@Table(name="gc_external_networks",jdbc="controllerJdbcTemplate")
public class ExternalNetwork {

    @ID
    private String networkId;
    private Boolean isDefault;

    public String getNetworkId() {
        return networkId;
    }

    public void setNetworkId(String networkId) {
        this.networkId = networkId;
    }

    public Boolean getIsDefault() {
        return isDefault;
    }

    public void setIsDefault(Boolean isDefault) {
        isDefault = isDefault;
    }
}
