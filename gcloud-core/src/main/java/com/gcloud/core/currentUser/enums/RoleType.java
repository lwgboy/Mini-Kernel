package com.gcloud.core.currentUser.enums;

public enum RoleType {
	SUPER_ADMIN("superadmin", "超级管理员"),
	ORDINARY_USER("ordinaryadmin", "普通管理员");
	
	
	private String roleId;
	private String roleName;
	
	RoleType(String roleId, String roleName){
		this.roleId = roleId;
		this.roleName = roleName;
	}

	public String getRoleId() {
		return roleId;
	}

	public void setRoleId(String roleId) {
		this.roleId = roleId;
	}

	public String getRoleName() {
		return roleName;
	}

	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}
	
}
