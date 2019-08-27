package com.gcloud.controller.image.handler.api;

import com.gcloud.controller.image.model.DescribeImageParams;
import com.gcloud.controller.image.service.IImageService;
import com.gcloud.core.annotations.CustomAnnotations.GcLog;
import com.gcloud.core.exception.GCloudException;
import com.gcloud.core.handle.ApiHandler;
import com.gcloud.core.handle.MessageHandler;
import com.gcloud.core.util.BeanUtil;
import com.gcloud.framework.db.PageResult;
import com.gcloud.header.Module;
import com.gcloud.header.SubModule;
import com.gcloud.header.image.model.ImageType;
import com.gcloud.header.image.msg.api.ApiDescribeImagesMsg;
import com.gcloud.header.image.msg.api.ApiDescribeImagesReplyMsg;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Created by yaowj on 2018/9/21.
 */
@ApiHandler(module= Module.ECS, subModule=SubModule.IMAGE, action="DescribeImages")
@GcLog(taskExpect = "查询镜像的列表")
public class ApiDescribeImagesHandler extends MessageHandler<ApiDescribeImagesMsg, ApiDescribeImagesReplyMsg> {

    @Autowired
    private IImageService imageService;

    @Override
    public ApiDescribeImagesReplyMsg handle(ApiDescribeImagesMsg msg) throws GCloudException {

        DescribeImageParams params = BeanUtil.copyProperties(msg, DescribeImageParams.class);
        PageResult<ImageType> response = imageService.describeImage(params, msg.getCurrentUser());
        ApiDescribeImagesReplyMsg reply = new ApiDescribeImagesReplyMsg();
        reply.init(response);

        return reply;
    }
}
