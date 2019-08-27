package com.gcloud.common.util;
import java.security.MessageDigest;
public class MD5Util {
	private final static String salt="aok";
    
    public static String encrypt(String plainText) throws Exception {
//    	plainText += salt;
        MessageDigest md = MessageDigest.getInstance("MD5");
        md.update(plainText.getBytes());
        byte b[] = md.digest();

        return CryptoUtil.byte2Hex(b);
    }
    
}