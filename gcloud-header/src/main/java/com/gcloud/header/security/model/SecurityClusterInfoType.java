package com.gcloud.header.security.model;

import java.io.Serializable;

public class SecurityClusterInfoType implements Serializable{
	private static final long serialVersionUID = 1L;

	private Integer id;
    private String clusterId;
    private String hostname;
    private Boolean ha;
    
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getClusterId() {
		return clusterId;
	}
	public void setClusterId(String clusterId) {
		this.clusterId = clusterId;
	}
	public String getHostname() {
		return hostname;
	}
	public void setHostname(String hostname) {
		this.hostname = hostname;
	}
	public Boolean getHa() {
		return ha;
	}
	public void setHa(Boolean ha) {
		this.ha = ha;
	}
}
