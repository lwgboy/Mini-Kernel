package com.gcloud.header.storage.msg.api.pool;

import com.gcloud.header.ApiReplyMessage;

public class ApiPoolsStatisticsReplyMsg extends ApiReplyMessage{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private int poolNum;
	private long poolTotalSize;
	private long poolUsedSize;
	private long poolAvailSize;
	public int getPoolNum() {
		return poolNum;
	}
	public void setPoolNum(int poolNum) {
		this.poolNum = poolNum;
	}
	public long getPoolTotalSize() {
		return poolTotalSize;
	}
	public void setPoolTotalSize(long poolTotalSize) {
		this.poolTotalSize = poolTotalSize;
	}
	public long getPoolUsedSize() {
		return poolUsedSize;
	}
	public void setPoolUsedSize(long poolUsedSize) {
		this.poolUsedSize = poolUsedSize;
	}
	public long getPoolAvailSize() {
		return poolAvailSize;
	}
	public void setPoolAvailSize(long poolAvailSize) {
		this.poolAvailSize = poolAvailSize;
	}
	
}
