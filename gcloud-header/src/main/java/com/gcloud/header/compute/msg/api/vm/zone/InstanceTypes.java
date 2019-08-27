
package com.gcloud.header.compute.msg.api.vm.zone;

import java.io.Serializable;
import java.util.List;

public class InstanceTypes implements Serializable {

    private static final long serialVersionUID = 1L;
    private List<InstanceType> supportedInstanceType;

    public List<InstanceType> getSupportedInstanceType() {
        return supportedInstanceType;
    }

    public void setSupportedInstanceType(List<InstanceType> supportedInstanceType) {
        this.supportedInstanceType = supportedInstanceType;
    }

}
