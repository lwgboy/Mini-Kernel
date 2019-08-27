package com.gcloud.controller.image.entity;

import com.gcloud.framework.db.jdbc.annotation.ID;
import com.gcloud.framework.db.jdbc.annotation.Table;

/**
 * Created by yaowj on 2018/11/21.
 */
@Table(name = "gc_image_properties", jdbc = "controllerJdbcTemplate")
public class ImageProperty {

    @ID
    private Long id;
    private String imageId;
    private String name;
    private String value;

    public static final String ID = "id";
    public static final String IMAGE_ID = "imageId";
    public static final String NAME = "name";
    public static final String VALUE = "value";

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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String updateId(Long id) {
        this.setId(id);
        return ID;
    }

    public String updateImageId(String imageId) {
        this.setImageId(imageId);
        return IMAGE_ID;
    }

    public String updateName(String name) {
        this.setName(name);
        return NAME;
    }

    public String updateValue(String value) {
        this.setValue(value);
        return VALUE;
    }
}
