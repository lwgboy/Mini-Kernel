
package com.gcloud.controller;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.gcloud.core.exception.GCloudException;
import com.gcloud.core.service.SpringUtil;
import com.gcloud.header.enums.ProviderType;
import com.gcloud.header.enums.ResourceType;
import com.gcloud.header.storage.StorageErrorCodes;

@Component
public class ResourceProviders {

    private static final Map<ResourceType, Map<Integer, IResourceProvider>> PROVIDERS = new HashMap<>();
    private static final Map<ResourceType, IResourceProvider> DEFAULT_PROVIDERS = new HashMap<>();

    @Autowired
    private ResourceProviderConfig config;

    @PostConstruct
    private void init() {
        for (IResourceProvider provider : SpringUtil.getBeans(IResourceProvider.class)) {
            ResourceProviders.register(provider);
        }
        ResourceProviders.registerDefault(ResourceType.STORAGE_POOL, config.getStorage());
        ResourceProviders.registerDefault(ResourceType.VOLUME, config.getStorage());
        ResourceProviders.registerDefault(ResourceType.SNAPSHOT, config.getStorage());
        ResourceProviders.registerDefault(ResourceType.IMAGE, config.getImage());
        ResourceProviders.registerDefault(ResourceType.SLB, config.getSlb());
        ResourceProviders.registerDefault(ResourceType.NETWORK, config.getNetwork());
        ResourceProviders.registerDefault(ResourceType.SUBNET, config.getNetwork());
        ResourceProviders.registerDefault(ResourceType.FLOATING_IP, config.getNetwork());
        ResourceProviders.registerDefault(ResourceType.OVS_BRIDGE, config.getNetwork());
        ResourceProviders.registerDefault(ResourceType.ROUTER, config.getNetwork());
        ResourceProviders.registerDefault(ResourceType.PORT, config.getNetwork());
        ResourceProviders.registerDefault(ResourceType.SECURITY_GROUP, config.getNetwork());
        ResourceProviders.registerDefault(ResourceType.VSERVER_GROUP, config.getSlb());
        ResourceProviders.registerDefault(ResourceType.SLB_LISTENER, config.getSlb());
        ResourceProviders.registerDefault(ResourceType.SCHEDULER, config.getSlb());
    }

    private static void register(IResourceProvider provider) {
        synchronized (PROVIDERS) {
            Map<Integer, IResourceProvider> tmp = PROVIDERS.get(provider.resourceType());
            if (tmp == null) {
                tmp = new HashMap<>();
                PROVIDERS.put(provider.resourceType(), tmp);
            }
            tmp.put(provider.providerType().getValue(), provider);
        }
    }

    private static void registerDefault(ResourceType resourceType, String providerName) {
        synchronized (PROVIDERS) {
            ProviderType providerType = ProviderType.valueOf(providerName.toUpperCase());
            IResourceProvider provider = ResourceProviders.get(resourceType, providerType.getValue());
            if (provider != null) {
                DEFAULT_PROVIDERS.put(resourceType, provider);
            }
        }
    }

    public static <T extends IResourceProvider> T checkAndGet(ResourceType resourceType, Integer providerType) {
        T res = ResourceProviders.get(resourceType, providerType);
        if (res == null) {
            throw new GCloudException(StorageErrorCodes.NO_SUCH_PROVIDER);
        }
        return res;
    }

    public static <T extends IResourceProvider> T get(ResourceType resourceType, Integer providerType) {
        Map<Integer, IResourceProvider> tmp = PROVIDERS.get(resourceType);
        if (tmp == null) {
            return null;
        }
        return (T)tmp.get(providerType);
    }

    public static <T extends IResourceProvider> T getDefault(ResourceType resourceType) {
        return (T)DEFAULT_PROVIDERS.get(resourceType);
    }

    public static <T extends IResourceProvider> T getOrDefault(ResourceType resourceType, Integer providerType) {
        T res = ResourceProviders.get(resourceType, providerType);
        return res == null ? getDefault(resourceType) : res;
    }

}
