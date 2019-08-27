package com.gcloud.controller.compute.workflow.vm.base;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.gcloud.core.workflow.core.BaseWorkFlows;
@Component
@Scope("prototype")
public class ShutdownVmsFlow extends BaseWorkFlows {
	public String getBatchFiled() {
		return "instanceIds";
	}

	@Override
	public String getFlowTypeCode() {
		// TODO Auto-generated method stub
		return "ShutdownVmsFlow";
	}

	@Override
	protected Class<?> getReqParamClass() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object preProcess() {
		return null;
	}

	@Override
	public void process() {
		// TODO Auto-generated method stub
		
	}

}
