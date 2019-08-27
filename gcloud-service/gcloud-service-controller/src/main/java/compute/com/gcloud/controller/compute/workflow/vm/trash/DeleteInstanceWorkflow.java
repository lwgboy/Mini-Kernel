package com.gcloud.controller.compute.workflow.vm.trash;

import com.gcloud.controller.compute.dao.InstanceDao;
import com.gcloud.controller.compute.entity.VmInstance;
import com.gcloud.controller.compute.service.vm.trash.IVmTrashService;
import com.gcloud.controller.compute.workflow.model.trash.DeleteInstanceWorkflowReq;
import com.gcloud.core.workflow.core.BaseWorkFlows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 * Created by yaowj on 2018/12/3.
 */
@Component
@Scope("prototype")
@Slf4j
public class DeleteInstanceWorkflow extends BaseWorkFlows {

    @Autowired
    private IVmTrashService vmTrashService;

    @Autowired
    private InstanceDao instanceDao;

    @Override
    public String getFlowTypeCode() {
        return "deleteInstanceWorkflow";
    }

    @Override
    public Object preProcess() {
        return null;
    }

    @Override
    public void process() {

    }

    @Override
    public boolean judgeExecute() {
        DeleteInstanceWorkflowReq req = (DeleteInstanceWorkflowReq)getReqParams();
        if(req.getDeleteNotExist() != null && req.getDeleteNotExist()){
            VmInstance instance = instanceDao.getById(req.getInstanceId());
            if(instance == null){
                return false;
            }
        }
        return true;
    }

    @Override
    protected Class<?> getReqParamClass() {
        return DeleteInstanceWorkflowReq.class;
    }
}
