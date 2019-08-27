package com.gcloud.controller.image.entity;

import com.gcloud.framework.db.jdbc.annotation.ID;
import com.gcloud.framework.db.jdbc.annotation.Table;

@Table(name = "gc_image_stores", jdbc = "controllerJdbcTemplate")
public class ImageStore {
	@ID
    private Long id;
    private String imageId;
    private String storeTarget;
    private String storeType;
    private String status;//downloading\deleting\active
    
    public static final String ID = "id";
    public static final String IMAGE_ID = "imageId";
    public static final String STORE_TARGET = "storeTarget";
    public static final String STORE_TYPE = "storeType";
    public static final String STATUS = "status";
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getImageId() {
		return imageId;
	}
	public void setImageId(String imageId) {
		this.imageId = imageId;
	}
	public String getStoreTarget() {
		return storeTarget;
	}
	public void setStoreTarget(String storeTarget) {
		this.storeTarget = storeTarget;
	}
	public String getStoreType() {
		return storeType;
	}
	public void setStoreType(String storeType) {
		this.storeType = storeType;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
    
	public String updateId(Long id) {
        this.setId(id);
        return ID;
    }

    public String updateImageId(String imageId) {
        this.setImageId(imageId);
        return IMAGE_ID;
    }

    public String updateStoreTarget(String storeTarget) {
        this.setStoreTarget(storeTarget);
        return STORE_TARGET;
    }
    
    public String updateStoreType(String storeType) {
        this.setStoreType(storeType);
        return STORE_TYPE;
    }

    public String updateStatus(String status) {
        this.setStatus(status);
        return STATUS;
    }
}
