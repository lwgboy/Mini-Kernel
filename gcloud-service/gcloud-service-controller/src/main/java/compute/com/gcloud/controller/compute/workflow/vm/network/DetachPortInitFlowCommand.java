package com.gcloud.controller.compute.workflow.vm.network;

import com.gcloud.controller.compute.service.vm.base.IVmBaseService;
import com.gcloud.controller.compute.service.vm.netowork.IVmNetworkService;
import com.gcloud.controller.compute.workflow.model.network.DetachPortInitFlowCommandReq;
import com.gcloud.controller.compute.workflow.model.network.DetachPortInitFlowCommandRes;
import com.gcloud.controller.network.service.IPortService;
import com.gcloud.core.workflow.core.BaseWorkFlowCommand;
import com.gcloud.header.compute.msg.node.vm.model.VmNetworkDetail;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.UUID;

/**
 * Created by yaowj on 2018/11/20.
 */
@Component
@Scope("prototype")
@Slf4j
public class DetachPortInitFlowCommand extends BaseWorkFlowCommand {

    @Autowired
    private IPortService portService;

    @Autowired
    private IVmBaseService vmBaseService;

    @Autowired
    private IVmNetworkService vmNetworkService;

    @Override
    protected Object process() throws Exception {
        DetachPortInitFlowCommandReq req = (DetachPortInitFlowCommandReq)getReqParams();
        DetachPortInitFlowCommandRes res = new DetachPortInitFlowCommandRes();

        vmNetworkService.detachPortInit(req.getInstanceId(), req.getNetworkInterfaceId(), req.getInTask());

        try{
            VmNetworkDetail networkDetail = portService.getNetworkDetail(req.getNetworkInterfaceId());

            res.setNetworkDetail(networkDetail);
            res.setInstanceId(req.getInstanceId());
            res.setTaskId(UUID.randomUUID().toString());
        }catch (Exception ex){
            errorRollback();
            throw ex;
        }


        return res;
    }

    @Override
    protected Object rollback() throws Exception {

        errorRollback();

        return null;
    }

    private void errorRollback(){
        DetachPortInitFlowCommandReq req = (DetachPortInitFlowCommandReq)getReqParams();
        vmBaseService.cleanState(req.getInstanceId(), req.getInTask());
    }

    @Override
    protected Object timeout() throws Exception {
        return null;
    }

    @Override
    protected Class<?> getReqParamClass() {
        return DetachPortInitFlowCommandReq.class;
    }

    @Override
    protected Class<?> getResParamClass() {
        return DetachPortInitFlowCommandRes.class;
    }
}
