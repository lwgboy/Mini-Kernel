package com.gcloud.header.compute.msg.node.vm.create;

import com.gcloud.header.NodeMessage;
import com.gcloud.header.compute.msg.node.vm.model.VmDetail;

public class BuildVmConfigMsg extends NodeMessage {
	private VmDetail vmDetail;
	private String userId;

	public VmDetail getVmDetail() {
		return vmDetail;
	}

	public void setVmDetail(VmDetail vmDetail) {
		this.vmDetail = vmDetail;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}
	
}
