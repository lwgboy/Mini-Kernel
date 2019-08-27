package com.gcloud.image.provider;

import com.gcloud.header.image.msg.node.DownloadImageMsg;

public interface IImageProvider {
	void downloadImage(DownloadImageMsg msg);
}
