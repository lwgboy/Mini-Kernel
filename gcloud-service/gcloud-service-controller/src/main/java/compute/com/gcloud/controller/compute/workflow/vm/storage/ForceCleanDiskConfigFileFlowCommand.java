package com.gcloud.controller.compute.workflow.vm.storage;

import com.gcloud.controller.compute.workflow.model.storage.ForceCleanDiskConfigFileFlowCommandReq;
import com.gcloud.controller.compute.workflow.model.storage.ForceCleanDiskConfigFileFlowCommandRes;
import com.gcloud.core.messagebus.MessageBus;
import com.gcloud.core.util.MessageUtil;
import com.gcloud.core.workflow.core.BaseWorkFlowCommand;
import com.gcloud.header.compute.msg.node.vm.storage.ForceCleanDiskConfigFileMsg;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
@Scope("prototype")
@Slf4j
public class ForceCleanDiskConfigFileFlowCommand extends BaseWorkFlowCommand {

    @Autowired
    private MessageBus bus;

    @Override
    protected Object process() throws Exception {

        ForceCleanDiskConfigFileFlowCommandReq req = (ForceCleanDiskConfigFileFlowCommandReq)getReqParams();

		ForceCleanDiskConfigFileMsg msg = new ForceCleanDiskConfigFileMsg();
        msg.setTaskId(getTaskId());
        msg.setInstanceId(req.getInstanceId());
        msg.setVolumeId(req.getVolumeId());
        msg.setTaskId(getTaskId());

        msg.setServiceId(MessageUtil.computeServiceId(req.getNode()));

        bus.send(msg);

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
        return ForceCleanDiskConfigFileFlowCommandReq.class;
    }

    @Override
    protected Class<?> getResParamClass() {
        return ForceCleanDiskConfigFileFlowCommandRes.class;
    }
}
