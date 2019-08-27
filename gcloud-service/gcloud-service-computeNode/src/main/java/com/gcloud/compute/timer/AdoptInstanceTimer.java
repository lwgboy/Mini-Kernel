package com.gcloud.compute.timer;

import com.gcloud.compute.cache.cache.VmExceptionCache;
import com.gcloud.compute.cache.cache.VmInstancesCache;
import com.gcloud.compute.service.vm.base.IVmAdoptNodeService;
import com.gcloud.core.messagebus.MessageBus;
import com.gcloud.header.compute.msg.node.vm.model.VmDetail;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

//@Component
@Slf4j
public class AdoptInstanceTimer {

    @Autowired
    private IVmAdoptNodeService adoptNodeService;

    @Autowired
    private MessageBus bus;

    private static boolean isInit = true;

    @Scheduled(fixedDelay = 10000L)
    public void check(){

        log.debug("AdoptVmsTimer begin");

        try {

            isInit = adoptNodeService.adoptVms(isInit);

        } catch (Exception e) {
            log.error("添加到startMap失败",e);
        }

        Map<String,VmDetail> vmMap = VmInstancesCache.getAll();
        List<VmDetail> vms = new ArrayList<>();
        for(String key : vmMap.keySet()){
            vms.add(vmMap.get(key));
        }

        Map<String,VmDetail> exceptionVmMap = VmExceptionCache.getAll();
        List<VmDetail> exceptionVms = new ArrayList<VmDetail>();
        for(String key : exceptionVmMap.keySet()){
            exceptionVms.add(exceptionVmMap.get(key));
        }


//        try{
//            AdoptInstanceMsg msg = new AdoptInstanceMsg();
//            msg.setServiceId(MessageUtil.controllerServiceId());
//            msg.setInstances(vms);
//            msg.setExceptionVms(exceptionVms);
//            bus.send(msg);
//
//        }catch (Exception e) {
//            log.error("DescribeVmsResponseCommand异步发送失败", e);
//        }

        log.debug("AdoptVmsTimer end");

    }

}
