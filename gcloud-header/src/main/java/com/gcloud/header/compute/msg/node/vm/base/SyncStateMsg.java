package com.gcloud.header.compute.msg.node.vm.base;

import com.gcloud.header.NodeMessage;
import com.gcloud.header.compute.msg.node.vm.model.VmStateInfo;

import java.util.Map;

/**
 * Created by yaowj on 2019/1/10.
 */
public class SyncStateMsg extends NodeMessage {

    private Map<String, VmStateInfo> stateInfos;
    private String hostname;

    public Map<String, VmStateInfo> getStateInfos() {
        return stateInfos;
    }

    public void setStateInfos(Map<String, VmStateInfo> stateInfos) {
        this.stateInfos = stateInfos;
    }

    public String getHostname() {
        return hostname;
    }

    public void setHostname(String hostname) {
        this.hostname = hostname;
    }
}
