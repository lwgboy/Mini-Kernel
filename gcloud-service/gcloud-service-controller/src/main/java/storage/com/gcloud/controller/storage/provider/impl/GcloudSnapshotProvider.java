
package com.gcloud.controller.storage.provider.impl;

import com.gcloud.controller.storage.dao.SnapshotDao;
import com.gcloud.controller.storage.dao.VolumeDao;
import com.gcloud.controller.storage.driver.IStorageDriver;
import com.gcloud.controller.storage.entity.Snapshot;
import com.gcloud.controller.storage.entity.StoragePool;
import com.gcloud.controller.storage.entity.Volume;
import com.gcloud.controller.storage.provider.ISnapshotProvider;
import com.gcloud.controller.storage.service.IVolumeService;
import com.gcloud.core.exception.GCloudException;
import com.gcloud.core.service.SpringUtil;
import com.gcloud.header.api.model.CurrentUser;
import com.gcloud.header.enums.ProviderType;
import com.gcloud.header.enums.ResourceType;
import com.gcloud.header.storage.enums.VolumeStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Component
public class GcloudSnapshotProvider implements ISnapshotProvider {

    @Autowired
    private IVolumeService volumeService;

    @Autowired
    private VolumeDao volumeDao;

    @Autowired
    private SnapshotDao snapshotDao;

    private static final Map<String, IStorageDriver> DRIVERS = new HashMap<>();

    @PostConstruct
    private void init() {
        for (IStorageDriver driver : SpringUtil.getBeans(IStorageDriver.class)) {
            DRIVERS.put(driver.storageType().getValue(), driver);
        }
    }

    @Override
    public ResourceType resourceType() {
        return ResourceType.SNAPSHOT;
    }

    @Override
    public ProviderType providerType() {
        return ProviderType.GCLOUD;
    }

    @Override
    public void createSnapshot(StoragePool pool, Volume volume, String snapshotId, String name, String description, CurrentUser currentUser, String taskId) throws GCloudException {
        Snapshot snap = new Snapshot();
        {
            snap.setId(snapshotId);
            snap.setDisplayName(name);
            snap.setVolumeSize(volume.getSize());
            snap.setVolumeId(volume.getId());
            snap.setDisplayDescription(description);
            snap.setCreatedAt(new Date());
            snap.setUserId(currentUser.getId());
            snap.setTenantId(currentUser.getDefaultTenant());
            snap.setStatus(VolumeStatus.CREATING.value());
            snap.setStorageType(volume.getStorageType());
            snap.setPoolName(volume.getPoolName());
            snap.setProvider(volume.getProvider());
            snap.setProviderRefId(snap.getId());
        }
        this.snapshotDao.save(snap);
        DRIVERS.get(volume.getStorageType()).createSnapshot(pool, volume.getProviderRefId(), snap);
    }

    @Override
    public void updateSnapshot(String snapshotRefId, String name, String description) throws GCloudException {
        // no need
    }

    @Override
    public void deleteSnapshot(StoragePool pool, Volume volume, Snapshot snapshot, String taskId) throws GCloudException {
        DRIVERS.get(snapshot.getStorageType()).deleteSnapshot(pool, volume.getProviderRefId(), snapshot, taskId);
    }

    @Override
    public void resetSnapshot(StoragePool pool, Volume volume, Snapshot snapshot, String diskId, String taskId) throws GCloudException {
        DRIVERS.get(snapshot.getStorageType()).resetSnapshot(pool, volume.getProviderRefId(), snapshot, volume.getSize());
    }

}
