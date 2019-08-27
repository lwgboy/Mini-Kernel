package com.gcloud.core.workflow.mng.impl;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.gcloud.core.workflow.dao.IWorkFlowInstanceStepDao;
import com.gcloud.core.workflow.entity.WorkFlowInstanceStep;
import com.gcloud.core.workflow.enums.FlowStepStatus;
import com.gcloud.core.workflow.mng.IWorkFlowInstanceStepMng;
@Service
public class WorkFlowInstanceStepMng implements IWorkFlowInstanceStepMng{
	
	@Autowired
	private IWorkFlowInstanceStepDao workFlowInstanceStepDao;

	@Override
	public void update(WorkFlowInstanceStep step, List<String> fields) {
		workFlowInstanceStepDao.update(step, fields);
	}

	@Override
	public Long save(WorkFlowInstanceStep step) {
		return workFlowInstanceStepDao.saveWithIncrementId(step);
	}

	@Override
	public WorkFlowInstanceStep findById(Long id) {
		return workFlowInstanceStepDao.getById(id);
	}

	@Override
	public WorkFlowInstanceStep findUnique(String field, String value) {
		return workFlowInstanceStepDao.findUniqueByProperty(field, value);
	}

	@Override
	public WorkFlowInstanceStep findByTemplateStepId(Integer templateStepId, Long flowId) {
		Map<String, Object> search = new HashMap<String, Object>();
		search.put("templateStepId", templateStepId);
		search.put("flowId", flowId);
		return workFlowInstanceStepDao.findUniqueByProperties(search);
	}

	@Override
	public WorkFlowInstanceStep getFirstStep(Long flowId) {
		return workFlowInstanceStepDao.getFirstStep(flowId);
	}
	
	@Override
	public List<WorkFlowInstanceStep> getStepsByIds(Long flowId, String toIds) {
		return workFlowInstanceStepDao.getStepsByIds(flowId, toIds);
	}

	@Override
	public List<WorkFlowInstanceStep> getNextRollBackSteps(Long flowId, boolean rollbackFailContinue) {
		return workFlowInstanceStepDao.getNextRollBackSteps(flowId, rollbackFailContinue);
	}

	@Override
	public boolean isStepsDone(Long flowId, String ids) {
		List<WorkFlowInstanceStep> steps = workFlowInstanceStepDao.getStepsByIds(flowId, ids);
		List<String> doneState = Arrays.asList(FlowStepStatus.FAILURE.name(),
				FlowStepStatus.SUCCESS.name(),FlowStepStatus.ROLLBACKED.name(),
				FlowStepStatus.ROLLBACKED_FAIL.name(),FlowStepStatus.SKIP.name());
		for(WorkFlowInstanceStep step:steps)
		{
			if(!doneState.contains(step.getState()))
			{
				return false;
			}
		}
		return true;
	}

	@Override
	public WorkFlowInstanceStep getLastedSuccessStep(Long flowId) {
		// TODO Auto-generated method stub
		return workFlowInstanceStepDao.getLastedSuccessStep(flowId);
	}

	@Override
	public List<WorkFlowInstanceStep> getAllNotFinishedStep() {
		// TODO Auto-generated method stub
		return workFlowInstanceStepDao.getAllNotFinishedStep();
	}
	
	@Override
	public List<String> getFirstStepRess(Long flowTaskId) {
		return workFlowInstanceStepDao.getFirstStepRess(flowTaskId);
	}
	
	@Override
	public List<String> getLastStepRess(Long flowTaskId) {
		return workFlowInstanceStepDao.getLastStepRess(flowTaskId);
	}

	@Override
	public JSONArray getRepeatStepRes(Long flowId, Integer stepId) {
		JSONArray results = new JSONArray();
		List<WorkFlowInstanceStep> steps = workFlowInstanceStepDao.getRepeatSteps(flowId, stepId);
		for(WorkFlowInstanceStep step:steps) {
			if(StringUtils.isNotBlank(step.getReqJson())) {
				if(step.getResJson().startsWith("{")) {
					JSONObject resObj = (JSONObject)JSONObject.parse(step.getResJson());
					results.add(resObj);
				}else {
					JSONArray resArr = (JSONArray)JSONObject.parse(step.getResJson());
					results.addAll(resArr);
				}
			}
		}
		return results;
	}
	
}
