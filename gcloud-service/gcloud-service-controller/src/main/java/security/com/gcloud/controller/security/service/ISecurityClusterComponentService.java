package com.gcloud.controller.security.service;

import com.gcloud.controller.security.entity.SecurityClusterComponent;
import com.gcloud.controller.security.enums.SecurityClusterComponentObjectType;
import com.gcloud.controller.security.enums.SecurityComponent;
import com.gcloud.controller.security.model.ComputeClusterCreateObjectInfo;
import com.gcloud.header.api.model.CurrentUser;
import com.gcloud.header.security.msg.model.CreateClusterDcParams;
import com.gcloud.header.security.msg.model.CreateClusterInfoParams;
import com.gcloud.header.security.msg.model.CreateClusterObjectParams;
import com.gcloud.header.security.msg.model.CreateClusterVmParams;

import java.util.List;
import java.util.Map;

public interface ISecurityClusterComponentService {

    ComputeClusterCreateObjectInfo createClusterComponentVm(String clusterId, String clusterName, CurrentUser loginUser, CreateClusterInfoParams clusterInfoParams, CreateClusterVmParams vmParams, SecurityComponent type, Boolean isHa);
    ComputeClusterCreateObjectInfo createClusterComponentDc(String clusterId, String clusterName, CurrentUser loginUser, CreateClusterInfoParams clusterInfoParams, CreateClusterDcParams dcParams, SecurityComponent type, Boolean isHa);
    Map<SecurityClusterComponentObjectType, Map<String, CreateClusterObjectParams>> getComponentConfig(List<SecurityClusterComponent> components, CreateClusterInfoParams createInfo);

}
