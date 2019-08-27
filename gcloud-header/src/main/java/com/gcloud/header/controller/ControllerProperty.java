package com.gcloud.header.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Component
public class ControllerProperty {

    @Value("${gcloud.controller.region}")
    public String region;

    @PostConstruct
    public void init(){
        initRegionId(region);
    }

    public static String REGION_ID;

    public static void initRegionId(String regionId){
        REGION_ID = regionId;
    }


    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

}
