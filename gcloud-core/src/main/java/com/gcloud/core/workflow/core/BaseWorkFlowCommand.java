package com.gcloud.core.workflow.core;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.gcloud.core.exception.GCloudException;
import com.gcloud.core.service.SpringUtil;
import com.gcloud.core.workflow.engine.WorkFlowEngine;
import com.gcloud.core.workflow.entity.WorkFlowInstanceStep;
import com.gcloud.core.workflow.enums.FeedbackState;
import com.gcloud.core.workflow.mng.IWorkFlowInstanceStepMng;
import com.gcloud.core.workflow.model.WorkflowFirstStepResException;
import com.gcloud.core.workflow.util.WorkFlowUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.scheduling.annotation.Async;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
@Slf4j
public abstract class BaseWorkFlowCommand {
	protected abstract Object process() throws Exception;
	
	//异步步骤feedback时才有后续处理--这里去掉，由计算节点feedback回来的消息自行执行
//	protected abstract Object postProcess(Map<String, Object> params) throws Exception;
	
	//异步步骤执行完之后在下一步调用之前调用
//	protected abstract Object postProcess() throws Exception;
	
	protected abstract Object rollback() throws Exception;
	
	protected abstract Object timeout() throws Exception;
	
	protected abstract Class<?> getReqParamClass();
	
	protected abstract Class<?> getResParamClass();
	
	WorkFlowInstanceStep step;
	
	/**
	 * work_flow_instance_Step表中的自增ID
	 */
	private Long workFlowStepId;
	
	private String taskId;
	
	public void execute()
	{
		IWorkFlowInstanceStepMng stepmng = (IWorkFlowInstanceStepMng)SpringUtil.getBean("workFlowInstanceStepMng");
		/////////////这部分是否整合到runNext中去  start////////
		initParams();
		
		WorkFlowInstanceStep step = stepmng.findById(workFlowStepId);
		List<String> updateFields = new ArrayList<String>();
		updateFields.add(step.updateTaskId(getTaskId()));
		updateFields.add(step.updateStartTime(new Date()));
		updateFields.add(step.updateTimeOut(getTimeOut()));
		stepmng.update(step, updateFields);
        /////////////这部分是否整合到runNext中去  end////////
		
		//根据步骤过滤条件判断是否跳过该步骤  跳过的话直接在这里feedback,状态为JUMP  flow  task_flow 如何考虑?
		if(!judgeExecute()) {
			WorkFlowEngine.feedbackHandler(getTaskId(), FeedbackState.SKIP.name(), null);
			return;
		}
		
		String errorInfo = "";
		try {
			Object result = process();
			if(result != null)
			{
				if(result instanceof Boolean && !step.isAsync()) {
					WorkFlowEngine.feedbackHandler(getTaskId(),(Boolean) result?FeedbackState.SUCCESS_Y.name():FeedbackState.SUCCESS_N.name(), null);
					return;
				} else {
					List<String> updFields = new ArrayList<String>();
					updFields.add(step.updateResJson(JSON.toJSONString(result, SerializerFeature.DisableCircularReferenceDetect)));
	 				stepmng.update(step, updFields);
				}
			}
		} catch(GCloudException ge) {
			errorInfo = ge.getMessage();
		}
		catch (Exception e) {
			log.error("BaseWorkFlowCommand execute stepId:" + step.getId() + ",errorInfo:" + e.getMessage(), e);
			if(StringUtils.isBlank(e.getMessage())) {
				errorInfo = "程序空指针异常，请联系管理员";
			} else {
				errorInfo = e.getMessage();
			}
		}
		if(StringUtils.isNotBlank(errorInfo) || !step.isAsync())//执行出错或为同步步骤，都需要feedback
		{
			if(StringUtils.isBlank(step.getFromIds())) {//符合最顶层instance、第一步 这两个条件，则另起线程feedback
				if(StringUtils.isNotBlank(errorInfo)) {
					List<String> updFields = new ArrayList<String>();
					updFields.add(step.updateResJson(JSON.toJSONString(new WorkflowFirstStepResException(errorInfo))));
	 				stepmng.update(step, updFields);
				}
				feedbackAsync(getTaskId(), errorInfo);
			} else {
				WorkFlowEngine.feedbackHandler(getTaskId(), StringUtils.isBlank(errorInfo)?FeedbackState.SUCCESS.name():FeedbackState.FAILURE.name(), errorInfo);
			}
		}
	}
	
	/*public void postHandler(Map<String, Object> params)
	{
		IWorkFlowInstanceStepMng stepmng = (IWorkFlowInstanceStepMng)SpringUtil.getBean("workFlowInstanceStepMng");
		initParams();
		try {
			Object result = postProcess(params);
			if(result != null)
			{
				WorkFlowInstanceStep step = stepmng.findById(workFlowStepId);
				step.setResJson(JSON.toJSONString(result));
				stepmng.update(step);
			}
		} catch (Exception e) {
			
		}
	}*/
	
	@Async
	public void feedbackAsync(String taskId, String errorInfo)
	{
		log.info("feedbackAsync taskId:" + taskId + ",errorInfo:" + errorInfo );
		WorkFlowEngine.feedbackHandler(taskId, StringUtils.isBlank(errorInfo)?FeedbackState.SUCCESS.name():FeedbackState.FAILURE.name(), errorInfo);
	}
	
	public void rollbackHandler()
	{
		initParams();
		try {
			IWorkFlowInstanceStepMng stepmng = (IWorkFlowInstanceStepMng)SpringUtil.getBean("workFlowInstanceStepMng");
			WorkFlowInstanceStep step = stepmng.findById(workFlowStepId);
			List<String> updateFields = new ArrayList<String>();
			updateFields.add(step.updateRollbackTaskId(getTaskId()));
			stepmng.update(step, updateFields);
			
			if(!step.isRollbackSkip()) {
				rollback();
			} else {
				WorkFlowEngine.feedbackHandler(getTaskId(), FeedbackState.SKIP.name(), "");
				return;
			}
		} catch(Exception e) {
			log.error("rollback fail taskId:" + getTaskId() + ",errorMsg:" + e.getMessage(), e);
			WorkFlowEngine.feedbackHandler(getTaskId(), FeedbackState.FAILURE.name(), "");
			return;
		}
		if(!step.isRollbackAsync()) {
			WorkFlowEngine.feedbackHandler(getTaskId(), FeedbackState.SUCCESS.name(), "");
		}
		
	}
	
	public void timeoutHandler() {
		initParams();
		boolean result= false;
		try {
			Object resultT = timeout();
			if(null != resultT && resultT instanceof Boolean) {
				result = (Boolean) resultT;
			}
		} catch(Exception e) {
			log.error("BaseWorkFlowCommand timeoutHandler stepId:" + step.getId() + ",errorInfo:" + e.getMessage());
			result = false;
		}
		
		if(null == step.getRollbackTaskId()) {
			WorkFlowEngine.feedbackHandler(step.getTaskId(), result?FeedbackState.SUCCESS.name():FeedbackState.FAILURE.name(), "");
		} else {
			WorkFlowEngine.feedbackHandler(step.getRollbackTaskId(), result?FeedbackState.SUCCESS.name():FeedbackState.FAILURE.name(), "");
		}
	}
	
	/*只操作同步逻辑，并且无论成功与否都继续往下执行
	 * public void postHandler() {
	      initParams();
		  try {
		      postProcess();
		  } catch(Exception e) {
			log.error("BaseWorkFlowCommand postHandler stepId:" + step.getId() + ",errorInfo:" + e.getMessage());
		  }
	}*/
	
	private void initParams() {
		IWorkFlowInstanceStepMng stepmng = (IWorkFlowInstanceStepMng)SpringUtil.getBean("workFlowInstanceStepMng");
		
		step = stepmng.findById(workFlowStepId);
		
		if(null != step.getReqJson()) {
			return;
		}
		
		JSONObject pars = new JSONObject();
		
		pars = WorkFlowUtil.initStepReqParams(workFlowStepId);
		
		List<String> updateFields = new ArrayList<String>();
		updateFields.add(step.updateReqJson(pars.toJSONString()));
		
		stepmng.update(step, updateFields);
		
	}
	
	public Object getReqParams() {  
		if(getReqParamClass() != null) {
			return JSONObject.parseObject(step.getReqJson(), getReqParamClass());
		}
		return null;
    }
	
	public Object getResParams() {  
		if(getResParamClass() != null && null != step.getResJson()) {
			return JSONObject.parseObject(step.getResJson(), getResParamClass());
		}
		return null;
    }
	
	public int getTimeOut() {
		return 3 * 60;//默认3分钟超时
	}
	
	public boolean judgeExecute() {
		return true;
	}
	
	public String getTaskId() {
		if(StringUtils.isBlank(taskId)) {
			taskId = UUID.randomUUID().toString();
		}
		return taskId;
	}
	
	public Long getWorkFlowStepId() {
		return workFlowStepId;
	}

	public void setWorkFlowStepId(Long workFlowStepId) {
		this.workFlowStepId = workFlowStepId;
	}

}
/*@Slf4j
class FeedbackThread implements Runnable {
	private String taskId;
	private Long stepId;
	private String errorInfo;
	@Override
    public void run() {
		log.info("FeedbackThread taskId:" + taskId + ",errorInfo:" + errorInfo);
		WorkFlowEngine.feedbackHandler(taskId, StringUtils.isBlank(errorInfo)?FeedbackState.SUCCESS.name():FeedbackState.FAILURE.name(), errorInfo, stepId);
    }
	public FeedbackThread(String taskId, String errorInfo, Long stepId)
    {
        this.taskId = taskId;
        this.errorInfo = errorInfo;
        this.stepId = stepId;
    }
}*/
