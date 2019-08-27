package com.gcloud.controller.compute.workflow.vm.base;

import com.gcloud.controller.compute.dao.InstanceDao;
import com.gcloud.controller.compute.entity.VmInstance;
import com.gcloud.controller.compute.workflow.model.vm.DestroyInstanceIfExistFlowCommandReq;
import com.gcloud.controller.compute.workflow.model.vm.DestroyInstanceIfExistFlowCommandRes;
import com.gcloud.core.messagebus.MessageBus;
import com.gcloud.core.util.MessageUtil;
import com.gcloud.core.workflow.core.BaseWorkFlowCommand;
import com.gcloud.header.compute.msg.node.vm.base.DestroyInstanceIfExistMsg;
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
public class DestroyInstanceIfExistFlowCommand extends BaseWorkFlowCommand {

    @Autowired
    private MessageBus bus;

    @Autowired
    private InstanceDao instanceDao;

    @Override
    protected Object process() throws Exception {

        DestroyInstanceIfExistFlowCommandReq req = (DestroyInstanceIfExistFlowCommandReq)getReqParams();

        VmInstance ins = instanceDao.getById(req.getInstanceId());

        DestroyInstanceIfExistMsg msg = new DestroyInstanceIfExistMsg();
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
        return DestroyInstanceIfExistFlowCommandReq.class;
    }

    @Override
    protected Class<?> getResParamClass() {
        return DestroyInstanceIfExistFlowCommandRes.class;
    }
}
