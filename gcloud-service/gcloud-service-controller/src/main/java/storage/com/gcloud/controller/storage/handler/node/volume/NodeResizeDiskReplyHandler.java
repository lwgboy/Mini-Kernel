
package com.gcloud.controller.storage.handler.node.volume;

import com.gcloud.controller.storage.service.IVolumeService;
import com.gcloud.core.handle.AsyncMessageHandler;
import com.gcloud.core.handle.Handler;
import com.gcloud.header.storage.msg.node.volume.NodeResizeDiskReplyMsg;

import org.springframework.beans.factory.annotation.Autowired;

@Handler
public class NodeResizeDiskReplyHandler extends AsyncMessageHandler<NodeResizeDiskReplyMsg> {

    @Autowired
    private IVolumeService volumeService;

    @Override
    public void handle(NodeResizeDiskReplyMsg msg) {
        if (msg.getSuccess()) {
            this.volumeService.handleResizeVolumeSuccess(msg.getVolumeId(), msg.getSize());
        }
        else {
            this.volumeService.handleResizeVolumeFailed(msg.getErrorCode(), msg.getVolumeId());
        }
    }

}
