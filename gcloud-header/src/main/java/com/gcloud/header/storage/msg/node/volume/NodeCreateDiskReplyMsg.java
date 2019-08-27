
package com.gcloud.header.storage.msg.node.volume;

import com.gcloud.header.NodeMessage;

public class NodeCreateDiskReplyMsg extends NodeMessage {

    private static final long serialVersionUID = 1L;

    private String volumeId;

    public String getVolumeId() {
        return volumeId;
    }

    public void setVolumeId(String volumeId) {
        this.volumeId = volumeId;
    }

}
