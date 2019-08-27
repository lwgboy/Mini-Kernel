package com.gcloud.header.storage.msg.node.pool;

import com.gcloud.header.ReplyMessage;
import com.gcloud.header.storage.model.StoragePoolInfo;

public class NodeGetStoragePoolReplyMsg  extends ReplyMessage{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private StoragePoolInfo info;

	public StoragePoolInfo getInfo() {
		return info;
	}

	public void setInfo(StoragePoolInfo info) {
		this.info = info;
	}
	
}
