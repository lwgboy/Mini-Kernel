package com.gcloud.header;

public enum Module {
	VM(""),
	ECS("弹性计算"),
	COMPUTE(""),
	USER(""),
	LOG(""),
	SLB("负载均衡"),
//	ENDPOINT("服务管理"),
	TENANT("租户管理");
	String cnName;
	private Module(String cnName) {
		// TODO Auto-generated constructor stub
		this.cnName=cnName;
	}
	public String getCnName(){
		return cnName;
	}
}
