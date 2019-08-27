package com.gcloud.controller.storage.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.gcloud.common.util.StringUtils;
import com.gcloud.controller.ResourceProviders;
import com.gcloud.controller.storage.dao.SnapshotDao;
import com.gcloud.controller.storage.dao.StoragePoolDao;
import com.gcloud.controller.storage.dao.VolumeDao;
import com.gcloud.controller.storage.entity.Snapshot;
import com.gcloud.controller.storage.entity.StoragePool;
import com.gcloud.controller.storage.entity.Volume;
import com.gcloud.controller.storage.model.DescribeSnapshotsParams;
import com.gcloud.controller.storage.provider.ISnapshotProvider;
import com.gcloud.controller.storage.service.ISnapshotService;
import com.gcloud.core.cache.container.CacheContainer;
import com.gcloud.core.cache.enums.CacheType;
import com.gcloud.core.exception.GCloudException;
import com.gcloud.framework.db.PageResult;
import com.gcloud.header.api.model.CurrentUser;
import com.gcloud.header.enums.ResourceType;
import com.gcloud.header.storage.StorageErrorCodes;
import com.gcloud.header.storage.enums.VolumeStatus;
import com.gcloud.header.storage.model.SnapshotType;

import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by yaowj on 2018/12/7.
 */
@Component
@Transactional(propagation = Propagation.REQUIRED)
@Slf4j
public class SnapshotServiceImpl implements ISnapshotService {

    @Autowired
    private StoragePoolDao poolDao;

    @Autowired
    private VolumeDao volumeDao;

    @Autowired
    private SnapshotDao snapshotDao;

    @Override
    public PageResult<SnapshotType> describeSnapshots(DescribeSnapshotsParams params, CurrentUser currentUser) {

        if (params == null) {
            params = new DescribeSnapshotsParams();
        }

        String snapshotJsonIds = params.getSnapshotIds();
        if (snapshotJsonIds != null) {
            List<String> idList = JSONObject.parseArray(snapshotJsonIds, String.class);
            if (idList == null) {
                throw new GCloudException(StorageErrorCodes.INPUT_SNAPSHOT_ID_ERROR);
            }
            if (idList.size() > 100) {
                throw new GCloudException(StorageErrorCodes.INPUT_SNAPSHOT_ID_ERROR);
            }
            params.setSnapshotIdList(idList);
        }

        return snapshotDao.describeSnapshots(params, SnapshotType.class, currentUser);
    }

    @Override
    public String createSnapshot(String diskId, String name, String description, CurrentUser currentUser, String taskId) throws GCloudException {
        Volume volume = volumeDao.checkAndGet(diskId);
        StoragePool pool = poolDao.checkAndGet(volume.getPoolId());
        String snapshotId = StringUtils.genUuid();
        this.getProvider(volume.getProvider()).createSnapshot(pool, volume, snapshotId, name, description, currentUser, taskId);
        return snapshotId;
    }

    @Override
    public void handleCreateSnapshotSuccess(String snapshotId) {
        this.snapshotDao.updateStatus(snapshotId, VolumeStatus.AVAILABLE.value());
        log.info("创建快照成功：{}", snapshotId);
    }

    @Override
    public void handleCreateSnapshotFailed(String errorCode, String snapshotId) {
        this.snapshotDao.deleteById(snapshotId);
        log.info("创建快照失败：{} -> {}", snapshotId, errorCode);
    }

    @Override
    public void updateSnapshot(String id, String name, String description) throws GCloudException {
        Snapshot snapshot = snapshotDao.checkAndGet(id);

        List<String> updateFields = new ArrayList<>();
        Snapshot updateSnap = new Snapshot();
        updateSnap.setId(id);
        updateFields.add(updateSnap.updateDisplayName(name));

        snapshotDao.update(updateSnap, updateFields);
        CacheContainer.getInstance().put(CacheType.SNAPSHOT_NAME, id, name);
        this.getProvider(snapshot.getProvider()).updateSnapshot(snapshot.getProviderRefId(), name, description);
    }

    @Override
    public void deleteSnapshot(String id, String taskId) throws GCloudException {
        Snapshot snapshot = snapshotDao.checkAndGet(id);
        Volume volume = volumeDao.checkAndGet(snapshot.getVolumeId());
        StoragePool pool = poolDao.checkAndGet(volume.getPoolId());
        snapshotDao.updateStatus(id, VolumeStatus.DELETING.value());
        this.getProvider(snapshot.getProvider()).deleteSnapshot(pool, volume, snapshot, taskId);
    }

    @Override
    public void handleDeleteSnapshotSuccess(String snapshotId) throws GCloudException {
        this.snapshotDao.deleteById(snapshotId);
        log.info("删除快照成功：{}", snapshotId);
    }

    @Override
    public void handleDeleteSnapshotFailed(String errorCode, String snapshotId) {
        this.snapshotDao.updateStatus(snapshotId, VolumeStatus.AVAILABLE.value());
        log.info("删除快照失败：{} -> {}", snapshotId, errorCode);
    }

    @Override
    public void resetSnapshot(String snapshotId, String diskId, String taskId) throws GCloudException {
        Snapshot snapshot = snapshotDao.checkAndGet(snapshotId);
        Volume volume = volumeDao.checkAndGet(snapshot.getVolumeId());
        StoragePool pool = poolDao.checkAndGet(volume.getPoolId());
        this.getProvider(snapshot.getProvider()).resetSnapshot(pool, volume, snapshot, diskId, taskId);
    }

    @Override
    public void handleResetSnapshotSuccess(String snapshotId, List<String> snapshotsToDelete) {
        this.snapshotDao.updateStatus(snapshotId, VolumeStatus.AVAILABLE.value());
        log.info("reset快照成功：{}", snapshotId);
        if (snapshotsToDelete != null) {
            for (String snapshotToDelete : snapshotsToDelete) {
                this.snapshotDao.deleteById(snapshotToDelete);
            }
        }
    }

    @Override
    public void handleResetSnapshotFailed(String errorCode, String snapshotId) {
        this.snapshotDao.updateStatus(snapshotId, VolumeStatus.AVAILABLE.value());
        log.info("reset快照失败：{} -> {}", snapshotId, errorCode);
    }

    private ISnapshotProvider getProvider(int providerType) {
        ISnapshotProvider provider = ResourceProviders.checkAndGet(ResourceType.SNAPSHOT, providerType);
        return provider;
    }

	@Override
	public List<Snapshot> findSnapshotByVolume(String volumeId) {
		return snapshotDao.findByVolume(volumeId);
	}

}
