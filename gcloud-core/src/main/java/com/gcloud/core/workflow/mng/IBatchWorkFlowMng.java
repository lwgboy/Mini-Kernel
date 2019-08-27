package com.gcloud.core.workflow.mng;

import java.util.Date;
import java.util.List;

import com.gcloud.core.workflow.entity.BatchWorkFlow;

public interface IBatchWorkFlowMng {
	void update(BatchWorkFlow batchFlow, List<String> fields);
	void delete(BatchWorkFlow batchFlow);
	Long save(BatchWorkFlow batchFlow);
	BatchWorkFlow findById(Long id);
	BatchWorkFlow findUnique(String field,String value);
	List<BatchWorkFlow> findByProperty(String field,String value);
}
