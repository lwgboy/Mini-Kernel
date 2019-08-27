
package com.gcloud.controller;

import com.gcloud.header.enums.ProviderType;
import com.gcloud.header.enums.ResourceType;

public interface IResourceProvider {

    ResourceType resourceType();

    ProviderType providerType();

}
