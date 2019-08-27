package com.gcloud.compute.handler.vm.network;

import com.gcloud.compute.service.vm.network.IVmNetworkNodeService;
import com.gcloud.core.handle.AsyncMessageHandler;
import com.gcloud.core.handle.Handler;
import com.gcloud.core.messagebus.MessageBus;
import com.gcloud.core.util.ErrorCodeUtil;
import com.gcloud.core.util.MessageUtil;
import com.gcloud.header.compute.msg.node.vm.network.ConfigNetFileMsg;
import com.gcloud.header.compute.msg.node.vm.network.ConfigNetFileReplyMsg;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Created by yaowj on 2018/11/15.
 */
@Slf4j
@Handler
public class ConfigNetFileHandler extends AsyncMessageHandler<ConfigNetFileMsg> {

    @Autowired
    private IVmNetworkNodeService vmNetworkNodeService;

    @Autowired
    private MessageBus bus;

    @Override
    public void handle(ConfigNetFileMsg msg) {

        ConfigNetFileReplyMsg replyMsg = msg.deriveMsg(ConfigNetFileReplyMsg.class);
        replyMsg.setSuccess(false);
        replyMsg.setServiceId(MessageUtil.controllerServiceId());
        try{
            vmNetworkNodeService.configNetFile(msg.getInstanceId(), msg.getNetworkDetail());
            replyMsg.setSuccess(true);
        }catch (Exception ex){
            log.error("1010606::配置网络文件异常", ex);
            replyMsg.setErrorCode(ErrorCodeUtil.getErrorCode(ex, "1010606::配置网络文件异常"));
        }
        bus.send(replyMsg);

    }
}
