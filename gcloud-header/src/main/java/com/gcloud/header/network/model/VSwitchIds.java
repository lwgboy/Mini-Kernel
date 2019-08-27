package com.gcloud.header.network.model;

import java.io.Serializable;
import java.util.List;

public class VSwitchIds implements Serializable {

	private List<String> vSwitchId;

	public List<String> getvSwitchId() {
		return vSwitchId;
	}

	public void setvSwitchId(List<String> vSwitchId) {
		this.vSwitchId = vSwitchId;
	}
	
	
}
