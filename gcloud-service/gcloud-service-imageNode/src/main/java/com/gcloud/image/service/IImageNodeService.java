package com.gcloud.image.service;

import com.gcloud.header.image.msg.node.DeleteImageMsg;
import com.gcloud.header.image.msg.node.DownloadImageMsg;

public interface IImageNodeService {
	void downloadImage(DownloadImageMsg msg);
	void deleteImage(DeleteImageMsg msg);
}
