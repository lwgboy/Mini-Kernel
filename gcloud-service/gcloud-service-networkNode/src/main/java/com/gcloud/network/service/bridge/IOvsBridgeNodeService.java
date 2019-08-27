package com.gcloud.network.service.bridge;

public interface IOvsBridgeNodeService {

    void createOvsBridge(String bridgeName);

    void deleteOvsBridge(String bridgeName);

}
