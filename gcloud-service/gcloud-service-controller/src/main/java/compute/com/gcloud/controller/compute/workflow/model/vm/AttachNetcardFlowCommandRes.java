package com.gcloud.controller.compute.workflow.model.vm;

import com.gcloud.header.compute.msg.node.vm.model.VmNetworkDetail;

public class AttachNetcardFlowCommandRes {
	private VmNetworkDetail vmNetwork;

	public VmNetworkDetail getVmNetwork() {
		return vmNetwork;
	}

	public void setVmNetwork(VmNetworkDetail vmNetwork) {
		this.vmNetwork = vmNetwork;
	}

}
