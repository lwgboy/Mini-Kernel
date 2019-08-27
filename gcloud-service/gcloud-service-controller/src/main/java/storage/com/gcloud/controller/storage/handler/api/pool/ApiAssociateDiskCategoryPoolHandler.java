package com.gcloud.controller.storage.handler.api.pool;

import org.springframework.beans.factory.annotation.Autowired;

import com.gcloud.controller.storage.model.AssociateDiskCategoryPoolParams;
import com.gcloud.controller.storage.service.IStoragePoolService;
import com.gcloud.core.annotations.CustomAnnotations.GcLog;
import com.gcloud.core.exception.GCloudException;
import com.gcloud.core.handle.ApiHandler;
import com.gcloud.core.handle.MessageHandler;
import com.gcloud.core.util.BeanUtil;
import com.gcloud.header.ApiReplyMessage;
import com.gcloud.header.Module;
import com.gcloud.header.SubModule;
import com.gcloud.header.storage.msg.api.pool.ApiAssociateDiskCategoryPoolMsg;
import com.gcloud.header.storage.msg.api.pool.StoragePoolActions;

@GcLog(taskExpect="磁盘类型关联存储池")
@ApiHandler(module = Module.ECS, subModule = SubModule.DISK, action = StoragePoolActions.ASSOCIATE_DISK_CATEGORY_POOL)
public class ApiAssociateDiskCategoryPoolHandler extends MessageHandler<ApiAssociateDiskCategoryPoolMsg, ApiReplyMessage>{

	@Autowired
    private IStoragePoolService poolService;
	
	@Override
	public ApiReplyMessage handle(ApiAssociateDiskCategoryPoolMsg msg) throws GCloudException {
		AssociateDiskCategoryPoolParams params = BeanUtil.copyProperties(msg, AssociateDiskCategoryPoolParams.class);
		poolService.associateDiskCategoryPool(params);
		
		ApiReplyMessage reply = new ApiReplyMessage();
		return reply;
	}

}
