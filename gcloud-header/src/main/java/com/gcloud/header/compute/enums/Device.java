package com.gcloud.header.compute.enums;

public enum Device {

	VDA("vda", "/dev/vda"),
	VDB("vdb", "/dev/vdb"),
	VDZ("vdz", "/dev/vdz"),
	FDA("fda", "/dev/fda"),
	HDC("hdc", "/dev/hdc");
	
	private String value;
	private String mountPath;

	Device(String value, String mountPath) {
		this.value = value;
		this.mountPath = mountPath;
	}

	public String getValue() {
		return value;
	}

	public String getMountPath() {
		return mountPath;
	}
}
