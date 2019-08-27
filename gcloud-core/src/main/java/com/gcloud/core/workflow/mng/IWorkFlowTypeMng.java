package com.gcloud.core.workflow.mng;

import com.gcloud.core.workflow.entity.WorkFlowType;

public interface IWorkFlowTypeMng {
	public WorkFlowType findUniqueByCode(String code);
}
