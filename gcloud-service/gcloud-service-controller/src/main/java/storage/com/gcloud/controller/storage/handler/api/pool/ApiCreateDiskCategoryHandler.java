
package com.gcloud.controller.storage.handler.api.pool;

import org.springframework.beans.factory.annotation.Autowired;

import com.gcloud.controller.storage.model.CreateDiskCategoryParams;
import com.gcloud.controller.storage.service.IStoragePoolService;
import com.gcloud.core.annotations.CustomAnnotations.GcLog;
import com.gcloud.core.exception.GCloudException;
import com.gcloud.core.handle.ApiHandler;
import com.gcloud.core.handle.MessageHandler;
import com.gcloud.core.util.BeanUtil;
import com.gcloud.header.Module;
import com.gcloud.header.SubModule;
import com.gcloud.header.storage.msg.api.pool.ApiCreateDiskCategoryMsg;
import com.gcloud.header.storage.msg.api.pool.ApiCreateDiskCategoryReplyMsg;
import com.gcloud.header.storage.msg.api.pool.StoragePoolActions;

@GcLog(taskExpect="创建磁盘类型")
@ApiHandler(module = Module.ECS, subModule = SubModule.DISK, action = StoragePoolActions.CREATE_DISK_CATEGORY)
public class ApiCreateDiskCategoryHandler extends MessageHandler<ApiCreateDiskCategoryMsg, ApiCreateDiskCategoryReplyMsg> {

    @Autowired
    private IStoragePoolService poolService;

    @Override
    public ApiCreateDiskCategoryReplyMsg handle(ApiCreateDiskCategoryMsg msg) throws GCloudException {
    	CreateDiskCategoryParams params = BeanUtil.copyProperties(msg, CreateDiskCategoryParams.class);
    	String typeId = poolService.createDiskCategory(params);
    	
        ApiCreateDiskCategoryReplyMsg reply = new ApiCreateDiskCategoryReplyMsg();
        reply.setTypeId(typeId);
        return reply;
    }

}
