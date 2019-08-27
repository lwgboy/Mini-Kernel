package com.gcloud.controller.storage.async.snapshot;

import com.gcloud.controller.provider.CinderProviderProxy;
import com.gcloud.controller.storage.dao.SnapshotDao;
import com.gcloud.controller.storage.model.CheckStatusResult;
import com.gcloud.controller.storage.util.VolumeUtil;
import com.gcloud.core.async.AsyncBase;
import com.gcloud.core.async.AsyncResult;
import com.gcloud.core.async.AsyncStatus;
import com.gcloud.core.service.SpringUtil;
import lombok.extern.slf4j.Slf4j;
import org.openstack4j.model.storage.block.Volume;
import org.openstack4j.model.storage.block.VolumeSnapshot;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by yaowj on 2018/11/5.
 */
@Slf4j
public class CreateCinderVolumeSnapshotRollbackAsync extends AsyncBase {

    private String snapshotId;
    private String snapshotRefId;

    @Override
    public AsyncResult execute() {

        AsyncResult result = new AsyncResult();
        AsyncStatus asyncStatus = null;

        List<Volume.Status> succStatus = new ArrayList<>();
        succStatus.add(Volume.Status.AVAILABLE);

        List<Volume.Status> failStatus = new ArrayList<>();
        failStatus.add(Volume.Status.ERROR);

        CheckStatusResult<VolumeSnapshot> checkResult = VolumeUtil.checkSnapshotState(snapshotRefId, succStatus, failStatus);
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
            SnapshotDao snapshotDao = SpringUtil.getBean(SnapshotDao.class);
            snapshotDao.deleteById(snapshotId);
        }catch (Exception ex){
            log.error("回滚创建volume快照删除数据库失败", ex);
        }

        try{
            CinderProviderProxy proxy = SpringUtil.getBean(CinderProviderProxy.class);
            proxy.deleteSnapshot(snapshotRefId);
        }catch (Exception ex){
            log.error("回滚创建volume快照删除cinder volume 失败", ex);
        }


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
