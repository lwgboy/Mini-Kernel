package com.gcloud.controller.compute.service.vm.base;

import java.util.List;

import com.gcloud.controller.compute.handler.api.model.DescribeInstanceTypesParams;
import com.gcloud.controller.compute.handler.api.model.DescribeInstancesParams;
import com.gcloud.controller.compute.model.vm.AssociateInstanceTypeParams;
import com.gcloud.controller.compute.model.vm.CreateInstanceTypeParams;
import com.gcloud.controller.compute.model.vm.DeleteInstanceTypeParams;
import com.gcloud.controller.compute.model.vm.DetailInstanceTypeParams;
import com.gcloud.controller.compute.model.vm.ModifyInstanceTypeParams;
import com.gcloud.core.exception.GCloudException;
import com.gcloud.framework.db.PageResult;
import com.gcloud.header.api.model.CurrentUser;
import com.gcloud.header.compute.msg.api.model.DetailInstanceType;
import com.gcloud.header.compute.msg.api.model.InstanceAttributesType;
import com.gcloud.header.compute.msg.api.model.InstanceTypeItemType;

public interface IVmBaseService {

    void startInstance(String instanceId) throws GCloudException;

    void startInstance(String instanceId, boolean inTask, boolean handleResource) throws GCloudException;

    void stopInstance(String instanceId) throws GCloudException;

    void stopInstance(String instanceId, boolean inTask, boolean handleResource) throws GCloudException;

    void rebootInstance(String instanceId, Boolean forceStop) throws GCloudException;

    void rebootInstance(String instanceId, Boolean forceStop, boolean inTask) throws GCloudException;

    List<InstanceTypeItemType> describeInstanceTypes(DescribeInstanceTypesParams params);
    
    void modifyInstanceAttribute(String instanceId, String instanceName, String password, String taskId);
    
    PageResult<InstanceAttributesType> describeInstances(DescribeInstancesParams params, CurrentUser currentUser);

    void cleanState(String instanceId, Boolean inTask);

    String queryInstanceVNC(String instanceId);
    
    void createInstanceType(CreateInstanceTypeParams params, CurrentUser currentUser);
    void deleteInstanceType(DeleteInstanceTypeParams params, CurrentUser currentUser);
    void modifyInstanceType(ModifyInstanceTypeParams params, CurrentUser currentUser);
    DetailInstanceType detailInstanceType(DetailInstanceTypeParams params, CurrentUser currentUser);
    void associateInstanceType(AssociateInstanceTypeParams params, CurrentUser currentUser);
    
}
