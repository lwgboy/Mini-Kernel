package com.gcloud.controller.compute.model.node;

import com.gcloud.common.util.StringUtils;

import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/*
 * @Date 2015-4-14
 *
 * @Author zhangzj@g-cloud.com.cn
 *
 * @Copyright 2015 www.g-cloud.com.cn Inc. All rights reserved.
 *
 * @Description 节点实体
 */
public class Node implements Serializable {

    private static final long serialVersionUID = 1L;
    private String type;
    private AvailableVmResource availableVmResource;
    private String hostName;
    private String nodeIp;
    private Date registerTime;
    private Date updateTime;
    private Map<String, String> comment = new HashMap<String, String>();
    private int refreshTime = 0;
    private boolean first = true;
    
    private String zoneId;

    public Node() {
        this.registerTime = new Date();
    }

    public Node(String hostName, String type) {
        this.hostName = hostName;
        this.type = type;
        this.registerTime = new Date();
    }

    public enum NodeType {
        VIRTUAL("virtual"),
        PHYSICAL("physical"),
        MANAGER("manager"),
        COMPUTE("compute");

        private String value;

        NodeType(String value) {
            this.value = value;
        }

        public String getValue() {
            return value;
        }
    }

    /**
     * 获取节点的名,如输入compute_yuanjz 输出 yuanjz
     *
     * @param str 源字串
     * @return 返回节点名字串
     */
    public static String toNodeName(String str) {
        String[] strs = str.split("_");
        if (strs.length == 1) {
            return str;
        } else if (strs.length == 2) {
            return strs[1];
        } else {
            return null;
        }
    }

    public AvailableVmResource getAvailableVmResource() {
        if (this.availableVmResource == null)
            this.availableVmResource = new AvailableVmResource();
        return availableVmResource;
    }

    public void setAvailableVmResource(AvailableVmResource availableVmResource) {
        this.availableVmResource = availableVmResource;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getHostName() {
        return hostName;
    }

    public void setHostName(String hostName) {
        this.hostName = hostName;
    }

    public String getNodeIp() {
        return nodeIp;
    }

    public void setNodeIp(String nodeIp) {
        this.nodeIp = nodeIp;
    }

    public Date getRegisterTime() {
        return registerTime;
    }

    public String getCommentInfo(NodeCommentInfo info) {
        if (StringUtils.isBlank(info.getValue()))
            return null;
        return comment.get(info.getValue());
    }

    public void setCommentInfo(NodeCommentInfo info, String value) {
        if (StringUtils.isBlank(info.getValue()))
            return;
        comment.put(info.getValue(), value);
    }

    private boolean check = false;

    public boolean isCheck() {
        return check;
    }

    public void setCheck(boolean check) {
        this.check = check;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public int getRefreshTime() {
        return refreshTime;
    }

    public void setRefreshTime(int refreshTime) {
        this.refreshTime = refreshTime;
    }

    public boolean isFirst() {
        return first;
    }

    public void setFirst(boolean first) {
        this.first = first;
    }

    public void setComment(Map<String, String> comment) {
        this.comment = comment;
    }

    public Map<String, String> getComment() {
        return comment;
    }

    public String getZoneId() {
        return zoneId;
    }

    public void setZoneId(String zoneId) {
        this.zoneId = zoneId;
    }
}
