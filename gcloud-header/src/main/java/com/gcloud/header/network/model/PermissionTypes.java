package com.gcloud.header.network.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class PermissionTypes  implements Serializable{
	private List<PermissionType> permission = new ArrayList<>();

	public List<PermissionType> getPermission() {
		return permission;
	}

	public void setPermission(List<PermissionType> permission) {
		this.permission = permission;
	}
}
