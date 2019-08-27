
package com.gcloud.header.compute.msg.api.vm.zone;

import java.io.Serializable;

public class DiskCategory implements Serializable {

    private static final long serialVersionUID = 1L;
    private String diskTypeId;
    private String diskTypeName;
    private String diskTypeCnName;
    private Integer min;
    private Integer max;

    public String getDiskTypeId() {
        return diskTypeId;
    }

    public void setDiskTypeId(String diskTypeId) {
        this.diskTypeId = diskTypeId;
    }

    public String getDiskTypeName() {
        return diskTypeName;
    }

    public void setDiskTypeName(String diskTypeName) {
        this.diskTypeName = diskTypeName;
    }

    public String getDiskTypeCnName() {
        return diskTypeCnName;
    }

    public void setDiskTypeCnName(String diskTypeCnName) {
        this.diskTypeCnName = diskTypeCnName;
    }

    public Integer getMin() {
        return min;
    }

    public void setMin(Integer min) {
        this.min = min;
    }

    public Integer getMax() {
        return max;
    }

    public void setMax(Integer max) {
        this.max = max;
    }

}
