package com.gcloud.header.slb.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class BackendServerResponse implements Serializable{
	private List<BackendServerSetType> backendServer=new ArrayList<>();

	public List<BackendServerSetType> getBackendServer() {
		return backendServer;
	}

	public void setBackendServer(List<BackendServerSetType> backendServer) {
		this.backendServer = backendServer;
	}
}
