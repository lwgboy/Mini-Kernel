/*
 * @Date 2015-4-22
 * 
 * @Author dengyf@g-cloud.com.cn
 * 
 * @Copyright 2015 www.g-cloud.com.cn Inc. All rights reserved. 
 * 
 * @Description 自动 生成 ID工具类 ，镜像ID、isoID、云服务器 ID等
 */
package com.gcloud.common.util;

import java.util.zip.Adler32;

import com.google.common.primitives.Longs;


public class GenIDUtil {
	public static String genernateId( final String prefix, final String location ) {
		Adler32 hash = new Adler32();
		String key = location;
		hash.update(key.getBytes());
		
	    /**
	     * @see http://tools.ietf.org/html/rfc3309
	     */
		for ( int i = key.length(); i < 128; i += 8 ) {
			hash.update( Longs.toByteArray( Double.doubleToRawLongBits( Math.random() ) ) );
		}
		String id = String.format( "%s-%08X", prefix, hash.getValue() );
		return id;
	}
}
