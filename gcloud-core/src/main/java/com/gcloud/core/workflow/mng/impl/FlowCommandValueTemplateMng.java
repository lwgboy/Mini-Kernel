package com.gcloud.core.workflow.mng.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.gcloud.core.workflow.dao.IFlowCommandValueTemplateDao;
import com.gcloud.core.workflow.entity.FlowCommandValueTemplate;
import com.gcloud.core.workflow.mng.IFlowCommandValueTemplateMng;
@Service
@Transactional
public class FlowCommandValueTemplateMng implements IFlowCommandValueTemplateMng{
	@Autowired
	private IFlowCommandValueTemplateDao flowCommandValueTemplateDao;

	@Override
	public List<FlowCommandValueTemplate> findByProperty(String field, String value) {
		return flowCommandValueTemplateDao.findByProperty(field, value);
	}

	@Override
	public List<FlowCommandValueTemplate> getTemplatesByStepId(String flowTypeCode, Integer stepId) {
		Map<String, Object> search = new HashMap<String, Object>();
		search.put("flow_type_code", flowTypeCode);
		search.put("step_id", stepId);
		return flowCommandValueTemplateDao.findByProperties(search);
	}

}
