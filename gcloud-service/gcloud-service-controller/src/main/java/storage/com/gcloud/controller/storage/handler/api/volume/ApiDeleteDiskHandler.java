package com.gcloud.controller.storage.handler.api.volume;

import com.gcloud.controller.ResourceIsolationCheck;
import com.gcloud.controller.enums.ResourceIsolationCheckType;
import com.gcloud.controller.storage.service.IVolumeService;
import com.gcloud.controller.storage.workflow.DeleteDiskWorkflow;
import com.gcloud.core.exception.GCloudException;
import com.gcloud.core.handle.ApiHandler;
import com.gcloud.core.handle.MessageHandler;
import com.gcloud.header.ApiReplyMessage;
import com.gcloud.header.Module;
import com.gcloud.header.SubModule;
import com.gcloud.header.storage.msg.api.volume.ApiDeleteDiskMsg;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import com.gcloud.core.annotations.CustomAnnotations.GcLog;
import com.gcloud.core.annotations.CustomAnnotations.LongTask;
import com.gcloud.core.cache.container.CacheContainer;
import com.gcloud.core.cache.enums.CacheType;

/**
 * Created by yaowj on 2018/9/28.
 */
@LongTask
@GcLog(taskExpect = "删除磁盘")
@ApiHandler(module= Module.ECS,subModule=SubModule.DISK, action="DeleteDisk")
@ResourceIsolationCheck(resourceIsolationCheckType = ResourceIsolationCheckType.VOLUME, resourceIdField = "diskId")
public class ApiDeleteDiskHandler extends MessageHandler<ApiDeleteDiskMsg, ApiReplyMessage> {

    @Autowired
    private IVolumeService volumeService;

    @Override
    public ApiReplyMessage handle(ApiDeleteDiskMsg msg) throws GCloudException {
        volumeService.deleteVolume(msg.getDiskId(), msg.getTaskId());
        msg.setObjectId(msg.getDiskId());
        msg.setObjectName(CacheContainer.getInstance().getString(CacheType.VOLUME_NAME, msg.getDiskId()));
        return new ApiReplyMessage();
    }
}
