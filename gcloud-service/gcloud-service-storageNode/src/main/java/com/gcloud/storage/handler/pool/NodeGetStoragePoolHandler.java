package com.gcloud.storage.handler.pool;

import org.springframework.beans.factory.annotation.Autowired;

import com.gcloud.core.exception.GCloudException;
import com.gcloud.core.handle.Handler;
import com.gcloud.core.handle.MessageHandler;
import com.gcloud.header.storage.msg.node.pool.NodeGetStoragePoolMsg;
import com.gcloud.header.storage.msg.node.pool.NodeGetStoragePoolReplyMsg;
import com.gcloud.storage.service.IStoragePoolService;

@Handler
public class NodeGetStoragePoolHandler extends MessageHandler<NodeGetStoragePoolMsg, NodeGetStoragePoolReplyMsg>{
	@Autowired
	IStoragePoolService storagePoolService;
	
	@Override
	public NodeGetStoragePoolReplyMsg handle(NodeGetStoragePoolMsg msg) throws GCloudException {
		NodeGetStoragePoolReplyMsg reply = new NodeGetStoragePoolReplyMsg();
		reply.setInfo(storagePoolService.getLocalPoolInfo(msg.getPoolName()));

        reply.setSuccess(true);
        return reply;
	}

}
