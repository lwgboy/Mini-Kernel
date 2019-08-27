package com.gcloud.core.workflow.dao.impl;

import java.util.Date;

import org.springframework.stereotype.Repository;

import com.gcloud.core.workflow.dao.IBatchWorkFlowDao;
import com.gcloud.core.workflow.entity.BatchWorkFlow;
import com.gcloud.framework.db.dao.impl.JdbcBaseDaoImpl;
@Repository
public class BatchWorkFlowDao extends JdbcBaseDaoImpl<BatchWorkFlow, Long> implements IBatchWorkFlowDao{

	@Override
	public void deleteExpireData(String endTime) {
		String sql ="delete b.* from gc_work_flow_batch b left join gc_work_flow_instance ins on b.flow_id = ins.id where ins.start_time <= '" + endTime + "'";
		this.jdbcTemplate.execute(sql);
		
	}

}
