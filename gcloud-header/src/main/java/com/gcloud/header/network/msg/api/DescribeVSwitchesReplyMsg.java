package com.gcloud.header.network.msg.api;

import java.util.List;

import com.gcloud.header.PageReplyMessage;
import com.gcloud.header.api.ApiModel;
import com.gcloud.header.network.model.DescribeVSwitchesResponse;
import com.gcloud.header.network.model.VSwitchSetType;

public class DescribeVSwitchesReplyMsg extends PageReplyMessage<VSwitchSetType> {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@ApiModel(description = "子网信息")
	DescribeVSwitchesResponse vSwitches;
	
	@Override
	public void setList(List<VSwitchSetType> list) {
		vSwitches = new DescribeVSwitchesResponse();
		vSwitches.setvSwitch(list);
	}

	public DescribeVSwitchesResponse getvSwitches() {
		return vSwitches;
	}

	public void setvSwitches(DescribeVSwitchesResponse vSwitches) {
		this.vSwitches = vSwitches;
	}
	
}
