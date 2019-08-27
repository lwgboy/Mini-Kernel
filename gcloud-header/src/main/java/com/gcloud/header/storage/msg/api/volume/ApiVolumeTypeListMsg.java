package com.gcloud.header.storage.msg.api.volume;

import com.gcloud.header.ApiMessage;

/**
 * Created by yaowj on 2018/11/8.
 */
public class ApiVolumeTypeListMsg extends ApiMessage {

    private static final long serialVersionUID = 1L;

    @Override
    public Class replyClazz() {
        return ApiVolumeTypeListReplyMsg.class;
    }
}
