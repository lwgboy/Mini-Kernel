package com.gcloud.controller.security.entity;


import com.gcloud.framework.db.jdbc.annotation.ID;
import com.gcloud.framework.db.jdbc.annotation.Table;

/**
 * Created by yaowj on 2018/7/30.
 */
@Table(name = "gc_security_cluster_network_config")
public class SecurityClusterNetworkConfig {

    @ID
    private String id;
    private String componentType;
    private String networkType;
    private String portType;
    private String configType;
    private String refConfig;

    private Integer orders;

    public static String ID = "id";
    public static String COMPONENT_TYPE = "componentType";
    public static String NETWORK_TYPE = "networkType";
    public static String PORT_TYPE = "portType";
    public static String CONFIG_TYPE = "configType";
    public static String REF_CONFIG = "refConfig";
    public static String ORDERS = "orders";


    public String getComponentType() {
        return componentType;
    }

    public void setComponentType(String componentType) {
        this.componentType = componentType;
    }

    public String getNetworkType() {
        return networkType;
    }

    public void setNetworkType(String networkType) {
        this.networkType = networkType;
    }

    public String getPortType() {
        return portType;
    }

    public void setPortType(String portType) {
        this.portType = portType;
    }

    public String getConfigType() {
        return configType;
    }

    public void setConfigType(String configType) {
        this.configType = configType;
    }

    public Integer getOrders() {
        return orders;
    }

    public void setOrders(Integer orders) {
        this.orders = orders;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getRefConfig() {
        return refConfig;
    }

    public void setRefConfig(String refConfig) {
        this.refConfig = refConfig;
    }

    public String updateId(String id) {
        this.setId(id);
        return ID;
    }

    public String updateComponentType(String componentType) {
        this.setComponentType(componentType);
        return COMPONENT_TYPE;
    }

    public String updateNetworkType(String networkType) {
        this.setNetworkType(networkType);
        return NETWORK_TYPE;
    }

    public String updatePortType(String portType) {
        this.setPortType(portType);
        return PORT_TYPE;
    }

    public String updateConfigType(String configType) {
        this.setConfigType(configType);
        return CONFIG_TYPE;
    }

    public String updateRefConfig(String refConfig) {
        this.setRefConfig(refConfig);
        return REF_CONFIG;
    }

    public String updateOrders(Integer orders) {
        this.setOrders(orders);
        return ORDERS;
    }
}
