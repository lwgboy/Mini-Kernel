
package com.gcloud.controller.storage.provider.impl;

import com.gcloud.common.util.StringUtils;
import com.gcloud.controller.ResourceStates;
import com.gcloud.controller.provider.CinderProviderProxy;
import com.gcloud.controller.storage.async.volume.CheckCinderCreateDiskAsync;
import com.gcloud.controller.storage.async.volume.CheckCinderDeleteDiskAsync;
import com.gcloud.controller.storage.async.volume.CheckCinderResizeDiskAsync;
import com.gcloud.controller.storage.async.volume.CreateCinderVolumeRollbackAsync;
import com.gcloud.controller.storage.dao.VolumeDao;
import com.gcloud.controller.storage.entity.Snapshot;
import com.gcloud.controller.storage.entity.StoragePool;
import com.gcloud.controller.storage.entity.Volume;
import com.gcloud.controller.storage.entity.VolumeAttachment;
import com.gcloud.controller.storage.model.CreateDiskResponse;
import com.gcloud.controller.storage.model.CreateVolumeParams;
import com.gcloud.controller.storage.provider.IVolumeProvider;
import com.gcloud.controller.storage.util.VolumeUtil;
import com.gcloud.core.exception.GCloudException;
import com.gcloud.core.simpleflow.Flow;
import com.gcloud.core.simpleflow.FlowDoneHandler;
import com.gcloud.core.simpleflow.NoRollbackFlow;
import com.gcloud.core.simpleflow.SimpleFlowChain;
import com.gcloud.header.api.model.CurrentUser;
import com.gcloud.header.enums.ProviderType;
import com.gcloud.header.enums.ResourceType;
import com.gcloud.header.log.enums.LogType;
import com.gcloud.header.storage.StorageErrorCodes;
import com.gcloud.header.storage.enums.VolumeStatus;
import org.openstack4j.api.Builders;
import org.openstack4j.model.storage.block.builder.VolumeBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Component
public class CinderVolumeProvider implements IVolumeProvider {

    @Autowired
    private CinderProviderProxy proxy;

    @Autowired
    private VolumeDao volumeDao;

    @Override
    public ResourceType resourceType() {
        return ResourceType.VOLUME;
    }

    @Override
    public ProviderType providerType() {
        return ProviderType.CINDER;
    }

    @Override
    public List<Volume> getVolumeList(Map<String, String> filterParams) throws GCloudException {
        // TODO: 加入updateat时间过滤，获取上个同步周期之后更改过的卷
        List<Volume> volList = new ArrayList<>();
        List<org.openstack4j.model.storage.block.Volume> cinderList = proxy.getVolumeList(filterParams);
        for (org.openstack4j.model.storage.block.Volume v : cinderList) {
            Volume vol = new Volume();
            vol.setBootable(v.bootable());
            vol.setCategory(v.getVolumeType());  // storage pool
            vol.setDescription(v.getDescription());
            vol.setDisplayName(v.getDisplayName());
            vol.setSize(v.getSize());
//            vol.setStatus(v.getStatus().toString());  // TODO: translate openstack status to gcloud status string.
            vol.setStatus(ResourceStates.status(ResourceType.VOLUME, ProviderType.CINDER, v.getStatus().value()));
            vol.setUpdatedAt(v.getUpdated());
            vol.setCreatedAt(v.getCreated());
            vol.setProvider(providerType().getValue());
            // can not set vol.Id, because it's not record in provider side.
            vol.setProviderRefId(v.getId());

            volList.add(vol);
        }

        return volList;
    }

    @Override
    public List<Snapshot> getSnapshotList(Map<String, String> filterParams) throws GCloudException {
        List<Snapshot> snapList = new ArrayList<>();
        List<org.openstack4j.model.storage.block.VolumeSnapshot> cinderList = proxy.getSnapshotList(filterParams);
        for (org.openstack4j.model.storage.block.VolumeSnapshot s : cinderList) {
            Snapshot snap = new Snapshot();
            snap.setDisplayDescription(s.getDisplayDescription());
            snap.setDisplayName(s.getDisplayName());
            snap.setUpdatedAt(s.getUpdated());
//            snap.setStatus(s.getStatus().toString()); // TODO: translate status name.
            snap.setStatus(ResourceStates.status(ResourceType.SNAPSHOT, ProviderType.CINDER, s.getStatus().value()));
            snap.setVolumeSize(s.getSize()); // ???
            snap.setCreatedAt(s.getCreated());
            snap.setProvider(providerType().getValue());
            snap.setProviderRefId(s.getId());

            snapList.add(snap);
        }

        return snapList;
    }

    @Override
    public CreateDiskResponse createVolume(String volumeId, CreateVolumeParams params, CurrentUser currentUser) throws GCloudException {
        SimpleFlowChain<org.openstack4j.model.storage.block.Volume, String> chain = new SimpleFlowChain<>("create volume");

        chain.name("create volume").then(new Flow<org.openstack4j.model.storage.block.Volume>("create volume in cinder") {

            @Override
            public void run(SimpleFlowChain chain, org.openstack4j.model.storage.block.Volume data) {
                VolumeBuilder builder = Builders.volume().size(params.getSize());
                if (StringUtils.isNotBlank(params.getDiskName())) {
                    builder.name(params.getDiskName());
                }

                if (StringUtils.isNotBlank(params.getDescription())) {
                    builder.description(params.getDescription());
                }

                if (StringUtils.isNotBlank(params.getSnapshotId())) {
                    builder.snapshot(params.getSnapshotId());
                }

                if (StringUtils.isNotBlank(params.getImageProviderRefId())) {
                    builder.imageRef(params.getImageProviderRefId());
                }

                if (params.isBootable()) {
                    builder.bootable(params.isBootable());
                }

                if (params.getDiskType() == null) {
                    throw new GCloudException("0060103::磁盘种类不能为空");
                }

                if (params.getPool() != null && StringUtils.isNotBlank(params.getPool().getId())) {
                    builder.volumeType(params.getPool().getProviderRefId());
                }

                org.openstack4j.model.storage.block.Volume cinderVol = proxy.createVolume(builder.build());
                chain.data(cinderVol);
                chain.next();
            }

            @Override
            public void rollback(SimpleFlowChain chain, org.openstack4j.model.storage.block.Volume vol) {
                CreateCinderVolumeRollbackAsync rollbackAsync = new CreateCinderVolumeRollbackAsync();
                rollbackAsync.setVolumeId(volumeId);
                rollbackAsync.setVolumeRefId(vol.getId());
                rollbackAsync.start();
            }
        }).then(new NoRollbackFlow<org.openstack4j.model.storage.block.Volume>("save volume to db") {

            @Override
            public void run(SimpleFlowChain chain, org.openstack4j.model.storage.block.Volume cinderVol) {
                // TODO Auto-generated method stub
                Volume volume = new Volume();
                volume.setId(volumeId);
                volume.setDisplayName(params.getDiskName());
                volume.setStatus(VolumeStatus.CREATING.value());
                volume.setSize(cinderVol.getSize());
                volume.setDiskType(params.getDiskType().getValue());
                volume.setCreatedAt(cinderVol.getCreated());
                volume.setDescription(cinderVol.getDescription());
                volume.setSnapshotId(cinderVol.getSnapshotId());
                volume.setUserId(currentUser.getId());
                volume.setTenantId(currentUser.getDefaultTenant());
                volume.setCategory(params.getDiskCategory());
                volume.setStorageType(params.getPool().getStorageType());
                volume.setPoolId(params.getPool().getId());
                volume.setPoolName(params.getPool().getPoolName());
                volume.setProvider(params.getPool().getProvider());
                volume.setProviderRefId(cinderVol.getId());
                volume.setZoneId(params.getZoneId());
                volume.setImageRef(params.getImageRef());

                volumeDao.save(volume);
                chain.setResult(volume.getId());
                chain.next();
            }

        }).done(new FlowDoneHandler<org.openstack4j.model.storage.block.Volume>() {
            @Override
            public void handle(org.openstack4j.model.storage.block.Volume cinderVol) {
                // TODO Auto-generated method stub
                CheckCinderCreateDiskAsync checkAsync = new CheckCinderCreateDiskAsync();
                checkAsync.setVolumeId(volumeId);
                checkAsync.setVolumeRefId(cinderVol.getId());
                checkAsync.setTaskId(params.getTaskId());
                checkAsync.start();
            }

        }).start();

        if (chain.getErrorCode() != null) {
            throw new GCloudException(chain.getErrorCode());
        }

        CreateDiskResponse response = new CreateDiskResponse();
        response.setLogType(LogType.CHECK_ASYNC);

        response.setDiskId(volumeId);

        return response;
    }

    @Override
    public void deleteVolume(StoragePool pool, Volume volume, String taskId) throws GCloudException {
        org.openstack4j.model.storage.block.Volume cinderVolume = this.proxy.getVolume(volume.getProviderRefId());
        //没有删除
        if(cinderVolume != null){
            this.proxy.deleteVolume(volume.getProviderRefId());
        }

        CheckCinderDeleteDiskAsync checkAsync = new CheckCinderDeleteDiskAsync();
        checkAsync.setVolumeId(volume.getId());
        checkAsync.setVolumeRefId(volume.getProviderRefId());
        checkAsync.setTaskId(taskId);
        checkAsync.start();
    }

    @Override
    public void updateVolume(String volumeRefId, String name, String description) throws GCloudException {
        this.proxy.updateVolume(volumeRefId, name, description);
    }

    @Override
    public void reserveVolume(String volumeRefId) throws GCloudException {
        this.proxy.reserveVolume(volumeRefId);
    }

    @Override
    public void unreserveVolume(String volumeRefId) throws GCloudException {
        this.proxy.unreserveVolume(volumeRefId);
    }

    @Override
    public String attachVolume(Volume volume, String gcAttachmentId, String instanceId, String mountPoint, String hostname, String taskId) throws GCloudException {
        this.proxy.attachVolume(volume.getProviderRefId(), instanceId, mountPoint, hostname);
        org.openstack4j.model.storage.block.Volume opvolume = proxy.getVolume(volume.getProviderRefId());
        String attachmentId = VolumeUtil.getVolumeAttachmentId(opvolume, instanceId, mountPoint, hostname);
        if (StringUtils.isBlank(attachmentId)) {
            throw new GCloudException(StorageErrorCodes.FAILED_TO_ATTACH_VOLUME);
        }
        return attachmentId;
    }

    @Override
    public void beginDetachingVolume(Volume volume) throws GCloudException {
        this.proxy.beginDetachingVolume(volume.getProviderRefId());
    }

    @Override
    public void rollDetachingVolume(Volume volume) throws GCloudException {
        this.proxy.rollDetachingVolume(volume.getProviderRefId());
    }

    @Override
    public void detachVolume(Volume volume, VolumeAttachment attachment) throws GCloudException {
        this.proxy.detachVolume(volume.getProviderRefId(), attachment.getProviderRefId());
        org.openstack4j.model.storage.block.Volume opvolume = proxy.getVolume(volume.getProviderRefId());
        String attachmentId = VolumeUtil.getVolumeAttachmentId(opvolume, attachment.getInstanceUuid(), attachment.getMountpoint(), attachment.getAttachedHost());
        if (StringUtils.isNotBlank(attachmentId)) {
            throw new GCloudException(StorageErrorCodes.FAILED_TO_DETACH_VOLUME);
        }
    }

    @Override
    public void resizeVolume(StoragePool pool, Volume volume, int newSize, String taskId) throws GCloudException {
        this.proxy.resizeVolume(volume.getProviderRefId(), newSize);

        CheckCinderResizeDiskAsync checkAsync = new CheckCinderResizeDiskAsync();
        checkAsync.setNewSize(newSize);
        checkAsync.setVolumeId(volume.getId());
        checkAsync.setTaskId(taskId);
        checkAsync.setVolumeRefId(volume.getProviderRefId());
        checkAsync.start();
    }

}
