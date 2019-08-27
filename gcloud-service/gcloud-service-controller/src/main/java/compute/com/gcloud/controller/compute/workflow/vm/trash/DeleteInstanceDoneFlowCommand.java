package com.gcloud.controller.compute.workflow.vm.trash;

import com.gcloud.controller.compute.dao.InstanceDao;
import com.gcloud.controller.compute.workflow.model.trash.DeleteInstanceDoneFlowCommandReq;
import com.gcloud.controller.compute.workflow.model.trash.DeleteInstanceDoneFlowCommandRes;
import com.gcloud.core.workflow.core.BaseWorkFlowCommand;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 * Created by yaowj on 2018/11/26.
 */
@Component
@Scope("prototype")
@Slf4j
public class DeleteInstanceDoneFlowCommand extends BaseWorkFlowCommand {

    @Autowired
    private InstanceDao instanceDao;

    @Override
    protected Object process() throws Exception {
        DeleteInstanceDoneFlowCommandReq req = (DeleteInstanceDoneFlowCommandReq)getReqParams();
        instanceDao.deleteById(req.getInstanceId());
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
        return DeleteInstanceDoneFlowCommandReq.class;
    }

    @Override
    protected Class<?> getResParamClass() {
        return DeleteInstanceDoneFlowCommandRes.class;
    }
}
