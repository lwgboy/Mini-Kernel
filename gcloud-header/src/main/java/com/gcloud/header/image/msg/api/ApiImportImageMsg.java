package com.gcloud.header.image.msg.api;

import com.gcloud.header.ApiMessage;
import com.gcloud.header.api.ApiModel;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotBlank;

/**
 * Created by yaowj on 2018/9/21.
 */
public class ApiImportImageMsg extends ApiMessage {

    private static final long serialVersionUID = 1L;

    @Override
    public Class replyClazz() {
        return ApiImportImageReplyMsg.class;
    }

    @Length(max = 255, message = "0090107::名称长度不能大于255")
    @ApiModel(description = "镜像名称")
    private String imageName;
    @NotBlank(message = "0090101::镜像路径不能为空")
    @ApiModel(description = "镜像文件路径")
    private String filePath;
    @NotBlank(message = "0090102::系统类型不能为空")
    @ApiModel(description = "系统类型")
    private String osType;
    @NotBlank(message = "0090103::架构不能为空")
    @ApiModel(description = "架构")
    private String architecture;
    private String description;

    public String getImageName() {
        return imageName;
    }

    public void setImageName(String imageName) {
        this.imageName = imageName;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public String getOsType() {
        return osType;
    }

    public void setOsType(String osType) {
        this.osType = osType;
    }

    public String getArchitecture() {
        return architecture;
    }

    public void setArchitecture(String architecture) {
        this.architecture = architecture;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
