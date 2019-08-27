package com.gcloud.common.util;

public class CryptoUtil {
	public static String byte2Hex(byte[] b) {
        StringBuilder hs = new StringBuilder();
        String stmp;
        for (int n = 0; b != null && n < b.length; n++) {
            stmp = Integer.toHexString(b[n] & 0XFF);
            if (stmp.length() == 1)
                hs.append('0');
            hs.append(stmp);
        }
        return hs.toString().toUpperCase();
    }

    private static String nullToEmpty(String str){

        return str == null ? "" : str;
    }
}
