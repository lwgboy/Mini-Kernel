
package com.gcloud.controller.storage.handler.node.volume;

import com.gcloud.controller.storage.service.IVolumeService;
import com.gcloud.core.handle.AsyncMessageHandler;
import com.gcloud.core.handle.Handler;
import com.gcloud.header.storage.msg.node.volume.NodeDeleteDiskReplyMsg;

import org.springframework.beans.factory.annotation.Autowired;

@Handler
public class NodeDeleteDiskReplyHandler extends AsyncMessageHandler<NodeDeleteDiskReplyMsg> {

    @Autowired
    private IVolumeService volumeService;

    @Override
    public void handle(NodeDeleteDiskReplyMsg msg) {
        if (msg.getSuccess()) {
            this.volumeService.handleDeleteVolumeSuccess(msg.getVolumeId());
        }
        else {
            this.volumeService.handleDeleteVolumeFailed(msg.getErrorCode(), msg.getVolumeId());
        }
    }

}
