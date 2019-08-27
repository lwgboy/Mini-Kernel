package com.gcloud.image.driver;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.gcloud.common.util.SystemUtil;
import com.gcloud.core.exception.GCloudException;
import com.gcloud.image.prop.ImageNodeProp;

@Component
public class FileStoreNodeDriver implements IImageStoreNodeDriver {
	@Autowired
	ImageNodeProp prop;

	@Override
	public void downloadImage(String sourceFilePath, String targetFilePath, String imageId) {
		//cp image
		String[] cmd = null;
        cmd = new String[]{"cp", sourceFilePath, targetFilePath};
        int res = SystemUtil.runAndGetCode(cmd);
        if(res != 0) {//失败
        	throw new GCloudException("::下载镜像失败");
        }
	}

}
