package com.gcloud.controller.compute.workflow.vm.base;

import com.gcloud.common.util.StringUtils;
import com.gcloud.controller.compute.dao.InstanceDao;
import com.gcloud.controller.compute.entity.VmInstance;
import com.gcloud.controller.compute.workflow.model.vm.StartInstanceCommandReq;
import com.gcloud.controller.compute.workflow.model.vm.StartInstanceCommandRes;
import com.gcloud.core.messagebus.MessageBus;
import com.gcloud.core.util.MessageUtil;
import com.gcloud.core.workflow.core.BaseWorkFlowCommand;
import com.gcloud.header.compute.enums.VmState;
import com.gcloud.header.compute.msg.node.vm.base.StartInstanceMsg;
import com.gcloud.header.compute.msg.node.vm.base.StopInstanceMsg;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 * Created by yaowj on 2018/11/30.
 */
@Component
@Scope("prototype")
@Slf4j
public class StartInstanceFlowCommand extends BaseWorkFlowCommand {

    @Autowired
    private InstanceDao instanceDao;

    @Autowired
    private MessageBus bus;

    @Override
    protected Object process() throws Exception {
        StartInstanceCommandReq req = (StartInstanceCommandReq)getReqParams();

        VmInstance vm = instanceDao.getById(req.getInstanceId());
        StartInstanceMsg msg = new StartInstanceMsg();
        msg.setTaskId(this.getTaskId());
        msg.setInstanceId(req.getInstanceId());
        msg.setServiceId(MessageUtil.computeServiceId(vm.getHostname()));

        StartInstanceCommandRes res = new StartInstanceCommandRes();
        res.setLastState(vm.getLastState());

        bus.send(msg);

        return res;
    }

    @Override
    protected Object rollback() throws Exception {
        StartInstanceCommandReq req = (StartInstanceCommandReq)getReqParams();
        StartInstanceCommandRes res = (StartInstanceCommandRes)getResParams();

        if((!StringUtils.isBlank(req.getBeginningState()) && req.getBeginningState().equals(VmState.STOPPED.value()))
                || (!StringUtils.isBlank(res.getLastState()) && res.getLastState().equals(VmState.STOPPED.value()))){

            VmInstance vm = instanceDao.getById(req.getInstanceId());
            StopInstanceMsg msg = new StopInstanceMsg();
            msg.setTaskId(this.getTaskId());
            msg.setInstanceId(vm.getId());
            msg.setServiceId(MessageUtil.computeServiceId(vm.getHostname()));

            bus.send(msg);
        }

        return null;
    }

    @Override
    protected Object timeout() throws Exception {
        return null;
    }

    @Override
    protected Class<?> getReqParamClass() {
        return StartInstanceCommandReq.class;
    }

    @Override
    protected Class<?> getResParamClass() {
        return StartInstanceCommandRes.class;
    }
}
