package com.gcloud.controller.compute.workflow.vm.network;

import com.gcloud.controller.compute.dao.InstanceDao;
import com.gcloud.controller.compute.entity.VmInstance;
import com.gcloud.controller.compute.workflow.model.network.ForceDetachPortFlowCommandReq;
import com.gcloud.controller.compute.workflow.model.network.ForceDetachPortFlowCommandRes;
import com.gcloud.core.messagebus.MessageBus;
import com.gcloud.core.util.MessageUtil;
import com.gcloud.core.workflow.core.BaseWorkFlowCommand;
import com.gcloud.header.compute.msg.node.vm.network.ForceDetachPortMsg;
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
public class ForceDetachPortFlowCommand extends BaseWorkFlowCommand {

    @Autowired
    private MessageBus bus;

    @Autowired
    private InstanceDao instanceDao;

    @Override
    protected Object process() throws Exception {
        ForceDetachPortFlowCommandReq req = (ForceDetachPortFlowCommandReq)getReqParams();

        VmInstance ins = instanceDao.getById(req.getInstanceId());

        ForceDetachPortMsg msg = new ForceDetachPortMsg();
        msg.setTaskId(getTaskId());
        msg.setInstanceId(req.getInstanceId());
        msg.setMacAddress(req.getNetworkDetail().getMacAddress());
        msg.setPortId(req.getNetworkDetail().getPortId());

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
        return ForceDetachPortFlowCommandReq.class;
    }

    @Override
    protected Class<?> getResParamClass() {
        return ForceDetachPortFlowCommandRes.class;
    }
}
