
package com.gcloud.storage.handler.volume;

import com.gcloud.core.handle.AsyncMessageHandler;
import com.gcloud.core.handle.Handler;
import com.gcloud.core.messagebus.MessageBus;
import com.gcloud.core.util.ErrorCodeUtil;
import com.gcloud.core.util.MessageUtil;
import com.gcloud.header.storage.msg.node.volume.NodeDeleteDiskMsg;
import com.gcloud.header.storage.msg.node.volume.NodeDeleteDiskReplyMsg;
import com.gcloud.storage.service.impl.NodeVolumeServiceImpl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

@Slf4j
@Handler
public class NodeDeleteDiskHandler extends AsyncMessageHandler<NodeDeleteDiskMsg> {

    @Autowired
    private NodeVolumeServiceImpl volumeService;

    @Autowired
    private MessageBus bus;

    @Override
    public void handle(NodeDeleteDiskMsg msg) {
        NodeDeleteDiskReplyMsg replyMsg = msg.deriveMsg(NodeDeleteDiskReplyMsg.class);
        replyMsg.setServiceId(MessageUtil.controllerServiceId());
        replyMsg.setVolumeId(msg.getVolumeId());
        try {
            log.info("正在删除存储卷：{}, {}", msg.getStorageType(), msg.getVolumeId());
            this.volumeService.deleteDisk(msg.getStorageType(), msg.getPoolName(), msg.getDriverName(), msg.getVolumeId(), msg.getSnapshots());
            replyMsg.setSuccess(true);
            log.info("删除存储卷成功：{}, {}", msg.getStorageType(), msg.getVolumeId());
        }
        catch (Exception ex) {
            log.error("::删除存储卷失败", ex);
            replyMsg.setSuccess(false);
            replyMsg.setErrorCode(ErrorCodeUtil.getErrorCode(ex, "::删除存储卷失败"));
        }
        this.bus.send(replyMsg);
    }

}
