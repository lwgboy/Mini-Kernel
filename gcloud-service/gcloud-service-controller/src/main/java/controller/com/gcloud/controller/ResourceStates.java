package com.gcloud.controller;

import com.gcloud.core.service.SpringUtil;
import com.gcloud.header.enums.ProviderType;
import com.gcloud.header.enums.ResourceType;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.Map;

@Component
public class ResourceStates {

    private static Map<ResourceType, Map<ProviderType, IResourceState>> resourceStates = new HashMap<>();

    @PostConstruct
    public void init(){
        for(IResourceState state : SpringUtil.getBeans(IResourceState.class)){

            if(state.resourceType() == null || state.providerType() == null){
                continue;
            }

            Map<ProviderType, IResourceState> resourceStateMap = resourceStates.get(state.resourceType());
            if(resourceStateMap == null){
                resourceStateMap = new HashMap<>();
                resourceStates.put(state.resourceType(), resourceStateMap);
            }
            resourceStateMap.put(state.providerType(), state);
        }
    }



    public static String status(ResourceType resourceType, ProviderType providerType, String status){
        IResourceState resourceState = resourceState(resourceType, providerType);
        if(resourceState == null){
            return null;
        }

        return resourceState.value(status);
    }


    public static String statusCnName(ResourceType resourceType, ProviderType providerType, String status){

        IResourceState resourceState = resourceState(resourceType, providerType);
        if(resourceState == null){
            return null;
        }

        return resourceState.cnName(status);
    }

    private static IResourceState resourceState(ResourceType resourceType, ProviderType providerType){
        if(resourceType == null || providerType == null){
            return null;
        }

        Map<ProviderType, IResourceState> resourceStateMap = resourceStates.get(resourceType);
        if(resourceStateMap == null){
            return null;
        }

        return resourceStateMap.get(providerType);
    }

}
