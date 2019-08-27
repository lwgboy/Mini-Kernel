package com.gcloud.controller.compute.workflow.vm.network;

import com.gcloud.controller.compute.dao.InstanceDao;
import com.gcloud.controller.compute.entity.VmInstance;
import com.gcloud.controller.compute.workflow.model.network.ForceCleanNetEnvConfigFlowCommandReq;
import com.gcloud.controller.compute.workflow.model.network.ForceCleanNetEnvConfigFlowCommandRes;
import com.gcloud.core.messagebus.MessageBus;
import com.gcloud.core.util.MessageUtil;
import com.gcloud.core.workflow.core.BaseWorkFlowCommand;
import com.gcloud.header.compute.msg.node.vm.network.ForceCleanNetEnvConfigMsg;
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
public class ForceCleanNetEnvConfigFlowCommand extends BaseWorkFlowCommand {

    @Autowired
    private MessageBus bus;

    @Autowired
    private InstanceDao instanceDao;

    @Override
    protected Object process() throws Exception {
        ForceCleanNetEnvConfigFlowCommandReq req = (ForceCleanNetEnvConfigFlowCommandReq)getReqParams();

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
    protected Object rollback() throws Exception {

        return null;
    }

    @Override
    protected Object timeout() throws Exception {
        return null;
    }

    @Override
    protected Class<?> getReqParamClass() {
        return ForceCleanNetEnvConfigFlowCommandReq.class;
    }

    @Override
    protected Class<?> getResParamClass() {
        return ForceCleanNetEnvConfigFlowCommandRes.class;
    }
}
