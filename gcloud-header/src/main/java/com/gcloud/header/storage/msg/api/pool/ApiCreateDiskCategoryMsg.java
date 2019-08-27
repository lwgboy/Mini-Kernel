
package com.gcloud.header.storage.msg.api.pool;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;

import org.hibernate.validator.constraints.Length;

import com.gcloud.header.ApiMessage;
import com.gcloud.header.api.ApiModel;
import com.gcloud.header.storage.StorageErrorCodes;

public class ApiCreateDiskCategoryMsg extends ApiMessage {

    private static final long serialVersionUID = 1L;

    @Override
    public Class replyClazz() {
        return ApiCreateDiskCategoryReplyMsg.class;
    }
    
    @ApiModel(description = "磁盘类型名称", require = false)
    @Length(min = 1, max = 255, message = StorageErrorCodes.INPUT_CATEGORY_NAME_ERROR)
    @NotBlank(message = "::磁盘类型名称不能为空")
    private String name;
    
    @ApiModel(description = "磁盘最小大小", require = false)
    @Min(value = 1, message = StorageErrorCodes.INPUT_DISK_SIZE_ERROR)
    private Integer minSize;
    
    @ApiModel(description = "磁盘最大大小", require = false)
    @Min(value = 1, message = StorageErrorCodes.INPUT_DISK_SIZE_ERROR)
    private Integer maxSize;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getMinSize() {
        return minSize;
    }

    public void setMinSize(Integer minSize) {
        this.minSize = minSize;
    }

    public Integer getMaxSize() {
        return maxSize;
    }

    public void setMaxSize(Integer maxSize) {
        this.maxSize = maxSize;
    }
}
