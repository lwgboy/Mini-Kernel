package com.gcloud.header;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.gcloud.header.api.ApiModel;

public class ApiReplyMessage extends ReplyMessage {
	@ApiModel(description="请求ID")
    @JsonProperty("RequestId")
    private String requestId;

    public String getRequestId() {
        return requestId;
    }

    public void setRequestId(String requestId) {
        this.requestId = requestId;
    }

}
