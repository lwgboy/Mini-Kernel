package com.gcloud.controller.compute.workflow.vm.network;

import com.gcloud.controller.compute.dao.InstanceDao;
import com.gcloud.controller.compute.entity.VmInstance;
import com.gcloud.controller.compute.workflow.model.network.ConfigNetEnvFlowCommandReq;
import com.gcloud.controller.compute.workflow.model.network.ConfigNetEnvFlowCommandRes;
import com.gcloud.core.messagebus.MessageBus;
import com.gcloud.core.util.MessageUtil;
import com.gcloud.core.workflow.core.BaseWorkFlowCommand;
import com.gcloud.header.compute.msg.node.vm.network.ConfigNetEnvMsg;
import com.gcloud.header.compute.msg.node.vm.network.ForceCleanNetEnvConfigMsg;
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
public class ConfigNetEnvFlowCommand extends BaseWorkFlowCommand {

    @Autowired
    private MessageBus bus;

    @Autowired
    private InstanceDao instanceDao;

    @Override
    protected Object process() throws Exception {
        ConfigNetEnvFlowCommandReq req = (ConfigNetEnvFlowCommandReq)getReqParams();

        VmInstance ins = instanceDao.getById(req.getInstanceId());

        ConfigNetEnvMsg msg = new ConfigNetEnvMsg();
        msg.setTaskId(getTaskId());
        msg.setInstanceId(req.getInstanceId());
        msg.setNetworkDetail(req.getNetworkDetail());

        msg.setServiceId(MessageUtil.computeServiceId(ins.getHostname()));

        bus.send(msg);

        return null;
    }

    @Override
    protected Object rollback() throws Exception {

        ConfigNetEnvFlowCommandReq req = (ConfigNetEnvFlowCommandReq)getReqParams();

        VmInstance ins = instanceDao.getById(req.getInstanceId());
        ForceCleanNetEnvConfigMsg msg = new ForceCleanNetEnvConfigMsg();
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
        return ConfigNetEnvFlowCommandReq.class;
    }

    @Override
    protected Class<?> getResParamClass() {
        return ConfigNetEnvFlowCommandRes.class;
    }
}
