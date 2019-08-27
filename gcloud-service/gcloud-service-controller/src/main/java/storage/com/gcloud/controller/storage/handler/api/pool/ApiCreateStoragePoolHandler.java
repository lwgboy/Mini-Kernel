
package com.gcloud.controller.storage.handler.api.pool;

import com.gcloud.controller.storage.service.IStoragePoolService;
import com.gcloud.core.annotations.CustomAnnotations.GcLog;
import com.gcloud.core.exception.GCloudException;
import com.gcloud.core.handle.ApiHandler;
import com.gcloud.core.handle.MessageHandler;
import com.gcloud.header.Module;
import com.gcloud.header.SubModule;
import com.gcloud.header.storage.msg.api.pool.ApiCreateStoragePoolMsg;
import com.gcloud.header.storage.msg.api.pool.ApiCreateStoragePoolReplyMsg;
import com.gcloud.header.storage.msg.api.pool.StoragePoolActions;
import org.springframework.beans.factory.annotation.Autowired;
@GcLog(taskExpect="创建存储池")
@ApiHandler(module = Module.ECS, subModule = SubModule.DISK, action = StoragePoolActions.CREATE_STORAGE_POOL)
public class ApiCreateStoragePoolHandler extends MessageHandler<ApiCreateStoragePoolMsg, ApiCreateStoragePoolReplyMsg> {

    @Autowired
    private IStoragePoolService poolService;

    @Override
    public ApiCreateStoragePoolReplyMsg handle(ApiCreateStoragePoolMsg msg) throws GCloudException {
        ApiCreateStoragePoolReplyMsg reply = new ApiCreateStoragePoolReplyMsg();
        reply.setPoolId(this.poolService.createStoragePool(msg.getDisplayName(), msg.getProvider(), msg.getStorageType(), msg.getPoolName(), msg.getZoneId(), msg.getCategoryId(),
                msg.getHostname(), msg.getDriver(), msg.getTaskId()));
        return reply;
    }

}
