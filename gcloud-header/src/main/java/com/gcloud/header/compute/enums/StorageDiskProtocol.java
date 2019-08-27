package com.gcloud.header.compute.enums;

import com.gcloud.header.enums.ProviderType;
import com.gcloud.header.storage.enums.StoragePoolDriver;

import java.util.Arrays;

public enum StorageDiskProtocol {

    CINDER_LOCAL(ProviderType.CINDER, StoragePoolDriver.LVM, DiskProtocol.LVM),
    CINDER_CEPH(ProviderType.CINDER, StoragePoolDriver.RBD, DiskProtocol.RBD),

    GCLOUD_LOCAL(ProviderType.GCLOUD, StoragePoolDriver.FILE, DiskProtocol.FILE),
    GCLOUD_CEPH(ProviderType.GCLOUD, StoragePoolDriver.RBD, DiskProtocol.RBD)

    ;

    private ProviderType providerType;
    private StoragePoolDriver storagePoolDriver;
    private DiskProtocol diskProtocol;

    StorageDiskProtocol(ProviderType providerType, StoragePoolDriver storagePoolDriver, DiskProtocol diskProtocol) {
        this.providerType = providerType;
        this.storagePoolDriver = storagePoolDriver;
        this.diskProtocol = diskProtocol;
    }

    public ProviderType getProviderType() {
        return providerType;
    }

    public StoragePoolDriver getStoragePoolDriver() {
        return storagePoolDriver;
    }

    public DiskProtocol getDiskProtocol() {
        return diskProtocol;
    }

    public static StorageDiskProtocol getByProviderAndStorageType(Integer provider, String storageDriver){

        ProviderType providerType = ProviderType.get(provider);
        StoragePoolDriver driver = StoragePoolDriver.get(storageDriver);
        return getByProviderAndStorageType(providerType, driver);

    }

    public static StorageDiskProtocol getByProviderAndStorageType(ProviderType provider, StoragePoolDriver storageDriver){

        if(provider == null || storageDriver == null){
            return null;
        }

        return Arrays.stream(StorageDiskProtocol.values()).filter(s -> s.getProviderType().equals(provider) && s.getStoragePoolDriver().equals(storageDriver))
                .findFirst().orElse(null);

    }

}
