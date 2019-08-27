package com.gcloud.compute.handler.vm.network;

import com.gcloud.compute.service.vm.network.IVmNetworkNodeService;
import com.gcloud.core.handle.AsyncMessageHandler;
import com.gcloud.core.handle.Handler;
import com.gcloud.core.messagebus.MessageBus;
import com.gcloud.core.util.ErrorCodeUtil;
import com.gcloud.core.util.MessageUtil;
import com.gcloud.header.compute.msg.node.vm.network.ForceCleanNetConfigFileMsg;
import com.gcloud.header.compute.msg.node.vm.network.ForceCleanNetConfigFileReplyMsg;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Created by yaowj on 2018/11/13.
 */
@Slf4j
@Handler
public class ForceCleanNetConfigFileHandler extends AsyncMessageHandler<ForceCleanNetConfigFileMsg> {

    @Autowired
    private IVmNetworkNodeService vmNetworkNodeService;

    @Autowired
    private MessageBus bus;

    @Override
    public void handle(ForceCleanNetConfigFileMsg msg) {
        ForceCleanNetConfigFileReplyMsg replyMsg = msg.deriveMsg(ForceCleanNetConfigFileReplyMsg.class);
        replyMsg.setSuccess(false);
        replyMsg.setServiceId(MessageUtil.controllerServiceId());
        try{
            vmNetworkNodeService.forceCleanNetConfigFile(msg.getInstanceId(), msg.getNetworkDetail());
            replyMsg.setSuccess(true);
        }catch (Exception ex){
            log.error("1010711::清除配置文件失败", ex);
            replyMsg.setErrorCode(ErrorCodeUtil.getErrorCode(ex, "1010711::清除配置文件失败"));
        }
        bus.send(replyMsg);

    }
}
