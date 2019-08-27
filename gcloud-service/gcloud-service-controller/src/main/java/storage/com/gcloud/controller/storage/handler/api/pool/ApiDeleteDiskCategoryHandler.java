package com.gcloud.controller.storage.handler.api.pool;

import org.springframework.beans.factory.annotation.Autowired;

import com.gcloud.controller.storage.model.DeleteDiskCategoryParams;
import com.gcloud.controller.storage.service.IStoragePoolService;
import com.gcloud.core.annotations.CustomAnnotations.GcLog;
import com.gcloud.core.exception.GCloudException;
import com.gcloud.core.handle.ApiHandler;
import com.gcloud.core.handle.MessageHandler;
import com.gcloud.core.util.BeanUtil;
import com.gcloud.header.ApiReplyMessage;
import com.gcloud.header.Module;
import com.gcloud.header.SubModule;
import com.gcloud.header.storage.msg.api.pool.ApiDeleteDiskCategoryMsg;
import com.gcloud.header.storage.msg.api.pool.StoragePoolActions;

@ApiHandler(module = Module.ECS, subModule = SubModule.DISK, action = StoragePoolActions.DELETE_DISK_CATEGORY)
@GcLog(taskExpect = "删除磁盘类别")
public class ApiDeleteDiskCategoryHandler extends MessageHandler<ApiDeleteDiskCategoryMsg, ApiReplyMessage>{

	@Autowired
	private IStoragePoolService poolService;
	
	@Override
	public ApiReplyMessage handle(ApiDeleteDiskCategoryMsg msg) throws GCloudException {
		DeleteDiskCategoryParams params = BeanUtil.copyProperties(msg, DeleteDiskCategoryParams.class);
		poolService.deleteDiskCategory(params, msg.getCurrentUser());
		
		ApiReplyMessage reply = new ApiReplyMessage();
		return reply;
	}

}
