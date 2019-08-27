package com.gcloud.header.storage.model;

import java.io.Serializable;

/**
 * Created by yaowj on 2018/11/8.
 */
public class VolumeTypeListResponse implements Serializable {

    private static final long serialVersionUID = 1L;

    private String id;
    private String name;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
