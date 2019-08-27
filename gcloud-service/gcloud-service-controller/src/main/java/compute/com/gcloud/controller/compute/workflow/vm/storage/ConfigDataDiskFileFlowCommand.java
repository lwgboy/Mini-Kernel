package com.gcloud.controller.compute.workflow.vm.storage;

import com.gcloud.controller.compute.dao.InstanceDao;
import com.gcloud.controller.compute.entity.VmInstance;
import com.gcloud.controller.compute.workflow.model.storage.ConfigDataDiskFileFlowCommandReq;
import com.gcloud.controller.compute.workflow.model.storage.ConfigDataDiskFileFlowCommandRes;
import com.gcloud.core.messagebus.MessageBus;
import com.gcloud.core.util.MessageUtil;
import com.gcloud.core.workflow.core.BaseWorkFlowCommand;
import com.gcloud.header.compute.msg.node.vm.storage.ConfigDataDiskFileMsg;
import com.gcloud.header.compute.msg.node.vm.storage.ForceCleanDiskConfigFileMsg;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 * Created by yaowj on 2018/11/15.
 */
@Component
@Scope("prototype")
@Slf4j
public class ConfigDataDiskFileFlowCommand extends BaseWorkFlowCommand {

    @Autowired
    private MessageBus bus;

    @Autowired
    private InstanceDao instanceDao;

    @Override
    protected Object process() throws Exception {

        ConfigDataDiskFileFlowCommandReq req = (ConfigDataDiskFileFlowCommandReq)getReqParams();

        ConfigDataDiskFileMsg msg = new ConfigDataDiskFileMsg();
        msg.setTaskId(getTaskId());
        msg.setInstanceId(req.getInstanceId());
        msg.setVolumeDetail(req.getVolumeDetail());
        msg.setServiceId(MessageUtil.computeServiceId(req.getVmHostName()));

        bus.send(msg);

        return null;
    }

    @Override
    protected Object rollback() throws Exception {

        ConfigDataDiskFileFlowCommandReq req = (ConfigDataDiskFileFlowCommandReq)getReqParams();

        VmInstance ins = instanceDao.getById(req.getInstanceId());

        ForceCleanDiskConfigFileMsg msg = new ForceCleanDiskConfigFileMsg();
        msg.setTaskId(getTaskId());
        msg.setInstanceId(req.getInstanceId());
        msg.setVolumeId(msg.getVolumeId());

        msg.setServiceId(MessageUtil.computeServiceId(ins.getHostname()));

        bus.send(msg);

        return null;
    }

    @Override
    protected Object timeout() throws Exception {
        return null;
    }

    @Override
    protected Class<?> getReqParamClass() {
        return ConfigDataDiskFileFlowCommandReq.class;
    }

    @Override
    protected Class<?> getResParamClass() {
        return ConfigDataDiskFileFlowCommandRes.class;
    }
}
