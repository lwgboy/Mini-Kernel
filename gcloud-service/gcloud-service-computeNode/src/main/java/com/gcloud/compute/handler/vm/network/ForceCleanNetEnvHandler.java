package com.gcloud.compute.handler.vm.network;

import com.gcloud.compute.service.vm.network.IVmNetworkNodeService;
import com.gcloud.core.handle.AsyncMessageHandler;
import com.gcloud.core.handle.Handler;
import com.gcloud.core.messagebus.MessageBus;
import com.gcloud.core.util.ErrorCodeUtil;
import com.gcloud.core.util.MessageUtil;
import com.gcloud.header.compute.msg.node.vm.network.ForceCleanNetEnvConfigMsg;
import com.gcloud.header.compute.msg.node.vm.network.ForceCleanNetEnvConfigReplyMsg;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Created by yaowj on 2018/11/13.
 */
@Slf4j
@Handler
public class ForceCleanNetEnvHandler extends AsyncMessageHandler<ForceCleanNetEnvConfigMsg> {
    @Autowired
    private IVmNetworkNodeService vmNetworkNodeService;

    @Autowired
    private MessageBus bus;

    @Override
    public void handle(ForceCleanNetEnvConfigMsg msg) {

        ForceCleanNetEnvConfigReplyMsg replyMsg = msg.deriveMsg(ForceCleanNetEnvConfigReplyMsg.class);
        replyMsg.setSuccess(false);
        replyMsg.setServiceId(MessageUtil.controllerServiceId());
        try{
            vmNetworkNodeService.forceCleanNetEnvConfig(msg.getInstanceId(), msg.getNetworkDetail());
            replyMsg.setSuccess(true);
        }catch (Exception ex){
            log.error("1010713::清除网络环境配置失败", ex);
            replyMsg.setErrorCode(ErrorCodeUtil.getErrorCode(ex, "1010713::清除网络环境配置失败"));
        }
        bus.send(replyMsg);


    }
}
