package com.gcloud.controller.log.util;

import com.gcloud.controller.log.model.LogFeedbackParams;
import com.gcloud.controller.log.service.ILogService;
import com.gcloud.core.service.SpringUtil;
import com.gcloud.header.log.enums.LogStatus;
import com.gcloud.header.log.enums.LogType;

public class LongTaskUtil {

    public static void syncSucc(LogType logType, String taskId){
        syncSucc(logType, taskId, null);
    }

    public static void syncSucc(LogType logType, String taskId, String objectId){
        if(logType == null || !logType.equals(LogType.SYNC)){
            return;
        }

        ILogService logService = SpringUtil.getBean(ILogService.class);
        LogFeedbackParams param = new LogFeedbackParams();
        param.setObjectId(objectId);
        param.setStatus(LogStatus.COMPLETE.getValue());
        param.setTaskId(taskId);
        logService.feedbackAsync(param);

    }

}
