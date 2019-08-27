package com.gcloud.header.storage.msg.api.volume;

import com.gcloud.header.ApiMessage;
import com.gcloud.header.api.ApiModel;
import com.gcloud.header.storage.StorageErrorCodes;

import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.Min;

/**
 * Created by yaowj on 2018/9/21.
 */
public class ApiCreateDiskMsg extends ApiMessage {

    private static final long serialVersionUID = 1L;

    @Override
    public Class replyClazz() {
        return ApiCreateDiskReplyMsg.class;
    }

    @ApiModel(description = "磁盘名称", require = true)
    @Length(max = 255, message = StorageErrorCodes.INPUT_DISK_NAME_ERROR)
    private String diskName;
    @ApiModel(description = "磁盘描述")
    @Length(max = 255, message = StorageErrorCodes.INPUT_DISK_DESCRIPTION_ERROR)
    private String description;
    @ApiModel(description = "容量大小，以GB为单位")
    @Min(value = 1, message = StorageErrorCodes.INPUT_DISK_SIZE_ERROR)
    private Integer size;
    @ApiModel(description = "快照ID")
    private String snapshotId;
    @ApiModel(description = "磁盘类型")
    @NotBlank(message = StorageErrorCodes.INPUT_DISK_CATEGORY_ERROR)
    private String diskCategory;
    @ApiModel(description = "可用区ID")
    @NotBlank(message = StorageErrorCodes.INPUT_ZONE_ID_ERROR)
    private String zoneId;

    public String getDiskName() {
        return diskName;
    }

    public void setDiskName(String diskName) {
        this.diskName = diskName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getSize() {
        return size;
    }

    public void setSize(Integer size) {
        this.size = size;
    }

    public String getSnapshotId() {
        return snapshotId;
    }

    public void setSnapshotId(String snapshotId) {
        this.snapshotId = snapshotId;
    }

    public String getDiskCategory() {
        return diskCategory;
    }

    public void setDiskCategory(String diskCategory) {
        this.diskCategory = diskCategory;
    }

    public String getZoneId() {
        return zoneId;
    }

    public void setZoneId(String zoneId) {
        this.zoneId = zoneId;
    }
}
