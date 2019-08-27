
package com.gcloud.header.compute.msg.api.vm.zone;

import javax.validation.constraints.NotBlank;

import com.gcloud.header.ApiMessage;
import com.gcloud.header.api.ApiModel;

public class ApiCreateZoneMsg extends ApiMessage {

    private static final long serialVersionUID = 1L;

    @Override
    public Class replyClazz() {
        return ApiCreateZoneReplyMsg.class;
    }
    
    @ApiModel(description = "可用区名称", require = true)
    @NotBlank(message = "0180101::可用区名称不能为空")
    private String zoneName;

    public String getZoneName() {
        return zoneName;
    }

    public void setZoneName(String zoneName) {
        this.zoneName = zoneName;
    }

}
