package com.gcloud.core.workflow.mng.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.gcloud.core.workflow.dao.IWorkFlowTemplateDao;
import com.gcloud.core.workflow.entity.WorkFlowTemplate;
import com.gcloud.core.workflow.mng.IWorkFlowTemplateMng;

@Service
@Transactional
public class WorkFlowTemplateMng implements IWorkFlowTemplateMng{
	@Autowired
	private IWorkFlowTemplateDao workFlowTemplateDao;

	@Override
	@Cacheable(value="flowtemplate", key="#id")
	public WorkFlowTemplate findById(Integer id) {
		return workFlowTemplateDao.getById(id);
	}

	@Override
	public WorkFlowTemplate findUnique(String field, String value) {
		return workFlowTemplateDao.findUniqueByProperty(field, value);
	}

	@Override
	public List<WorkFlowTemplate> findByProperty(String field, String value) {
		return workFlowTemplateDao.findByProperty(field, value);
	}

}
