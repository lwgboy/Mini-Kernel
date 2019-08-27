package com.gcloud.header.storage.msg.api.volume;

import com.gcloud.header.ApiReplyMessage;
import com.gcloud.header.storage.model.VolumeTypeListResponse;

import java.util.List;

/**
 * Created by yaowj on 2018/11/8.
 */
public class ApiVolumeTypeListReplyMsg extends ApiReplyMessage {

    private static final long serialVersionUID = 1L;

    private List<VolumeTypeListResponse> volumeTypes;

    public List<VolumeTypeListResponse> getVolumeTypes() {
        return volumeTypes;
    }

    public void setVolumeTypes(List<VolumeTypeListResponse> volumeTypes) {
        this.volumeTypes = volumeTypes;
    }
}
