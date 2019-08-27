package com.gcloud.controller.image.handler.api.statistics;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.gcloud.controller.image.service.IImageService;
import com.gcloud.core.annotations.CustomAnnotations.GcLog;
import com.gcloud.core.exception.GCloudException;
import com.gcloud.core.handle.ApiHandler;
import com.gcloud.core.handle.MessageHandler;
import com.gcloud.header.Module;
import com.gcloud.header.SubModule;
import com.gcloud.header.image.model.ImageStatisticsItem;
import com.gcloud.header.image.msg.api.ApiImagesStatisticsMsg;
import com.gcloud.header.image.msg.api.ApiImagesStatisticsReplyMsg;

@ApiHandler(module = Module.ECS,subModule=SubModule.IMAGE,action = "ImageStatistics")
@GcLog(taskExpect = "镜像统计")
public class ApiImagesStatisticsHandler extends MessageHandler<ApiImagesStatisticsMsg, ApiImagesStatisticsReplyMsg>{
	@Autowired
	private IImageService imageService;

	@Override
	public ApiImagesStatisticsReplyMsg handle(ApiImagesStatisticsMsg msg) throws GCloudException {
		List<ImageStatisticsItem> response = imageService.imageStatistics();
		ApiImagesStatisticsReplyMsg replyMsg = new ApiImagesStatisticsReplyMsg();
		replyMsg.init(response);
		replyMsg.setAllNum(response.stream().map(ImageStatisticsItem::getCountNum).reduce(0, Integer::sum));
		return replyMsg;
	}
}
