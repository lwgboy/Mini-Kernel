package com.gcloud.controller.network.entity;

import com.gcloud.framework.db.jdbc.annotation.ID;
import com.gcloud.framework.db.jdbc.annotation.Table;

@Table(name="gc_qos_port_policy_bindings", jdbc="controllerJdbcTemplate")
public class QosPortPolicyBinding {

	@ID
	private Long id;
	private String policyId;
	private String portId;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getPolicyId() {
		return policyId;
	}

	public void setPolicyId(String policyId) {
		this.policyId = policyId;
	}

	public String getPortId() {
		return portId;
	}

	public void setPortId(String portId) {
		this.portId = portId;
	}
}
