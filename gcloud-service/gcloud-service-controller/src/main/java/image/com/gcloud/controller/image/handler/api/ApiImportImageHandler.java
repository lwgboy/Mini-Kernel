package com.gcloud.controller.image.handler.api;

import com.gcloud.controller.image.model.CreateImageParams;
import com.gcloud.controller.image.service.IImageService;
import com.gcloud.core.annotations.CustomAnnotations.GcLog;
import com.gcloud.core.annotations.CustomAnnotations.LongTask;
import com.gcloud.core.cache.container.CacheContainer;
import com.gcloud.core.cache.enums.CacheType;
import com.gcloud.core.exception.GCloudException;
import com.gcloud.core.handle.ApiHandler;
import com.gcloud.core.handle.MessageHandler;
import com.gcloud.core.util.BeanUtil;
import com.gcloud.header.Module;
import com.gcloud.header.SubModule;
import com.gcloud.header.image.msg.api.ApiImportImageMsg;
import com.gcloud.header.image.msg.api.ApiImportImageReplyMsg;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Created by yaowj on 2018/9/21.
 */
@LongTask
@GcLog(taskExpect = "上传镜像")
@ApiHandler(module= Module.ECS, subModule=SubModule.IMAGE, action="ImportImage")
public class ApiImportImageHandler extends MessageHandler<ApiImportImageMsg, ApiImportImageReplyMsg> {

    @Autowired
    private IImageService imageService;

    @Override
    public ApiImportImageReplyMsg handle(ApiImportImageMsg msg) throws GCloudException {

        CreateImageParams params = BeanUtil.copyProperties(msg, CreateImageParams.class);
        String imageId = imageService.createImage(params, msg.getCurrentUser());
        ApiImportImageReplyMsg reply = new ApiImportImageReplyMsg();
        reply.setImageId(imageId);
        msg.setObjectId(imageId);
		msg.setObjectName(CacheContainer.getInstance().getString(CacheType.IMAGE_NAME, imageId));
        return reply;
    }
}
