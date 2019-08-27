package com.gcloud.compute.timer;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.gcloud.common.util.StringUtils;
import com.gcloud.compute.lock.VmStartLock;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class AddStartMapTimer {

    @Scheduled(fixedDelay = 2000L)
    public void check(){

        log.debug("AddStartMapTimer begin");

        try {

            while(true){
                String instanceId = VmStartLock.getFirst();
                if(!StringUtils.isBlank(instanceId)){
                    log.debug(String.format("【检测开机序列】 开机队列到开机池, 获取到【%s】 ", instanceId));
                    if(VmStartLock.addConcurrentNum(instanceId)){
                        VmStartLock.removeStartList(instanceId);
                    }else{
                        break;
                    }
                }else{
                    break;
                }
            }

        } catch (Exception e) {
            log.error("添加到startMap失败",e);
        }

        log.debug("AddStartMapTimer end");

    }

}
