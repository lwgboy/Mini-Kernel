package com.gcloud.header.storage.msg.node.pool;

import com.gcloud.header.NeedReplyMessage;

public class NodeGetStoragePoolMsg extends NeedReplyMessage{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String storageType;
    private String poolName;
    private String driverName;
	public String getStorageType() {
		return storageType;
	}
	public void setStorageType(String storageType) {
		this.storageType = storageType;
	}
	public String getPoolName() {
		return poolName;
	}
	public void setPoolName(String poolName) {
		this.poolName = poolName;
	}
	public String getDriverName() {
		return driverName;
	}
	public void setDriverName(String driverName) {
		this.driverName = driverName;
	}
	@Override
	public Class replyClazz() {
		return NodeGetStoragePoolReplyMsg.class;
	}
    
}
