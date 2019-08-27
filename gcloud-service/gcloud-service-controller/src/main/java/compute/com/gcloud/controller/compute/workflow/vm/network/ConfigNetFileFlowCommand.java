package com.gcloud.controller.compute.workflow.vm.network;

import com.gcloud.controller.compute.dao.InstanceDao;
import com.gcloud.controller.compute.entity.VmInstance;
import com.gcloud.controller.compute.workflow.model.network.ConfigNetFileFlowCommandReq;
import com.gcloud.controller.compute.workflow.model.network.ConfigNetFileFlowCommandRes;
import com.gcloud.core.messagebus.MessageBus;
import com.gcloud.core.util.MessageUtil;
import com.gcloud.core.workflow.core.BaseWorkFlowCommand;
import com.gcloud.header.compute.msg.node.vm.network.ConfigNetFileMsg;
import com.gcloud.header.compute.msg.node.vm.network.ForceCleanNetConfigFileMsg;
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
public class ConfigNetFileFlowCommand extends BaseWorkFlowCommand {

    @Autowired
    private MessageBus bus;

    @Autowired
    private InstanceDao instanceDao;

    @Override
    protected Object process() throws Exception {

        ConfigNetFileFlowCommandReq req = (ConfigNetFileFlowCommandReq)getReqParams();

        VmInstance ins = instanceDao.getById(req.getInstanceId());

        ConfigNetFileMsg msg = new ConfigNetFileMsg();
        msg.setTaskId(getTaskId());
        msg.setInstanceId(req.getInstanceId());
        msg.setNetworkDetail(req.getNetworkDetail());

        msg.setServiceId(MessageUtil.computeServiceId(ins.getHostname()));

        bus.send(msg);

        return null;
    }

    @Override
    protected Object rollback() throws Exception {

        ConfigNetFileFlowCommandReq req = (ConfigNetFileFlowCommandReq)getReqParams();

        VmInstance ins = instanceDao.getById(req.getInstanceId());

        ForceCleanNetConfigFileMsg msg = new ForceCleanNetConfigFileMsg();
        msg.setTaskId(getTaskId());
        msg.setInstanceId(req.getInstanceId());
        msg.setNetworkDetail(req.getNetworkDetail());

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
        return ConfigNetFileFlowCommandReq.class;
    }

    @Override
    protected Class<?> getResParamClass() {
        return ConfigNetFileFlowCommandRes.class;
    }
}
