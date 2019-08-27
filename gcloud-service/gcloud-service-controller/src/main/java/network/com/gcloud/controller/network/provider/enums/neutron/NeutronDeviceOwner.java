package com.gcloud.controller.network.provider.enums.neutron;

import com.gcloud.header.compute.enums.DeviceOwner;

import java.util.Arrays;

public enum  NeutronDeviceOwner {

    COMPUTE("compute:node", DeviceOwner.COMPUTE),
    FOREIGN("network:foreign", DeviceOwner.FOREIGN),
    DHCP("network:dhcp", DeviceOwner.DHCP),
    FLOATINGIP("network:floatingip", DeviceOwner.FLOATINGIP),
    ROUTER("network:router_interface", DeviceOwner.ROUTER),
    LOADBALANCER("neutron:LOADBALANCERV2", DeviceOwner.LOADBALANCER),
    GATEWAY("network:router_gateway", DeviceOwner.GATEWAY);

    private String neutronDeviceOwner;
    private DeviceOwner deviceOwner;

    NeutronDeviceOwner(String neutronDeviceOwner, DeviceOwner deviceOwner) {
        this.neutronDeviceOwner = neutronDeviceOwner;
        this.deviceOwner = deviceOwner;
    }

    public String getNeutronDeviceOwner() {
        return neutronDeviceOwner;
    }

    public DeviceOwner getDeviceOwner() {
        return deviceOwner;
    }

    public static String toGCloudValue(String neutronDeviceOwner){
        NeutronDeviceOwner deviceOwner = Arrays.stream(NeutronDeviceOwner.values()).filter(o -> o.getNeutronDeviceOwner().equals(neutronDeviceOwner)).findFirst().orElse(null);
        return deviceOwner == null ? null : deviceOwner.getDeviceOwner().getValue();
    }
}
