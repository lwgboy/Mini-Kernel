package com.gcloud.controller.image.handler.api;

import com.gcloud.controller.image.service.IImageService;
import com.gcloud.core.annotations.CustomAnnotations.GcLog;
import com.gcloud.core.cache.container.CacheContainer;
import com.gcloud.core.cache.enums.CacheType;
import com.gcloud.core.exception.GCloudException;
import com.gcloud.core.handle.ApiHandler;
import com.gcloud.core.handle.MessageHandler;
import com.gcloud.header.ApiReplyMessage;
import com.gcloud.header.Module;
import com.gcloud.header.SubModule;
import com.gcloud.header.image.msg.api.ApiModifyImageAttributeMsg;
import org.springframework.beans.factory.annotation.Autowired;
/**
 * Created by yaowj on 2018/9/21.
 */
@GcLog(taskExpect = "修改镜像属性")
@ApiHandler(module= Module.ECS, subModule=SubModule.IMAGE, action="ModifyImageAttribute")
//@ResourceIsolationCheck(resourceIsolationCheckType = ResourceIsolationCheckType.IMAGE, resourceIdField = "imageId")
public class ApiModifyImageAttributeHandler extends MessageHandler<ApiModifyImageAttributeMsg, ApiReplyMessage> {

    @Autowired
    private IImageService imageService;

    @Override
    public ApiReplyMessage handle(ApiModifyImageAttributeMsg msg) throws GCloudException {
        imageService.updateImage(msg.getImageId(), msg.getImageName());
        msg.setObjectId(msg.getImageId());
		msg.setObjectName(CacheContainer.getInstance().getString(CacheType.IMAGE_NAME, msg.getImageId()));
        return new ApiReplyMessage();
    }
}
