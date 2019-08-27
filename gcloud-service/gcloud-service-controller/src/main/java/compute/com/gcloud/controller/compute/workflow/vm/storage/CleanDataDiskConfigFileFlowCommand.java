package com.gcloud.controller.compute.workflow.vm.storage;

import com.gcloud.controller.compute.dao.InstanceDao;
import com.gcloud.controller.compute.workflow.model.storage.CleanDataDiskConfigFileFlowCommandReq;
import com.gcloud.controller.compute.workflow.model.storage.CleanDataDiskConfigFileFlowCommandRes;
import com.gcloud.core.messagebus.MessageBus;
import com.gcloud.core.util.MessageUtil;
import com.gcloud.core.workflow.core.BaseWorkFlowCommand;
import com.gcloud.header.compute.msg.node.vm.storage.CleanDataDiskConfigFileMsg;
import com.gcloud.header.compute.msg.node.vm.storage.ConfigDataDiskFileMsg;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
@Scope("prototype")
@Slf4j
public class CleanDataDiskConfigFileFlowCommand extends BaseWorkFlowCommand {

    @Autowired
    private MessageBus bus;

    @Autowired
    private InstanceDao instanceDao;

    @Override
    protected Object process() throws Exception {

        CleanDataDiskConfigFileFlowCommandReq req = (CleanDataDiskConfigFileFlowCommandReq)getReqParams();

		CleanDataDiskConfigFileMsg msg = new CleanDataDiskConfigFileMsg();
        msg.setTaskId(getTaskId());
        msg.setInstanceId(req.getInstanceId());
        msg.setVolumeDetail(req.getVolumeDetail());
        msg.setTaskId(getTaskId());

        msg.setServiceId(MessageUtil.computeServiceId(req.getVmHostName()));

        bus.send(msg);

        return null;
    }

    @Override
    protected Object rollback() throws Exception {


        CleanDataDiskConfigFileFlowCommandReq req = (CleanDataDiskConfigFileFlowCommandReq)getReqParams();

        ConfigDataDiskFileMsg msg = new ConfigDataDiskFileMsg();
        msg.setTaskId(getTaskId());
        msg.setInstanceId(req.getInstanceId());
        msg.setVolumeDetail(req.getVolumeDetail());
        msg.setServiceId(MessageUtil.computeServiceId(req.getVmHostName()));

        bus.send(msg);

        return null;
    }

    @Override
    protected Object timeout() throws Exception {
        return null;
    }

    @Override
    protected Class<?> getReqParamClass() {
        return CleanDataDiskConfigFileFlowCommandReq.class;
    }

    @Override
    protected Class<?> getResParamClass() {
        return CleanDataDiskConfigFileFlowCommandRes.class;
    }
}
