package com.gcloud.controller.network.entity;

import com.gcloud.framework.db.jdbc.annotation.ID;
import com.gcloud.framework.db.jdbc.annotation.Table;

/**
 * Created by yaowj on 2018/10/30.
 */
@Table(name = "gc_qos_bandwidth_limit_rules", jdbc = "controllerJdbcTemplate")
public class QosBandwidthLimitRule {

    @ID
    private String id;
    private String qosPolicyId;
    private Integer maxKbps;
    private Integer maxBurstKbps;
    private String direction;

    public static final String ID = "id";
    public static final String QOS_POLICY_ID = "qosPolicyId";
    public static final String MAX_KBPS = "maxKbps";
    public static final String MAX_BURST_KBPS = "maxBurstKbps";
    public static final String DIRECTION = "direction";

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getQosPolicyId() {
        return qosPolicyId;
    }

    public void setQosPolicyId(String qosPolicyId) {
        this.qosPolicyId = qosPolicyId;
    }

    public Integer getMaxKbps() {
        return maxKbps;
    }

    public void setMaxKbps(Integer maxKbps) {
        this.maxKbps = maxKbps;
    }

    public Integer getMaxBurstKbps() {
        return maxBurstKbps;
    }

    public void setMaxBurstKbps(Integer maxBurstKbps) {
        this.maxBurstKbps = maxBurstKbps;
    }

    public String getDirection() {
        return direction;
    }

    public void setDirection(String direction) {
        this.direction = direction;
    }


    public String updateId(String id) {
        this.setId(id);
        return ID;
    }

    public String updateQosPolicyId(String qosPolicyId) {
        this.setQosPolicyId(qosPolicyId);
        return QOS_POLICY_ID;
    }

    public String updateMaxKbps(Integer maxKbps) {
        this.setMaxKbps(maxKbps);
        return MAX_KBPS;
    }

    public String updateMaxBurstKbps(Integer maxBurstKbps) {
        this.setMaxBurstKbps(maxBurstKbps);
        return MAX_BURST_KBPS;
    }

    public String updateDirection(String direction) {
        this.setDirection(direction);
        return DIRECTION;
    }
}
