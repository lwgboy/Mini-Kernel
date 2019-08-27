package com.gcloud.controller.compute.entity;

import com.gcloud.framework.db.jdbc.annotation.ID;
import com.gcloud.framework.db.jdbc.annotation.Table;

import java.util.Date;


/*
 * @Date 2015-4-16
 *
 * @Author yaowj@g-cloud.com.cn
 *
 * @Copyright 2015 www.g-cloud.com.cn Inc. All rights reserved.
 *
 * @Description 节点连接日志
 */
@Table(name = "gc_compute_node_connect_logs",jdbc="controllerJdbcTemplate")
public class ComputeNodeConnectLog {

    @ID
    private Long id;
    private String hostname;
    private String nodeIp;
    private String logCode;
    private String connectType;
    private String remark;
    private Date logTime;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Date getLogTime() {
        return logTime;
    }

    public void setLogTime(Date logTime) {
        this.logTime = logTime;
    }

    public String getHostname() {
        return hostname;
    }

    public void setHostname(String hostname) {
        this.hostname = hostname;
    }


    public String getNodeIp() {
        return nodeIp;
    }

    public void setNodeIp(String nodeIp) {
        this.nodeIp = nodeIp;
    }

    public String getLogCode() {
        return logCode;
    }

    public void setLogCode(String logCode) {
        this.logCode = logCode;
    }

    public String getConnectType() {
        return connectType;
    }

    public void setConnectType(String connectType) {
        this.connectType = connectType;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }


}
