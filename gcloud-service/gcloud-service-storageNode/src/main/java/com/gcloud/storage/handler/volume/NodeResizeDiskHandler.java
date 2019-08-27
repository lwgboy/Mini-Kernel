
package com.gcloud.storage.handler.volume;

import com.gcloud.core.handle.AsyncMessageHandler;
import com.gcloud.core.handle.Handler;
import com.gcloud.core.messagebus.MessageBus;
import com.gcloud.core.util.ErrorCodeUtil;
import com.gcloud.core.util.MessageUtil;
import com.gcloud.header.storage.msg.node.volume.NodeResizeDiskMsg;
import com.gcloud.header.storage.msg.node.volume.NodeResizeDiskReplyMsg;
import com.gcloud.storage.service.impl.NodeVolumeServiceImpl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

@Slf4j
@Handler
public class NodeResizeDiskHandler extends AsyncMessageHandler<NodeResizeDiskMsg> {

    @Autowired
    private NodeVolumeServiceImpl volumeService;

    @Autowired
    private MessageBus bus;

    @Override
    public void handle(NodeResizeDiskMsg msg) {
        NodeResizeDiskReplyMsg replyMsg = msg.deriveMsg(NodeResizeDiskReplyMsg.class);
        replyMsg.setServiceId(MessageUtil.controllerServiceId());
        replyMsg.setVolumeId(msg.getVolumeId());
        try {
            log.info("正在resize存储卷：{}, {}", msg.getStorageType(), msg.getVolumeId());
            this.volumeService.resizeDisk(msg.getStorageType(), msg.getPoolName(), msg.getDriverName(), msg.getVolumeId(), msg.getOldSize(), msg.getNewSize());
            replyMsg.setSize(msg.getNewSize());
            replyMsg.setSuccess(true);
            log.info("resize存储卷成功：{}, {}", msg.getStorageType(), msg.getVolumeId());
        }
        catch (Exception ex) {
            log.error("::resize存储卷失败", ex);
            replyMsg.setSuccess(false);
            replyMsg.setErrorCode(ErrorCodeUtil.getErrorCode(ex, "::resize存储卷失败"));
        }
        this.bus.send(replyMsg);
    }

}
