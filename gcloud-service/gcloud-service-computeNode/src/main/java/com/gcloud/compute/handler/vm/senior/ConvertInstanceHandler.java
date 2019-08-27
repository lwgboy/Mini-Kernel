package com.gcloud.compute.handler.vm.senior;

import com.gcloud.compute.service.vm.senior.IVmSeniorNodeService;
import com.gcloud.core.handle.AsyncMessageHandler;
import com.gcloud.core.handle.Handler;
import com.gcloud.core.messagebus.MessageBus;
import com.gcloud.core.util.ErrorCodeUtil;
import com.gcloud.core.util.MessageUtil;
import com.gcloud.header.compute.enums.FileFormat;
import com.gcloud.header.compute.msg.node.vm.senior.ConvertInstanceMsg;
import com.gcloud.header.compute.msg.node.vm.senior.ConvertInstanceReplyMsg;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Created by yaowj on 2018/11/30.
 */
@Slf4j
@Handler
public class ConvertInstanceHandler extends AsyncMessageHandler<ConvertInstanceMsg> {

    @Autowired
    private IVmSeniorNodeService vmSeniorNodeService;

    @Autowired
    private MessageBus bus;

    @Override
    public void handle(ConvertInstanceMsg msg) {

        ConvertInstanceReplyMsg replyMsg = msg.deriveMsg(ConvertInstanceReplyMsg.class);
        replyMsg.setSuccess(false);
        replyMsg.setServiceId(MessageUtil.controllerServiceId());
        try{
            vmSeniorNodeService.convertToImage(msg.getInstanceId(), FileFormat.value(msg.getTargetFormat()), msg.getVolumeDetail());
            replyMsg.setSuccess(true);
        }catch (Exception ex){
            log.error("云服务器转镜像，转换失败", ex);
            replyMsg.setErrorCode(ErrorCodeUtil.getErrorCode(ex, "1011201::转换失败"));
        }
        bus.send(replyMsg);

    }
}
