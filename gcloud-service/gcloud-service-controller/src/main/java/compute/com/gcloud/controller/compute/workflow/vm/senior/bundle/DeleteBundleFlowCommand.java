package com.gcloud.controller.compute.workflow.vm.senior.bundle;

import com.gcloud.controller.compute.dao.InstanceDao;
import com.gcloud.controller.compute.entity.VmInstance;
import com.gcloud.controller.compute.workflow.model.senior.DeleteBundleFlowCommandReq;
import com.gcloud.controller.compute.workflow.model.senior.DeleteBundleFlowCommandRes;
import com.gcloud.core.messagebus.MessageBus;
import com.gcloud.core.util.MessageUtil;
import com.gcloud.core.workflow.core.BaseWorkFlowCommand;
import com.gcloud.header.compute.msg.node.vm.senior.DeleteBundleMsg;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 * Created by yaowj on 2019/1/29.
 */
@Component
@Scope("prototype")
@Slf4j
public class DeleteBundleFlowCommand extends BaseWorkFlowCommand {

    @Autowired
    private MessageBus bus;

    @Autowired
    private InstanceDao instanceDao;

    @Override
    protected Object process() throws Exception {

        DeleteBundleFlowCommandReq req = (DeleteBundleFlowCommandReq)getReqParams();

        VmInstance ins = instanceDao.getById(req.getInstanceId());

        DeleteBundleMsg msg = new DeleteBundleMsg();
        msg.setTaskId(getTaskId());
        msg.setInstanceId(req.getInstanceId());
        msg.setNodeIp(req.getNodeIp());
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
        return DeleteBundleFlowCommandReq.class;
    }

    @Override
    protected Class<?> getResParamClass() {
        return DeleteBundleFlowCommandRes.class;
    }
}
