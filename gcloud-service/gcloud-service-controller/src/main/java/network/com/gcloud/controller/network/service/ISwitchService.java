package com.gcloud.controller.network.service;

import com.gcloud.controller.network.model.DescribeVSwitchesParams;
import com.gcloud.framework.db.PageResult;
import com.gcloud.header.api.model.CurrentUser;
import com.gcloud.header.network.model.VSwitchSetType;
import com.gcloud.header.network.msg.api.CreateVSwitchMsg;

public interface ISwitchService {

    PageResult<VSwitchSetType> describeVSwitches(DescribeVSwitchesParams params, CurrentUser currentUser);

    String createVSwitch(CreateVSwitchMsg params, CurrentUser currentUser);

    void deleteVSwitch(String subnetId);

}
