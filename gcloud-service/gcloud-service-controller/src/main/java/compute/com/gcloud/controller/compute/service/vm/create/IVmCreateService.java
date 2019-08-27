package com.gcloud.controller.compute.service.vm.create;

import com.gcloud.controller.compute.model.vm.CreateInstanceByImageInitParams;
import com.gcloud.controller.compute.model.vm.CreateInstanceByImageInitResponse;
import com.gcloud.header.api.model.CurrentUser;

public interface IVmCreateService {
	CreateInstanceByImageInitResponse createInstanceByImageInit(CreateInstanceByImageInitParams req, CurrentUser currentUser);
}
