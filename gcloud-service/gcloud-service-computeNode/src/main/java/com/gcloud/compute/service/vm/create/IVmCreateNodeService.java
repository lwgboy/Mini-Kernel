package com.gcloud.compute.service.vm.create;

import com.gcloud.header.compute.msg.node.vm.model.VmDetail;

/*
 * @Date Oct 31, 2018
 * 
 * @Author zhangzj
 * 
 * @Copyright 2018 www.g-cloud.com.cn Inc. All rights reserved.
 * 
 * @Description TODO
 */
public interface IVmCreateNodeService {

	void checkCreateVmNodeEnv(String userId, String instanceId);

	void connectVolume();

	void createDomain(String instanceId, String userId);

	void buildVmConfig(VmDetail vmIns, String userId);

}
