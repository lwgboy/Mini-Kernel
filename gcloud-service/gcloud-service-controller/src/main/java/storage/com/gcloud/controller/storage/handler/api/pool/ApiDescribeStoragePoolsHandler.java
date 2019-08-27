
package com.gcloud.controller.storage.handler.api.pool;

import com.gcloud.controller.storage.service.IStoragePoolService;
import com.gcloud.core.annotations.CustomAnnotations.GcLog;
import com.gcloud.core.exception.GCloudException;
import com.gcloud.core.handle.ApiHandler;
import com.gcloud.core.handle.MessageHandler;
import com.gcloud.header.Module;
import com.gcloud.header.SubModule;
import com.gcloud.header.storage.msg.api.pool.ApiDescribeStoragePoolsMsg;
import com.gcloud.header.storage.msg.api.pool.ApiDescribeStoragePoolsReplyMsg;
import com.gcloud.header.storage.msg.api.pool.StoragePoolActions;
import org.springframework.beans.factory.annotation.Autowired;
@GcLog(taskExpect="存储池列表")
@ApiHandler(module = Module.ECS, subModule = SubModule.DISK, action = StoragePoolActions.DESCRIBE_STORAGE_POOLS)
public class ApiDescribeStoragePoolsHandler extends MessageHandler<ApiDescribeStoragePoolsMsg, ApiDescribeStoragePoolsReplyMsg> {

    @Autowired
    private IStoragePoolService poolService;

    @Override
    public ApiDescribeStoragePoolsReplyMsg handle(ApiDescribeStoragePoolsMsg msg) throws GCloudException {
        ApiDescribeStoragePoolsReplyMsg reply = new ApiDescribeStoragePoolsReplyMsg();
        reply.init(this.poolService.describeStoragePools(1, 10));
        return reply;
    }

}
