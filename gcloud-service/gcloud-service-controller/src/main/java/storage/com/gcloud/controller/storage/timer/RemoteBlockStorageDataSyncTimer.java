package com.gcloud.controller.storage.timer;

import com.gcloud.controller.storage.dao.SnapshotDao;
import com.gcloud.controller.storage.dao.VolumeDao;
import com.gcloud.controller.storage.entity.Snapshot;
import com.gcloud.controller.storage.entity.Volume;
import com.gcloud.controller.storage.provider.IVolumeProvider;
import com.gcloud.core.cache.container.CacheContainer;
import com.gcloud.core.cache.enums.CacheType;
import com.gcloud.core.quartz.GcloudJobBean;
import com.gcloud.core.quartz.annotation.QuartzTimer;
import com.gcloud.core.service.SpringUtil;
import com.gcloud.header.enums.ProviderType;
import lombok.extern.slf4j.Slf4j;
import org.quartz.DisallowConcurrentExecution;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.PersistJobDataAfterExecution;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
@PersistJobDataAfterExecution
@DisallowConcurrentExecution
@QuartzTimer(fixedDelay = 1800 * 1000L)  // half hour
@Slf4j
public class RemoteBlockStorageDataSyncTimer extends GcloudJobBean {

    @Autowired
    private VolumeDao volumeDao;

    @Autowired
    private SnapshotDao snapshotDao;

    // 记录上次更新时间，以便判断本次的数据是否在上次更新后又更新过，减少访问DB次数
    private static final String updateTimeKey = "BlockStorageLastUpdateTime";
    private long lastUpdateTime = 0L;

    @Override
    protected void executeInternal(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        Object time = CacheContainer.getInstance().get(CacheType.TIMER, updateTimeKey);
        if (time == null) {
            log.warn("get block storage last update time from redis got null");
        } else {
            lastUpdateTime = (long)time;
        }
        log.info("RemoteBlockStorageDataSyncTimer start. update time: " + lastUpdateTime/1000);
        for (IVolumeProvider provider : SpringUtil.getBeans(IVolumeProvider.class)) {
            log.debug("syncing with provider: " + provider.getClass().getName());
            // gcloud type resource is no need to synchronize.
            if (provider.providerType() == ProviderType.GCLOUD) continue;

            this.updateVolume(provider);
            this.updateSnapshot(provider);
        }

        lastUpdateTime = (new Date()).getTime();
        CacheContainer.getInstance().put(CacheType.TIMER, updateTimeKey, lastUpdateTime);
        log.info("RemoteBlockStorageDataSyncTimer end. update time: " + lastUpdateTime/1000);
    }

    private void updateVolume(IVolumeProvider provider) {

        Volume volume = volumeDao.findOneByProperty(Volume.PROVIDER, provider.providerType().getValue());
        if(volume == null){
            log.debug(String.format("%s volume 没有数据不进行同步", provider.providerType().name()));
            return;
        }

        List<Volume> vol = provider.getVolumeList(null);
        List<String> fields = Arrays.asList(Volume.STATUS, Volume.UPDATED_AT);
        for (Volume v : vol) {
            // has been updated last time.
            if (v.getUpdatedAt().getTime() <= lastUpdateTime) continue;

            try {
                Map<String, Object> items = new HashMap<>();
                items.put(Volume.PROVIDER, v.getProvider());
                items.put(Volume.PROVIDER_REF_ID, v.getProviderRefId());
                List<Volume> entities = this.volumeDao.findByProperties(items);

                // volume must be unique.
                if (entities == null || entities.size() == 0) {
                    log.warn("can not found volume with provider [" + v.getProvider() + "] and provider_ref_id ["
                            + v.getProviderRefId() + "], ignore update status.");
                    continue;
                }
                if (entities.size() > 1) {
                    log.warn("found more than one matched entity, ignore.");
                    continue;
                }

                Volume target = entities.get(0);

                // just update status and udpate time.
                target.setUpdatedAt(v.getUpdatedAt());
                target.setStatus(v.getStatus());
                log.debug(String.format("==volume status== sync update volumeId=%s, status=%s", target.getId(), v.getStatus()));
                this.volumeDao.update(target, fields);
            }
            catch (Exception e) {
                log.error("got exception when syncing volume status: " + e.getMessage());
            }
        }
    }
    /*  无法模板化，部分方法没有移入ResourceProviderEntity
    private <T> T getEntity(Class<T extends ResourceProviderEntity> type, Object v) throws Exception {
        T t = type.newInstance();

        Map<String, Object> items = new HashMap<>();
        items.put(t.PROVIDER, v.getProvider());
        items.put(t.PROVIDER_REF_ID, v.getProviderRefId());
        List<Volume> entities = this.volumeDao.findByProperties(items);

        // volume must be unique.
        if (entities == null || entities.size() == 0) {
            log.warn("can not found volume with provider [" + v.getProvider() + "] and provider_ref_id ["
                    + v.getProviderRefId() + "], ignore update status.");
            return null;
        }
        if (entities.size() > 1) {
            log.warn("found more than one matched entity, ignore.");
            return null;
        }

        T target = (T)(entities.get(0));

        // just update status and udpate time.
        target.setUpdatedAt(v.getUpdatedAt());
        target.setStatus(v.getStatus());

        return target;
    }*/

    private void updateSnapshot(IVolumeProvider provider) {

        Snapshot snapshot = snapshotDao.findOneByProperty(Snapshot.PROVIDER, provider.providerType().getValue());
        if(snapshot == null){
            log.debug(String.format("%s snapshot 没有数据不进行同步", provider.providerType().name()));
            return;
        }

        List<Snapshot> snap = provider.getSnapshotList(null);
        List<String> fields = Arrays.asList(Snapshot.STATUS, Snapshot.UPDATED_AT);
        for (Snapshot s : snap) {
            if (s.getUpdatedAt().getTime() <= lastUpdateTime) continue;
            try {
                Map<String, Object> items = new HashMap<>();
                items.put(Snapshot.PROVIDER, s.getProvider());
                items.put(Snapshot.PROVIDER_REF_ID, s.getProviderRefId());
                List<Snapshot> entities = this.snapshotDao.findByProperties(items);

                // snapshot must be unique.
                if (entities == null || entities.size() == 0) {
                    log.warn("can not found snapshot with provider [" + s.getProvider() + "] and provider_ref_id ["
                            + s.getProviderRefId() + "], ignore update status.");
                    continue;
                }
                if (entities.size() > 1) {
                    log.warn("found more than one matched entity, ignore.");
                    continue;
                }

                Snapshot target = entities.get(0);

                // just update status and udpate time.
                target.setUpdatedAt(s.getUpdatedAt());
                target.setStatus(s.getStatus());
                this.snapshotDao.update(target, fields);
            }
            catch (Exception e) {
                log.error("got exception when syncing snapshot status: " + e.getMessage());
            }
        }
    }
    /*
    private void updateVolume(IVolumeProvider provider) {
        List<Volume> vol = provider.getVolumeList(null);
        for (Volume v : vol) {
            // cache is also update when update to DB
            if (v.getUpdateAt() > cache.get(v.getId())) {
                int tryTimes = 0;
                while (tryTimes <= 5) {
                    tryTimes++;
                    // try to lock
                    if (lock.getLock(v.getId(), v.getUpdateAt().toMillions(), 2)) {
                        // got lock
                        // update dao
                        try {
                            // after get lcok, check cache again, in case of DB was update after last cache check.
                            volumeDao.update(v, List<String> fields);
                            // update local cache
                        }
                        finally {
                            lock.releaseLock(v.getId());
                        }
                    }
                    // lock failed, check if need to update
                    else {
                        String timeValue;
                        try {
                            // may be the lock had been release.
                            timeValue = lock.getValueOfLock(v.getId());
                        } catch (Exception ex) {
                            continue;
                        }
                        if (timeValue.after(v.getUpdateAt())) {
                            // lock time is newer, no need to update
                            continue;
                        }
                        else {
                            // the current update is newer, worth to update, wait for the lock.

                        }
                    }
                }
                if (tryTimes > 5) {
                    log.warn("update failed, ignore.");
                }
            }
            else {
                // data of this volume from remote is not newer than local, ignore update.
                continue;
            }
        }
    }*/
}
