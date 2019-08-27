package com.gcloud.core.workflow.core;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.gcloud.core.service.SpringUtil;
import com.gcloud.core.workflow.engine.WorkFlowEngine;
import com.gcloud.core.workflow.entity.BatchWorkFlow;
import com.gcloud.core.workflow.entity.FlowTask;
import com.gcloud.core.workflow.entity.WorkFlowInstance;
import com.gcloud.core.workflow.entity.WorkFlowInstanceStep;
import com.gcloud.core.workflow.enums.BatchWorkFlowState;
import com.gcloud.core.workflow.enums.FeedbackState;
import com.gcloud.core.workflow.enums.FlowInstanceStatus;
import com.gcloud.core.workflow.enums.FlowTaskState;
import com.gcloud.core.workflow.mng.IBatchWorkFlowMng;
import com.gcloud.core.workflow.mng.IFlowTaskMng;
import com.gcloud.core.workflow.mng.IWorkFlowInstanceMng;
import com.gcloud.core.workflow.mng.IWorkFlowInstanceStepMng;
import org.apache.commons.lang.StringUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public abstract class BaseWorkFlows {
	public abstract String getFlowTypeCode();
	
	public abstract Object preProcess();
	
	public abstract void process();
	
	protected abstract Class<?> getReqParamClass();
	
	public String batchFiled;
	
	private int batchSize = 1;
	
	private String taskId;
	
//	private Long flowId;
	
	private Long parentFlowId;
	
	private Integer parentFlowStepId;
	
	private String flowTypeCode;
	
	private Map<String, Object> reqParamsMap;
	
	private boolean needFeedbackLog = true;//是否需要feedback给log
	
	public Object execute(Map<String, Object> params) {
		reqParamsMap = params;

		if(!judgeExecute() && null != getParentFlowId()) {
			IWorkFlowInstanceStepMng stepmng = (IWorkFlowInstanceStepMng)SpringUtil.getBean("workFlowInstanceStepMng");
			WorkFlowInstanceStep parentStep = stepmng.findByTemplateStepId(getParentFlowStepId(), getParentFlowId());
			WorkFlowEngine.feedbackHandler(parentStep.getTaskId(), FeedbackState.SKIP.name(), null);
			return null;
		}
		
		Object preRes = preProcess();
		if(null != preRes) {
			params.put("preRes", preRes);
		}
		
		List<Object> batchObj = null;
		if(null != getBatchFiled()) {
			if(params.get(getBatchFiled()) instanceof Integer) {
				batchSize = (int)params.get(getBatchFiled());
			} else if(params.get(getBatchFiled()) instanceof List) {
				//这里需要修改，改成支持对象作为成员放到req对象中，而不是放到objectId中
				batchObj =(List<Object>)params.get(getBatchFiled());
				batchSize = batchObj.size();
			}
		}
		
		IFlowTaskMng flowTaskMng = (IFlowTaskMng)SpringUtil.getBean("flowTaskMng");
		FlowTask task = new FlowTask();
		task.setTaskId(getTaskId());
		task.setFlowTypeCode(getFlowTypeCode());
		task.setState(FlowTaskState.EXECUTING.name());
		task.setBatchSize(batchSize);
		task.setStartTime(new Date());
		if(null != params.get("region")) {
			task.setRegionId(params.get("region").toString());
		}
		if(null != params.get("curUserId")) {
			task.setUserId(params.get("curUserId").toString());
		}
		if(null != getParentFlowId()) {
			task.setParentFlowId(getParentFlowId());
			task.setParentFlowStepId(getParentFlowStepId());
		}
		task.setNeedFeedbackLog(needFeedbackLog);
		flowTaskMng.save(task);
		
		IBatchWorkFlowMng batchWorkFlowMng = (IBatchWorkFlowMng)SpringUtil.getBean("batchWorkFlowMng");
		IWorkFlowInstanceMng instanceMng = (IWorkFlowInstanceMng)SpringUtil.getBean("workFlowInstanceMng");
		
		String topestFlowTaskId = "";
		if(null == getParentFlowId()) {
			topestFlowTaskId = task.getTaskId();
		} else {
			WorkFlowInstance parentInstance = instanceMng.findById(getParentFlowId());
			topestFlowTaskId = parentInstance.getTopestFlowTaskId();
		}
		
		List<WorkFlowInstance> inss = new ArrayList<WorkFlowInstance>();
		for(int i=0;i<batchSize;i++) {
			WorkFlowInstance instance = new WorkFlowInstance();
			instance.setFlowTypeCode(getFlowTypeCode());
			instance.setTaskId(UUID.randomUUID().toString());
			instance.setState(FlowInstanceStatus.WAITING.name());
			instance.setParamsJson(JSONObject.toJSONString(params, SerializerFeature.DisableCircularReferenceDetect));
			instance.setStartTime(new Date());
			if(null != batchObj) {
				//这里需要修改，改成支持对象作为成员放到req对象中，而不是放到objectId中
				instance.setBatchParams(JSONObject.toJSONString(batchObj.get(i), SerializerFeature.DisableCircularReferenceDetect));
			}
			instance.setTopestFlowTaskId(topestFlowTaskId);
			
			instanceMng.save(instance);
			
			instance = instanceMng.findUnique("taskId", instance.getTaskId());
			
			BatchWorkFlow batchFlow = new BatchWorkFlow();
			batchFlow.setPtaskId(getTaskId());
			batchFlow.setFlowId(instance.getId());
			batchFlow.setState(BatchWorkFlowState.EXECUTING.name());
			batchWorkFlowMng.save(batchFlow);
			
			inss.add(instance);//目的先把数据保存到数据库中
		}
		for(WorkFlowInstance ins:inss) {
			WorkFlowEngine.start(ins);
		}
		
		process();//兼容任务流单独测试
		
		return null;//WorkFlowEngine.getTaskFlowResponse(instance.getId());
	}
	
	public boolean judgeExecute() {
		return true;
	}
	
	public Object getReqParams() {  
		if(getReqParamClass() != null) {
//			return BeanUtil.convertMapToBean(reqParamsMap, getReqParamClass());
			return JSONObject.parseObject(JSON.toJSONString(reqParamsMap, SerializerFeature.BrowserCompatible).replace("\\\\u", "\\u"), getReqParamClass());
		}
		return null;
    }

	public String getBatchFiled() {
		return batchFiled;
	}

	public void setBatchFiled(String batchFiled) {
		this.batchFiled = batchFiled;
	}
	
	public String getTaskId(){
		if(StringUtils.isBlank(taskId)) {
			taskId = UUID.randomUUID().toString();
		}
		return taskId;
	}
	
	public void setTaskId(String taskId){
		this.taskId = taskId;
	}

	public Long getParentFlowId() {
		return parentFlowId;
	}

	public void setParentFlowId(Long parentFlowId) {
		this.parentFlowId = parentFlowId;
	}

	public Integer getParentFlowStepId() {
		return parentFlowStepId;
	}

	public void setParentFlowStepId(Integer parentFlowStepId) {
		this.parentFlowStepId = parentFlowStepId;
	}

	public boolean isNeedFeedbackLog() {
		return needFeedbackLog;
	}

	public void setNeedFeedbackLog(boolean needFeedbackLog) {
		this.needFeedbackLog = needFeedbackLog;
	}

//	public Long getFlowId() {
//		return flowId;
//	}

}
