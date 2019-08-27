package com.gcloud.controller.storage.workflow;

import com.gcloud.controller.storage.workflow.model.volume.DeleteDiskFlowCommandReq;
import com.gcloud.controller.storage.service.IVolumeService;
import com.gcloud.core.workflow.core.BaseWorkFlowCommand;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 * Created by yaowj on 2018/12/4.
 */
@Component
@Scope("prototype")
@Slf4j
public class DeleteDiskFlowCommand extends BaseWorkFlowCommand {

    @Autowired
    private IVolumeService volumeService;

    @Override
    protected Object process() throws Exception {
        DeleteDiskFlowCommandReq req = (DeleteDiskFlowCommandReq)getReqParams();
        volumeService.deleteVolume(req.getVolumeId(), getTaskId());

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
    public boolean judgeExecute() {
        DeleteDiskFlowCommandReq req = (DeleteDiskFlowCommandReq)getReqParams();
        return req == null || req.isDelete();
    }

    @Override
    protected Class<?> getReqParamClass() {
        return DeleteDiskFlowCommandReq.class;
    }

    @Override
    protected Class<?> getResParamClass() {
        return null;
    }
}
