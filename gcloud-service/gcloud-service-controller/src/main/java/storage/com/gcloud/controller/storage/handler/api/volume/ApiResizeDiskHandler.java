package com.gcloud.controller.storage.handler.api.volume;

import com.gcloud.controller.ResourceIsolationCheck;
import com.gcloud.controller.enums.ResourceIsolationCheckType;
import com.gcloud.controller.storage.service.IVolumeService;
import com.gcloud.core.annotations.CustomAnnotations.GcLog;
import com.gcloud.core.annotations.CustomAnnotations.LongTask;
import com.gcloud.core.cache.container.CacheContainer;
import com.gcloud.core.cache.enums.CacheType;
import com.gcloud.core.exception.GCloudException;
import com.gcloud.core.handle.ApiHandler;
import com.gcloud.core.handle.MessageHandler;
import com.gcloud.header.ApiReplyMessage;
import com.gcloud.header.Module;
import com.gcloud.header.SubModule;
import com.gcloud.header.storage.msg.api.volume.ApiResizeDiskMsg;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Created by yaowj on 2018/9/21.
 */
@LongTask
@GcLog(taskExpect = "磁盘扩容")
@ApiHandler(module= Module.ECS,subModule=SubModule.DISK, action="ResizeDisk")
@ResourceIsolationCheck(resourceIsolationCheckType = ResourceIsolationCheckType.VOLUME, resourceIdField = "diskId")
public class ApiResizeDiskHandler extends MessageHandler<ApiResizeDiskMsg, ApiReplyMessage> {

    @Autowired
    private IVolumeService volumeService;

    @Override
    public ApiReplyMessage handle(ApiResizeDiskMsg msg) throws GCloudException {

        volumeService.resizeVolume(msg.getDiskId(), msg.getNewSize(), msg.getTaskId());
        msg.setObjectId(msg.getDiskId());
        msg.setObjectName(CacheContainer.getInstance().getString(CacheType.VOLUME_NAME, msg.getDiskId()));
        return new ApiReplyMessage();
    }
}
