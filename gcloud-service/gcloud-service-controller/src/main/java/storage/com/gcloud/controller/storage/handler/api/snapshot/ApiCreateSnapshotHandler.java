package com.gcloud.controller.storage.handler.api.snapshot;

import com.gcloud.controller.storage.service.ISnapshotService;
import com.gcloud.core.exception.GCloudException;
import com.gcloud.core.handle.ApiHandler;
import com.gcloud.core.handle.MessageHandler;
import com.gcloud.header.Module;
import com.gcloud.header.SubModule;
import com.gcloud.header.storage.msg.api.snapshot.ApiCreateSnapshotMsg;
import com.gcloud.header.storage.msg.api.snapshot.ApiCreateSnapshotReplyMsg;
import org.springframework.beans.factory.annotation.Autowired;
import com.gcloud.core.annotations.CustomAnnotations.GcLog;
import com.gcloud.core.annotations.CustomAnnotations.LongTask;

/**
 * Created by yaowj on 2018/11/7.
 */
@LongTask
@GcLog(taskExpect = "创建快照")
@ApiHandler(module= Module.ECS,subModule=SubModule.SNAPSHOT, action="CreateSnapshot")
public class ApiCreateSnapshotHandler  extends MessageHandler<ApiCreateSnapshotMsg, ApiCreateSnapshotReplyMsg> {

    @Autowired
    private ISnapshotService snapshotService;

    @Override
    public ApiCreateSnapshotReplyMsg handle(ApiCreateSnapshotMsg msg) throws GCloudException {
        String id = snapshotService.createSnapshot(msg.getDiskId(), msg.getSnapshotName(), msg.getDescription(), msg.getCurrentUser(), msg.getTaskId());
        ApiCreateSnapshotReplyMsg reply = new ApiCreateSnapshotReplyMsg();
        reply.setSnapshotId(id);
        return reply;
    }
}
