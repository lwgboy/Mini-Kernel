package com.gcloud.common.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class NetworkUtil {
	public static String calculateNetmask(int number) {
		int divisor = 8;
		int remainder = number % divisor;
		int multiple = number / divisor;
		StringBuilder sb = new StringBuilder("");
		for (int i = 0; i < multiple; i++) {
			if (i > 0) {
				sb.append(".");
			}
			sb.append("255");
		}
		if (multiple < 4) {
			int tmp = 0;
			while (remainder-- > 0) {
				tmp += 1 << --divisor;
			}
			sb.append(".").append(tmp);
			multiple++;
		}
		if (multiple < 4) {
			for (int i = multiple; i < 4; i++) {
				sb.append(".0");
			}
		}
		return sb.toString();
	}
	
	public static boolean isIpv4(String ipAddress) {

		String ip = "^(1\\d{2}|2[0-4]\\d|25[0-5]|[1-9]\\d|[1-9])\\."
			    +"(00?\\d|1\\d{2}|2[0-4]\\d|25[0-5]|[1-9]\\d|\\d)\\."
			    +"(00?\\d|1\\d{2}|2[0-4]\\d|25[0-5]|[1-9]\\d|\\d)\\."
			    +"(00?\\d|1\\d{2}|2[0-4]\\d|25[0-5]|[1-9]\\d|\\d)$";

		Pattern pattern = Pattern.compile(ip);
		Matcher matcher = pattern.matcher(ipAddress);
		return matcher.matches();

	}

	public static boolean checkCidrIp(String cidr, String value) {
		if (value != null && !"".equals(value) && cidr != null && !"".equals(cidr)) {
			String subnetMask = cidr.substring(cidr.lastIndexOf('/') + 1);
			int subnetRemain = 32 - Integer.parseInt(subnetMask);

			String subnetIp = cidr.substring(0, cidr.lastIndexOf('/'));
			String[] subnetIpArr = subnetIp.split("\\.");
			String subnetIpBin = "";
			for (int i = 0; i < subnetIpArr.length; i++) {
				subnetIpBin = subnetIpBin + String.format("%08d", Integer.parseInt(Integer.toBinaryString(Integer.parseInt(subnetIpArr[i]))));
			}
			String subnetRange = subnetIpBin.substring(0, subnetIpBin.length() - subnetRemain);

			String[] curIpArr = value.split("\\.");
			String curIpBin = "";
			for (int i = 0; i < curIpArr.length; i++) {
				curIpBin = curIpBin + String.format("%08d", Integer.parseInt(Integer.toBinaryString(Integer.parseInt(curIpArr[i]))));
			}

			if (curIpBin.indexOf(subnetRange) != 0) {
				return false;
			}
		}
		return true;
	}
}
