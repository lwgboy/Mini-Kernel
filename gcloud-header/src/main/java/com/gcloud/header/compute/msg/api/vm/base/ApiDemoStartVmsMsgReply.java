package com.gcloud.header.compute.msg.api.vm.base;

import com.gcloud.header.ApiReplyMessage;
import com.gcloud.header.compute.model.VmResponse;

import java.util.List;

public class ApiDemoStartVmsMsgReply extends ApiReplyMessage{

    private List<VmResponse> responses;

    public List<VmResponse> getResponses() {
        return responses;
    }

    public void setResponses(List<VmResponse> responses) {
        this.responses = responses;
    }
}
