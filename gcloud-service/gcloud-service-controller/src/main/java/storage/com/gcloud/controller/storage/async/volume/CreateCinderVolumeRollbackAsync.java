package com.gcloud.controller.storage.async.volume;

import com.gcloud.controller.provider.CinderProviderProxy;
import com.gcloud.controller.storage.dao.VolumeDao;
import com.gcloud.controller.storage.model.CheckStatusResult;
import com.gcloud.controller.storage.model.CinderVolumeCheckResult;
import com.gcloud.controller.storage.util.VolumeUtil;
import com.gcloud.core.async.AsyncBase;
import com.gcloud.core.async.AsyncResult;
import com.gcloud.core.async.AsyncStatus;
import com.gcloud.core.service.SpringUtil;
import lombok.extern.slf4j.Slf4j;
import org.openstack4j.model.storage.block.Volume;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by yaowj on 2018/11/5.
 */
@Slf4j
public class CreateCinderVolumeRollbackAsync extends AsyncBase {

    private String volumeId;
    private String volumeRefId;

    @Override
    public AsyncResult execute() {

        AsyncStatus asyncStatus = null;

        List<Volume.Status> succStatus = new ArrayList<>();
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

        return new AsyncResult(asyncStatus);
    }

    @Override
    public long timeout() {
        return 60 * 60 * 1000L;
    }

    @Override
    public void defaultHandler() {
        try{
            VolumeDao volumeDao = SpringUtil.getBean(VolumeDao.class);
            volumeDao.deleteById(volumeId);
        }catch (Exception ex){
            log.error("回滚创建volume删除数据库失败", ex);
        }

        try{
            CinderProviderProxy proxy = SpringUtil.getBean(CinderProviderProxy.class);
            proxy.deleteVolume(volumeId);
        }catch (Exception ex){
            log.error("回滚创建volume删除cinder volume 失败", ex);
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
