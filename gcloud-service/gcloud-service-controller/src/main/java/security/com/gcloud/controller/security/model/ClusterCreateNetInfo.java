package com.gcloud.controller.security.model;

import com.gcloud.controller.security.enums.SecurityComponent;

import java.util.List;
import java.util.Map;

/**
 * Created by yaowj on 2018/7/30.
 */
public class ClusterCreateNetInfo {

    private Map<SecurityComponent, List<ClusterCreateNetcardInfo>> createNetcardInfo;
    private List<ClusterCreateOvsBridgeInfo> createOvsBridgeInfos;

    public Map<SecurityComponent, List<ClusterCreateNetcardInfo>> getCreateNetcardInfo() {
        return createNetcardInfo;
    }

    public void setCreateNetcardInfo(Map<SecurityComponent, List<ClusterCreateNetcardInfo>> createNetcardInfo) {
        this.createNetcardInfo = createNetcardInfo;
    }

    public List<ClusterCreateOvsBridgeInfo> getCreateOvsBridgeInfos() {
        return createOvsBridgeInfos;
    }

    public void setCreateOvsBridgeInfos(List<ClusterCreateOvsBridgeInfo> createOvsBridgeInfos) {
        this.createOvsBridgeInfos = createOvsBridgeInfos;
    }
}
