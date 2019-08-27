package com.gcloud.controller.log.service.impl;

import com.gcloud.common.util.StringUtils;
import com.gcloud.controller.log.dao.LogDao;
import com.gcloud.controller.log.entity.Log;
import com.gcloud.controller.log.model.LogFeedbackParams;
import com.gcloud.controller.log.service.ILogService;
import com.gcloud.core.annotations.CustomAnnotations.GcLog;
import com.gcloud.core.annotations.CustomAnnotations.LongTask;
import com.gcloud.core.error.ErrorInfo;
import com.gcloud.core.exception.GCloudException;
import com.gcloud.core.handle.MessageHandler;
import com.gcloud.core.workflow.dao.IWorkFlowInstanceStepDao;
import com.gcloud.core.workflow.engine.WorkFlowEngine;
import com.gcloud.core.workflow.entity.WorkFlowInstanceStep;
import com.gcloud.core.workflow.enums.FeedbackState;
import com.gcloud.framework.db.PageResult;
import com.gcloud.header.ApiMessage;
import com.gcloud.header.GMessage;
import com.gcloud.header.ReplyMessage;
import com.gcloud.header.compute.msg.api.model.InstanceAttributesType;
import com.gcloud.header.log.LogRecordMsg;
import com.gcloud.header.log.enums.LogResult;
import com.gcloud.header.log.model.LogAttributesType;
import com.gcloud.header.log.model.Task;
import com.gcloud.header.log.msg.api.ApiDescribeLogsMsg;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

@Service
@Transactional
@Slf4j
public class LogService implements ILogService {

    private static final int TRY_TIME = 5;

    private static final int INTERVAL = 3000;

    @Autowired
    private LogDao logDao;

    @Autowired
    private IWorkFlowInstanceStepDao workFlowInstanceStepDao;

    @Override
    public Long save(Log log) {
        // TODO Auto-generated method stub
        return logDao.saveWithIncrementId(log);
    }

    @Async("asyncExecutor")
    public void recordLog(GMessage message, MessageHandler handler, GCloudException ge, Date startTime) {
        GcLog gcLog = (GcLog) handler.getClass().getAnnotation(GcLog.class);
        ApiMessage msg = (ApiMessage) message;
        LongTask longTask = (LongTask) handler.getClass().getAnnotation(LongTask.class);
        boolean sync = !(longTask != null && longTask.value().equals("true"));

        Log log = null;
        log = logDao.findTask(message.getTaskId(), null);
        if (null == log) {
            log = new Log();

            if (null == ge) {
                log.setResult(sync ? LogResult.SUCCESS.getResult() : LogResult.RUNNING.getResult());
                log.setFinalResult(sync ? LogResult.SUCCESS.getResultCn() : LogResult.RUNNING.getResultCn());
                if (sync) {
                    log.setDescription(gcLog.taskExpect());
                    log.setEndTime(new Date());
                    log.setPercent(100);
                }
            } else {
                log.setResult(LogResult.FAIL.getResult());
                log.setFinalResult(LogResult.FAIL.getResultCn());
                ErrorInfo errorInfo = new ErrorInfo(ge);
                log.setDescription(errorInfo.getMessage());
                log.setErrorCode(errorInfo.getCode());
                log.setEndTime(new Date());
            }
        }
        log.setCommand(handler.getClass().getSimpleName().substring(0, handler.getClass().getSimpleName().indexOf("$$")));
        log.setStartTime(startTime);
        log.setFunName(msg.getModule() + "/" + msg.getAction());
        log.setIp(msg.getIp());
        log.setUserId(msg.getCurrentUser().getId());
        log.setUserName(msg.getCurrentUser().getLoginName());
        log.setRemark(msg.getRemark());

        log.setObjectId(message.getObjectId());
        log.setObjectName(message.getObjectName());
        log.setTaskExpect(gcLog.taskExpect());
        log.setTaskId(message.getTaskId());
        if (null != longTask) {
            log.setTimeout(longTask.timeout());
        }

        logDao.saveOrUpdate(log);

    }

    @Override
    @Async("asyncExecutor")
    public void recordMultiLog(GMessage message, MessageHandler handler, GCloudException ge, Date startTime, ReplyMessage reply) {
        ApiMessage msg = (ApiMessage) message;
        LongTask longTask = (LongTask) handler.getClass().getAnnotation(LongTask.class);
        boolean sync = !(longTask != null && longTask.value().equals("true"));

        for (Task task : reply.getTasks()) {
            Log log = null;
            log = logDao.findTask(task.getId(), null);
            if (null == log) {
                log = new Log();
                if (null == ge) {
                    log.setResult(sync ? LogResult.SUCCESS.getResult() : LogResult.RUNNING.getResult());
                    log.setFinalResult(sync ? LogResult.SUCCESS.getResultCn() : LogResult.RUNNING.getResultCn());
                    if (sync) {
                        log.setDescription(task.getExpect());
                        log.setEndTime(new Date());
                    }
                } else {
                    log.setResult(LogResult.FAIL.getResult());
                    log.setFinalResult(LogResult.FAIL.getResultCn());
                    ErrorInfo errorInfo = new ErrorInfo(ge);
                    log.setDescription(errorInfo.getMessage());
                    log.setErrorCode(errorInfo.getCode());
                    log.setEndTime(new Date());
                }
            }

            log.setCommand(handler.getClass().getSimpleName().substring(0, handler.getClass().getSimpleName().indexOf("$$")));
            log.setStartTime(startTime);
            log.setFunName(msg.getModule() + "/" + msg.getAction());
            log.setIp(msg.getIp());
            log.setUserId(msg.getCurrentUser().getId());
            log.setUserName(msg.getCurrentUser().getLoginName());
            log.setRemark(msg.getRemark());

            log.setObjectId(task.getObjectId());
            log.setObjectName(task.getObjectName());
            log.setTaskExpect(task.getExpect());
            log.setTaskId(task.getId());
            if (null != longTask) {
                log.setTimeout(longTask.timeout());
            }

            logDao.saveOrUpdate(log);
        }
    }

    @Override
    @Transactional(propagation = Propagation.NOT_SUPPORTED)
    public void feedback(LogFeedbackParams params) {
        handleFeedback(params);
    }

    @Override
    @Async("asyncExecutor")
    @Transactional(propagation = Propagation.NOT_SUPPORTED)
    public void feedbackAsync(LogFeedbackParams params) {
        handleFeedback(params);
    }

    public void handleFeedback(LogFeedbackParams params){
        log.debug("LogService feedback taskId:" + params.getTaskId() + ",objectId:" + params.getObjectId() + ",status:" + params.getStatus() + ",code:" + params.getCode());
        List<WorkFlowInstanceStep> steps = workFlowInstanceStepDao.getStepsByTaskId(params.getTaskId());
        if (steps.size() > 0) {
            String feedbackStatus = params.getStatus().equals("COMPLETE") ? FeedbackState.SUCCESS.name() : FeedbackState.FAILURE.name();
            WorkFlowEngine.feedbackHandler(params.getTaskId(), feedbackStatus, params.getCode());
            return;
        }
        Log log = null;
        for (int i = 0; i < TRY_TIME; i++) {
            log = logDao.findTask(params.getTaskId(), params.getObjectId());
            if (log == null) {
                if (i < TRY_TIME - 1)
                    try {
                        Thread.sleep(INTERVAL);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
            } else {
                break;
            }
        }
        if (log == null) {
            log = new Log();
        }
        log.setTaskId(params.getTaskId());
        if (null != params.getObjectId()) {
            log.setObjectId(params.getObjectId());
        }

        if (!StringUtils.isBlank(params.getCode())) {
            String[] strs = params.getCode().split("::");
            if (strs.length > 1) {
                log.setErrorCode(strs[0]);
                log.setDescription(strs[1]);
            } else {
                log.setErrorCode(params.getCode());
                log.setDescription("");//CovertError.errorMap.get(code)
            }
        }
        if (params.getStatus().equals("IN_PROGRESS")) {
            log.setResult(LogResult.RUNNING.getResult());
        } else if (params.getStatus().equals("FAILED")) {
            log.setResult(LogResult.FAIL.getResult());
            log.setFinalResult(LogResult.FAIL.getResultCn());
            log.setEndTime(new Date());
        } else if (params.getStatus().equals("COMPLETE")) {
            log.setErrorCode("");
            log.setResult(LogResult.SUCCESS.getResult());
            log.setFinalResult(LogResult.SUCCESS.getResultCn());
            log.setPercent(100);
            log.setDescription(log.getTaskExpect());
            log.setEndTime(new Date());
        } else if (params.getStatus().equals("TIMEOUT")) {
            log.setResult(LogResult.TIMEOUT.getResult());
            log.setFinalResult(LogResult.TIMEOUT.getResultCn());
            log.setDescription(LogResult.TIMEOUT.getResultCn());
            log.setEndTime(new Date());
        }
        logDao.saveOrUpdate(log);
    }


    @Override
    public void logRecord(LogRecordMsg msg) {
        Log log = new Log();
        log.setCommand(msg.getCommand());
        log.setStartTime(msg.getStartTime());
        log.setFunName(msg.getFunName());
        log.setIp(msg.getIp());
        log.setUserId(msg.getCurUserId());
        log.setUserName(msg.getCurUserName());
        log.setRemark(msg.getRemark());
        log.setObjectId(msg.getObjectId());
        log.setObjectName(msg.getObjectName());
        log.setTaskExpect(msg.getTaskExpect());
        log.setTaskId(msg.getTaskId());
        log.setResult(msg.getResult());
        log.setFinalResult(msg.getFinalResult());

        log.setErrorCode(msg.getErrorCode());
        if (msg.getResult().equals(LogResult.SUCCESS.getResult())) {
            log.setDescription(msg.getDescription());
            log.setEndTime(msg.getEndTime());
        } else if (msg.getResult().equals(LogResult.FAIL.getResult())) {
            log.setEndTime(msg.getEndTime());
        }

        logDao.save(log);
    }

	@Override
	public PageResult<LogAttributesType> describeLogs(ApiDescribeLogsMsg msg) {
		return logDao.describeLogs(msg, LogAttributesType.class);
	}

}
