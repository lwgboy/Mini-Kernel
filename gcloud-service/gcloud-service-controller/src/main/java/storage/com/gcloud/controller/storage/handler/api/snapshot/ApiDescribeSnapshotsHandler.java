package com.gcloud.controller.storage.handler.api.snapshot;

import com.gcloud.controller.storage.model.DescribeSnapshotsParams;
import com.gcloud.controller.storage.service.ISnapshotService;
import com.gcloud.core.exception.GCloudException;
import com.gcloud.core.handle.ApiHandler;
import com.gcloud.core.handle.MessageHandler;
import com.gcloud.core.util.BeanUtil;
import com.gcloud.framework.db.PageResult;
import com.gcloud.header.Module;
import com.gcloud.header.SubModule;
import com.gcloud.header.storage.model.SnapshotType;
import com.gcloud.header.storage.msg.api.snapshot.ApiCreateSnapshotMsg;
import com.gcloud.header.storage.msg.api.snapshot.ApiCreateSnapshotReplyMsg;
import com.gcloud.header.storage.msg.api.snapshot.ApiDescribeSnapshotMsg;
import com.gcloud.header.storage.msg.api.snapshot.ApiDescribeSnapshotsReplyMsg;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Created by yaowj on 2018/11/7.
 */
@ApiHandler(module= Module.ECS,subModule=SubModule.SNAPSHOT, action="DescribeSnapshots")
public class ApiDescribeSnapshotsHandler extends MessageHandler<ApiDescribeSnapshotMsg, ApiDescribeSnapshotsReplyMsg> {

    @Autowired
    private ISnapshotService snapshotService;

    @Override
    public ApiDescribeSnapshotsReplyMsg handle(ApiDescribeSnapshotMsg msg) throws GCloudException {
        DescribeSnapshotsParams params = BeanUtil.copyProperties(msg, DescribeSnapshotsParams.class);
        PageResult<SnapshotType> response = snapshotService.describeSnapshots(params, msg.getCurrentUser());
        ApiDescribeSnapshotsReplyMsg reply = new ApiDescribeSnapshotsReplyMsg();
        reply.init(response);
        return reply;
    }
}
