package com.gcloud.controller.network.provider;

import com.gcloud.controller.IResourceProvider;
import com.gcloud.controller.network.entity.Network;
import com.gcloud.controller.network.entity.Subnet;
import com.gcloud.controller.network.model.CreateSubnetParams;
import com.gcloud.header.api.model.CurrentUser;

import java.util.List;
import java.util.Map;

public interface ISubnetProvider extends IResourceProvider {

	void createSubnet(Network network, String subnetId, CreateSubnetParams params, CurrentUser currentUser);

    void deleteSubnet(String subnetRefId);

    void modifyAttribute(String subnetRefId, String subnetName);

    List<Subnet> list(Map<String, String> filter);

}
