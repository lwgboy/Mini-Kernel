package com.gcloud.controller.security.service;

import com.gcloud.controller.security.model.ApiListSecurityCluseterParams;
import com.gcloud.controller.security.model.CreateClusterParams;
import com.gcloud.controller.security.model.CreateClusterResponse;
import com.gcloud.controller.security.model.SecurityClusterAddableInstanceParams;
import com.gcloud.controller.security.model.DescribeSecurityClusterComponentParams;
import com.gcloud.controller.security.model.DescribeSecurityClusterParams;
import com.gcloud.controller.security.model.EnableClusterHaParams;
import com.gcloud.controller.security.model.EnableClusterHaResponse;
import com.gcloud.controller.security.model.ModifySecurityClusterParams;
import com.gcloud.controller.security.model.SecurityClusterAddInstanceParams;
import com.gcloud.controller.security.model.SecurityClusterDetailParams;
import com.gcloud.controller.security.model.SecurityClusterRemoveInstanceParams;
import com.gcloud.controller.security.model.SecurityClusterTopologyParams;
import com.gcloud.framework.db.PageResult;
import com.gcloud.header.api.model.CurrentUser;
import com.gcloud.header.security.model.SecurityClusterComponentType;
import com.gcloud.header.security.model.SecurityClusterDetailResponse;
import com.gcloud.header.security.model.SecurityClusterInstanceType;
import com.gcloud.header.security.model.SecurityClusterListType;
import com.gcloud.header.security.model.SecurityClusterTopologyResponse;
import com.gcloud.header.security.model.SecurityClusterType;

public interface ISecurityClusterService {

    CreateClusterResponse createCluster(CreateClusterParams ccp, CurrentUser currentUser);
    void delete(String id);
    void cleanClusterData(String id);

    EnableClusterHaResponse enableHa(EnableClusterHaParams params, CurrentUser currentUser);
    void disable(String clusterId, CurrentUser currentUser);
    void cleanClusterHaData(String clusterId);
    
    PageResult<SecurityClusterType> describeSecurityCluster(DescribeSecurityClusterParams params, CurrentUser currentUser);
    PageResult<SecurityClusterComponentType> describeSecurityClusterComponent(DescribeSecurityClusterComponentParams params, CurrentUser currentUser);
    PageResult<SecurityClusterInstanceType> describeSecurityClusterAddableInstance(SecurityClusterAddableInstanceParams params, CurrentUser currentUser);
    void modifySecurityCluster(ModifySecurityClusterParams params, CurrentUser currentUser);
    void securityClusterAddInstance(SecurityClusterAddInstanceParams params, CurrentUser currentUser);
    void securityClusterRemoveInstance(SecurityClusterRemoveInstanceParams params, CurrentUser currentUser);
    SecurityClusterDetailResponse securityClusterDetail(SecurityClusterDetailParams params, CurrentUser currentUser);
    SecurityClusterTopologyResponse securityClusterTopology(SecurityClusterTopologyParams params, CurrentUser currentUser);
    PageResult<SecurityClusterListType> apiListSecurityCluseter(ApiListSecurityCluseterParams params, CurrentUser currentUser);
}
