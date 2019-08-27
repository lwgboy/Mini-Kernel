package com.gcloud.controller.image.driver;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gcloud.controller.image.dao.ImageStoreDao;
import com.gcloud.controller.image.entity.ImageStore;
import com.gcloud.controller.image.enums.ImageStoreStatus;

@Service
public class RbdStoreDriver  implements IImageStoreDriver {
	@Autowired
	private ImageStoreDao storeDao;

	@Override
	public void copyImage(String sourceFilePath, String imageId) {
		//rbd copy 到images池
		
		ImageStore store = new ImageStore();
		store.setImageId(imageId);
		store.setStoreTarget("images");//后续通过配置文件配置
		store.setStoreType(ImageDriverEnum.RBD.getStorageType());
		store.setStatus(ImageStoreStatus.ACTIVE.value());
		
		storeDao.save(store);
	}

	@Override
	public void deleteImage(String imageId) {
		//rbd delete image
	}

	@Override
	public void distributeImage(String imageId, String target, String targetType, String taskId) {
		//file store in ceph
		//distribute to node or dd to vg
	}

	@Override
	public long getImageActualSize(String imageId) {
		return 0;
	}

	/*@Override
	public void deleteImageCache(String imageId) {
		
	}*/
	
}
