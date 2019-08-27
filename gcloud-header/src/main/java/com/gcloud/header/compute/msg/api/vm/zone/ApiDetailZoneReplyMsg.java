package com.gcloud.header.compute.msg.api.vm.zone;

import com.gcloud.header.ApiReplyMessage;
import com.gcloud.header.compute.msg.api.model.DetailZone;

public class ApiDetailZoneReplyMsg extends ApiReplyMessage{

	private static final long serialVersionUID = 1L;
	
	private DetailZone detailZone;

	public DetailZone getDetailZone() {
		return detailZone;
	}

	public void setDetailZone(DetailZone detailZone) {
		this.detailZone = detailZone;
	}
}
