package com.gcloud.controller.compute.service.vm.netowork;

/**
 * Created by yaowj on 2018/11/15.
 */
public interface IVmNetworkService {

//    void attachPortCheck(String instanceId, String portId);
//    void attachPortInit(String instanceId, String portId);

    void attachPortInit(String instanceId, String portId, String customOvsBr, Boolean noArpLimit, boolean inTask);
    void detachPortInit(String instanceId, String portId, boolean inTask);

    void detachDone(String instanceId, String portId, boolean inTask);

}
