
package com.gcloud.header.compute.msg.api.vm.zone;

import com.gcloud.header.ApiPageMessage;

public class ApiDescribeZonesMsg extends ApiPageMessage {

    private static final long serialVersionUID = 1L;

    @Override
    public Class replyClazz() {
        return ApiDescribeZonesReplyMsg.class;
    }

}
