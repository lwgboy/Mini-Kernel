package com.gcloud.image.provider.impl;

import java.io.File;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.gcloud.common.util.SystemUtil;
import com.gcloud.core.exception.GCloudException;
import com.gcloud.core.messagebus.MessageBus;
import com.gcloud.core.util.MessageUtil;
import com.gcloud.header.image.msg.api.ApiGenDownloadMsg;
import com.gcloud.header.image.msg.api.ApiGenDownloadReplyMsg;
import com.gcloud.header.image.msg.node.DownloadImageMsg;
import com.gcloud.image.prop.ImageNodeProp;
import com.gcloud.image.provider.IImageProvider;
import com.gcloud.service.common.compute.uitls.LogUtil;

import lombok.extern.slf4j.Slf4j;
@Component
@Slf4j
public class GlanceImageNodeProviderImpl implements IImageProvider {
	@Autowired
    private ImageNodeProp props;

    @Autowired
    private MessageBus bus;
    
	public void downloadImage(DownloadImageMsg msg) {
		String imagePath = this.props.getImageCachedPath();
        if (!imagePath.endsWith(File.separator)) {
            imagePath += File.separator;
        }
        File imageDir = new File(imagePath);
        if (!imageDir.exists()) {
            imageDir.mkdirs();
        }
        File imageFile = new File(imagePath + msg.getImageId());
        if (!imageFile.exists()) {
            String tmp = imagePath + msg.getImageId() + ".tmp";
            log.info("image not exist, downloading {}", tmp);
            ApiGenDownloadMsg genMsg = new ApiGenDownloadMsg();
            genMsg.setServiceId(MessageUtil.controllerServiceId());
            genMsg.setImageId(msg.getImageId());
            ApiGenDownloadReplyMsg reply = this.bus.call(genMsg, ApiGenDownloadReplyMsg.class);
            if (!reply.getSuccess()) {
                throw new GCloudException(reply.getErrorMsg());
            }
            String[] cmd = new String[] {"glance", "--os-image-url", reply.getDownloadInfo().getServiceUrl(), "--os-auth-token", reply.getDownloadInfo().getTokenId(),
                    "image-download", reply.getDownloadInfo().getImageRefId(), "--file", tmp};
            int res = SystemUtil.runAndGetCode(cmd);
            LogUtil.handleLog(cmd, res, "::下载镜像失败");
            SystemUtil.runAndGetCode(new String[] {"mv", tmp, imagePath + msg.getImageId()});
        }
	}

}
