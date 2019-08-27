package com.gcloud.controller.compute.workflow.vm.trash;

import com.gcloud.controller.compute.workflow.model.trash.ForceDetachAndDeleteNetcardInitFlowCommandReq;
import com.gcloud.controller.compute.workflow.model.trash.ForceDetachAndDeleteNetcardInitFlowCommandRes;
import com.gcloud.controller.network.service.IPortService;
import com.gcloud.core.workflow.core.BaseWorkFlowCommand;
import com.gcloud.header.compute.msg.node.vm.model.VmNetworkDetail;
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
public class ForceDetachAndDeleteNetcardInitFlowCommand extends BaseWorkFlowCommand {

    @Autowired
    private IPortService portService;

    @Override
    protected Object process() throws Exception {
        ForceDetachAndDeleteNetcardInitFlowCommandReq req = (ForceDetachAndDeleteNetcardInitFlowCommandReq)getReqParams();

        ForceDetachAndDeleteNetcardInitFlowCommandRes res = new ForceDetachAndDeleteNetcardInitFlowCommandRes();
        boolean delete = req.getNetcard() == null || req.getNetcard().isDelete();
        res.setDelete(delete);
        res.setNetcardId(req.getNetcard().getNetcardId());
        res.setInstanceId(req.getInstanceId());

        VmNetworkDetail networkDetail = portService.getNetworkDetail(req.getNetcard().getNetcardId());
        res.setNetworkDetail(networkDetail);

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
        return ForceDetachAndDeleteNetcardInitFlowCommandReq.class;
    }

    @Override
    protected Class<?> getResParamClass() {
        return ForceDetachAndDeleteNetcardInitFlowCommandRes.class;
    }
}
