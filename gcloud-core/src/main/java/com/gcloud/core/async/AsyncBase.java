package com.gcloud.core.async;

import com.gcloud.core.exception.GCloudException;
import com.gcloud.core.service.SpringUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;

import java.util.UUID;
import java.util.concurrent.Future;

/**
 * Created by yaowj on 2018/9/25.
 */
@Slf4j
public abstract class AsyncBase {

    private String asyncId;

    public abstract long timeout();

    public void successHandler(){
        defaultHandler();
    }
    public void failHandler(){
        defaultHandler();
    }
    public void timeoutHandler(){
        defaultHandler();
    }
    public void exceptionHandler(){
        defaultHandler();
    }
    public void defaultHandler(){

    }

    public long sleepTime(){
        return 100L;
    }
    //改为返回AsyncResult 为满足后续复杂场景逻辑处理。
    //只有返回 RUNNING才会继续 循环。
    //EXCEPTION 是抛出异常后赋值。
    public abstract AsyncResult execute();

    protected AsyncResult run(){

        long timeout = timeout();
        boolean willTimeout = timeout > 0;
        long leftTime = timeout;
        AsyncResult asyncResult = null;
        AsyncStatus asyncStatus = AsyncStatus.RUNNING;
        do{
            long beginTime = System.currentTimeMillis();
            try{
                asyncResult = execute();
                asyncStatus = asyncResult.getAsyncStatus();
            }catch (Exception ex){
                log.error("async run error, uuid:" + this.getAsyncId(), ex);
                asyncStatus = AsyncStatus.EXCEPTION;
            }

            if(asyncStatus != AsyncStatus.RUNNING){
                break;
            }
            leftTime = leftTime - (System.currentTimeMillis() - beginTime);

            if(sleepTime() > 0){
                try{
                    Thread.sleep(sleepTime());
                }catch (Exception ex){
                    log.error("asycn sleep error uuid:" + this.getAsyncId(), ex);
                }
            }

        }while (!willTimeout || leftTime > 0);

        //完成，去掉map里面的数据
        removeAsyncInfo(this.getAsyncId());

        if(willTimeout && leftTime < 0){
            asyncStatus = AsyncStatus.TIMEOUT;
            log.debug("async is timeout, uuid:" + this.getAsyncId());
            this.timeoutHandler();
        }else if(asyncStatus == AsyncStatus.SUCCEED){
            log.debug(String.format("async execute is success, uuid:%s", this.asyncId));
            this.successHandler();
        }else if(asyncStatus == AsyncStatus.FAILED || asyncStatus == null){
            log.debug(String.format("async execute is fail, uuid:%s", this.asyncId));
            this.failHandler();
        }else if(asyncStatus == AsyncStatus.EXCEPTION){
            log.debug(String.format("async execute throws exception , uuid:%s", this.asyncId));
            this.exceptionHandler();
        }

        asyncResult.setAsyncStatus(asyncStatus);

        return asyncResult;
    }

    public void removeAsyncInfo(String id){
        try{
            AsyncThreadPool pool = SpringUtil.getBean(AsyncThreadPool.class);
            if(pool != null){
                pool.removeAsyncTask(id);
            }
        }catch (Exception ex){
            log.error("remove async info error", ex);
        }

    }

    public void start() throws GCloudException{

        AsyncThreadPool pool = SpringUtil.getBean(AsyncThreadPool.class);
        if(pool == null){
            throw new GCloudException("::not support");
        }

        Future<?> future = pool.getExecutor().submit(this::run);
        AsyncInfo asyncInfo = new AsyncInfo(this.getAsyncId(), future, timeout(), System.currentTimeMillis());
        pool.putAsyncTask(this.getAsyncId(), asyncInfo);
    }

    public String getAsyncId(){
        if(StringUtils.isBlank(asyncId)){
            asyncId = UUID.randomUUID().toString();
        }
        return asyncId;
    }

}
