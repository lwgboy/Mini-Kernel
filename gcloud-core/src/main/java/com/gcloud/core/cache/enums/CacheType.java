package com.gcloud.core.cache.enums;

public enum CacheType {
	ACTIVE_INFO,   // 服务激活信息
	NODE_INFO,     // 节点信息
	TIMER,         // 定时器相关数据
	TOKEN_USER,
	SIGN_USER,
	//资源名称缓存
	SUBNET_NAME,
	NETWORK_NAME,
	PORT_NAME,
	EIP_NAME,
	ROUTER_NAME,
	SECURITYGROUP_NAME,
	LOADBALANCER_NAME,
	VSEVERGROUP_NAME,
	IMAGE_NAME,
	VOLUME_NAME,
	SNAPSHOT_NAME,
	STORAGEPOOL_NAME,
	INSTANCE_ALIAS; //虚拟机别名缓存
}
