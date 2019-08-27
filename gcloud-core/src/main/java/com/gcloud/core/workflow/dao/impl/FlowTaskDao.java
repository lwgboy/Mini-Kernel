package com.gcloud.core.workflow.dao.impl;

import java.util.Date;

import org.springframework.stereotype.Repository;

import com.gcloud.core.workflow.dao.IFlowTaskDao;
import com.gcloud.core.workflow.entity.FlowTask;
import com.gcloud.framework.db.dao.impl.JdbcBaseDaoImpl;

import lombok.extern.slf4j.Slf4j;
@Slf4j
@Repository
public class FlowTaskDao  extends JdbcBaseDaoImpl<FlowTask, Long> implements IFlowTaskDao{

	@Override
	public void deleteExpireData(String endTime) {
		String sql ="delete from gc_work_flow_task where start_time <= '" + endTime + "'";
		log.info("deleteExpireData sql:" + sql);
		this.jdbcTemplate.execute(sql);
	}

}
