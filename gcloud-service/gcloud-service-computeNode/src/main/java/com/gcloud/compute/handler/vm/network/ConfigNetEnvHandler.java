package com.gcloud.compute.handler.vm.network;

import com.gcloud.compute.service.vm.network.IVmNetworkNodeService;
import com.gcloud.core.handle.AsyncMessageHandler;
import com.gcloud.core.handle.Handler;
import com.gcloud.core.messagebus.MessageBus;
import com.gcloud.core.util.ErrorCodeUtil;
import com.gcloud.core.util.MessageUtil;
import com.gcloud.header.compute.msg.node.vm.network.ConfigNetEnvMsg;
import com.gcloud.header.compute.msg.node.vm.network.ConfigNetEnvReplyMsg;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Created by yaowj on 2018/11/15.
 */
@Slf4j
@Handler
public class ConfigNetEnvHandler extends AsyncMessageHandler<ConfigNetEnvMsg> {

    @Autowired
    private IVmNetworkNodeService vmNetworkNodeService;

    @Autowired
    private MessageBus bus;

    @Override
    public void handle(ConfigNetEnvMsg msg) {

        ConfigNetEnvReplyMsg replyMsg = msg.deriveMsg(ConfigNetEnvReplyMsg.class);
        replyMsg.setSuccess(false);
        replyMsg.setServiceId(MessageUtil.controllerServiceId());
        try{
            vmNetworkNodeService.configNetEnv(msg.getInstanceId(), msg.getNetworkDetail());
            replyMsg.setSuccess(true);
        }catch (Exception ex){
            log.error("1010603::配置网络异常", ex);
            replyMsg.setErrorCode(ErrorCodeUtil.getErrorCode(ex, "1010603::配置网络异常"));
        }
        bus.send(replyMsg);

    }
}
