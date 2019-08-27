package com.gcloud.header.compute.msg.api.vm.base;

import com.gcloud.header.ApiReplyMessage;
import com.gcloud.header.compute.msg.api.model.DetailInstanceType;

public class ApiDetailInstanceTypeReplyMsg extends ApiReplyMessage{

	private static final long serialVersionUID = 1L;
	
	private DetailInstanceType detailInstanceType;

	public DetailInstanceType getDetailInstanceType() {
		return detailInstanceType;
	}

	public void setDetailInstanceType(DetailInstanceType detailInstanceType) {
		this.detailInstanceType = detailInstanceType;
	}
}
