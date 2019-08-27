package com.gcloud.controller.network.entity;

/**
 * Created by yaowj on 2018/10/25.
 */

import com.gcloud.framework.db.jdbc.annotation.ID;
import com.gcloud.framework.db.jdbc.annotation.Table;
@Table(name="gc_security_group_port_bindings",jdbc="controllerJdbcTemplate")
public class SecurityGroupPortBinding {

    @ID
    private Long id;
    private String portId;
    private String securityGroupId;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getPortId() {
        return portId;
    }

    public void setPortId(String portId) {
        this.portId = portId;
    }

    public String getSecurityGroupId() {
        return securityGroupId;
    }

    public void setSecurityGroupId(String securityGroupId) {
        this.securityGroupId = securityGroupId;
    }
}
