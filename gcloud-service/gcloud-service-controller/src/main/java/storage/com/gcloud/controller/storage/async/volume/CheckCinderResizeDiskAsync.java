package com.gcloud.controller.storage.async.volume;

import com.gcloud.controller.ResourceStates;
import com.gcloud.controller.async.LogAsync;
import com.gcloud.controller.provider.CinderProviderProxy;
import com.gcloud.controller.storage.dao.VolumeDao;
import com.gcloud.core.async.AsyncBase;
import com.gcloud.core.async.AsyncResult;
import com.gcloud.core.async.AsyncStatus;
import com.gcloud.core.service.SpringUtil;
import com.gcloud.header.enums.ProviderType;
import com.gcloud.header.enums.ResourceType;

import org.openstack4j.api.OSClient;
import org.openstack4j.model.identity.v3.Token;
import com.gcloud.controller.storage.entity.Volume;
import org.openstack4j.openstack.OSFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by yaowj on 2018/9/28.
 */
public class CheckCinderResizeDiskAsync extends LogAsync {

    private String volumeRefId;
    private String volumeId;
    private Integer newSize;
    private org.openstack4j.model.storage.block.Volume volume = null;

    @Override
    public long timeout() {
        return 60 * 60 * 1000L;
    }

    @Override
    public AsyncResult execute() {

        AsyncStatus asyncStatus = null;
        volume = null;

        CinderProviderProxy proxy = SpringUtil.getBean(CinderProviderProxy.class);
        volume = proxy.getVolume(volumeRefId);

        org.openstack4j.model.storage.block.Volume.Status status = volume.getStatus();
        int size = volume.getSize();
        if((org.openstack4j.model.storage.block.Volume.Status.IN_USE == status || org.openstack4j.model.storage.block.Volume.Status.AVAILABLE == status)){

            if(size == newSize){
                asyncStatus = AsyncStatus.SUCCEED;
            }else{
                asyncStatus = AsyncStatus.FAILED;
            }
        }else if(org.openstack4j.model.storage.block.Volume.Status.ERROR == status){
            asyncStatus = AsyncStatus.FAILED;
        }else{
            asyncStatus = AsyncStatus.RUNNING;
        }

        return new AsyncResult(asyncStatus);
    }

    @Override
    public void defaultHandler(){
        VolumeDao volumeDao = SpringUtil.getBean(VolumeDao.class);
        if(volume != null){
            Volume updateVol = new Volume();
            List<String> updateField = new ArrayList<>();
            updateVol.setId(volumeId);

            org.openstack4j.model.storage.block.Volume.Status status = volume.getStatus();
            String state = status == null ? null : status.value();

            updateField.add(updateVol.updateStatus(ResourceStates.status(ResourceType.VOLUME, ProviderType.CINDER, state)));
            updateField.add(updateVol.updateSize(volume.getSize()));

            volumeDao.update(updateVol, updateField);
        }
    }

    public String getVolumeRefId() {
        return volumeRefId;
    }

    public void setVolumeRefId(String volumeRefId) {
        this.volumeRefId = volumeRefId;
    }

    public String getVolumeId() {
        return volumeId;
    }

    public void setVolumeId(String volumeId) {
        this.volumeId = volumeId;
    }

    public Integer getNewSize() {
        return newSize;
    }

    public void setNewSize(Integer newSize) {
        this.newSize = newSize;
    }
}
