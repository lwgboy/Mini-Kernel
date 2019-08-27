package com.gcloud.controller.compute.workflow.vm.network;

import com.gcloud.controller.compute.dao.InstanceDao;
import com.gcloud.controller.compute.entity.VmInstance;
import com.gcloud.controller.compute.workflow.model.network.CleanNetEnvConfigFlowCommandReq;
import com.gcloud.controller.compute.workflow.model.network.CleanNetEnvConfigFlowCommandRes;
import com.gcloud.core.messagebus.MessageBus;
import com.gcloud.core.util.MessageUtil;
import com.gcloud.core.workflow.core.BaseWorkFlowCommand;
import com.gcloud.header.compute.msg.node.vm.network.CleanNetEnvConfigMsg;
import com.gcloud.header.compute.msg.node.vm.network.ConfigNetEnvMsg;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 * Created by yaowj on 2018/11/13.
 */
@Component
@Scope("prototype")
@Slf4j
public class CleanNetEnvConfigFlowCommand extends BaseWorkFlowCommand {

    @Autowired
    private MessageBus bus;

    @Autowired
    private InstanceDao instanceDao;

    @Override
    protected Object process() throws Exception {
        CleanNetEnvConfigFlowCommandReq req = (CleanNetEnvConfigFlowCommandReq)getReqParams();

        VmInstance ins = instanceDao.getById(req.getInstanceId());

        CleanNetEnvConfigMsg msg = new CleanNetEnvConfigMsg();
        msg.setTaskId(getTaskId());
        msg.setInstanceId(req.getInstanceId());
        msg.setNetworkDetail(req.getNetworkDetail());

        msg.setServiceId(MessageUtil.computeServiceId(ins.getHostname()));

        bus.send(msg);

        return null;
    }

    @Override
    protected Object rollback() throws Exception {
        CleanNetEnvConfigFlowCommandReq req = (CleanNetEnvConfigFlowCommandReq)getReqParams();

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
    protected Object timeout() throws Exception {
        return null;
    }

    @Override
    protected Class<?> getReqParamClass() {
        return CleanNetEnvConfigFlowCommandReq.class;
    }

    @Override
    protected Class<?> getResParamClass() {
        return CleanNetEnvConfigFlowCommandRes.class;
    }
}
