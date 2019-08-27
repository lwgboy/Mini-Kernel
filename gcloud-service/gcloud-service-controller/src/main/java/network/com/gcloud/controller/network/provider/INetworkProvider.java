package com.gcloud.controller.network.provider;

import com.gcloud.controller.IResourceProvider;
import com.gcloud.controller.network.entity.Network;
import com.gcloud.controller.network.model.CreateNetworkParams;
import com.gcloud.header.api.model.CurrentUser;
import com.gcloud.header.network.msg.api.CreateExternalNetworkMsg;

import java.util.List;
import java.util.Map;

public interface INetworkProvider extends IResourceProvider {

    void createNetwork(String networkId, CreateNetworkParams msg, CurrentUser currentUser);
    
    void createExternalNetwork(String networkId, CreateExternalNetworkMsg msg);

    void removeNetwork(String vpcRefId);

    void updateNetwork(String vpcRefId, String vpcName);

    List<Network> list(Map<String, String> filter);
}
