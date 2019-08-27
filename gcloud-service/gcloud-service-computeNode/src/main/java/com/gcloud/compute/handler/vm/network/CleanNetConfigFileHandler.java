package com.gcloud.compute.handler.vm.network;

import com.gcloud.compute.service.vm.network.IVmNetworkNodeService;
import com.gcloud.core.handle.AsyncMessageHandler;
import com.gcloud.core.handle.Handler;
import com.gcloud.core.messagebus.MessageBus;
import com.gcloud.core.util.ErrorCodeUtil;
import com.gcloud.core.util.MessageUtil;
import com.gcloud.header.compute.msg.node.vm.network.CleanNetConfigFileMsg;
import com.gcloud.header.compute.msg.node.vm.network.CleanNetConfigFileReplyMsg;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Created by yaowj on 2018/11/13.
 */
@Slf4j
@Handler
public class CleanNetConfigFileHandler extends AsyncMessageHandler<CleanNetConfigFileMsg> {

    @Autowired
    private IVmNetworkNodeService vmNetworkNodeService;

    @Autowired
    private MessageBus bus;

    @Override
    public void handle(CleanNetConfigFileMsg msg) {
        CleanNetConfigFileReplyMsg replyMsg = msg.deriveMsg(CleanNetConfigFileReplyMsg.class);
        replyMsg.setSuccess(false);
        replyMsg.setServiceId(MessageUtil.controllerServiceId());
        try{
            vmNetworkNodeService.cleanNetConfigFile(msg.getInstanceId(), msg.getNetworkDetail());
            replyMsg.setSuccess(true);
        }catch (Exception ex){
            log.error("1010705::清除配置文件失败", ex);
            replyMsg.setErrorCode(ErrorCodeUtil.getErrorCode(ex, "1010705::清除配置文件失败"));
        }
        bus.send(replyMsg);

    }
}
