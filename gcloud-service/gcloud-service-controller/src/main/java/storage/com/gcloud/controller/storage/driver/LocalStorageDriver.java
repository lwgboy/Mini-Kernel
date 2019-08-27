
package com.gcloud.controller.storage.driver;

import com.gcloud.controller.storage.dao.SnapshotDao;
import com.gcloud.controller.storage.entity.Snapshot;
import com.gcloud.controller.storage.entity.StoragePool;
import com.gcloud.controller.storage.entity.Volume;
import com.gcloud.controller.storage.model.CreateDiskResponse;
import com.gcloud.core.exception.GCloudException;
import com.gcloud.core.messagebus.MessageBus;
import com.gcloud.core.util.MessageUtil;
import com.gcloud.header.ResourceProviderVo;
import com.gcloud.header.compute.enums.StorageType;
import com.gcloud.header.log.enums.LogType;
import com.gcloud.header.storage.model.StoragePoolInfo;
import com.gcloud.header.storage.msg.node.pool.NodeCreateStoragePoolMsg;
import com.gcloud.header.storage.msg.node.pool.NodeGetStoragePoolMsg;
import com.gcloud.header.storage.msg.node.pool.NodeGetStoragePoolReplyMsg;
import com.gcloud.header.storage.msg.node.volume.NodeCreateDiskMsg;
import com.gcloud.header.storage.msg.node.volume.NodeCreateSnapshotMsg;
import com.gcloud.header.storage.msg.node.volume.NodeDeleteDiskMsg;
import com.gcloud.header.storage.msg.node.volume.NodeDeleteSnapshotMsg;
import com.gcloud.header.storage.msg.node.volume.NodeResetSnapshotMsg;
import com.gcloud.header.storage.msg.node.volume.NodeResizeDiskMsg;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Slf4j
@Component
public class LocalStorageDriver implements IStorageDriver {

    @Autowired
    private MessageBus bus;

    @Autowired
    private SnapshotDao snapshotDao;

    @PostConstruct
    private void init() {

    }

    @Override
    public StorageType storageType() {
        return StorageType.LOCAL;
    }

    @Override
    public void createStoragePool(String poolId, String poolName, String hostname, String taskId) throws GCloudException {
        NodeCreateStoragePoolMsg msg = new NodeCreateStoragePoolMsg();
        msg.setStorageType(this.storageType().getValue());
        msg.setPoolId(poolId);
        msg.setPoolName(poolName);
        msg.setServiceId(MessageUtil.storageServiceId(hostname));
        msg.setTaskId(taskId);
        this.bus.send(msg);
    }

    @Override
    public void deleteStoragePool(String poolName) throws GCloudException {

    }

    @Override
    public CreateDiskResponse createVolume(String taskId, StoragePool pool, Volume volume) throws GCloudException {
        NodeCreateDiskMsg msg = new NodeCreateDiskMsg();
        msg.setStorageType(this.storageType().getValue());
        msg.setPoolName(volume.getPoolName());
        msg.setDriverName(pool.getDriver());
        msg.setVolumeId(volume.getProviderRefId());
        msg.setSize(volume.getSize());
        msg.setImageId(volume.getImageRef());
        msg.setServiceId(MessageUtil.storageServiceId(pool.getHostname()));
        msg.setTaskId(taskId);
        this.bus.send(msg);

        CreateDiskResponse response = new CreateDiskResponse();
        response.setLogType(LogType.ASYNC);
        response.setDiskId(volume.getId());
        return response;
    }

    @Override
    public void deleteVolume(String taskId, StoragePool pool, Volume volume) throws GCloudException {
        NodeDeleteDiskMsg msg = new NodeDeleteDiskMsg();
        msg.setStorageType(this.storageType().getValue());
        msg.setDriverName(pool.getDriver());
        msg.setPoolName(volume.getPoolName());
        msg.setVolumeId(volume.getProviderRefId());
        for (Snapshot snapshot : this.snapshotDao.findByVolume(volume.getId())) {
            msg.getSnapshots().add(new ResourceProviderVo(snapshot.getId(), snapshot.getProviderRefId()));
        }
        msg.setServiceId(MessageUtil.storageServiceId(pool.getHostname()));
        msg.setTaskId(taskId);
        this.bus.send(msg);
    }

    @Override
    public void resizeVolume(String taskId, StoragePool pool, Volume volume, int newSize) throws GCloudException {
        NodeResizeDiskMsg msg = new NodeResizeDiskMsg();
        msg.setStorageType(this.storageType().getValue());
        msg.setPoolName(volume.getPoolName());
        msg.setDriverName(pool.getDriver());
        msg.setVolumeId(volume.getProviderRefId());
        msg.setOldSize(volume.getSize());
        msg.setNewSize(newSize);
        msg.setServiceId(MessageUtil.storageServiceId(pool.getHostname()));
        msg.setTaskId(taskId);
        this.bus.send(msg);
    }

    @Override
    public void createSnapshot(StoragePool pool, String volumeRefId, Snapshot snapshot) throws GCloudException {
        NodeCreateSnapshotMsg msg = new NodeCreateSnapshotMsg();
        msg.setStorageType(this.storageType().getValue());
        msg.setPoolName(pool.getPoolName());
        msg.setDriverName(pool.getDriver());
        msg.setVolumeRefId(volumeRefId);
        msg.setSnapshotId(snapshot.getId());
        msg.setSnapshotRefId(snapshot.getProviderRefId());
        msg.setServiceId(MessageUtil.storageServiceId(pool.getHostname()));
        this.bus.send(msg);
    }

    @Override
    public void deleteSnapshot(StoragePool pool, String volumeRefId, Snapshot snapshot, String taskId) throws GCloudException {
        NodeDeleteSnapshotMsg msg = new NodeDeleteSnapshotMsg();
        msg.setStorageType(this.storageType().getValue());
        msg.setPoolName(pool.getPoolName());
        msg.setDriverName(pool.getDriver());
        msg.setVolumeRefId(volumeRefId);
        msg.setSnapshotId(snapshot.getId());
        msg.setSnapshotRefId(snapshot.getProviderRefId());
        msg.setTaskId(taskId);
        msg.setServiceId(MessageUtil.storageServiceId(pool.getHostname()));
        this.bus.send(msg);
    }

    @Override
    public void resetSnapshot(StoragePool pool, String volumeRefId, Snapshot snapshot, Integer size) throws GCloudException {
        NodeResetSnapshotMsg msg = new NodeResetSnapshotMsg();
        msg.setStorageType(this.storageType().getValue());
        msg.setPoolName(pool.getPoolName());
        msg.setDriverName(pool.getDriver());
        msg.setVolumeRefId(volumeRefId);
        msg.setSnapshotId(snapshot.getId());
        msg.setSnapshotRefId(snapshot.getProviderRefId());
        msg.setSize(size);
        msg.setServiceId(MessageUtil.storageServiceId(pool.getHostname()));
        this.bus.send(msg);
    }

	@Override
	public StoragePoolInfo getStoragePool(StoragePool pool) throws GCloudException {
		NodeGetStoragePoolMsg msg = new NodeGetStoragePoolMsg();
        msg.setStorageType(this.storageType().getValue());
        msg.setPoolName(pool.getPoolName());
        msg.setDriverName(pool.getDriver());
        msg.setServiceId(MessageUtil.storageServiceId(pool.getHostname()));
        NodeGetStoragePoolReplyMsg reply = this.bus.call(msg, NodeGetStoragePoolReplyMsg.class);
		return reply.getInfo();
	}

}
