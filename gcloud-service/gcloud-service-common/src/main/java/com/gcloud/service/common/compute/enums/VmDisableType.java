/*
 * @Date 2015-11-9
 * 
 * @Author chenyu1@g-cloud.com.cn
 * 
 * @Copyright 2015 www.g-cloud.com.cn Inc. All rights reserved.
 * 
 * 
 */
package com.gcloud.service.common.compute.enums;

public enum VmDisableType {
	DISABLE1(1, "云服务器配置文件的节点不属于本节点"),
	DISABLE2(2, "获取libvirt配置文件内容失败"),
	DISABLE3(3, "获取instance配置文件内容失败"),
	DISABLE4(4, "镜像原文件不存在"),
	DISABLE5(5, "iso原文件不存在"),
	DISABLE6(6, "获取块设备详细信息失败，该卷可能不是被管理的"),
	DISABLE7(7, "块设备的状态错误"),
	DISABLE8(8, "获取块设备的虚拟大小失败"),
	DISABLE9(9, "br不存在或者pre不存在或者aft不存在"),
	DISABLE10(10, "获取网卡信息失败"),
	DISABLE11(11, "云服务器文件夹下面的文件数目少于2"),
	DISABLE12(12, "instance.xml或libvirt.xml文件不存在"),
	DISABLE13(13, "云服务器上的光盘不存在"),
	DISABLE14(14, "ovs网桥不存在")
	;
	
	private Integer value;
	private String disablecn;
	
	VmDisableType(Integer value, String disablecn){
		this.value = value;
		this.disablecn = disablecn;
	}

	public Integer getValue() {
		return value;
	}

	public String getDisablecn() {
		return disablecn;
	}
	public static String getDisablecn(Integer value){
		for(VmDisableType type : VmDisableType.values()){
			if(type.getValue().equals(value)){
				return type.getDisablecn();
			}
		}
		return null;
	}
	
	public static VmDisableType getDisableType(Integer value){
		for(VmDisableType type : VmDisableType.values()){
			if(type.getValue().equals(value)){
				return type;
			}
		}
		return null;
	}
}
