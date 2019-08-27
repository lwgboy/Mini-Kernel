package com.gcloud.controller.storage.async.snapshot;

import com.gcloud.controller.ResourceStates;
import com.gcloud.controller.async.LogAsync;
import com.gcloud.controller.provider.CinderProviderProxy;
import com.gcloud.controller.storage.dao.SnapshotDao;
import com.gcloud.controller.storage.dao.VolumeDao;
import com.gcloud.core.async.AsyncResult;
import com.gcloud.core.async.AsyncStatus;
import com.gcloud.core.service.SpringUtil;
import com.gcloud.header.enums.ProviderType;
import com.gcloud.header.enums.ResourceType;

import org.openstack4j.model.storage.block.Volume;
import org.openstack4j.model.storage.block.VolumeSnapshot;

/**
 * Created by yaowj on 2018/11/7.
 */
public class CheckCinderDeleteVolumeSnapshotAsync extends LogAsync {

    private String snapshotId;
    private String snapshotRefId;
    private VolumeSnapshot snapshot;

    @Override
    public long timeout() {
        return 60 * 60 * 1000L;
    }

    @Override
    public AsyncResult execute() {


        AsyncStatus asyncStatus = null;

        CinderProviderProxy proxy = SpringUtil.getBean(CinderProviderProxy.class);
        snapshot = proxy.getVolumeSnapshot(snapshotRefId);
        if(snapshot == null){
            asyncStatus = AsyncStatus.SUCCEED;
        }else{
            org.openstack4j.model.storage.block.Volume.Status status = snapshot.getStatus();
            if(org.openstack4j.model.storage.block.Volume.Status.ERROR_DELETING == status){
                asyncStatus = AsyncStatus.FAILED;
            }else{
                asyncStatus = AsyncStatus.RUNNING;
            }
        }

        return new AsyncResult(asyncStatus);
    }

    @Override
    public void defaultHandler(){
        SnapshotDao snapshotDao = SpringUtil.getBean(SnapshotDao.class);
        if(snapshot != null){
            org.openstack4j.model.storage.block.Volume.Status status = snapshot.getStatus();
            snapshotDao.updateStatus(snapshotId, ResourceStates.status(ResourceType.SNAPSHOT, ProviderType.CINDER, status.value()));
        }
    }

    @Override
    public void successHandler() {
        SnapshotDao snapshotDao = SpringUtil.getBean(SnapshotDao.class);
        snapshotDao.deleteById(snapshotId);
    }


    public String getSnapshotId() {
        return snapshotId;
    }

    public void setSnapshotId(String snapshotId) {
        this.snapshotId = snapshotId;
    }

    public String getSnapshotRefId() {
        return snapshotRefId;
    }

    public void setSnapshotRefId(String snapshotRefId) {
        this.snapshotRefId = snapshotRefId;
    }
}
