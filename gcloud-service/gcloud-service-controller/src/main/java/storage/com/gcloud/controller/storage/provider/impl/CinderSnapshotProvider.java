
package com.gcloud.controller.storage.provider.impl;

import com.gcloud.common.util.StringUtils;
import com.gcloud.controller.ResourceStates;
import com.gcloud.controller.provider.CinderProviderProxy;
import com.gcloud.controller.storage.async.snapshot.CheckCinderCreateVolumeSnapshotAsync;
import com.gcloud.controller.storage.async.snapshot.CheckCinderDeleteVolumeSnapshotAsync;
import com.gcloud.controller.storage.async.snapshot.CreateCinderVolumeSnapshotRollbackAsync;
import com.gcloud.controller.storage.dao.SnapshotDao;
import com.gcloud.controller.storage.dao.VolumeDao;
import com.gcloud.controller.storage.entity.Snapshot;
import com.gcloud.controller.storage.entity.StoragePool;
import com.gcloud.controller.storage.entity.Volume;
import com.gcloud.controller.storage.provider.ISnapshotProvider;
import com.gcloud.core.exception.GCloudException;
import com.gcloud.core.simpleflow.Flow;
import com.gcloud.core.simpleflow.FlowDoneHandler;
import com.gcloud.core.simpleflow.NoRollbackFlow;
import com.gcloud.core.simpleflow.SimpleFlowChain;
import com.gcloud.header.api.model.CurrentUser;
import com.gcloud.header.enums.ProviderType;
import com.gcloud.header.enums.ResourceType;
import com.gcloud.header.storage.enums.SnapshotStatus;
import com.gcloud.header.storage.enums.VolumeStatus;

import org.openstack4j.api.Builders;
import org.openstack4j.model.storage.block.VolumeSnapshot;
import org.openstack4j.model.storage.block.builder.VolumeSnapshotBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class CinderSnapshotProvider implements ISnapshotProvider {

    @Autowired
    private CinderProviderProxy proxy;

    @Autowired
    private VolumeDao volumeDao;

    @Autowired
    private SnapshotDao snapshotDao;

    @Override
    public ResourceType resourceType() {
        return ResourceType.SNAPSHOT;
    }

    @Override
    public ProviderType providerType() {
        return ProviderType.CINDER;
    }

    @Override
    public void createSnapshot(StoragePool pool, Volume volume, String snapshotId, String name, String description, CurrentUser currentUser, String taskId) throws GCloudException {

        SimpleFlowChain<VolumeSnapshot, String> chain = new SimpleFlowChain<>("create volume snapshot");
        chain.then(new Flow<VolumeSnapshot>() {

            @Override
            public void run(SimpleFlowChain chain, VolumeSnapshot data) {

                VolumeSnapshotBuilder builder = Builders.volumeSnapshot().volume(volume.getProviderRefId());
                if (StringUtils.isNotBlank(name)) {
                    builder.name(name);
                }

                if (StringUtils.isNotBlank(description)) {
                    builder.description(description);
                }
                builder.force(true);

                VolumeSnapshot snap = proxy.createSnapshot(builder.toCreate().build());

                chain.data(snap);
                chain.next();
            }

            @Override
            public void rollback(SimpleFlowChain chain, VolumeSnapshot data) {
                CreateCinderVolumeSnapshotRollbackAsync async = new CreateCinderVolumeSnapshotRollbackAsync();
                async.setSnapshotRefId(data.getId());
                async.setSnapshotId(snapshotId);
                async.start();
            }
        }).then(new NoRollbackFlow<VolumeSnapshot>() {
            @Override
            public void run(SimpleFlowChain chain, VolumeSnapshot data) {

                volumeDao.updateVolumeStatus(volume.getId(), VolumeStatus.CREATING_BACKUP.value());

                Snapshot dbSnap = new Snapshot();
                dbSnap.setId(snapshotId);
                dbSnap.setDisplayName(data.getName());
                dbSnap.setVolumeSize(data.getSize());
                dbSnap.setVolumeId(volume.getId());
                dbSnap.setDisplayDescription(data.getDescription());
                dbSnap.setCreatedAt(data.getCreated());
                dbSnap.setUserId(currentUser.getId());
                dbSnap.setTenantId(currentUser.getDefaultTenant());
                dbSnap.setStatus(SnapshotStatus.PROGRESSING.value());
                dbSnap.setStorageType(volume.getStorageType());
                dbSnap.setPoolName(volume.getPoolName());
                dbSnap.setProvider(volume.getProvider());
                dbSnap.setProviderRefId(data.getId());
                snapshotDao.save(dbSnap);

                chain.setResult(data.getId());
                chain.next();
            }

        }).done(new FlowDoneHandler<VolumeSnapshot>() {
            @Override
            public void handle(VolumeSnapshot data) {
                CheckCinderCreateVolumeSnapshotAsync async = new CheckCinderCreateVolumeSnapshotAsync();
                async.setVolumeId(volume.getId());
                async.setSnapshotId(snapshotId);
                async.setSnapshotRefId(data.getId());
                async.setTaskId(taskId);
                async.start();

            }
        }).start();

        if (chain.getErrorCode() != null) {
            throw new GCloudException(chain.getErrorCode());
        }
    }

    @Override
    public void updateSnapshot(String snapshotRefId, String name, String description) throws GCloudException {
        this.proxy.updateSnapshot(snapshotRefId, name, description);
    }

    @Override
    public void deleteSnapshot(StoragePool pool, Volume volume, Snapshot snapshot, String taskId) throws GCloudException {
        this.proxy.deleteSnapshot(snapshot.getProviderRefId());

        CheckCinderDeleteVolumeSnapshotAsync async = new CheckCinderDeleteVolumeSnapshotAsync();
        async.setSnapshotId(snapshot.getId());
        async.setSnapshotRefId(snapshot.getProviderRefId());
        async.setTaskId(taskId);
        async.start();
    }

    @Override
    public void resetSnapshot(StoragePool pool, Volume volume, Snapshot snapshot, String diskId, String taskId) throws GCloudException {
        SimpleFlowChain<VolumeSnapshot, String> chain = new SimpleFlowChain<>("revert volume snapshot");
        chain.then(new Flow<VolumeSnapshot>() {

            @Override
            public void run(SimpleFlowChain chain, VolumeSnapshot data) {
                proxy.resetVolumeSnapshot(volume.getProviderRefId(), snapshot.getProviderRefId());
                chain.next();
            }

            @Override
            public void rollback(SimpleFlowChain chain, VolumeSnapshot data) {

            }

        }).then(new NoRollbackFlow<VolumeSnapshot>() {
            @Override
            public void run(SimpleFlowChain chain, VolumeSnapshot data) {

                volumeDao.updateVolumeStatus(volume.getId(), VolumeStatus.RESTORING_BACKUP.value());
                snapshotDao.updateStatus(snapshot.getId(), SnapshotStatus.RESTORING.value());

                chain.next();
            }

        }).done(new FlowDoneHandler<VolumeSnapshot>() {
            @Override
            public void handle(VolumeSnapshot data) {
                CheckCinderCreateVolumeSnapshotAsync async = new CheckCinderCreateVolumeSnapshotAsync();
                async.setVolumeId(volume.getId());
                async.setSnapshotId(snapshot.getId());
                async.setSnapshotRefId(snapshot.getProviderRefId());
                async.setTaskId(taskId);
                async.start();

            }
        }).start();

        if (chain.getErrorCode() != null) {
            throw new GCloudException(chain.getErrorCode());
        }
    }

}
