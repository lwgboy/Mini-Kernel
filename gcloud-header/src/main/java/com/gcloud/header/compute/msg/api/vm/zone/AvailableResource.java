
package com.gcloud.header.compute.msg.api.vm.zone;

import java.io.Serializable;

public class AvailableResource implements Serializable {

    private static final long serialVersionUID = 1L;
    private InstanceTypes instanceTypes;
    private SystemDiskCategories systemDiskCategories;
    private DataDiskCategories dataDiskCategories;

    public InstanceTypes getInstanceTypes() {
        return instanceTypes;
    }

    public void setInstanceTypes(InstanceTypes instanceTypes) {
        this.instanceTypes = instanceTypes;
    }

    public SystemDiskCategories getSystemDiskCategories() {
        return systemDiskCategories;
    }

    public void setSystemDiskCategories(SystemDiskCategories systemDiskCategories) {
        this.systemDiskCategories = systemDiskCategories;
    }

    public DataDiskCategories getDataDiskCategories() {
        return dataDiskCategories;
    }

    public void setDataDiskCategories(DataDiskCategories dataDiskCategories) {
        this.dataDiskCategories = dataDiskCategories;
    }

}
