
package com.gcloud.header.compute.msg.api.vm.zone;

import java.io.Serializable;

public class InstanceType implements Serializable {

    private static final long serialVersionUID = 1L;

    private String instanceTypeId;
    private String instanceTypeName;

    public String getInstanceTypeId() {
        return instanceTypeId;
    }

    public void setInstanceTypeId(String instanceTypeId) {
        this.instanceTypeId = instanceTypeId;
    }

    public String getInstanceTypeName() {
        return instanceTypeName;
    }

    public void setInstanceTypeName(String instanceTypeName) {
        this.instanceTypeName = instanceTypeName;
    }

}
