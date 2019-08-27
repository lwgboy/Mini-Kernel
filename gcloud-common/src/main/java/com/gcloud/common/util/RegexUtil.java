package com.gcloud.common.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegexUtil {
	/**1-65535为合法的端口
	 * @param portStr
	 * @return
	 */
	public static boolean isValidPort(String portStr) {
		String regEx = "((^[1-9]\\d{0,3}$)|(^[1-5]\\d{4}$)|(^6[0-4]\\d{3}$)|(^65[0-4]\\d{2}$)|(^655[0-2]\\d$)|(^6553[0-5]$))";
	    Pattern pattern = Pattern.compile(regEx);
		Matcher matcher = pattern.matcher(portStr);
	    return matcher.matches();
	}
}
