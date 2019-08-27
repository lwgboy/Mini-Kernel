
package com.gcloud.header.compute.msg.api.vm.zone;

import java.util.List;

import javax.validation.constraints.NotBlank;

import com.gcloud.header.ApiMessage;
import com.gcloud.header.api.ApiModel;

public class ApiUpdateComputeNodeZoneMsg extends ApiMessage {

    private static final long serialVersionUID = 1L;


    @Override
    public Class replyClazz() {
        return ApiUpdateComputeNodeZoneReplyMsg.class;
    }

    @ApiModel(description = "可用区ID", require = true)
    @NotBlank(message = "0180501::可用区ID不能为空")
    private String zoneId;
    
    private List<Integer> nodeIds;
    
    public String getZoneId() {
        return zoneId;
    }

    public void setZoneId(String zoneId) {
        this.zoneId = zoneId;
    }

    public List<Integer> getNodeIds() {
        return nodeIds;
    }

    public void setNodeIds(List<Integer> nodeIds) {
        this.nodeIds = nodeIds;
    }

}
