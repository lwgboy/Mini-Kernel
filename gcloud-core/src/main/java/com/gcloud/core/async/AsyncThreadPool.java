package com.gcloud.core.async;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * Created by yaowj on 2018/4/3.
 */
@Component
public class AsyncThreadPool {

    @Value("${gcloud.controller.asyncThreadController.poolSize:20}")
    private Integer poolSize;
    @Value("${gcloud.controller.asyncThreadController.keepAliveTime:60}")
    private Long keepAliveTime;
    @Value("${gcloud.controller.asyncThreadController.checkThread.enable:false}")
    private Boolean enableCheckThread;


    private Map<String, AsyncInfo> asyncTasks = new HashMap<>();

    private ExecutorService executor;

    @PostConstruct
    public void init(){
        ThreadPoolExecutor pool = new ThreadPoolExecutor(poolSize, poolSize, keepAliveTime, TimeUnit.SECONDS, new LinkedBlockingQueue<Runnable>());
        pool.allowCoreThreadTimeOut(true);
        executor = pool;
    }


    public ExecutorService getExecutor(){
        return executor;
    }

    public void putAsyncTask(String id, AsyncInfo asyncInfo){
        if(enableCheckThread){
            asyncTasks.put(id, asyncInfo);
        }
    }

    public void removeAsyncTask(String id){
        if(enableCheckThread){
            asyncTasks.remove(id);
        }
    }

    public Map<String, AsyncInfo> getAsyncTasks() {
        return asyncTasks;
    }

    public void setAsyncTasks(Map<String, AsyncInfo> asyncTasks) {
        this.asyncTasks = asyncTasks;
    }

    public boolean getEnableCheckThread() {
        return enableCheckThread;
    }

    public void setEnableCheckThread(boolean enableCheckThread) {
        this.enableCheckThread = enableCheckThread;
    }
}
