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
 * Created by yaowj on 2018/9/28.
 */
public class CheckCinderAttachDiskAsync extends LogAsync {

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

        List<Volume.Status> succStatus = new ArrayList<>();
        succStatus.add(Volume.Status.IN_USE);

        List<org.openstack4j.model.storage.block.Volume.Status> failStatus = new ArrayList<>();
        failStatus.add(Volume.Status.ERROR);
        failStatus.add(Volume.Status.AVAILABLE);

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

    @Override
    public void defaultHandler(){
        VolumeDao volumeDao = SpringUtil.getBean(VolumeDao.class);
        if(volume != null){
            org.openstack4j.model.storage.block.Volume.Status status = volume.getStatus();
            volumeDao.updateVolumeStatus(volumeId, ResourceStates.status(ResourceType.VOLUME, ProviderType.CINDER, status.value()));
        }
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
