package com.gcloud.api.region;

public class Region {
	private String id;
	private String name;
	private boolean local;
	private String remoteAddress;
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public boolean isLocal() {
		return local;
	}
	public void setLocal(boolean local) {
		this.local = local;
	}
	public String getRemoteAddress() {
		return remoteAddress;
	}
	public void setRemoteAddress(String remoteAddress) {
		this.remoteAddress = remoteAddress;
	}
}
