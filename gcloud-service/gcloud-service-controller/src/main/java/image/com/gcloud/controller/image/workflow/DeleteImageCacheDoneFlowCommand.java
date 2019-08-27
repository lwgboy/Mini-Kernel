package com.gcloud.controller.image.workflow;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.gcloud.controller.image.dao.ImageStoreDao;
import com.gcloud.controller.image.workflow.model.DeleteImageCacheDoneFlowCommandReq;
import com.gcloud.core.workflow.core.BaseWorkFlowCommand;

import lombok.extern.slf4j.Slf4j;
@Component
@Scope("prototype")
@Slf4j
public class DeleteImageCacheDoneFlowCommand extends BaseWorkFlowCommand {
	@Autowired
	private ImageStoreDao storeDao;
	
	@Override
	protected Object process() throws Exception {
		DeleteImageCacheDoneFlowCommandReq req = (DeleteImageCacheDoneFlowCommandReq) getReqParams();
		//删除对应的image store info
    	storeDao.deleteByImageId(req.getImageId());
		return null;
	}

	@Override
	protected Object rollback() throws Exception {
		return null;
	}

	@Override
	protected Object timeout() throws Exception {
		return null;
	}

	@Override
	protected Class<?> getReqParamClass() {
		return DeleteImageCacheDoneFlowCommandReq.class;
	}

	@Override
	protected Class<?> getResParamClass() {
		return null;
	}

}
