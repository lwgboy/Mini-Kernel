package com.gcloud.controller.storage.service;

import java.util.List;

import com.gcloud.controller.storage.entity.Snapshot;
import com.gcloud.controller.storage.model.DescribeSnapshotsParams;
import com.gcloud.core.exception.GCloudException;
import com.gcloud.framework.db.PageResult;
import com.gcloud.header.api.model.CurrentUser;
import com.gcloud.header.storage.model.SnapshotType;

/**
 * Created by yaowj on 2018/11/7.
 */
public interface ISnapshotService {

    String createSnapshot(String diskId, String name, String description, CurrentUser currentUser, String taskId) throws GCloudException;

    void handleCreateSnapshotSuccess(String snapshotId);

    void handleCreateSnapshotFailed(String errorCode, String snapshotId);

    void updateSnapshot(String id, String name, String description) throws GCloudException;

    void deleteSnapshot(String id, String taskId) throws GCloudException;

    void handleDeleteSnapshotSuccess(String snapshotId);

    void handleDeleteSnapshotFailed(String errorCode, String snapshotId);

    void resetSnapshot(String snapshotId, String diskId, String taskId) throws GCloudException;

    void handleResetSnapshotSuccess(String snapshotId, List<String> snapshotsToDelete);

    void handleResetSnapshotFailed(String errorCode, String snapshotId);

    PageResult<SnapshotType> describeSnapshots(DescribeSnapshotsParams params, CurrentUser currentUser);
    
    List<Snapshot> findSnapshotByVolume(String volumeId);

}
