package com.gcloud.header.compute.msg.api.region;

import com.gcloud.header.ApiMessage;

public class ApiDescribeRegionMsg extends ApiMessage {

    @Override
    public Class replyClazz() {
        return ApiDescribeRegionReplyMsg.class;
    }


}
