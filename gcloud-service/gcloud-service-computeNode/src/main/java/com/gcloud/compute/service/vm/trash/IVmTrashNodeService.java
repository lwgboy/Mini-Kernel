package com.gcloud.compute.service.vm.trash;

/**
 * Created by yaowj on 2018/11/28.
 */
public interface IVmTrashNodeService {

    void cleanInstanceFile(String instanceId);

    void cleanInstanceInfo(String instanceId);

}
