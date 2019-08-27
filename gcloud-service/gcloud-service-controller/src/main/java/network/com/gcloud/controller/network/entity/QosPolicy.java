package com.gcloud.controller.network.entity;

import com.gcloud.framework.db.jdbc.annotation.ID;
import com.gcloud.framework.db.jdbc.annotation.Table;

@Table(name="gc_qos_policies", jdbc="controllerJdbcTemplate")
public class QosPolicy {
	@ID
	private String id;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}
}
