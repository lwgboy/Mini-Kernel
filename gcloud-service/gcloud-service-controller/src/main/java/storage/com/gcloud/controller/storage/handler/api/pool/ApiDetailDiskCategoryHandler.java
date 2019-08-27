package com.gcloud.controller.storage.handler.api.pool;

import org.springframework.beans.factory.annotation.Autowired;

import com.gcloud.controller.storage.model.DetailDiskCategoryParams;
import com.gcloud.controller.storage.service.IStoragePoolService;
import com.gcloud.core.exception.GCloudException;
import com.gcloud.core.handle.ApiHandler;
import com.gcloud.core.handle.MessageHandler;
import com.gcloud.core.util.BeanUtil;
import com.gcloud.header.Module;
import com.gcloud.header.SubModule;
import com.gcloud.header.storage.model.DiskCategoryModel;
import com.gcloud.header.storage.msg.api.pool.ApiDetailDiskCategoryMsg;
import com.gcloud.header.storage.msg.api.pool.ApiDetailDiskCategoryReplyMsg;
import com.gcloud.header.storage.msg.api.pool.StoragePoolActions;

@ApiHandler(module = Module.ECS, subModule = SubModule.DISK, action = StoragePoolActions.DETAIL_DISK_CATEGORY)
public class ApiDetailDiskCategoryHandler extends MessageHandler<ApiDetailDiskCategoryMsg, ApiDetailDiskCategoryReplyMsg>{

	@Autowired
	private IStoragePoolService poolService;
	
	@Override
	public ApiDetailDiskCategoryReplyMsg handle(ApiDetailDiskCategoryMsg msg) throws GCloudException {
		DetailDiskCategoryParams params = BeanUtil.copyProperties(msg, DetailDiskCategoryParams.class);
		DiskCategoryModel response = poolService.detailDiskCategory(params, msg.getCurrentUser());
		
		ApiDetailDiskCategoryReplyMsg reply = new ApiDetailDiskCategoryReplyMsg();
		reply.setDiskCategory(response);
		return reply;
	}

}
