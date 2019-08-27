
package com.gcloud.controller.storage.provider;

import com.gcloud.controller.IResourceProvider;
import com.gcloud.controller.storage.entity.Snapshot;
import com.gcloud.controller.storage.entity.StoragePool;
import com.gcloud.controller.storage.entity.Volume;
import com.gcloud.controller.storage.entity.VolumeAttachment;
import com.gcloud.controller.storage.model.CreateDiskResponse;
import com.gcloud.controller.storage.model.CreateVolumeParams;
import com.gcloud.core.exception.GCloudException;
import com.gcloud.header.api.model.CurrentUser;

import java.util.List;
import java.util.Map;

public interface IVolumeProvider extends IResourceProvider {

    CreateDiskResponse createVolume(String volumeId, CreateVolumeParams params, CurrentUser currentUser) throws GCloudException;

    void deleteVolume(StoragePool pool, Volume volume, String taskId) throws GCloudException;

    String attachVolume(Volume volume, String gcAttachmentId, String instanceId, String mountPoint, String hostname, String taskId) throws GCloudException;

    void updateVolume(String volumeRefId, String name, String description) throws GCloudException;

    void reserveVolume(String volumeRefId) throws GCloudException;

    void unreserveVolume(String volumeRefId) throws GCloudException;

    void beginDetachingVolume(Volume volume) throws GCloudException;

    void detachVolume(Volume volume, VolumeAttachment attachment) throws GCloudException;

    void rollDetachingVolume(Volume volume) throws GCloudException;

    void resizeVolume(StoragePool pool, Volume volume, int newSize, String taskId) throws GCloudException;

    List<Volume> getVolumeList(Map<String, String> filterParams) throws GCloudException;

    List<Snapshot> getSnapshotList(Map<String, String> filterParams) throws GCloudException;

}
