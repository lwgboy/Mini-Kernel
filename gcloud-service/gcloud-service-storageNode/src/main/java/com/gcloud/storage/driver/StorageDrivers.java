
package com.gcloud.storage.driver;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.springframework.stereotype.Component;

import com.gcloud.core.service.SpringUtil;
import com.gcloud.header.storage.enums.StoragePoolDriver;

@Component
public class StorageDrivers {

    private static final Map<StoragePoolDriver, IStorageDriver> DRIVERS = new HashMap<>();

    @PostConstruct
    private void init() {
        for (IStorageDriver driver : SpringUtil.getBeans(IStorageDriver.class)) {
            DRIVERS.put(driver.driver(), driver);
        }
    }

    public IStorageDriver get(String driverName) {
        return DRIVERS.get(StoragePoolDriver.get(driverName));
    }

}
