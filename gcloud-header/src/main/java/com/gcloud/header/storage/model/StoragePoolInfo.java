package com.gcloud.header.storage.model;

import java.io.Serializable;

public class StoragePoolInfo implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private long totalSize = 0;
	private long availSize = 0;
	private long usedSize = 0;
	public long getTotalSize() {
		return totalSize;
	}
	public void setTotalSize(long totalSize) {
		this.totalSize = totalSize;
	}
	public long getAvailSize() {
		return availSize;
	}
	public void setAvailSize(long availSize) {
		this.availSize = availSize;
	}
	public long getUsedSize() {
		return usedSize;
	}
	public void setUsedSize(long usedSize) {
		this.usedSize = usedSize;
	}
	
}
