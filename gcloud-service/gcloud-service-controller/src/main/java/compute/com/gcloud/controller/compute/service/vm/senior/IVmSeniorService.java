package com.gcloud.controller.compute.service.vm.senior;

import com.gcloud.controller.compute.model.vm.VmBundleResponse;
import com.gcloud.header.api.model.CurrentUser;

/**
 * Created by yaowj on 2018/12/3.
 */
public interface IVmSeniorService {

    VmBundleResponse bundle(String instanceId, String imageName, boolean inTask, CurrentUser currentUser);

    void modifyInstanceInit(String instanceId, String instanceType, boolean inTask);
}
