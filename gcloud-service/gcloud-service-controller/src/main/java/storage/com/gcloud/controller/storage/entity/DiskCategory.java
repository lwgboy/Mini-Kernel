
package com.gcloud.controller.storage.entity;

import com.gcloud.framework.db.jdbc.annotation.ID;
import com.gcloud.framework.db.jdbc.annotation.Table;

@Table(name = "gc_disk_categories", jdbc = "controllerJdbcTemplate")
public class DiskCategory {

	@ID
    private String id;
    private String code;
    private String name;
    private Integer minSize;
    private Integer maxSize;

    public static final String ID = "id";
    public static final String NAME = "name";
    public static final String MIN_SIZE = "minSize";
    public static final String MAX_SIZE = "maxSize";

    public String updateId(String id) {
        this.setId(id);
        return ID;
    }

    public String updateName(String name) {
        this.setName(name);
        return NAME;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

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
