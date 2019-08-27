package com.gcloud.header.image.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.gcloud.framework.db.jdbc.annotation.TableField;
import com.gcloud.header.GcloudConstants;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by yaowj on 2018/11/22.
 */
public class ImageType implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableField("id")
    private String imageId;
    @TableField("name")
    private String imageName;
    private String description;
    @TableField("min_disk")
    private Long size;
    private String status;
    @TableField("created_at")
    @JsonFormat(timezone = GcloudConstants.DEFAULT_TIMEZONE, pattern = GcloudConstants.DEFAULT_DATEFORMAT)
    private Date creationTime;
    private String architecture;
    private String imageOwnerAlias;
    private String osType;
    private Boolean disable;

    public String getImageId() {
        return imageId;
    }

    public void setImageId(String imageId) {
        this.imageId = imageId;
    }

    public String getImageName() {
        return imageName;
    }

    public void setImageName(String imageName) {
        this.imageName = imageName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Long getSize() {
        return size;
    }

    public void setSize(Long size) {
        this.size = size;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Date getCreationTime() {
        return creationTime;
    }

    public void setCreationTime(Date creationTime) {
        this.creationTime = creationTime;
    }

    public String getArchitecture() {
        return architecture;
    }

    public void setArchitecture(String architecture) {
        this.architecture = architecture;
    }

    public String getImageOwnerAlias() {
        return imageOwnerAlias;
    }

    public void setImageOwnerAlias(String imageOwnerAlias) {
        this.imageOwnerAlias = imageOwnerAlias;
    }

    public String getOsType() {
        return osType;
    }

    public void setOsType(String osType) {
        this.osType = osType;
    }

	public Boolean getDisable() {
		return disable;
	}

	public void setDisable(Boolean disable) {
		this.disable = disable;
	}
}
