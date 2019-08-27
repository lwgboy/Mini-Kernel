package com.gcloud.common.util;


public class DesPlusUtil {
	private static DESPlus des;
	
	public static DESPlus getDes() throws Exception {
		if(des == null) {
			des = new DESPlus("gc3lo2ud1");
		}
		return des;
	}
	
}
