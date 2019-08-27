package com.gcloud.controller.compute.service.vm.storage;

public interface IVmDiskService {

    void attachDataDiskInit(String instanceId, String volumeId, boolean inTask);
    void detachDataDiskInit(String instanceId, String volumeId, boolean inTask);

    void detachDataDiskApiCheck(String volumeId);

}
