package com.gcloud.core.workflow.engine;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

import org.apache.commons.lang.StringUtils;

import com.alibaba.fastjson.JSONObject;
import com.gcloud.core.service.SpringUtil;
import com.gcloud.core.workflow.core.BaseWorkFlowCommand;
import com.gcloud.core.workflow.core.BaseWorkFlows;
import com.gcloud.core.workflow.entity.BatchWorkFlow;
import com.gcloud.core.workflow.entity.FlowTask;
import com.gcloud.core.workflow.entity.WorkFlowInstance;
import com.gcloud.core.workflow.entity.WorkFlowInstanceStep;
import com.gcloud.core.workflow.entity.WorkFlowTemplate;
import com.gcloud.core.workflow.entity.WorkFlowType;
import com.gcloud.core.workflow.enums.BatchWorkFlowState;
import com.gcloud.core.workflow.enums.FeedbackState;
import com.gcloud.core.workflow.enums.FlowInstanceStatus;
import com.gcloud.core.workflow.enums.FlowStepStatus;
import com.gcloud.core.workflow.enums.FlowTaskState;
import com.gcloud.core.workflow.enums.FromRelationType;
import com.gcloud.core.workflow.enums.StepType;
import com.gcloud.core.workflow.mng.IBatchWorkFlowMng;
import com.gcloud.core.workflow.mng.IFlowTaskMng;
import com.gcloud.core.workflow.mng.IWorkFlowInstanceMng;
import com.gcloud.core.workflow.mng.IWorkFlowInstanceStepMng;
import com.gcloud.core.workflow.mng.IWorkFlowTemplateMng;
import com.gcloud.core.workflow.mng.IWorkFlowTypeMng;
import com.gcloud.core.workflow.mng.impl.FlowTaskMng;
import com.gcloud.core.workflow.service.IFlowTaskFeedbackLogService;
import com.gcloud.core.workflow.util.WorkFlowUtil;

import lombok.extern.slf4j.Slf4j;
@Slf4j
public class WorkFlowEngine {
	
	public static boolean start(WorkFlowInstance instance)
	{
		IWorkFlowTemplateMng templateMng = (IWorkFlowTemplateMng)SpringUtil.getBean("workFlowTemplateMng");
		
		List<WorkFlowTemplate> templateSteps = templateMng.findByProperty("flowTypeCode", instance.getFlowTypeCode());
		
		IWorkFlowInstanceStepMng mng = (IWorkFlowInstanceStepMng)SpringUtil.getBean("workFlowInstanceStepMng");
		
		Integer firstStepId = null;
		
		for(WorkFlowTemplate tStep:templateSteps)
		{
			WorkFlowInstanceStep step = new WorkFlowInstanceStep();
			step.setExecCommand(tStep.getExecCommand());
			step.setFromIds(tStep.getFromIds());
			step.setNecessary(tStep.isNecessary());
			step.setState(FlowStepStatus.NOT_EXECUTE.name());
			step.setFlowId(instance.getId());
			step.setTemplateStepId(tStep.getStepId());
			step.setyToIds(tStep.getyToIds());
			step.setnToIds(tStep.getnToIds());
			step.setStepType(tStep.getStepType());
			step.setAsync(tStep.isAsync());
			step.setRollbackAsync(tStep.isRollbackAsync());
			step.setRollbackSkip(tStep.isRollbackSkip());
			step.setFromRelation(tStep.getFromRelation());
			step.setRepeatType(tStep.getRepeatType());
			step.setVisible(tStep.isVisible());
			step.setTopestFlowTaskId(instance.getTopestFlowTaskId());
			step.setRollbackFailContinue(tStep.isRollbackFailContinue());
			step.setStepDesc(tStep.getStepDesc());
			
			mng.save(step);
			
			if(StringUtils.isBlank(tStep.getFromIds()))
			{
				firstStepId = tStep.getStepId();
			}
			
		}
		IWorkFlowInstanceMng instanceMng = (IWorkFlowInstanceMng)SpringUtil.getBean("workFlowInstanceMng");
		WorkFlowInstance ins = instanceMng.findById(instance.getId());
		List<String> updateFields = new ArrayList<String>();
		updateFields.add(ins.updateState(FlowInstanceStatus.EXECUTING.name()));
		
		instanceMng.update(ins, updateFields);
		
		WorkFlowInstanceStep firststep = mng.findByTemplateStepId(firstStepId, instance.getId());
		
		runNext(firststep);
		return true;
	}
	
	public static void runNext(WorkFlowInstanceStep step)
	{
		if(null != step.getRepeatType() && null == step.getRepeatIndex()) {
			runNext(WorkFlowUtil.editWorkFlowInstanceStep(step));
			return;
		}
		IWorkFlowInstanceStepMng stepmng = (IWorkFlowInstanceStepMng)SpringUtil.getBean("workFlowInstanceStepMng");
		List<String> updateFields = new ArrayList<>();
		updateFields.add(step.updateState(FlowStepStatus.EXECUTING.name()));
		stepmng.update(step, updateFields);
		//command
		if(step.getStepType().equals(StepType.COMMAND.getType()))
		{
			BaseWorkFlowCommand command = (BaseWorkFlowCommand)SpringUtil.getBean(step.getExecCommand());
			command.setWorkFlowStepId(step.getId());
			command.execute();
		}
		else if(step.getStepType().equals(StepType.FLOW.getType()))
		{
			
			String pars = new String();
			if(null != step.getReqJson()) {
				pars = step.getReqJson();
			} else {
				pars = WorkFlowUtil.initStepReqParams(step.getId()).toJSONString();
			}
			
			//嵌套flow处理
			IWorkFlowInstanceMng instanceMng = (IWorkFlowInstanceMng)SpringUtil.getBean("workFlowInstanceMng");
			WorkFlowInstance parentInstance = instanceMng.findById(step.getFlowId());
			List<String> updFields = new ArrayList<>();
			updFields.add(step.updateTaskId(UUID.randomUUID().toString()));
			updFields.add(step.updateStartTime(new Date()));
			updFields.add(step.updateReqJson(pars));
			stepmng.update(step, updFields);
			
			WorkFlowInstance instance = new WorkFlowInstance();
			instance.setStartTime(new Date());
			instance.setFlowTypeCode(step.getExecCommand());
//			instance.setRegionId(parentInstance.getRegionId());
//			instance.setUserId(parentInstance.getUserId());
			instance.setTaskId(UUID.randomUUID().toString());
			instance.setState(FlowInstanceStatus.WAITING.name());
			instance.setParamsJson(pars);
			instance.setParentFlowId(parentInstance.getId());
			instance.setParentFlowStepId(step.getTemplateStepId());
			
			instanceMng.save(instance);
			
			instance = instanceMng.findUnique("taskId", instance.getTaskId());
			
			WorkFlowEngine.start(instance);
		} else { //flow_task
			JSONObject params = new JSONObject();
			if(null != step.getReqJson()) {
				params = JSONObject.parseObject(step.getReqJson());
			} else {
				params = WorkFlowUtil.initStepReqParams(step.getId());
			}
			List<String> updFields = new ArrayList<>();
			updFields.add(step.updateTaskId(UUID.randomUUID().toString()));
			updFields.add(step.updateStartTime(new Date()));
			updFields.add(step.updateReqJson(params.toJSONString()));
			stepmng.update(step, updFields);
			
			BaseWorkFlows flowTask = (BaseWorkFlows)SpringUtil.getBean(step.getExecCommand());
			
			flowTask.setParentFlowId(step.getFlowId());
			flowTask.setParentFlowStepId(step.getTemplateStepId());
			try {
				flowTask.execute(params);
			} catch(Exception ex) {
				String error = ex.getMessage();
				if(StringUtils.isBlank(error)) {
					error = "程序空指针异常，请联系管理员";
				}
				WorkFlowEngine.feedbackHandler(step.getTaskId(), FeedbackState.FAILURE.name(), error);
			}
		}
		return;
	}
	
	public static boolean rollback(WorkFlowInstance ins) {
		IWorkFlowInstanceStepMng mng = (IWorkFlowInstanceStepMng)SpringUtil.getBean("workFlowInstanceStepMng");
		List<WorkFlowInstanceStep> steps = mng.getNextRollBackSteps(ins.getId(), ins.getState().equals(FlowInstanceStatus.ROLLBACKING_FAIL_NOT_CONTINUE.name()));
		
		if(steps.size() == 0)
		{
			IWorkFlowInstanceMng insMng = (IWorkFlowInstanceMng)SpringUtil.getBean("workFlowInstanceMng");
			WorkFlowInstance instance = insMng.findById(ins.getId());
			
			if(null == instance.getParentFlowId())
			{
				log.info("rollback(WorkFlowInstance ins) ins.id=" + ins.getId() + ",ins.state:" + ins.getState());
				workflowInstaceDone(ins.getId(),ins.getState().equals(FlowInstanceStatus.ROLLBACKING.name())?FlowInstanceStatus.ROLLBACKED.name():FlowInstanceStatus.ROLLBACKED_FAIL.name());
			}
			else
			{
				List<String> updateFields = new ArrayList<String>();
				updateFields.add(instance.updateState(FlowInstanceStatus.ROLLBACKED.name()));
				updateFields.add(instance.updateEndTime(new Date()));
				insMng.update(instance, updateFields);
				
				WorkFlowInstanceStep parentStep = mng.findByTemplateStepId(instance.getParentFlowStepId(), instance.getParentFlowId());
				List<String> updFields = new ArrayList<>();
				updFields.add(parentStep.updateState(FlowStepStatus.ROLLBACKED.name()));
				mng.update(parentStep, updFields);
				
				WorkFlowInstance parentInstance = insMng.findById(instance.getParentFlowId());
				if(!parentInstance.getState().equals(FlowInstanceStatus.ROLLBACKING.name()))
				{
					List<String> updaFields = new ArrayList<String>();
					updaFields.add(parentInstance.updateState(FlowInstanceStatus.ROLLBACKING.name()));
					insMng.update(parentInstance, updFields);
				}
				rollback(parentInstance);
			}
			
		}
		
		for(WorkFlowInstanceStep step:steps)
		{
			log.info("rollback(WorkFlowInstance ins) -> rollback flow step,instanceId:" + step.getFlowId() + ",instanceState:" + ins.getState() + ",stepId: " + step.getId() + ",state:" + step.getState());
			rollback(step);
		}
		
		return true;
	}
	
	public static boolean rollback(WorkFlowInstanceStep step)
	{
		log.info("rollback flow step,stepId: " + step.getId() + ",state:" + step.getState());
		IWorkFlowInstanceStepMng stepmng = (IWorkFlowInstanceStepMng)SpringUtil.getBean("workFlowInstanceStepMng");
		IWorkFlowInstanceMng insMng = (IWorkFlowInstanceMng)SpringUtil.getBean("workFlowInstanceMng");
		List<String> updFields = new ArrayList<>();
		updFields.add(step.updateState(FlowStepStatus.ROLLBACKING.name()));
		updFields.add(step.updateRollbackStartTime(new Date()));
		stepmng.update(step, updFields);
		//command
		if (step.getStepType().equals(StepType.COMMAND.getType())) {
			BaseWorkFlowCommand command = (BaseWorkFlowCommand)SpringUtil.getBean(step.getExecCommand());
			command.setWorkFlowStepId(step.getId());
			command.rollbackHandler();
		} else if(step.getStepType().equals(StepType.FLOW.getType())) {
			//嵌套流程的rollback
			WorkFlowInstance subInstance = insMng.getSubFlow(step.getFlowId(), step.getTemplateStepId());
			rollback(subInstance);
		} else {//flow_task
			List<String> updateFields = new ArrayList<>();
			updateFields.add(step.updateRollbackTaskId(UUID.randomUUID().toString()));
			stepmng.update(step, updateFields);
			IFlowTaskMng taskMng = (IFlowTaskMng)SpringUtil.getBean(FlowTaskMng.class);
			List<WorkFlowInstance> subInstances = taskMng.getSubFlows(step.getFlowId(), step.getTemplateStepId());
			for(WorkFlowInstance ins:subInstances) {
				log.info("rollback flow instance,instanceId: " + ins.getId() + ",state:" + ins.getState());
				List<String> insUpdateFields = new ArrayList<String>();
				insUpdateFields.add(ins.updateState(FlowInstanceStatus.ROLLBACKING.name()));
				insUpdateFields.add(ins.updateEndTime(new Date()));
				insMng.update(ins, insUpdateFields);
				rollback(ins);
			}
		}
		
		return true;
	}
	
	public static void feedbackHandler(String taskId, String state, String errorCode)
	{
		log.debug("workflow feedbackHandler taskId=" + taskId + ",state=" + state + ",errorCode=" + errorCode);
		IWorkFlowInstanceStepMng mng = (IWorkFlowInstanceStepMng)SpringUtil.getBean("workFlowInstanceStepMng");
		IWorkFlowInstanceMng insMng = (IWorkFlowInstanceMng)SpringUtil.getBean("workFlowInstanceMng");
		WorkFlowInstanceStep step = mng.findUnique("taskId", taskId);
		WorkFlowInstance ins;
		log.debug("workflow feedbackHandler taskId=" + taskId + ",state=" + state + ",errorCode=" + errorCode + ",step:" + step);
		if(step!=null)
		{
			/*if(step.getStepType().equals(StepType.COMMAND.getType()))
			{
				BaseWorkFlowCommand command = (BaseWorkFlowCommand)SpringUtil.getBean(step.getExecCommand());
				command.setWorkFlowStepId(step.getId());
				command.postHandler();
			}*/
			if(FeedbackState.include(step.getState())) {//防止重复feedback
				log.debug("workflow feedbackHandler taskId=" + taskId + ",重复feedback，不做任何处理");
				return;
			}
			
			ins = insMng.findById(step.getFlowId());
			
			List<String> updateFields = new ArrayList<>();
			updateFields.add(step.updateState(state));
			updateFields.add(step.updateUpdateTime(new Date()));
			mng.update(step, updateFields);
			
			if(!ins.getState().equals(FlowInstanceStatus.EXECUTING.name()))//防止超时处理后，底层才feedback回来的情况
			{
				log.debug("instance state is not EXECUTING, maybe this task is already timeout handler,taskId:" + taskId + ",state=" + state + ",errorCode=" + errorCode);
				return ;
			}
			
			if(state.equals(FeedbackState.SUCCESS.name()) || state.equals(FeedbackState.SKIP.name()))
			{
				if(step.getFromRelation().equals(FromRelationType.FROM_ALL_DONE.getType()) && null != step.getFromIds()  && step.getFromIds().indexOf(",") != -1)//有多个from步骤
				{
					if(!mng.isStepsDone(ins.getId(), step.getFromIds()))
					{
						return ;
					}
					
				}
				runSteps(ins.getId(), step.getyToIds());
			}
			else if(state.equals(FeedbackState.FAILURE.name()))
			{
				if(step.isNecessary())
				{
					//getFlowtaskByInsId
					//修改父步骤的状态及生成rollbackTaskId
					
					List<String> updFields = new ArrayList<String>();
					updFields.add(ins.updateState(FlowInstanceStatus.ROLLBACKING.name()));
					updFields.add(ins.updateErrorCode(WorkFlowUtil.getSubStr(errorCode, WorkFlowUtil.ERRORCODE_LEN)));
					insMng.update(ins, updFields);
					log.info("feedbackHandler failure -> rollback flow instance,instanceId: " + ins.getId() + ",state:" + ins.getState());
					rollback(ins);
				} else {
					if(step.getFromRelation().equals(FromRelationType.FROM_ALL_DONE.getType()) && null != step.getFromIds()  && step.getFromIds().indexOf(",") != -1)//有多个from步骤
					{
						if(!mng.isStepsDone(ins.getId(), step.getFromIds()))
						{
							return ;
						}
						
					}
					
					runSteps(ins.getId(), step.getnToIds());
				}
				
			} else if(state.equals(FeedbackState.SUCCESS_Y.name()) || state.equals(FeedbackState.SUCCESS_N.name())) {
				runSteps(ins.getId(), state.equals(FeedbackState.SUCCESS_Y.name())?step.getyToIds():step.getnToIds());
			}
		}
		else 
		{
			WorkFlowInstanceStep rollbackstep = mng.findUnique("rollbackTaskId", taskId);
			
			if(rollbackstep == null)
			{
				return ;
			}
			if(rollbackstep.getState().equals(FlowStepStatus.ROLLBACKED.name()) 
					|| rollbackstep.getState().equals(FlowStepStatus.FAILURE.name()) 
					|| rollbackstep.getState().equals(FlowStepStatus.ROLLBACKED_FAIL.name()) 
					|| rollbackstep.getState().equals(FlowStepStatus.ROLLBACKED_SKIP.name())) {
				log.debug("workflow feedbackHandler taskId=" + taskId + ",重复feedback，不做任何处理");
				return ;
			}
			
			ins = insMng.findById(rollbackstep.getFlowId());
			List<String> updateFields = new ArrayList<>();
			updateFields.add(rollbackstep.updateState(state.equals(FeedbackState.SUCCESS.name())?FlowStepStatus.ROLLBACKED.name():(state.equals(FeedbackState.FAILURE.name())?FlowStepStatus.ROLLBACKED_FAIL.name():FlowStepStatus.ROLLBACKED_SKIP.name())));
			updateFields.add(rollbackstep.updateRollbackUpdateTime(new Date()));
			
			mng.update(rollbackstep, updateFields);
			
			if(state.equals(FeedbackState.FAILURE.name())) {
				IWorkFlowTypeMng flowtypeMng = (IWorkFlowTypeMng)SpringUtil.getBean(IWorkFlowTypeMng.class);
				WorkFlowType flowType = flowtypeMng.findUniqueByCode(ins.getFlowTypeCode());
				List<String> updFields = new ArrayList<String>();
				//没有配置流程类型数据时，回滚失败后，流程继续进行
				if(flowType == null || !flowType.isFaultRollbackContinue()) {//根据流程的回滚出错后是否继续回滚
					updFields.add(ins.updateState(FlowInstanceStatus.ROLLBACKING_FAIL_NOT_CONTINUE.name()));
				} else {
					updFields.add(ins.updateState(FlowInstanceStatus.ROLLBACKING_FAIL.name()));
				}
				insMng.update(ins, updFields);
			}
			log.info("feedbackHandler -> rollback flow instance,instanceId: " + ins.getId() + ",state:" + ins.getState());
			rollback(ins);
		}
	}
	
	/**一个工作流实例结束的处理
	 * @param flowId
	 * @param flowInstanceStatus
	 */
	private static void workflowInstaceDone(Long flowId,String flowInstanceStatus)
	{
		IWorkFlowInstanceStepMng mng = (IWorkFlowInstanceStepMng)SpringUtil.getBean("workFlowInstanceStepMng");
		IWorkFlowInstanceMng insMng = (IWorkFlowInstanceMng)SpringUtil.getBean("workFlowInstanceMng");
		WorkFlowInstance instance = insMng.findById(flowId);
		List<String> updFields = new ArrayList<String>();
		updFields.add(instance.updateState(flowInstanceStatus));
		updFields.add(instance.updateEndTime(new Date()));
		insMng.update(instance, updFields);
		
		if(null == instance.getParentFlowId())
		{
			//BatchWorkFlow
			IBatchWorkFlowMng batchFlowMng = (IBatchWorkFlowMng)SpringUtil.getBean("batchWorkFlowMng");
			BatchWorkFlow batchFlow = batchFlowMng.findUnique("flowId", instance.getId().toString());
			updFields = new ArrayList<String>();
			updFields.add(batchFlow.updateState(flowInstanceStatus));
			batchFlowMng.update(batchFlow, updFields);
			
			//FlowTask
			List<BatchWorkFlow> tasks = batchFlowMng.findByProperty("ptaskId", batchFlow.getPtaskId());
			boolean taskFinished = true;
			for(BatchWorkFlow task:tasks)
			{
				if(task.getState().equals(BatchWorkFlowState.EXECUTING.name()))
				{
					taskFinished = false;
					break;
				}
			}
			IFlowTaskMng flowTaskMng = (IFlowTaskMng)SpringUtil.getBean("flowTaskMng");
			FlowTask task = flowTaskMng.findUnique("taskId", batchFlow.getPtaskId());
			boolean isRollingback = false;
			if(null == task.getParentFlowId()) {
				if(task.isNeedFeedbackLog()) {
					feedbackLog(instance, null);//批量操作feedback操作日志状态
				}
			} else if(null != task.getParentFlowId() && 
					(flowInstanceStatus.equals(FlowInstanceStatus.ROLLBACKED.name()) || flowInstanceStatus.equals(FlowInstanceStatus.ROLLBACKED_FAIL.name()))) {
				//这里需要把二级平行的instance也回滚
				for(BatchWorkFlow tasktem:tasks) {
					WorkFlowInstance ins = insMng.findById(tasktem.getFlowId());
					if(ins.getState().equals(FlowInstanceStatus.EXECUTING.name()) || ins.getState().equals(FlowInstanceStatus.SUCCESS.name())) {
						isRollingback = true;
						log.info("workflowInstaceDone -> rollback flow instance,instanceId: " + ins.getId() + ",state:" + ins.getState());
						List<String> insUpdateFields = new ArrayList<String>();
						insUpdateFields.add(ins.updateState(FlowInstanceStatus.ROLLBACKING.name()));
						insUpdateFields.add(ins.updateEndTime(new Date()));
						insMng.update(ins, insUpdateFields);
						rollback(ins);
					}
				}
			}
			
			if(taskFinished && !isRollingback)
			{
				List<String> updaFields = new ArrayList<String>();
				updaFields.add(task.updateState(FlowTaskState.DONE.name()));
				updaFields.add(task.updateEndTime(new Date()));
				flowTaskMng.update(task, updaFields);
				
				if(null == task.getParentFlowId()) {
					//feedbackLog(instance, task);//单操作时--所有任务流都用批量操作形式记录操作日志，故这里不用feedback
				} else {
					WorkFlowInstanceStep parentStep = mng.findByTemplateStepId(task.getParentFlowStepId(), task.getParentFlowId());
					//flow_task参数返回，返回批量流程最后步骤的返回数据list
					List<JSONObject> res = WorkFlowUtil.getFlowTaskRes(task.getId());
					if(null != res && res.size()>0) {
						List<String> updateFields = new ArrayList<String>();
						updateFields.add(parentStep.updateResJson(res.toString()));
						mng.update(parentStep, updateFields);
					}
					List<BatchWorkFlow> fails = tasks.stream()
							.filter(fail -> fail.getState().equals(BatchWorkFlowState.FAILURE.name()) 
									|| fail.getState().equals(BatchWorkFlowState.ROLLBACKED.name()) 
									|| fail.getState().equals(BatchWorkFlowState.ROLLBACKED_FAIL.name()))
							.collect(Collectors.toList());
					if(StringUtils.isNotBlank(parentStep.getRollbackTaskId())) {
						List<BatchWorkFlow> rollbackFails = tasks.stream().filter(fail -> fail.getState().equals(fail.getState().equals(BatchWorkFlowState.ROLLBACKED_FAIL.name()))).collect(Collectors.toList());
						feedbackHandler(parentStep.getRollbackTaskId(), rollbackFails.size()>0?FeedbackState.FAILURE.name():FeedbackState.SUCCESS.name(), "");
					} else {
						feedbackHandler(parentStep.getTaskId(), fails.size()==0?FeedbackState.SUCCESS.name():FeedbackState.FAILURE.name(), "");
					}
					
				}
				
			}
			
		}
		else
		{
			//嵌套的流程返回数据给父流程的相应步骤
			WorkFlowInstanceStep step = mng.getLastedSuccessStep(instance.getId());
			
			WorkFlowInstanceStep parentStep = mng.findByTemplateStepId(instance.getParentFlowStepId(), instance.getParentFlowId());
			List<String> updateFields = new ArrayList<String>();
			updateFields.add(parentStep.updateResJson(step.getResJson()));
			mng.update(parentStep, updateFields);
			
			feedbackHandler(parentStep.getTaskId(), FeedbackState.SUCCESS.name(), "");
			
		}
	}
	
	/**反馈总任务状态
	 * @param taskFlowId
	 * @param taskFlowStepType
	 */
	private static void feedbackLog(WorkFlowInstance instance, FlowTask task)
	{
		Map<String, Object> par = new HashMap<String, Object>();
		par.put("state", instance.getState().equals(FlowInstanceStatus.SUCCESS.name())?"COMPLETE":"FAILED");
		par.put("errorCode", instance.getErrorCode());
		par.put("taskId", (null == task)?instance.getTaskId():task.getTaskId());
		
		log.debug(par.toString());
		log.debug("WorkflowEngine feedbackLog taskId:" + par.get("taskId").toString() + ",status:" + par.get("state").toString() + ",code:" + instance.getErrorCode());
		
		IFlowTaskFeedbackLogService logMng = (IFlowTaskFeedbackLogService)SpringUtil.getBean("flowTaskFeedbackLogService");
		logMng.flowTaskFeedbackLog(par.get("taskId").toString(), null, par.get("state").toString(), instance.getErrorCode());
		
	}
	
	private static void runSteps(Long flowId, String stepIds)
	{
		IWorkFlowInstanceStepMng mng = (IWorkFlowInstanceStepMng)SpringUtil.getBean("workFlowInstanceStepMng");
		List<WorkFlowInstanceStep> nextSteps = mng.getStepsByIds(flowId, stepIds);
		if(nextSteps.size() == 0)
		{
			workflowInstaceDone(flowId,FlowInstanceStatus.SUCCESS.name());
		}
		for(WorkFlowInstanceStep item:nextSteps)
		{
			runNext(item);
		}
	}
}
