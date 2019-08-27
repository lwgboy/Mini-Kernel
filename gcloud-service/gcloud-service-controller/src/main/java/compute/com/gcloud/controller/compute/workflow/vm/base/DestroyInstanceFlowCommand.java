package com.gcloud.controller.compute.workflow.vm.base;

import com.gcloud.controller.compute.dao.InstanceDao;
import com.gcloud.controller.compute.entity.VmInstance;
import com.gcloud.controller.compute.workflow.model.vm.DestroyInstanceFlowCommandReq;
import com.gcloud.controller.compute.workflow.model.vm.DestroyInstanceFlowCommandRes;
import com.gcloud.core.messagebus.MessageBus;
import com.gcloud.core.util.MessageUtil;
import com.gcloud.core.workflow.core.BaseWorkFlowCommand;
import com.gcloud.header.compute.msg.node.vm.base.DestroyInstanceMsg;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 * Created by yaowj on 2018/11/12.
 */
@Component
@Scope("prototype")
@Slf4j
public class DestroyInstanceFlowCommand extends BaseWorkFlowCommand {

    @Autowired
    private MessageBus bus;

    @Autowired
    private InstanceDao instanceDao;

    @Override
    protected Object process() throws Exception {

        DestroyInstanceFlowCommandReq req = (DestroyInstanceFlowCommandReq)getReqParams();

        VmInstance ins = instanceDao.getById(req.getInstanceId());

        DestroyInstanceMsg msg = new DestroyInstanceMsg();
        msg.setInstanceId(req.getInstanceId());
        msg.setTaskId(this.getTaskId());
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
        return DestroyInstanceFlowCommandReq.class;
    }

    @Override
    protected Class<?> getResParamClass() {
        return DestroyInstanceFlowCommandRes.class;
    }
}
