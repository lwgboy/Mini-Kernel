package com.gcloud.core.currentUser.policy.model;

import java.util.List;

public class FilterPolicyModel {
	private List<Object> params;
	private String whereSql;
	public List<Object> getParams() {
		return params;
	}
	public void setParams(List<Object> params) {
		this.params = params;
	}
	public String getWhereSql() {
		return whereSql;
	}
	public void setWhereSql(String whereSql) {
		this.whereSql = whereSql;
	}
}
