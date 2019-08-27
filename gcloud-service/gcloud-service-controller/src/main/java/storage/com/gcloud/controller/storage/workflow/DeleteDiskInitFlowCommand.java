package com.gcloud.controller.storage.workflow;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.gcloud.controller.storage.entity.Snapshot;
import com.gcloud.controller.storage.service.ISnapshotService;
import com.gcloud.controller.storage.workflow.model.volume.DeleteDiskInitFlowCommandReq;
import com.gcloud.controller.storage.workflow.model.volume.DeleteDiskInitFlowCommandRes;
import com.gcloud.core.workflow.core.BaseWorkFlowCommand;

import lombok.extern.slf4j.Slf4j;
@Component
@Scope("prototype")
@Slf4j
public class DeleteDiskInitFlowCommand  extends BaseWorkFlowCommand {
	@Autowired
	ISnapshotService snapshotService;
	
	@Override
	protected Object process() throws Exception {
		DeleteDiskInitFlowCommandReq req = (DeleteDiskInitFlowCommandReq)getReqParams();
		List<Snapshot> snaps = snapshotService.findSnapshotByVolume(req.getVolumeId());
		DeleteDiskInitFlowCommandRes res = new DeleteDiskInitFlowCommandRes();
		res.setSnapshotIds(snaps.stream().map(Snapshot::getId).collect(Collectors.toList()));
		log.debug(res.getSnapshotIds().toString());
		return res;
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
		return DeleteDiskInitFlowCommandReq.class;
	}

	@Override
	protected Class<?> getResParamClass() {
		return DeleteDiskInitFlowCommandRes.class;
	}

}
