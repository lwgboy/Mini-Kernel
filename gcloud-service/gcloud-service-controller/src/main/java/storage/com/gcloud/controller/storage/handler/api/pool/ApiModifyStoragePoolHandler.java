
package com.gcloud.controller.storage.handler.api.pool;

import com.gcloud.controller.storage.service.IStoragePoolService;
import com.gcloud.core.annotations.CustomAnnotations.GcLog;
import com.gcloud.core.cache.container.CacheContainer;
import com.gcloud.core.cache.enums.CacheType;
import com.gcloud.core.exception.GCloudException;
import com.gcloud.core.handle.ApiHandler;
import com.gcloud.core.handle.MessageHandler;
import com.gcloud.header.Module;
import com.gcloud.header.SubModule;
import com.gcloud.header.storage.msg.api.pool.ApiModifyStoragePoolMsg;
import com.gcloud.header.storage.msg.api.pool.ApiModifyStoragePoolReplyMsg;
import com.gcloud.header.storage.msg.api.pool.StoragePoolActions;
import org.springframework.beans.factory.annotation.Autowired;
@GcLog(taskExpect="修改存储池")
@ApiHandler(module = Module.ECS, subModule = SubModule.DISK, action = StoragePoolActions.MODIFY_STORAGE_POOL)
public class ApiModifyStoragePoolHandler extends MessageHandler<ApiModifyStoragePoolMsg, ApiModifyStoragePoolReplyMsg> {

    @Autowired
    private IStoragePoolService poolService;

    @Override
    public ApiModifyStoragePoolReplyMsg handle(ApiModifyStoragePoolMsg msg) throws GCloudException {
        ApiModifyStoragePoolReplyMsg reply = new ApiModifyStoragePoolReplyMsg();
        this.poolService.modifyStoragePool(msg.getPoolId(), msg.getDisplayName());
        msg.setObjectId(msg.getPoolId());
        msg.setObjectName(CacheContainer.getInstance().getString(CacheType.STORAGEPOOL_NAME, msg.getPoolId()));
        return reply;
    }

}
