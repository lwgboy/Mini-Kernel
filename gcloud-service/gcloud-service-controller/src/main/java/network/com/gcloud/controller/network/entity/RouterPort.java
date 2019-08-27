package com.gcloud.controller.network.entity;

import com.gcloud.framework.db.jdbc.annotation.ID;
import com.gcloud.framework.db.jdbc.annotation.Table;

@Table(name = "gc_router_ports", jdbc = "controllerJdbcTemplate")
public class RouterPort {

    @ID
    private Long id;
    private String routerId;
    private String portId;
    private String portType;

    public static final String ID = "id";
    public static final String ROUTER_ID = "routerId";
    public static final String PORT_ID = "portId";
    public static final String PORT_TYPE = "portType";

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getRouterId() {
        return routerId;
    }

    public void setRouterId(String routerId) {
        this.routerId = routerId;
    }

    public String getPortId() {
        return portId;
    }

    public void setPortId(String portId) {
        this.portId = portId;
    }

    public String getPortType() {
        return portType;
    }

    public void setPortType(String portType) {
        this.portType = portType;
    }

    public String updateId(Long id) {
        this.setId(id);
        return ID;
    }

    public String updateRouterId(String routerId) {
        this.setRouterId(routerId);
        return ROUTER_ID;
    }

    public String updatePortId(String portId) {
        this.setPortId(portId);
        return PORT_ID;
    }

    public String updatePortType(String portType) {
        this.setPortType(portType);
        return PORT_TYPE;
    }

}
