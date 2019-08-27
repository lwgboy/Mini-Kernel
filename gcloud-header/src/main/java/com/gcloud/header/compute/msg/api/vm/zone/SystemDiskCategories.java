
package com.gcloud.header.compute.msg.api.vm.zone;

import java.io.Serializable;
import java.util.List;

public class SystemDiskCategories implements Serializable {

    private static final long serialVersionUID = 1L;
    private List<DiskCategory> supportedSystemDiskCategory;

    public List<DiskCategory> getSupportedSystemDiskCategory() {
        return supportedSystemDiskCategory;
    }

    public void setSupportedSystemDiskCategory(List<DiskCategory> supportedSystemDiskCategory) {
        this.supportedSystemDiskCategory = supportedSystemDiskCategory;
    }

}
