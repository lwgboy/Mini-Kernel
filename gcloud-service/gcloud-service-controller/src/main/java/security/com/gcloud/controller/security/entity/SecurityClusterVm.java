package com.gcloud.controller.security.entity;

import com.gcloud.framework.db.jdbc.annotation.ID;
import com.gcloud.framework.db.jdbc.annotation.Table;

@Table(name = "gc_security_cluster_vm")
public class SecurityClusterVm {
	
	@ID
	private String id;
	private String clusterId;
	private String instanceId;
	
	public static final String ID = "id";
	public static final String CLUSTER_ID = "clusterId";
	public static final String INSTANCE_ID = "instanceId";
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getClusterId() {
		return clusterId;
	}
	public void setClusterId(String clusterId) {
		this.clusterId = clusterId;
	}
	public String getInstanceId() {
		return instanceId;
	}
	public void setInstanceId(String instanceId) {
		this.instanceId = instanceId;
	}
	
	public String updateId (String id) {
		this.id = id;
		return ID;
	}
	
	public String updateClusterId(String clusterId) {
		this.clusterId = clusterId;
		return CLUSTER_ID;
	}
	
	public String updateInstanceId(String instanceId) {
		this.instanceId = instanceId;
		return INSTANCE_ID;
	}
	
}
