package com.gcloud.controller.storage.model;

import org.openstack4j.model.storage.block.Volume;

/**
 * Created by yaowj on 2018/9/28.
 */
public class CinderVolumeCheckResult {

    private Boolean success;
    private Volume volume;

    public Boolean getSuccess() {
        return success;
    }

    public void setSuccess(Boolean success) {
        this.success = success;
    }

    public Volume getVolume() {
        return volume;
    }

    public void setVolume(Volume volume) {
        this.volume = volume;
    }
}
