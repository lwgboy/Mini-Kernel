package com.gcloud.controller.storage.handler.api.snapshot;

import com.gcloud.controller.ResourceIsolationCheck;
import com.gcloud.controller.enums.ResourceIsolationCheckType;
import com.gcloud.controller.storage.service.ISnapshotService;
import com.gcloud.core.exception.GCloudException;
import com.gcloud.core.handle.ApiHandler;
import com.gcloud.core.handle.MessageHandler;
import com.gcloud.header.ApiReplyMessage;
import com.gcloud.header.Module;
import com.gcloud.header.SubModule;
import com.gcloud.header.storage.msg.api.snapshot.ApiResetDiskMsg;

import org.springframework.beans.factory.annotation.Autowired;
import com.gcloud.core.annotations.CustomAnnotations.GcLog;
import com.gcloud.core.annotations.CustomAnnotations.LongTask;
import com.gcloud.core.cache.container.CacheContainer;
import com.gcloud.core.cache.enums.CacheType;

@LongTask
@GcLog(taskExpect = "快照恢复")
@ApiHandler(module = Module.ECS, subModule = SubModule.SNAPSHOT, action = "ResetDisk")
@ResourceIsolationCheck(resourceIsolationCheckType = ResourceIsolationCheckType.SNAPSHOT, resourceIdField = "snapshotId")
public class ApiResetDiskHandler extends MessageHandler<ApiResetDiskMsg, ApiReplyMessage> {

    @Autowired
    private ISnapshotService snapshotService;

    @Override
    public ApiReplyMessage handle(ApiResetDiskMsg msg) throws GCloudException {
        snapshotService.resetSnapshot(msg.getSnapshotId(), msg.getDiskId(), msg.getTaskId());
        msg.setObjectId(msg.getSnapshotId());
        msg.setObjectName(CacheContainer.getInstance().getString(CacheType.SNAPSHOT_NAME, msg.getSnapshotId()));
        return new ApiReplyMessage();
    }
}
