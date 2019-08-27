
package com.gcloud.controller.storage.handler.api.pool;

import org.springframework.beans.factory.annotation.Autowired;

import com.gcloud.controller.storage.model.DescribeDiskCategoriesParams;
import com.gcloud.controller.storage.service.IStoragePoolService;
import com.gcloud.core.annotations.CustomAnnotations.GcLog;
import com.gcloud.core.exception.GCloudException;
import com.gcloud.core.handle.ApiHandler;
import com.gcloud.core.handle.MessageHandler;
import com.gcloud.core.util.BeanUtil;
import com.gcloud.framework.db.PageResult;
import com.gcloud.header.Module;
import com.gcloud.header.SubModule;
import com.gcloud.header.storage.model.DiskCategoryModel;
import com.gcloud.header.storage.msg.api.pool.ApiDescribeDiskCategoriesMsg;
import com.gcloud.header.storage.msg.api.pool.ApiDescribeDiskCategoriesReplyMsg;
import com.gcloud.header.storage.msg.api.pool.StoragePoolActions;

@GcLog(taskExpect="磁盘类型列表")
@ApiHandler(module = Module.ECS, subModule = SubModule.DISK, action = StoragePoolActions.DESCRIBE_DISK_CATEGORIES)
public class ApiDescribeDiskCategoriesHandler extends MessageHandler<ApiDescribeDiskCategoriesMsg, ApiDescribeDiskCategoriesReplyMsg> {

    @Autowired
    private IStoragePoolService poolService;

    @Override
    public ApiDescribeDiskCategoriesReplyMsg handle(ApiDescribeDiskCategoriesMsg msg) throws GCloudException {
    	DescribeDiskCategoriesParams params = BeanUtil.copyProperties(msg, DescribeDiskCategoriesParams.class);
        PageResult<DiskCategoryModel> response = poolService.describeDiskCategories(params);
        
        ApiDescribeDiskCategoriesReplyMsg reply = new ApiDescribeDiskCategoriesReplyMsg();
        reply.init(response);
        return reply;
    }

}
