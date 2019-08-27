package com.gcloud.header.storage.model;

import java.io.Serializable;
import java.util.List;

/**
 * Created by yaowj on 2018/9/29.
 */
public class DescribeDisksResponse implements Serializable {

    private static final long serialVersionUID = 1L;

    private List<DiskItemType> disk;

    public List<DiskItemType> getDisk() {
        return disk;
    }

    public void setDisk(List<DiskItemType> disk) {
        this.disk = disk;
    }
}
