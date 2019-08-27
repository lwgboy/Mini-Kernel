package com.gcloud.controller.network.service;

import com.gcloud.controller.network.enums.OvsBridgeRefType;

public interface IOvsBridgeService {

    String createOvsBridge(String bridge, String hostname, String name, String flowTaskId);

    String deleteOvsBridge(String id, String flowTaskId);

    String allocate(String id, OvsBridgeRefType refType, String refId);

    int release(String id, OvsBridgeRefType refType, String refId);

    int release(OvsBridgeRefType refType, String refId);

}
