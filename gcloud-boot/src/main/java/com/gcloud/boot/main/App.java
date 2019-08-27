package com.gcloud.boot.main;


import org.springframework.boot.SpringApplication;

import com.gcloud.boot.config.ActiveProfiles;
import com.gcloud.boot.config.Application;
import com.gcloud.boot.config.ApplicationWithoutDB;
import com.gcloud.boot.config.ComponentPackages;


/**
 * Hello world!
 *
 */
public class App 
{
	public static void main(String[] args) {
		ActiveProfiles activeProfiles=ActiveProfiles.getInstance();
		activeProfiles.init(args);
		ComponentPackages.getInstance().init(activeProfiles.getIncludes());//初始数据用于fiterCustom过滤
		args=activeProfiles.process(args);
		if(activeProfiles.isWithDb()){
			SpringApplication.run(Application.class, args);
		}else{
			SpringApplication.run(ApplicationWithoutDB.class, args);
		}
	}
	
}
