package com.gcloud.controller.compute.service.vm.trash;

/**
 * Created by yaowj on 2018/12/3.
 */
public interface IVmTrashService {

    void delete(String instanceId, boolean inTask, String taskId);

}
