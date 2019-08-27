package com.gcloud.controller.image.handler.api;

import org.springframework.beans.factory.annotation.Autowired;

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
import com.gcloud.header.image.msg.api.ApiDisableImageMsg;

@GcLog(taskExpect = "启用/禁用镜像")
@ApiHandler(module= Module.ECS, subModule=SubModule.IMAGE, action="DisableImage")
public class ApiDisableImageHandler extends MessageHandler<ApiDisableImageMsg, ApiReplyMessage>{
	@Autowired
    private IImageService imageService;

	@Override
	public ApiReplyMessage handle(ApiDisableImageMsg msg) throws GCloudException {
		imageService.disableImage(msg.getImageId(), msg.isDisable());
		msg.setObjectId(msg.getImageId());
		msg.setObjectName(CacheContainer.getInstance().getString(CacheType.IMAGE_NAME, msg.getImageId()));
        return new ApiReplyMessage();
	}

}
