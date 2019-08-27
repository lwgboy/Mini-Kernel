
package com.gcloud.header.compute.msg.api.vm.zone;

import java.io.Serializable;
import java.util.List;

public class DataDiskCategories implements Serializable {

    private static final long serialVersionUID = 1L;
    private List<DiskCategory> supportedDataDiskCategory;

    public List<DiskCategory> getSupportedDataDiskCategory() {
        return supportedDataDiskCategory;
    }

    public void setSupportedDataDiskCategory(List<DiskCategory> supportedDataDiskCategory) {
        this.supportedDataDiskCategory = supportedDataDiskCategory;
    }

}
