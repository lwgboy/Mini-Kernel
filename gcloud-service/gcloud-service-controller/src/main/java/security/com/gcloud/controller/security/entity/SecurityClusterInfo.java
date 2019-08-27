package com.gcloud.controller.security.entity;


import com.gcloud.framework.db.jdbc.annotation.ID;
import com.gcloud.framework.db.jdbc.annotation.Table;

/**
 * Created by yaowj on 2018/8/10.
 */
@Table(name = "gc_security_cluster_info")
public class SecurityClusterInfo {

    @ID
    private Integer id;
    private String clusterId;
    private String hostname;
    private Boolean ha;
    
    
    public static final String ID = "id";
    public static final String CLUSTER_ID = "clusterId";
    public static final String HOST_NAME = "hostname";
    public static final String HA = "ha";

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getClusterId() {
        return clusterId;
    }

    public void setClusterId(String clusterId) {
        this.clusterId = clusterId;
    }

    public String getHostname() {
        return hostname;
    }

    public void setHostname(String hostname) {
        this.hostname = hostname;
    }

    public Boolean getHa() {
        return ha;
    }

    public void setHa(Boolean ha) {
        this.ha = ha;
    }
    
    public String updateId(Integer id) {
    	this.setId(id);
    	return ID;
    }
    
    public String updateClusterId(String clusterId) {
    	this.setClusterId(clusterId);
    	return CLUSTER_ID;
    }
    
    public String updateHostname(String hostname) {
    	this.setHostname(hostname);
    	return HOST_NAME;
    }
    
    public String updateHa(Boolean ha) {
    	this.setHa(ha);
    	return HA;
    }
}
