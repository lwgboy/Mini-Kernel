
package com.gcloud.controller.storage.driver;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.springframework.stereotype.Component;

import com.gcloud.core.service.SpringUtil;

@Component
public class GcloudStorageDrivers {

    private static final Map<String, IStorageDriver> DRIVERS = new HashMap<>();

    @PostConstruct
    private void init() {
        for (IStorageDriver driver : SpringUtil.getBeans(IStorageDriver.class)) {
            DRIVERS.put(driver.storageType().getValue(), driver);
        }
    }

    public static IStorageDriver get(String storageType) {
        return DRIVERS.get(storageType);
    }

}
