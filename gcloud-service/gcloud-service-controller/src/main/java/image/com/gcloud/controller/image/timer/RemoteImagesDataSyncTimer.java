package com.gcloud.controller.image.timer;

import com.gcloud.controller.image.dao.ImageDao;
import com.gcloud.controller.image.entity.Image;
import com.gcloud.controller.image.provider.IImageProvider;
import com.gcloud.controller.storage.entity.Snapshot;
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
public class RemoteImagesDataSyncTimer extends GcloudJobBean {

    @Autowired
    private ImageDao imageDao;

    private long lastUpdateTime = 0L;
    private static final String updateTimeKey = "ImageLastUpdateTime";

    @Override
    protected void executeInternal(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        Object time = CacheContainer.getInstance().get(CacheType.TIMER, updateTimeKey);
        if (time == null) {
            log.warn("get image last update time from redis got null");
        } else {
            lastUpdateTime = (long)time;
        }
        log.info("RemoteImageDataSyncTimer start. update time: " + lastUpdateTime/1000);
        for (IImageProvider provider : SpringUtil.getBeans(IImageProvider.class)) {
            // gcloud type resource is no need to synchronize.
            log.debug("syncing with provider: " + provider.getClass().getName());
            if (provider.providerType() == ProviderType.GCLOUD) continue;

            this.updateImages(provider);
        }

        lastUpdateTime = (new Date()).getTime();
        CacheContainer.getInstance().put(CacheType.TIMER, updateTimeKey, lastUpdateTime);
        log.info("RemoteImageDataSyncTimer end. update time: " + lastUpdateTime/1000);
    }

    private void updateImages(IImageProvider provider) {

        Image image = imageDao.findOneByProperty(Snapshot.PROVIDER, provider.providerType().getValue());
        if(image == null){
            log.debug(String.format("%s image 没有数据不进行同步", provider.providerType().name()));
            return;
        }

        List<Image> images = provider.listImage(null);
        log.debug("image provider syncing: " + provider.getClass().toString());
        List<String> fields = Arrays.asList(Image.STATUS, Image.UPDATED_AT);

        for (Image i : images) {
            if (i.getUpdatedAt().getTime() <= lastUpdateTime) continue;
            try {
                Map<String, Object> items = new HashMap<>();
                items.put(Image.PROVIDER, i.getProvider());
                items.put(Image.PROVIDER_REF_ID, i.getProviderRefId());
                List<Image> entities = this.imageDao.findByProperties(items);

                if (entities == null || entities.size() == 0) {
                    log.warn("can not found image with provider [" + i.getProvider() + "] and provider_ref_id ["
                            + i.getProviderRefId() + "], ignore update status.");
                    continue;
                }
                if (entities.size() > 1) {
                    log.warn("found more than one matched entity, ignore.");
                    continue;
                }

                Image target = entities.get(0);
                target.setUpdatedAt(i.getUpdatedAt());
                target.setStatus(i.getStatus());
                this.imageDao.update(target, fields);
            }
            catch (Exception e) {
                log.error("got exception when syncing image status: " + e.getMessage());
            }
        }
    }
}
