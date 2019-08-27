package com.gcloud.controller.compute.workflow.vm.network;

import com.gcloud.controller.compute.service.vm.base.IVmBaseService;
import com.gcloud.controller.compute.service.vm.netowork.IVmNetworkService;
import com.gcloud.controller.compute.workflow.model.network.AttachPortInitFlowCommandReq;
import com.gcloud.controller.compute.workflow.model.network.AttachPortInitFlowCommandRes;
import com.gcloud.controller.network.dao.PortDao;
import com.gcloud.controller.network.entity.Port;
import com.gcloud.controller.network.service.IPortService;
import com.gcloud.core.workflow.core.BaseWorkFlowCommand;
import com.gcloud.header.compute.msg.node.vm.model.VmNetworkDetail;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.UUID;

/**
 * Created by yaowj on 2018/11/15.
 */
@Component
@Scope("prototype")
@Slf4j
public class AttachPortInitFlowCommand extends BaseWorkFlowCommand {


    @Autowired
    private IVmBaseService vmBaseService;

    @Autowired
    private IPortService portService;

    @Autowired
    private PortDao portDao;

    @Autowired
    private IVmNetworkService vmNetworkService;

    @Override
    protected Object process() throws Exception {
        AttachPortInitFlowCommandReq req = (AttachPortInitFlowCommandReq)getReqParams();

        vmNetworkService.attachPortInit(req.getInstanceId(), req.getNetworkInterfaceId(), req.getOvsBridgeId(), null, req.getInTask());

        AttachPortInitFlowCommandRes res = new AttachPortInitFlowCommandRes();
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

    public void errorRollback(){
        AttachPortInitFlowCommandReq req = (AttachPortInitFlowCommandReq)getReqParams();

        vmBaseService.cleanState(req.getInstanceId(), req.getInTask());

        Port port = portDao.getById(req.getNetworkInterfaceId());
        if(port != null){
            Port updatePort = new Port();

            updatePort.setId(req.getNetworkInterfaceId());
            updatePort.setProviderRefId(port.getProviderRefId());
            updatePort.setProvider(port.getProvider());
            updatePort.setSufId("");
            updatePort.setAftName("");
            updatePort.setPreName("");
            updatePort.setBrName("");
            updatePort.setDeviceOwner("");
            updatePort.setDeviceId("");


            portService.updatePort(updatePort);
        }


    }

    @Override
    protected Object timeout() throws Exception {
        return null;
    }

    @Override
    protected Class<?> getReqParamClass() {
        return AttachPortInitFlowCommandReq.class;
    }

    @Override
    protected Class<?> getResParamClass() {
        return AttachPortInitFlowCommandRes.class;
    }
}
