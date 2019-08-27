package com.gcloud.controller.network.provider;

import com.gcloud.controller.IResourceProvider;

public interface IOvsBridgeProvider extends IResourceProvider {

    String createOvsBridge(String bridge, String hostname, String name, String flowTaskId);

    void deleteOvsBridge(String id, String flowTaskId, String hostname, String bridge);

}
