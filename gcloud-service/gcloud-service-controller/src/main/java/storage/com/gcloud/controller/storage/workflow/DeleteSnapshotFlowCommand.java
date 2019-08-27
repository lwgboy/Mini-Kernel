package com.gcloud.controller.storage.workflow;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.gcloud.controller.storage.service.ISnapshotService;
import com.gcloud.controller.storage.workflow.model.volume.DeleteSnapshotFlowCommandReq;
import com.gcloud.core.workflow.core.BaseWorkFlowCommand;

import lombok.extern.slf4j.Slf4j;
@Component
@Scope("prototype")
@Slf4j
public class DeleteSnapshotFlowCommand extends BaseWorkFlowCommand{
	
	@Autowired
	ISnapshotService snapshotService;
	
	@Override
    public boolean judgeExecute() {
		//没有快照时，或者配置不删除快照时
		DeleteSnapshotFlowCommandReq req = (DeleteSnapshotFlowCommandReq)getReqParams();
		log.debug("删除快照" + req.getRepeatParams());
		return (req.getRepeatParams()==null || !req.isDelete())?false:true;
    }
	
	@Override
	protected Object process() throws Exception {
		DeleteSnapshotFlowCommandReq req = (DeleteSnapshotFlowCommandReq)getReqParams();
		snapshotService.deleteSnapshot(req.getRepeatParams(), getTaskId());
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
		return DeleteSnapshotFlowCommandReq.class;
	}

	@Override
	protected Class<?> getResParamClass() {
		return null;
	}

}
