package com.gcloud.controller.storage.async.volume;

import com.gcloud.controller.ResourceStates;
import com.gcloud.controller.async.LogAsync;
import com.gcloud.controller.provider.CinderProviderProxy;
import com.gcloud.controller.storage.dao.VolumeDao;
import com.gcloud.controller.storage.service.IVolumeService;
import com.gcloud.core.async.AsyncBase;
import com.gcloud.core.async.AsyncResult;
import com.gcloud.core.async.AsyncStatus;
import com.gcloud.core.service.SpringUtil;
import com.gcloud.header.enums.ProviderType;
import com.gcloud.header.enums.ResourceType;

import org.openstack4j.api.OSClient;
import org.openstack4j.model.identity.v3.Token;
import org.openstack4j.openstack.OSFactory;

/**
 * Created by yaowj on 2018/9/28.
 */
public class CheckCinderDeleteDiskAsync extends LogAsync {

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

        CinderProviderProxy proxy = SpringUtil.getBean(CinderProviderProxy.class);
        volume = proxy.getVolume(volumeRefId);
        if(volume == null){
            asyncStatus = AsyncStatus.SUCCEED;
        }else{
            org.openstack4j.model.storage.block.Volume.Status status = volume.getStatus();
            if(org.openstack4j.model.storage.block.Volume.Status.DELETING == status){
                asyncStatus = AsyncStatus.RUNNING;
            }else{
                asyncStatus = AsyncStatus.FAILED;
            }
        }

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

    @Override
    public void successHandler() {
        IVolumeService volumeService = SpringUtil.getBean(IVolumeService.class);
        volumeService.handleDeleteVolumeSuccess(volumeId);
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
