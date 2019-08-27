package com.gcloud.header.security.msg.model;

import java.io.Serializable;

/**
 * Created by yaowj on 2018/7/30.
 */
public class CreateClusterInfoParams implements Serializable {

    private static final long serialVersionUID = 1L;

    private String clusterGroupId;
    private String groupId;
    private String hostName;
    private String zoneId;

    public String getClusterGroupId() {
        return clusterGroupId;
    }

    public void setClusterGroupId(String clusterGroupId) {
        this.clusterGroupId = clusterGroupId;
    }

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public String getHostName() {
        return hostName;
    }

    public void setHostName(String hostName) {
        this.hostName = hostName;
    }

    public String getZoneId() {
        return zoneId;
    }

    public void setZoneId(String zoneId) {
        this.zoneId = zoneId;
    }
}
