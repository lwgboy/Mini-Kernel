package com.gcloud.header.compute.enums;

public enum ComputeNodeState {
	CONNECTED("正常", 1), 
	DISCONNECTED("失去连接", 0);

	private ComputeNodeState(String cnName, int value) {
		this.cnName = cnName;
		this.value = value;
	}

	private int value;
	private String cnName;

	public int getValue() {
		return this.value;
	}

	public String getCnName() {
		return cnName;
	}

	/**
	 * 
	 * @Title: getCnName
	 * @Description: 根据value获取cnName
	 * @date 2015-4-25 上午11:31:30
	 *
	 * @param value
	 * @return
	 */
	public static String getCnName(int value) {
		for (ComputeNodeState nodeState : ComputeNodeState.values()) {
			if (nodeState.getValue() == value) {
				return nodeState.cnName;
			}
		}
		return null;
	}
	
	public void setCnName(String cnName) {
		this.cnName = cnName;
	}
}
