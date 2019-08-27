package com.gcloud.controller.image.handler.api;

import com.gcloud.controller.image.service.IImageService;
import com.gcloud.core.annotations.CustomAnnotations.LongTask;
import com.gcloud.core.exception.GCloudException;
import com.gcloud.core.handle.ApiHandler;
import com.gcloud.core.handle.MessageHandler;
import com.gcloud.header.Module;
import com.gcloud.header.SubModule;
import com.gcloud.header.image.msg.api.ApiGenDownloadMsg;
import com.gcloud.header.image.msg.api.ApiGenDownloadReplyMsg;
import org.springframework.beans.factory.annotation.Autowired;

@LongTask
@ApiHandler(module = Module.ECS, subModule = SubModule.IMAGE, action = "GenDownload")
public class ApiGenDownloadHandler extends MessageHandler<ApiGenDownloadMsg, ApiGenDownloadReplyMsg> {

    @Autowired
    private IImageService imageService;

    @Override
    public ApiGenDownloadReplyMsg handle(ApiGenDownloadMsg msg) throws GCloudException {
        ApiGenDownloadReplyMsg reply = new ApiGenDownloadReplyMsg();
        try {
            reply.setDownloadInfo(imageService.genDownload(msg.getImageId()));
            reply.setSuccess(true);
        }
        catch (Exception e) {
            reply.setSuccess(false);
            reply.setErrorMsg(e.getMessage());
        }
        return reply;
    }

}
