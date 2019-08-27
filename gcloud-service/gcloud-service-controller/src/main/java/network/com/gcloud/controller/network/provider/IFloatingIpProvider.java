package com.gcloud.controller.network.provider;

import com.gcloud.controller.IResourceProvider;
import com.gcloud.controller.network.entity.FloatingIp;
import com.gcloud.controller.network.model.AllocateEipAddressResponse;
import com.gcloud.header.api.model.CurrentUser;

import java.util.List;
import java.util.Map;

public interface IFloatingIpProvider extends IResourceProvider {

    AllocateEipAddressResponse allocateEipAddress(String networkId, String regionId, CurrentUser currentUser);

    void associateEipAddress(String allocationRefId, String netcardId);

    void unAssociateEipAddress(String allocationRefId);

    void releaseEipAddress(String allocationRefId);

    List<FloatingIp> list(Map<String, String> filter);
}
