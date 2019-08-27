package com.gcloud.header.compute.enums;

public enum CloudDiskTargetDev {
	VDA(0, "vda"),
	VDB(1, "vdb"),
	VDC(2, "vdc"),
	VDD(3, "vdd"),
	VDE(4, "vde"),
	VDF(5, "vdf"),
	VDG(6, "vdg"),
	VDH(7, "vdh"),
	VDI(8, "vdi"),
	VDJ(9, "vdj");
	
	private Integer value;
	private String name;
	
	CloudDiskTargetDev (Integer value, String name) {
		this.value = value;
		this.name = name;
	}

	public Integer getValue() {
		return value;
	}
	
	public String getName(){
		return name;
	}
	
	public static String getDev(Integer index) {
		for(CloudDiskTargetDev dev:CloudDiskTargetDev.values()) {
			if(dev.getValue().equals(index)) {
				return dev.getName();
			}
		}
		return null;
	}
}
