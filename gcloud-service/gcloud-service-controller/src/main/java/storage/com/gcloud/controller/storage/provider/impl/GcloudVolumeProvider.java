
package com.gcloud.controller.storage.provider.impl;

import com.gcloud.controller.storage.dao.SnapshotDao;
import com.gcloud.controller.storage.dao.VolumeDao;
import com.gcloud.controller.storage.driver.IStorageDriver;
import com.gcloud.controller.storage.entity.Snapshot;
import com.gcloud.controller.storage.entity.StoragePool;
import com.gcloud.controller.storage.entity.Volume;
import com.gcloud.controller.storage.entity.VolumeAttachment;
import com.gcloud.controller.storage.model.CreateDiskResponse;
import com.gcloud.controller.storage.model.CreateVolumeParams;
import com.gcloud.controller.storage.provider.IVolumeProvider;
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
import java.util.List;
import java.util.Map;

@Component
public class GcloudVolumeProvider implements IVolumeProvider {

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
        return ResourceType.VOLUME;
    }

    @Override
    public ProviderType providerType() {
        return ProviderType.GCLOUD;
    }

    public List<Volume> getVolumeList(Map<String, String> filterParams) throws GCloudException {
        return null;
    }

    public List<Snapshot> getSnapshotList(Map<String, String> filterParams) throws GCloudException {
        return null;
    }

    @Override
    public CreateDiskResponse createVolume(String volumeId, CreateVolumeParams params, CurrentUser currentUser) throws GCloudException {
        Volume volume = new Volume();
        {
            volume.setId(volumeId);
            volume.setDisplayName(params.getDiskName());
            volume.setStatus(VolumeStatus.CREATING.value());
            volume.setSize(params.getSize());
            volume.setDiskType(params.getDiskType().getValue());
            volume.setCreatedAt(new Date());
            volume.setDescription(params.getDescription());
            volume.setSnapshotId(params.getSnapshotId());
            volume.setUserId(currentUser.getId());
            volume.setTenantId(currentUser.getDefaultTenant());
            volume.setCategory(params.getDiskCategory());
            volume.setStorageType(params.getPool().getStorageType());
            volume.setPoolId(params.getPool().getId());
            volume.setPoolName(params.getPool().getPoolName());
            volume.setProvider(params.getPool().getProvider());
            volume.setProviderRefId(volume.getId());
            volume.setZoneId(params.getZoneId());
            volume.setImageRef(params.getImageRef());
        }
        this.volumeDao.save(volume);
        CreateDiskResponse response = DRIVERS.get(volume.getStorageType()).createVolume(params.getTaskId(), params.getPool(), volume);

        return response;
    }

    @Override
    public void deleteVolume(StoragePool pool, Volume volume, String taskId) throws GCloudException {
        DRIVERS.get(volume.getStorageType()).deleteVolume(taskId, pool, volume);
    }

    @Override
    public void updateVolume(String volumeRefId, String name, String description) throws GCloudException {
        // no need
    }

    @Override
    public void reserveVolume(String volumeRefId) throws GCloudException {
        // no need
    }

    @Override
    public void unreserveVolume(String volumeRefId) throws GCloudException {
        // no need
    }

    @Override
    public String attachVolume(Volume volume, String gcAttachmentId, String instanceId, String mountPoint, String hostname, String taskId) throws GCloudException {
        return gcAttachmentId;
    }

    @Override
    public void beginDetachingVolume(Volume volume) throws GCloudException {
        // no need
    }

    @Override
    public void rollDetachingVolume(Volume volume) throws GCloudException {
        // no need
    }

    @Override
    public void detachVolume(Volume volume, VolumeAttachment attachment) throws GCloudException {
        // no need
    }

    @Override
    public void resizeVolume(StoragePool pool, Volume volume, int newSize, String taskId) throws GCloudException {
        DRIVERS.get(volume.getStorageType()).resizeVolume(taskId, pool, volume, newSize);
    }

}
