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
import com.gcloud.header.storage.msg.api.snapshot.ApiDeleteSnapshotMsg;
import org.springframework.beans.factory.annotation.Autowired;
import com.gcloud.core.annotations.CustomAnnotations.GcLog;
import com.gcloud.core.annotations.CustomAnnotations.LongTask;
import com.gcloud.core.cache.container.CacheContainer;
import com.gcloud.core.cache.enums.CacheType;
/**
 * Created by yaowj on 2018/11/7.
 */
@LongTask
@GcLog(taskExpect = "删除快照")
@ApiHandler(module= Module.ECS,subModule=SubModule.SNAPSHOT, action="DeleteSnapshot")
@ResourceIsolationCheck(resourceIsolationCheckType = ResourceIsolationCheckType.SNAPSHOT, resourceIdField = "snapshotId")
public class ApiDeleteSnapshotHandler extends MessageHandler<ApiDeleteSnapshotMsg, ApiReplyMessage> {

    @Autowired
    private ISnapshotService snapshotService;

    @Override
    public ApiReplyMessage handle(ApiDeleteSnapshotMsg msg) throws GCloudException {
        snapshotService.deleteSnapshot(msg.getSnapshotId(), msg.getTaskId());
        msg.setObjectId(msg.getSnapshotId());
        msg.setObjectName(CacheContainer.getInstance().getString(CacheType.SNAPSHOT_NAME, msg.getSnapshotId()));
        return new ApiReplyMessage();
    }
}
