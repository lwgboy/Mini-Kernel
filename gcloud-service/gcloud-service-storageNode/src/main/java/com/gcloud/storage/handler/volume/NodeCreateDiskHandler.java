
package com.gcloud.storage.handler.volume;

import com.gcloud.core.handle.AsyncMessageHandler;
import com.gcloud.core.handle.Handler;
import com.gcloud.core.messagebus.MessageBus;
import com.gcloud.core.util.ErrorCodeUtil;
import com.gcloud.core.util.MessageUtil;
import com.gcloud.header.storage.msg.node.volume.NodeCreateDiskMsg;
import com.gcloud.header.storage.msg.node.volume.NodeCreateDiskReplyMsg;
import com.gcloud.storage.service.impl.NodeVolumeServiceImpl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

@Slf4j
@Handler
public class NodeCreateDiskHandler extends AsyncMessageHandler<NodeCreateDiskMsg> {

    @Autowired
    private NodeVolumeServiceImpl volumeService;

    @Autowired
    private MessageBus bus;

    @Override
    public void handle(NodeCreateDiskMsg msg) {
        NodeCreateDiskReplyMsg replyMsg = msg.deriveMsg(NodeCreateDiskReplyMsg.class);
        replyMsg.setServiceId(MessageUtil.controllerServiceId());
        replyMsg.setVolumeId(msg.getVolumeId());
        try {
            log.info("正在创建存储卷：{}, {}", msg.getStorageType(), msg.getVolumeId());
            this.volumeService.createDisk(msg.getStorageType(), msg.getPoolName(), msg.getDriverName(), msg.getVolumeId(), msg.getSize(), msg.getImageId());
            replyMsg.setSuccess(true);
            log.info("创建存储卷成功：{}, {}", msg.getStorageType(), msg.getVolumeId());
        }
        catch (Exception ex) {
            log.error("::创建存储卷失败", ex);
            replyMsg.setSuccess(false);
            replyMsg.setErrorCode(ErrorCodeUtil.getErrorCode(ex, "::创建存储卷失败"));
        }
        this.bus.send(replyMsg);
    }

}
