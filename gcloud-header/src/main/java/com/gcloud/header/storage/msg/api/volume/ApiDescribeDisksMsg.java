package com.gcloud.header.storage.msg.api.volume;

import java.util.List;

import com.gcloud.header.ApiPageMessage;
import com.gcloud.header.api.ApiModel;

/**
 * Created by yaowj on 2018/9/29.
 */
public class ApiDescribeDisksMsg extends ApiPageMessage {

    private static final long serialVersionUID = 1L;

    @ApiModel(description = "all , system , data，默认值为 all")
    private String diskType;
    @ApiModel(description = "磁盘状态")
    private String status;
    @ApiModel(description = "磁盘名称")
    private String diskName;
    @ApiModel(description = "实例ID")
    private String instanceId;
    @ApiModel(description = "磁盘ID列表")
    private List<String> diskIds;

    @Override
    public Class replyClazz() {
        return ApiDescribeDisksReplyMsg.class;
    }

    public String getDiskType() {
        return diskType;
    }

    public void setDiskType(String diskType) {
        this.diskType = diskType;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getDiskName() {
        return diskName;
    }

    public void setDiskName(String diskName) {
        this.diskName = diskName;
    }

    public String getInstanceId() {
        return instanceId;
    }

    public void setInstanceId(String instanceId) {
        this.instanceId = instanceId;
    }

	public List<String> getDiskIds() {
		return diskIds;
	}

	public void setDiskIds(List<String> diskIds) {
		this.diskIds = diskIds;
	}
}
