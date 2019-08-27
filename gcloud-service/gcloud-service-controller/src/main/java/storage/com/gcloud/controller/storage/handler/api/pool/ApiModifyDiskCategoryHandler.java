package com.gcloud.controller.storage.handler.api.pool;

import org.springframework.beans.factory.annotation.Autowired;

import com.gcloud.controller.storage.model.ModifyDiskCategoryParams;
import com.gcloud.controller.storage.service.IStoragePoolService;
import com.gcloud.core.exception.GCloudException;
import com.gcloud.core.handle.ApiHandler;
import com.gcloud.core.handle.MessageHandler;
import com.gcloud.core.util.BeanUtil;
import com.gcloud.header.ApiReplyMessage;
import com.gcloud.header.Module;
import com.gcloud.header.SubModule;
import com.gcloud.header.storage.msg.api.pool.ApiModifyDiskCategoryMsg;
import com.gcloud.header.storage.msg.api.pool.StoragePoolActions;

@ApiHandler(module = Module.ECS, subModule = SubModule.DISK, action = StoragePoolActions.MODIFY_DISK_CATEGORY)
public class ApiModifyDiskCategoryHandler extends MessageHandler<ApiModifyDiskCategoryMsg, ApiReplyMessage>{

	@Autowired
	private IStoragePoolService poolService;
	
	@Override
	public ApiReplyMessage handle(ApiModifyDiskCategoryMsg msg) throws GCloudException {
		ModifyDiskCategoryParams params = BeanUtil.copyProperties(msg, ModifyDiskCategoryParams.class);
		poolService.modifyDiskCategory(params, msg.getCurrentUser());
		
		ApiReplyMessage reply = new ApiReplyMessage();
		return reply;
	}

}
