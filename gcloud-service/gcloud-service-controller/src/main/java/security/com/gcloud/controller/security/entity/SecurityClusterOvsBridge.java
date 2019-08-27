package com.gcloud.controller.security.entity;


import com.gcloud.framework.db.jdbc.annotation.ID;
import com.gcloud.framework.db.jdbc.annotation.Table;

/**
 * Created by yaowj on 2018/7/30.
 */
@Table(name = "gc_security_cluster_ovsbridge")
public class SecurityClusterOvsBridge {

    @ID
    private String id;
    private String clusterId;
    private String ovsBridgeId;
    private Boolean ha;

    public static final String ID = "id";
    public static final String CLUSTER_ID = "clusterId";
    public static final String OVS_BRIDGE_ID = "ovsBridgeId";
    public static final String HA = "ha";

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getClusterId() {
        return clusterId;
    }

    public void setClusterId(String clusterId) {
        this.clusterId = clusterId;
    }

    public Boolean getHa() {
        return ha;
    }

    public void setHa(Boolean ha) {
        this.ha = ha;
    }

    public String getOvsBridgeId() {
        return ovsBridgeId;
    }

    public void setOvsBridgeId(String ovsBridgeId) {
        this.ovsBridgeId = ovsBridgeId;
    }

    public String updateId(String id) {
        this.setId(id);
        return ID;
    }

    public String updateClusterId(String clusterId) {
        this.setClusterId(clusterId);
        return CLUSTER_ID;
    }

    public String updateOvsBridgeId(String ovsBridgeId) {
        this.setOvsBridgeId(ovsBridgeId);
        return OVS_BRIDGE_ID;
    }

    public String updateHa(Boolean ha) {
        this.setHa(ha);
        return HA;
    }
}
