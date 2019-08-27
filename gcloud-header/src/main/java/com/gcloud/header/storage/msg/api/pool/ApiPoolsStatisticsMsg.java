package com.gcloud.header.storage.msg.api.pool;

import com.gcloud.header.ApiMessage;
import com.gcloud.header.api.ApiModel;

public class ApiPoolsStatisticsMsg extends ApiMessage{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@ApiModel(description="存储池ID", require=false)
	private String poolId;

	public String getPoolId() {
		return poolId;
	}

	public void setPoolId(String poolId) {
		this.poolId = poolId;
	}

	@Override
	public Class replyClazz() {
		return ApiPoolsStatisticsReplyMsg.class;
	}

}
