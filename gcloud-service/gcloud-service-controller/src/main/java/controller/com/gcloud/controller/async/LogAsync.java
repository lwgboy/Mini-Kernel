package com.gcloud.controller.async;

import com.gcloud.common.util.StringUtils;
import com.gcloud.controller.log.model.LogFeedbackParams;
import com.gcloud.controller.log.service.ILogService;
import com.gcloud.core.async.AsyncBase;
import com.gcloud.core.async.AsyncResult;
import com.gcloud.core.async.AsyncStatus;
import com.gcloud.core.service.SpringUtil;
import com.gcloud.header.log.enums.LogStatus;

/**
 * Created by yaowj on 2018/10/9.
 */
public abstract class LogAsync extends AsyncBase {

    private String taskId;
    private String objectId;

    @Override
    protected AsyncResult run() {

        AsyncResult result = new AsyncResult();
        AsyncStatus asyncStatus = null;

        try {
            result = super.run();
        }catch (Exception ex){
            asyncStatus = AsyncStatus.EXCEPTION;
        }

        //feedback
        if(StringUtils.isNotBlank(taskId)){

            ILogService logService = SpringUtil.getBean(ILogService.class);
            LogFeedbackParams param = new LogFeedbackParams();
            param.setCode(result.getErrorMsg());
            param.setObjectId(objectId);
            LogStatus logStatus = toLogStatus(null == asyncStatus?result.getAsyncStatus():asyncStatus);
            if(logStatus != null){
                param.setStatus(logStatus.getValue());
            }
            param.setTaskId(taskId);
            logService.feedback(param);

        }

        return result;
    }

    public LogStatus toLogStatus(AsyncStatus asyncStatus){

        LogStatus logStatus = null;
        if(asyncStatus != null){
            switch (asyncStatus){
                case SUCCEED:
                    logStatus = LogStatus.COMPLETE;
                    break;
                case RUNNING:
                    logStatus = LogStatus.IN_PROGRESS;
                    break;
                case FAILED:
                    logStatus = LogStatus.FAILED;
                    break;
                case EXCEPTION:
                    logStatus = LogStatus.FAILED;
                    break;
                case TIMEOUT:
                    logStatus = LogStatus.TIMEOUT;
                    break;
                default:
                    logStatus = LogStatus.FAILED;
                    break;
            }
        }

        return logStatus;
    }

    public String getTaskId() {
        return taskId;
    }

    public void setTaskId(String taskId) {
        this.taskId = taskId;
    }

    public String getObjectId() {
        return objectId;
    }

    public void setObjectId(String objectId) {
        this.objectId = objectId;
    }
}
