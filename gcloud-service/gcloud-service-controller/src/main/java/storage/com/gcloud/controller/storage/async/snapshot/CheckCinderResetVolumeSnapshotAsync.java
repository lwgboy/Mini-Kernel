package com.gcloud.controller.storage.async.snapshot;

import com.gcloud.controller.ResourceStates;
import com.gcloud.controller.async.LogAsync;
import com.gcloud.controller.storage.dao.SnapshotDao;
import com.gcloud.controller.storage.dao.VolumeDao;
import com.gcloud.controller.storage.model.CheckStatusResult;
import com.gcloud.controller.storage.util.VolumeUtil;
import com.gcloud.core.async.AsyncResult;
import com.gcloud.core.async.AsyncStatus;
import com.gcloud.core.service.SpringUtil;
import com.gcloud.header.enums.ProviderType;
import com.gcloud.header.enums.ResourceType;
import com.gcloud.header.storage.enums.VolumeStatus;

import org.openstack4j.model.storage.block.Volume;
import org.openstack4j.model.storage.block.VolumeSnapshot;

import java.util.ArrayList;
import java.util.List;

public class CheckCinderResetVolumeSnapshotAsync extends LogAsync {

    private String volumeId;
    private String snapshotId;
    private String snapshotRefId;
    private VolumeSnapshot volumeSnapshot;

    @Override
    public long timeout() {
        return 60 * 60 * 1000L;
    }

    @Override
    public AsyncResult execute() {


        AsyncStatus asyncStatus = null;

        List<Volume.Status> succStatus = new ArrayList<>();
        succStatus.add(org.openstack4j.model.storage.block.Volume.Status.AVAILABLE);

        List<org.openstack4j.model.storage.block.Volume.Status> failStatus = new ArrayList<>();
        failStatus.add(org.openstack4j.model.storage.block.Volume.Status.ERROR);

        CheckStatusResult<VolumeSnapshot> checkResult = VolumeUtil.checkSnapshotState(snapshotRefId, succStatus, failStatus);
        Boolean success = checkResult.getSuccess();
        if(success == null){
            asyncStatus = AsyncStatus.RUNNING;
        }else if(success){
            asyncStatus = AsyncStatus.SUCCEED;
        }else{
            asyncStatus = AsyncStatus.FAILED;
        }

        volumeSnapshot = checkResult.getEntity();

        return new AsyncResult(asyncStatus);
    }

    public void handle(boolean ignoreNull){

        SnapshotDao snapshotDao = SpringUtil.getBean(SnapshotDao.class);
        VolumeDao volumeDao = SpringUtil.getBean(VolumeDao.class);
        if(volumeSnapshot == null){
            if(!ignoreNull){
                snapshotDao.deleteById(snapshotId);
            }
        }else{

            org.openstack4j.model.storage.block.Volume.Status status = volumeSnapshot.getStatus();
            String state = status == null ? null : status.value();
            snapshotDao.updateStatus(snapshotId, ResourceStates.status(ResourceType.SNAPSHOT, ProviderType.CINDER, state));
        }
        volumeDao.updateVolumeStatus(this.volumeId, VolumeStatus.AVAILABLE.value());
    }

    @Override
    public void successHandler() {
        handle(false);
    }

    @Override
    public void failHandler() {
        handle(false);
    }

    @Override
    public void timeoutHandler() {
        handle(true);
    }

    @Override
    public void exceptionHandler() {
        handle(true);
    }

    public String getVolumeId() {
        return volumeId;
    }

    public void setVolumeId(String volumeId) {
        this.volumeId = volumeId;
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
