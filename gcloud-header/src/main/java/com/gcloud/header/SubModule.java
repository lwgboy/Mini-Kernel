package com.gcloud.header;

public enum SubModule {
	NONE(null),
	VM("虚拟机实例管理"),
	DISK("磁盘管理"),
	IMAGE("镜像管理"),
	SNAPSHOT("快照管理"),
	VPC("专有网络管理"),
	VROUTER("路由器管理"),
	VSWITCH("交换机管理"),
	SECURITYGROUP("安全组管理"),
	NETWORKINTERFACE("网卡管理"),
	EIPADDRSS("公网弹性IP管理"),
	SECURITYCLUSTER("安全集群"),
	NETWORK("网络管理"),
	NODE("节点管理");
	String cnName;
	private SubModule(String cnName) {
		// TODO Auto-generated constructor stub
		this.cnName=cnName;
	}
	public String getCnName(){
		return cnName;
	}
}
