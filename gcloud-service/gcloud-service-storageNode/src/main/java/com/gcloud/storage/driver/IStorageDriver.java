
package com.gcloud.storage.driver;

import java.util.List;

import com.gcloud.core.exception.GCloudException;
import com.gcloud.header.ResourceProviderVo;
import com.gcloud.header.storage.enums.StoragePoolDriver;
import com.gcloud.storage.NodeStoragePoolVo;

public interface IStorageDriver {

    StoragePoolDriver driver();

    void createDisk(NodeStoragePoolVo pool, String volumeId, Integer size, String imageId) throws GCloudException;

    void deleteDisk(NodeStoragePoolVo pool, String volumeId, List<ResourceProviderVo> snapshots) throws GCloudException;

    void resizeDisk(NodeStoragePoolVo pool, String volumeId, Integer oldSize, Integer newSize) throws GCloudException;

    void createSnapshot(NodeStoragePoolVo pool, String volumeRefId, String snapshotId, String snapshotRefId) throws GCloudException;

    void deleteSnapshot(NodeStoragePoolVo pool, String volumeRefId, String snapshotId, String snapshotRefId) throws GCloudException;

    List<String> resetSnapshot(NodeStoragePoolVo pool, String volumeRefId, String snapshotId, String snapshotRefId, Integer size) throws GCloudException;

}
