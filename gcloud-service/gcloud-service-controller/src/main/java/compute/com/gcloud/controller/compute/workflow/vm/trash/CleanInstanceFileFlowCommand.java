package com.gcloud.controller.compute.workflow.vm.trash;

import com.gcloud.controller.compute.dao.InstanceDao;
import com.gcloud.controller.compute.entity.VmInstance;
import com.gcloud.controller.compute.workflow.model.trash.CleanInstanceFileFlowCommandReq;
import com.gcloud.controller.compute.workflow.model.trash.CleanInstanceFileFlowCommandRes;
import com.gcloud.core.messagebus.MessageBus;
import com.gcloud.core.util.MessageUtil;
import com.gcloud.core.workflow.core.BaseWorkFlowCommand;
import com.gcloud.header.compute.msg.node.vm.trash.CleanInstanceFileMsg;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 * Created by yaowj on 2018/11/26.
 */
@Component
@Scope("prototype")
@Slf4j
public class CleanInstanceFileFlowCommand extends BaseWorkFlowCommand {

    @Autowired
    private InstanceDao instanceDao;

    @Autowired
    private MessageBus bus;

    @Override
    protected Object process() throws Exception {

        CleanInstanceFileFlowCommandReq req = (CleanInstanceFileFlowCommandReq)getReqParams();

        VmInstance ins = instanceDao.getById(req.getInstanceId());

        CleanInstanceFileMsg msg = new CleanInstanceFileMsg();
        msg.setInstanceId(req.getInstanceId());
        msg.setTaskId(getTaskId());
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
        return CleanInstanceFileFlowCommandReq.class;
    }

    @Override
    protected Class<?> getResParamClass() {
        return CleanInstanceFileFlowCommandRes.class;
    }
}
