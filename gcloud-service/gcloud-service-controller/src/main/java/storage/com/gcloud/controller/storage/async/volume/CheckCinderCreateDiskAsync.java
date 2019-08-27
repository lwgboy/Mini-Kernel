package com.gcloud.controller.storage.async.volume;

import com.gcloud.controller.ResourceStates;
import com.gcloud.controller.async.LogAsync;
import com.gcloud.controller.storage.dao.VolumeDao;
import com.gcloud.controller.storage.model.CheckStatusResult;
import com.gcloud.controller.storage.model.CinderVolumeCheckResult;
import com.gcloud.controller.storage.util.VolumeUtil;
import com.gcloud.core.async.AsyncBase;
import com.gcloud.core.async.AsyncResult;
import com.gcloud.core.async.AsyncStatus;
import com.gcloud.core.service.SpringUtil;
import com.gcloud.header.enums.ProviderType;
import com.gcloud.header.enums.ResourceType;

import org.openstack4j.model.network.State;
import org.openstack4j.model.storage.block.Volume;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by yaowj on 2018/9/26.
 */
public class CheckCinderCreateDiskAsync extends LogAsync {

    private String volumeId;
    private String volumeRefId;

    private org.openstack4j.model.storage.block.Volume volume = null;

    @Override
    public long timeout() {
        return 60 * 60 * 1000L;
    }

    @Override
    public AsyncResult execute() {

        AsyncStatus asyncStatus = null;
        volume = null;

        List<org.openstack4j.model.storage.block.Volume.Status> succStatus = new ArrayList<>();
        succStatus.add(org.openstack4j.model.storage.block.Volume.Status.AVAILABLE);

        List<org.openstack4j.model.storage.block.Volume.Status> failStatus = new ArrayList<>();
        failStatus.add(org.openstack4j.model.storage.block.Volume.Status.ERROR);

        CheckStatusResult<Volume> checkResult = VolumeUtil.checkVolumeState(volumeRefId, succStatus, failStatus);
        Boolean success = checkResult.getSuccess();
        if(success == null){
            asyncStatus = AsyncStatus.RUNNING;
        }else if(success){
            asyncStatus = AsyncStatus.SUCCEED;
        }else{
            asyncStatus = AsyncStatus.FAILED;
        }

        volume = checkResult.getEntity();

        return new AsyncResult(asyncStatus);
    }

    public void handle(boolean ignoreNull){

        VolumeDao volumeDao = SpringUtil.getBean(VolumeDao.class);
        if(volume == null){
            if(!ignoreNull){
                volumeDao.deleteById(volumeId);
            }
        }else{

            org.openstack4j.model.storage.block.Volume.Status status = volume.getStatus();
            String state = status == null ? null : status.value();
            volumeDao.updateVolumeStatus(volumeId, ResourceStates.status(ResourceType.VOLUME, ProviderType.CINDER, state));
        }

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

    public String getVolumeRefId() {
        return volumeRefId;
    }

    public void setVolumeRefId(String volumeRefId) {
        this.volumeRefId = volumeRefId;
    }

}
