
package com.gcloud.controller.storage.provider;

import com.gcloud.controller.IResourceProvider;
import com.gcloud.controller.storage.entity.Snapshot;
import com.gcloud.controller.storage.entity.StoragePool;
import com.gcloud.controller.storage.entity.Volume;
import com.gcloud.core.exception.GCloudException;
import com.gcloud.header.api.model.CurrentUser;

public interface ISnapshotProvider extends IResourceProvider {

    void createSnapshot(StoragePool pool, Volume volume, String snapshotId, String name, String description, CurrentUser currentUser, String taskId);

    void updateSnapshot(String snapshotRefId, String name, String description);

    void deleteSnapshot(StoragePool pool, Volume volume, Snapshot snapshot, String taskId);

    void resetSnapshot(StoragePool pool, Volume volume, Snapshot snapshot, String diskId, String taskId) throws GCloudException;

}
