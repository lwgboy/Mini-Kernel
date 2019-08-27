package com.gcloud.controller.network.service;

import com.gcloud.controller.compute.entity.VmInstance;
import com.gcloud.controller.network.entity.Port;
import com.gcloud.controller.network.model.CreatePortParams;
import com.gcloud.controller.network.model.DescribeNetworkInterfacesParams;
import com.gcloud.framework.db.PageResult;
import com.gcloud.header.api.model.CurrentUser;
import com.gcloud.header.compute.msg.node.vm.model.VmNetworkDetail;
import com.gcloud.header.enums.ProviderType;
import com.gcloud.header.network.model.NetworkInterfaceSet;

import java.util.List;

/**
 * Created by yaowj on 2018/10/25.
 */
public interface IPortService {

    String create(CreatePortParams params, CurrentUser currentUser);

    void update(String portId, List<String> securityGroupIds, String portName);
    void updatePort(String portId, List<String> securityGroupIds, String portName);
    void updatePort(Port port);

    void delete(String portId);

    void updateQos(String portId, Integer egress, Integer ingress);
    void updatePortQos(String portId, Integer egress, Integer ingress);

    void attachPort(VmInstance instance, String portId, String customOvsBr, Boolean noArpLimit);

    PageResult<NetworkInterfaceSet> describe(DescribeNetworkInterfacesParams params, CurrentUser currentUser);

    VmNetworkDetail getNetworkDetail(String portId);

    void cleanPortData(String port);
    void createPortData(CreatePortParams params, ProviderType provider, CurrentUser currentUser, String portId, String portRefId, String macAddress, String state, String ipAddress);

    void detachDone(Port port);
}
