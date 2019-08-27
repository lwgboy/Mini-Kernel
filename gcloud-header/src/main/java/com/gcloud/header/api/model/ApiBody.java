package com.gcloud.header.api.model;

import java.util.List;

public class ApiBody {

	private String name;
	private String url;
	private String requestMethod;
	private String permissionCode;
	private String[] permissions;
	private List<ApiModel> requestParam;  // 参数类
	private List<ApiModel> requestBody;  // 参数类
	private List<ApiModel> response;  // 返回类
	private String description;
	private boolean admin;
	private boolean common;
	
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public List<ApiModel> getRequestParam() {
		return requestParam;
	}
	public void setRequestParam(List<ApiModel> requestParam) {
		this.requestParam = requestParam;
	}
	public List<ApiModel> getRequestBody() {
		return requestBody;
	}
	public void setRequestBody(List<ApiModel> requestBody) {
		this.requestBody = requestBody;
	}
	public List<ApiModel> getResponse() {
		return response;
	}
	public void setResponse(List<ApiModel> response) {
		this.response = response;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String[] getPermissions() {
		return permissions;
	}
	public void setPermissions(String[] permissions) {
		this.permissions = permissions;
	}
	public boolean isAdmin() {
		return admin;
	}
	public void setAdmin(boolean admin) {
		this.admin = admin;
	}
	public boolean isCommon() {
		return common;
	}
	public void setCommon(boolean common) {
		this.common = common;
	}
	public String getPermissionCode() {
		return permissionCode;
	}
	public void setPermissionCode(String permissionCode) {
		this.permissionCode = permissionCode;
	}
	public String getRequestMethod() {
		return requestMethod;
	}
	public void setRequestMethod(String requestMethod) {
		this.requestMethod = requestMethod;
	}
}
