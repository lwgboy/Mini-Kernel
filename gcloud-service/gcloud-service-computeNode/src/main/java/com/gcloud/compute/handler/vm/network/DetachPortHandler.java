package com.gcloud.compute.handler.vm.network;

import com.gcloud.compute.service.vm.network.IVmNetworkNodeService;
import com.gcloud.core.handle.AsyncMessageHandler;
import com.gcloud.core.handle.Handler;
import com.gcloud.core.messagebus.MessageBus;
import com.gcloud.core.util.ErrorCodeUtil;
import com.gcloud.core.util.MessageUtil;
import com.gcloud.header.compute.msg.node.vm.network.DetachPortMsg;
import com.gcloud.header.compute.msg.node.vm.network.DetachPortReplyMsg;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Created by yaowj on 2018/11/13.
 */
@Slf4j
@Handler
public class DetachPortHandler extends AsyncMessageHandler<DetachPortMsg> {

    @Autowired
    private IVmNetworkNodeService vmNetworkNodeService;

    @Autowired
    private MessageBus bus;

    @Override
    public void handle(DetachPortMsg msg) {

        DetachPortReplyMsg replyMsg = msg.deriveMsg(DetachPortReplyMsg.class);
        replyMsg.setSuccess(false);
        replyMsg.setServiceId(MessageUtil.controllerServiceId());
        try{
            vmNetworkNodeService.detachPort(msg.getInstanceId(), msg.getNetworkDetail());
            replyMsg.setSuccess(true);
        }catch (Exception ex){
            log.error("1010701::卸载网卡失败", ex);
            replyMsg.setErrorCode(ErrorCodeUtil.getErrorCode(ex, "1010701::卸载网卡失败"));
        }
        bus.send(replyMsg);

    }
}
