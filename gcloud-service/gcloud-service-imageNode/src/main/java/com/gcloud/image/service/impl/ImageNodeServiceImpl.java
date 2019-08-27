package com.gcloud.image.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gcloud.common.util.SystemUtil;
import com.gcloud.core.messagebus.MessageBus;
import com.gcloud.core.util.ErrorCodeUtil;
import com.gcloud.core.util.MessageUtil;
import com.gcloud.header.image.msg.node.DeleteImageMsg;
import com.gcloud.header.image.msg.node.DeleteImageReplyMsg;
import com.gcloud.header.image.msg.node.DownloadImageMsg;
import com.gcloud.header.image.msg.node.DownloadImageReplyMsg;
import com.gcloud.image.prop.ImageNodeProp;
import com.gcloud.image.provider.enums.ImageProviderEnum;
import com.gcloud.image.service.IImageNodeService;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class ImageNodeServiceImpl implements IImageNodeService {
	@Autowired
	ImageNodeProp prop;
	
	@Autowired
	private MessageBus bus;

	@Override
	public void downloadImage(DownloadImageMsg msg) {
		
		DownloadImageReplyMsg reply = new DownloadImageReplyMsg();
		reply.setServiceId(MessageUtil.controllerServiceId());
		reply.setSuccess(true);
		reply.setTaskId(msg.getTaskId());
		reply.setImageId(msg.getImageId());
		reply.setStoreTarget(msg.getServiceId().substring(msg.getServiceId().indexOf("-") + 1));
		
		try {
			ImageProviderEnum.getByType(msg.getProvider()).downloadImage(msg);//是否会等待执行结束？如果不会，则需要定时获取进度
		} catch (Exception e) {
			log.error("ImageServiceImpl downloadImage error", e);
			reply.setSuccess(false);
			reply.setErrorCode(ErrorCodeUtil.getErrorCode(e, "::下载镜像异常"));
		}
		
		bus.send(reply);
	}

	@Override
	public void deleteImage(DeleteImageMsg msg) {
		DeleteImageReplyMsg reply = new DeleteImageReplyMsg();
		reply.setServiceId(MessageUtil.controllerServiceId());
		reply.setSuccess(true);
		reply.setTaskId(msg.getTaskId());
		reply.setImageId(msg.getImageId());
		reply.setStoreTarget(msg.getServiceId().substring(msg.getServiceId().indexOf("-") + 1));
		try {
			String[] cmd = null;
	        cmd = new String[]{"rm", "-f", prop.getImageCachedPath() + msg.getImageId()};
	        int res = SystemUtil.runAndGetCode(cmd);//是否会等待执行结束？如果不会，则需要定时获取进度
		} catch (Exception e) {
			log.error("ImageServiceImpl deleteImage error", e);
			reply.setSuccess(false);
			reply.setErrorCode(ErrorCodeUtil.getErrorCode(e, "::删除镜像缓存异常"));
		}
		
		bus.send(reply);
	}

}
