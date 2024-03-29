package com.gcloud.header.storage.model;

import java.io.Serializable;

public class DiskConnection implements Serializable {

	private String host;
	private String port;

	public DiskConnection() {

	}

	public DiskConnection(String host, String port) {
		this.host = host;
		this.port = port;
	}

	public String getHost() {
		return host;
	}

	public void setHost(String host) {
		this.host = host;
	}

	public String getPort() {
		return port;
	}

	public void setPort(String port) {
		this.port = port;
	}
}
