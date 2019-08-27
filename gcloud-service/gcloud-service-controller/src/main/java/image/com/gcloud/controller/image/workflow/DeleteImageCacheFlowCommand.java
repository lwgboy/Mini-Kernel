package com.gcloud.controller.image.workflow;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.gcloud.controller.image.entity.ImageStore;
import com.gcloud.controller.image.workflow.model.DeleteImageCacheFlowCommandReq;
import com.gcloud.core.messagebus.MessageBus;
import com.gcloud.core.util.MessageUtil;
import com.gcloud.core.workflow.core.BaseWorkFlowCommand;
import com.gcloud.header.image.msg.node.DeleteImageMsg;

import lombok.extern.slf4j.Slf4j;

@Component
@Scope("prototype")
@Slf4j
public class DeleteImageCacheFlowCommand extends BaseWorkFlowCommand{
	@Autowired
	MessageBus bus;
	
	@Override
    public boolean judgeExecute() {
		DeleteImageCacheFlowCommandReq req = (DeleteImageCacheFlowCommandReq)getReqParams();
		return req.getRepeatParams()==null?false:true;
    }
	
	@Override
	protected Object process() throws Exception {
		DeleteImageCacheFlowCommandReq req = (DeleteImageCacheFlowCommandReq)getReqParams();
		ImageStore store = req.getRepeatParams();
		DeleteImageMsg deleteMsg = new DeleteImageMsg();
		String controllerService = MessageUtil.controllerServiceId();
		String controllerHost = controllerService.substring(controllerService.indexOf("-") + 1);
		deleteMsg.setServiceId(store.getStoreType().equals("node")?MessageUtil.imageServiceId(store.getStoreTarget()):MessageUtil.imageServiceId(controllerHost));
		deleteMsg.setImageId(req.getImageId());
		deleteMsg.setStoreTarget(store.getStoreTarget());
		deleteMsg.setStoreType(store.getStoreType());
		deleteMsg.setTaskId(getTaskId());
        
        bus.send(deleteMsg);
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
		return DeleteImageCacheFlowCommandReq.class;
	}

	@Override
	protected Class<?> getResParamClass() {
		return null;
	}

}
