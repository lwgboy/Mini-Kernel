package com.gcloud.boot.config;


import java.net.InetAddress;
import java.net.UnknownHostException;

import org.springframework.core.env.PropertySource;

public class HostInfoPropertySource extends PropertySource<Object>{
	public static final String HostInfo_PROPERTY_SOURCE_NAME = "hostInfo";
	private static final String PREFIX = "hostInfo";
	public HostInfoPropertySource(String name) {
		super(name, new Object());
		// TODO Auto-generated constructor stub
	}
	public HostInfoPropertySource() {
		this(HostInfo_PROPERTY_SOURCE_NAME);
		// TODO Auto-generated constructor stub
	}

	@Override
	public Object getProperty(String name) {
		// TODO Auto-generated method stub
		if (!name.startsWith(PREFIX)) {
			return null;
		}
		try {
			InetAddress addr = InetAddress.getLocalHost();
			//String ip=addr.getHostAddress().toString(); //获取本机ip  
	        String hostName=addr.getHostName().toString(); //获取本机计算机名称  
	        //return hostName+"-"+ip;
	        return hostName;
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		return null;
	}

}
