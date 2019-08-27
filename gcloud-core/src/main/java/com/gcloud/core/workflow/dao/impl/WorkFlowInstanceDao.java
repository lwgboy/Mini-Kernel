package com.gcloud.core.workflow.dao.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Repository;

import com.gcloud.core.workflow.dao.IWorkFlowInstanceDao;
import com.gcloud.core.workflow.entity.WorkFlowInstance;
import com.gcloud.framework.db.dao.impl.JdbcBaseDaoImpl;
@Repository
public class WorkFlowInstanceDao extends JdbcBaseDaoImpl<WorkFlowInstance, Long> implements IWorkFlowInstanceDao{

	@Override
	public List<WorkFlowInstance> getFlowsByIds(List<Long> ids) {
		if (ids.size() > 0) {
			StringBuffer sql = new StringBuffer();
			sql.append("select * from gc_work_flow_instance w where ");
			
			sql.append(" w.id in (");
			sql.append(ids.stream().map(id -> id.toString()).collect(Collectors.joining(",")));
			sql.append(") ");
			
			return this.findBySql(sql.toString());
		}
		return new ArrayList<WorkFlowInstance>();
	}

	@Override
	public void deleteExpireData(String endTime) {
		String sql ="delete from gc_work_flow_instance where start_time <= '" + endTime + "'";
		this.jdbcTemplate.execute(sql);
	}

}
