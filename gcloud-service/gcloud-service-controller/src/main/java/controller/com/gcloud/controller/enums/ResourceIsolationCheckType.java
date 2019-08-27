package com.gcloud.controller.enums;

import com.gcloud.controller.compute.handler.api.check.InstanceResourceIsolationCheckImpl;
import com.gcloud.controller.storage.handler.api.check.*;
import com.gcloud.controller.image.handler.api.check.ImageResourceIsolationCheckImpl;
import com.gcloud.controller.network.handler.api.check.*;

public enum ResourceIsolationCheckType {
	INSTANCE("instance", InstanceResourceIsolationCheckImpl.class),
	PORT("port", PortResourceIsolationCheckImpl.class),
	VOLUME("volume", VolumeResourceIsolationCheckImpl.class),
	SNAPSHOT("snapshot", SnapshotResourceIsolationCheckImpl.class),
	IMAGE("image", ImageResourceIsolationCheckImpl.class),
	EIP("eip", EipResourceIsolationCheckImpl.class),
	NETWORK("network", NetworkResourceIsolationCheckImpl.class),
	ROUTER("router", RouterResourceIsolationCheckImpl.class),
	SECURITYGROUP("securityGroup", SecurityGroupResourceIsolationCheckImpl.class),
	SECURITYGROUPRULE("securityGroupRule", SecurityGroupRuleResourceIsolationCheckImpl.class),
	SUBNET("subnet", SubnetResourceIsolationCheckImpl.class),
	LOADBALANCER("loadBalancer", null),
	LISTENER("listener", null);
	
	private String resourceType;
	private Class<?> checkClazz;
	
	ResourceIsolationCheckType(String resourceType, Class<?> checkClazz) {
		this.resourceType = resourceType;
		this.checkClazz = checkClazz;
	}
	
	public String getResourceType() {
		return resourceType;
	}

	public Class<?> getCheckClazz() {
		return checkClazz;
	}

	public void setResourceType(String resourceType) {
		this.resourceType = resourceType;
	}

	public void setCheckClazz(Class<?> checkClazz) {
		this.checkClazz = checkClazz;
	}
}
