package com.gcloud.controller.storage.handler.api.volume;

import com.gcloud.controller.storage.model.DescribeDisksParams;
import com.gcloud.controller.storage.service.IVolumeService;
import com.gcloud.core.exception.GCloudException;
import com.gcloud.core.handle.ApiHandler;
import com.gcloud.core.handle.MessageHandler;
import com.gcloud.core.util.BeanUtil;
import com.gcloud.framework.db.PageResult;
import com.gcloud.header.Module;
import com.gcloud.header.SubModule;
import com.gcloud.header.storage.model.DiskItemType;
import com.gcloud.header.storage.msg.api.volume.ApiDescribeDisksMsg;
import com.gcloud.header.storage.msg.api.volume.ApiDescribeDisksReplyMsg;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Created by yaowj on 2018/9/29.
 */
@ApiHandler(module= Module.ECS,subModule=SubModule.DISK, action="DescribeDisks")
public class ApiDescribeDisksHandler extends MessageHandler<ApiDescribeDisksMsg, ApiDescribeDisksReplyMsg> {

    @Autowired
    private IVolumeService volumeService;

    @Override
    public ApiDescribeDisksReplyMsg handle(ApiDescribeDisksMsg msg) throws GCloudException {

        DescribeDisksParams params = BeanUtil.copyProperties(msg, DescribeDisksParams.class);
        PageResult<DiskItemType> response = volumeService.describeDisks(params, msg.getCurrentUser());
        ApiDescribeDisksReplyMsg replyMsg = new ApiDescribeDisksReplyMsg();
        replyMsg.init(response);
        return replyMsg;
    }
}
