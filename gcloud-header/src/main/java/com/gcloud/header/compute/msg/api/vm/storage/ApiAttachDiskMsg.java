package com.gcloud.header.compute.msg.api.vm.storage;

import com.gcloud.header.ApiMessage;
import com.gcloud.header.api.ApiModel;
import org.hibernate.validator.constraints.NotBlank;

public class ApiAttachDiskMsg extends ApiMessage {
    @ApiModel(description = "虚拟机ID", require = true)
    @NotBlank(message = "0010901::云服务器ID不能为空")
    private String instanceId;
    @ApiModel(description = "磁盘ID", require = true)
    @NotBlank(message = "0010902::磁盘ID不能为空")
    private String diskId;

    @Override
    public Class replyClazz() {
        return ApiAttachDiskReplyMsg.class;
    }

    public String getInstanceId() {
        return instanceId;
    }

    public void setInstanceId(String instanceId) {
        this.instanceId = instanceId;
    }

    public String getDiskId() {
        return diskId;
    }

    public void setDiskId(String diskId) {
        this.diskId = diskId;
    }
}
