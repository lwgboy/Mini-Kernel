package com.gcloud.header.compute.msg.api.model;

import com.gcloud.header.api.ApiModel;
import com.gcloud.header.storage.enums.DiskType;
import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.Range;

import java.io.Serializable;

public class DiskInfo implements Serializable {
	
	@ApiModel(description = "数据盘大小")
	@Range(min=1, message="0010108::系统盘大小必须大于0")
	private Integer size;
	@ApiModel(description = "磁盘种类")
	@NotBlank
	private String category;

	@ApiModel(description = "快照ID")
	private String snapshotId;
	@ApiModel(description = "磁盘名称")
	private String diskName;
	@ApiModel(description = "磁盘描述")
	private String description;
	@ApiModel(description = "磁盘类型")
	private DiskType diskType;
	@ApiModel(description = "镜像")
	private String imageRef;
	@ApiModel(description = "可用区")
	private String zoneId;

	public String getImageRef() {
		return imageRef;
	}

	public void setImageRef(String imageRef) {
		this.imageRef = imageRef;
	}

	public DiskType getDiskType() {
		return diskType;
	}

	public void setDiskType(DiskType diskType) {
		this.diskType = diskType;
	}

	public String getSnapshotId() {
		return snapshotId;
	}

	public void setSnapshotId(String snapshotId) {
		this.snapshotId = snapshotId;
	}

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

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public String getZoneId() {
		return zoneId;
	}

	public void setZoneId(String zoneId) {
		this.zoneId = zoneId;
	}
}
