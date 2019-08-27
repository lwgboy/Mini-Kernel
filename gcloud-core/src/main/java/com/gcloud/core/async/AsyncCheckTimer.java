package com.gcloud.core.async;

import com.gcloud.core.service.SpringUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Future;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * Created by yaowj on 2018/9/26.
 * 防止异步里面一直卡着不超时，这里发现如果超时，则直接中断
 */
@Component
@Slf4j
@ConditionalOnExpression("${gcloud.controller.asyncThreadController.checkThread.enable:false} == true")
public class AsyncCheckTimer {

    //比超时时间延迟的时间，默认延迟5分钟
    @Value("${gcloud.controller.asyncThreadController.checkThread.interruptedDelay:300000}")
    private Long interruptedDelay;

    @Scheduled(fixedDelayString = "${gcloud.controller.asyncThreadController.checkThread.scheduleRate:300000}")
    public void check(){

        log.debug("AsyncCheckTimer begin");

        AsyncThreadPool pool = SpringUtil.getBean(AsyncThreadPool.class);
        if(pool == null){
            log.debug("AsyncCheckTimer end, pool is null");
            return;
        }

        log.debug("===pool active count===" + ((ThreadPoolExecutor)pool.getExecutor()).getActiveCount());

        Map<String, AsyncInfo> infoMap = new HashMap<>();
        infoMap.putAll(pool.getAsyncTasks());
        if(infoMap.size() == 0){
            log.debug("AsyncCheckTimer end, there is no thread in info map");
            return;
        }

        for(Map.Entry<String, AsyncInfo> asyncInfo : infoMap.entrySet()){
            try{
                AsyncInfo info = asyncInfo.getValue();
                if(info == null){
                    pool.removeAsyncTask(asyncInfo.getKey());
                }else{
                    Future<?> future = info.getFuture();
                    if(future.isDone()){
                        log.debug("async is done, uuid=" + asyncInfo.getKey());
                        pool.removeAsyncTask(asyncInfo.getKey());
                    }else{

                        //延迟再中断
                        boolean isTimeout = System.currentTimeMillis() - info.getStartTime() > info.getTimeout() + interruptedDelay;
                        if(isTimeout){
                            log.debug("async is timeout, uuid=" + asyncInfo.getKey());
                            future.cancel(true);
                            pool.removeAsyncTask(asyncInfo.getKey());
                            log.debug("async is canceled, uuid=" + asyncInfo.getKey());
                        }

                    }
                }
            }catch (Exception ex){
                log.error("async check error", ex);
            }


        }

        log.debug("AsyncCheckTimer end");

    }

}
