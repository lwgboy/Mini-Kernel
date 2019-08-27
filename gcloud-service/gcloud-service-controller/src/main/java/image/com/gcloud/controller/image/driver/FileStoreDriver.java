package com.gcloud.controller.image.driver;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gcloud.common.util.FileUtil;
import com.gcloud.common.util.SystemUtil;
import com.gcloud.controller.image.distribute.ImageDistributeEnum;
import com.gcloud.controller.image.driver.IImageStoreDriver;
import com.gcloud.controller.image.prop.ImageProp;
import com.gcloud.core.exception.GCloudException;

@Service
public class FileStoreDriver implements IImageStoreDriver {
	@Autowired
	private ImageProp prop;
	
	@Override
	public void copyImage(String sourceFilePath, String imageId) {
		//cp image
		String[] cmd = null;
        cmd = new String[]{"cp", sourceFilePath, prop.getImageFilesystemStoreDir() + imageId};
        int res = SystemUtil.runAndGetCode(cmd);
        
        if(res != 0) {//失败
        	throw new GCloudException("::上传镜像失败");
        }
	}

	@Override
	public void deleteImage(String imageId) {
		//remove image
		String[] cmd = null;
        cmd = new String[]{"rm", "-f", prop.getImageFilesystemStoreDir() + imageId};
        int res = SystemUtil.runAndGetCode(cmd);
        if(res != 0) {//失败
        	throw new GCloudException("::删除镜像失败");
        }
	}

	/* 
	 * target ： vg-ID、hostname、image pool
	 * targetType : vg、node、pool
	 */
	@Override
	public void distributeImage(String imageId, String target, String targetType, String taskId) {
		//file store in nfs 
		ImageDistributeEnum.getByType(ImageDriverEnum.FILE.name(), targetType).distributeImage(imageId, target, taskId);
	}

	@Override
	public long getImageActualSize(String imageId) {
		long size = 0;
		try {
			size = FileUtil.getFileSize(prop.getImageFilesystemStoreDir() + imageId);
        } catch(Exception ex) {
        	throw new GCloudException("::获取镜像文件大小出错");
        }
		return size;
	}

	/*@Override
	public void deleteImageCache(String imageId) {
		List<ImageStore> stores = storeDao.findByProperty("imageId", imageId);
		for(ImageStore store:stores) {
			DeleteImageMsg deleteMsg = new DeleteImageMsg();
			String controllerService = MessageUtil.controllerServiceId();
			String controllerHost = controllerService.substring(controllerService.indexOf("-") + 1);
			deleteMsg.setServiceId(store.getStoreType().equals("node")?MessageUtil.imageServiceId(store.getStoreTarget()):MessageUtil.imageServiceId(controllerHost));
			deleteMsg.setImageId(imageId);
			deleteMsg.setStoreTarget(store.getStoreTarget());
			deleteMsg.setStoreType(store.getStoreType());
			deleteMsg.setTaskId(null);
	        
	        bus.send(deleteMsg);
//			ImageDistributeEnum.getByType(ImageDriverEnum.FILE.name(), store.getStoreType()).deleteImageCache(imageId, store.getStoreTarget(), null);
		}
	}*/

}
