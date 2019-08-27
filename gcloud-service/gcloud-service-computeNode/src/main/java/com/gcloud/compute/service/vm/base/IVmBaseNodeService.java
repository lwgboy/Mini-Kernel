package com.gcloud.compute.service.vm.base;

import com.gcloud.core.exception.GCloudException;

/**
 * Created by yaowj on 2018/9/7.
 */
public interface IVmBaseNodeService {

	void startup(String instanceId);

	void startupDesktop(String instanceId);

	void reboot(String instanceId, Boolean forceStop);

	void stop(String instanceId);

	String vmGcloudState(String instanceId);

	void destroy(String instanceId);

	void destroyIfExist(String instanceId);

	void configInstanceResource(String instanceId, Integer cpu, Integer memory, Integer orgCpu, Integer orgMemory);
	
	void changePassword(String instanceId, String password) throws GCloudException;

	String queryVnc(String instanceId);
	
	void changeHostName(String instanceId, String hostname) throws GCloudException;
	
	Boolean checkVmReady(String instanceId, int timeout) throws GCloudException;
}
