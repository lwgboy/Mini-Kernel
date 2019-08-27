package com.gcloud.controller.image.async;

import java.util.ArrayList;
import java.util.List;

import com.gcloud.controller.async.LogAsync;
import com.gcloud.controller.image.dao.ImageDao;
import com.gcloud.controller.image.dao.ImagePropertyDao;
import com.gcloud.controller.image.driver.ImageDriverEnum;
import com.gcloud.controller.image.entity.Image;
import com.gcloud.controller.image.prop.ImageProp;
import com.gcloud.core.async.AsyncResult;
import com.gcloud.core.async.AsyncStatus;
import com.gcloud.core.service.SpringUtil;
import com.gcloud.core.util.ErrorCodeUtil;
import com.gcloud.header.image.enums.ImageStatus;

public class UploadGcloudImageAsync  extends LogAsync{
	
	private String imageId;
	private String filePath;
	
	@Override
	public long timeout() {
		return 1800 * 1000L;
	}

	@Override
	public AsyncResult execute() {
		AsyncStatus status = AsyncStatus.SUCCEED;
        AsyncResult result = new AsyncResult();
        ImageProp prop = SpringUtil.getBean(ImageProp.class);

    	/*if(ImageDriverEnum.getByType(prop.getStroageType()).getImageActualSize(imageId) == imageSize) {
    		result.setAsyncStatus(AsyncStatus.SUCCEED);
    	} else {
    		result.setAsyncStatus(AsyncStatus.RUNNING);
    	}*/
        try {
	        ImageDriverEnum.getByType(prop.getStroageType()).copyImage(filePath, imageId);
		} catch (Exception ex){
	        status = AsyncStatus.FAILED;
	        result.setErrorMsg(ErrorCodeUtil.getErrorCode(ex, "::上传镜像失败"));
	    }

        result.setAsyncStatus(status);
        
        return result;
	}
	
	@Override
    public void successHandler() {

        ImageDao imageDao = SpringUtil.getBean(ImageDao.class);
        List<String> updateField = new ArrayList<>();

        Image image = new Image();
        image.setId(imageId);

        updateField.add(image.updateStatus(ImageStatus.ACTIVE.value()));

        imageDao.update(image, updateField);
    }

    @Override
    public void failHandler() {
        delete();
    }

    @Override
    public void timeoutHandler() {
    	 delete();
    }

    @Override
    public void exceptionHandler() {
        delete();
    }

    public void delete(){
    	ImageProp prop = SpringUtil.getBean(ImageProp.class);
    	ImageDriverEnum.getByType(prop.getStroageType()).deleteImage(imageId);

        ImageDao imageDao = SpringUtil.getBean(ImageDao.class);
        ImagePropertyDao imagePropertyDao = SpringUtil.getBean(ImagePropertyDao.class);

        imageDao.deleteById(imageId);
        imagePropertyDao.deleteByImageId(imageId);

    }

    @Override
    public void defaultHandler() {

        ImageDao imageDao = SpringUtil.getBean(ImageDao.class);
        List<String> updateField = new ArrayList<>();

        Image image = new Image();
        image.setId(imageId);
        updateField.add(image.updateStatus(ImageStatus.ACTIVE.name()));

        imageDao.update(image, updateField);

    }

	public String getImageId() {
		return imageId;
	}

	public void setImageId(String imageId) {
		this.imageId = imageId;
	}

	public String getFilePath() {
		return filePath;
	}

	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}

}
