package com.gcloud.controller.network.service;

import com.gcloud.controller.network.model.CreateSubnetParams;
import com.gcloud.header.api.model.CurrentUser;

public interface ISubnetService {
    String createSubnet(CreateSubnetParams params, CurrentUser currentUser);

    void deleteSubnet(String subnetId);

    void modifyAttribute(String subnetId, String subnetName);


}
