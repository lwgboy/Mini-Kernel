package com.gcloud.controller.compute.workflow.vm.network;

import com.gcloud.controller.compute.dao.InstanceDao;
import com.gcloud.controller.compute.entity.VmInstance;
import com.gcloud.controller.compute.workflow.model.network.CleanNetConfigFileFlowCommandReq;
import com.gcloud.controller.compute.workflow.model.network.CleanNetConfigFileFlowCommandRes;
import com.gcloud.core.messagebus.MessageBus;
import com.gcloud.core.util.MessageUtil;
import com.gcloud.core.workflow.core.BaseWorkFlowCommand;
import com.gcloud.header.compute.msg.node.vm.network.CleanNetConfigFileMsg;
import com.gcloud.header.compute.msg.node.vm.network.ConfigNetFileMsg;
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
public class CleanNetConfigFileFlowCommand extends BaseWorkFlowCommand {

    @Autowired
    private MessageBus bus;

    @Autowired
    private InstanceDao instanceDao;

    @Override
    protected Object process() throws Exception {

        CleanNetConfigFileFlowCommandReq req = (CleanNetConfigFileFlowCommandReq)getReqParams();

        VmInstance ins = instanceDao.getById(req.getInstanceId());

        CleanNetConfigFileMsg msg = new CleanNetConfigFileMsg();
        msg.setTaskId(getTaskId());
        msg.setInstanceId(req.getInstanceId());
        msg.setNetworkDetail(req.getNetworkDetail());

        msg.setServiceId(MessageUtil.computeServiceId(ins.getHostname()));

        bus.send(msg);

        return null;
    }

    @Override
    protected Object rollback() throws Exception {
        CleanNetConfigFileFlowCommandReq req = (CleanNetConfigFileFlowCommandReq)getReqParams();

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
    protected Object timeout() throws Exception {
        return null;
    }

    @Override
    protected Class<?> getReqParamClass() {
        return CleanNetConfigFileFlowCommandReq.class;
    }

    @Override
    protected Class<?> getResParamClass() {
        return CleanNetConfigFileFlowCommandRes.class;
    }
}
