
package com.gcloud.controller.storage.handler.node.volume;

import com.gcloud.controller.storage.dao.VolumeDao;
import com.gcloud.core.handle.AsyncMessageHandler;
import com.gcloud.core.handle.Handler;
import com.gcloud.header.storage.enums.VolumeStatus;
import com.gcloud.header.storage.msg.node.volume.NodeCreateDiskReplyMsg;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

@Handler
@Slf4j
public class NodeCreateDiskReplyHandler extends AsyncMessageHandler<NodeCreateDiskReplyMsg> {

    @Autowired
    private VolumeDao volumeDao;

    @Override
    public void handle(NodeCreateDiskReplyMsg msg) {
        if (msg.getSuccess()) {
            this.volumeDao.updateVolumeStatus(msg.getVolumeId(), VolumeStatus.AVAILABLE);
            log.info("创建存储卷成功：{}", msg.getVolumeId());
        }
        else {
            log.info("创建存储卷失败：{} -> {}", msg.getVolumeId(), msg.getErrorCode());
            volumeDao.deleteById(msg.getVolumeId());
        }
    }

}
