package com.gcloud.controller;

import com.gcloud.header.enums.ProviderType;
import com.gcloud.header.enums.ResourceType;

public interface IResourceState {

    ResourceType resourceType();
    ProviderType providerType();

    String value(String status);
    String cnName(String status);


}
