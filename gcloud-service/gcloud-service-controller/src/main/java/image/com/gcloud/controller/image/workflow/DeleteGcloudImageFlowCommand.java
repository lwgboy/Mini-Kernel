package com.gcloud.controller.image.workflow;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.gcloud.controller.image.dao.ImageDao;
import com.gcloud.controller.image.dao.ImagePropertyDao;
import com.gcloud.controller.image.dao.ImageStoreDao;
import com.gcloud.controller.image.driver.ImageDriverEnum;
import com.gcloud.controller.image.entity.Image;
import com.gcloud.controller.image.entity.ImageStore;
import com.gcloud.controller.image.prop.ImageProp;
import com.gcloud.controller.image.workflow.model.DeleteGcloudImageFlowCommandReq;
import com.gcloud.controller.image.workflow.model.DeleteGcloudImageFlowCommandRes;
import com.gcloud.core.workflow.core.BaseWorkFlowCommand;

import lombok.extern.slf4j.Slf4j;
@Component
@Scope("prototype")
@Slf4j
public class DeleteGcloudImageFlowCommand extends BaseWorkFlowCommand {
	
	@Autowired
    ImageProp prop;
	
	@Autowired
    private ImageDao imageDao;

    @Autowired
    private ImagePropertyDao imagePropertyDao;
    
    @Autowired
	private ImageStoreDao storeDao;

	@Override
	protected Object process() throws Exception {
		DeleteGcloudImageFlowCommandReq req = (DeleteGcloudImageFlowCommandReq)getReqParams();
		ImageDriverEnum.getByType(prop.getStroageType()).deleteImage(req.getImageId());
		
		imageDao.deleteById(req.getImageId());
        imagePropertyDao.deleteByImageId(req.getImageId());
        
        List<ImageStore> stores = storeDao.findByProperty("imageId", req.getImageId());
        DeleteGcloudImageFlowCommandRes res = new DeleteGcloudImageFlowCommandRes();
        res.setStores(stores);
		return res;
	}

	@Override
	protected Object rollback() throws Exception {
		return null;
	}

	@Override
	protected Object timeout() throws Exception {
		DeleteGcloudImageFlowCommandReq req = (DeleteGcloudImageFlowCommandReq)getReqParams();
		Image img = imageDao.getById(req.getImageId());
		if(img == null) {
			return true;
		}
		return false;
	}

	@Override
	protected Class<?> getReqParamClass() {
		return DeleteGcloudImageFlowCommandReq.class;
	}

	@Override
	protected Class<?> getResParamClass() {
		return DeleteGcloudImageFlowCommandRes.class;
	}

}
