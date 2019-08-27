package com.gcloud.header.network.model;

import java.io.Serializable;
import java.util.List;

public class NetworkInterfaces implements Serializable {

    private static final long serialVersionUID = 1L;

    private List<NetworkInterfaceType> networkInterface;

    public List<NetworkInterfaceType> getNetworkInterface() {
        return networkInterface;
    }

    public void setNetworkInterface(List<NetworkInterfaceType> networkInterface) {
        this.networkInterface = networkInterface;
    }
}
