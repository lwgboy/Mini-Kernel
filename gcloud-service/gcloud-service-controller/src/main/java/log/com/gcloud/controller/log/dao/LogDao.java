package com.gcloud.controller.log.dao;

import com.gcloud.common.util.StringUtils;
import com.gcloud.controller.log.entity.Log;
import com.gcloud.framework.db.PageResult;
import com.gcloud.framework.db.dao.impl.JdbcBaseDaoImpl;
import com.gcloud.header.log.msg.api.ApiDescribeLogsMsg;

import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
@Repository
public class LogDao extends JdbcBaseDaoImpl<Log, Long> {
	public Log findTask(String taskId,String objectId) {
		StringBuffer sql = new StringBuffer();
		List<Object> parameterValues = new ArrayList<Object>();
		sql.append("select * from gc_log where task_id = ?");
		parameterValues.add(taskId);
		if(null != objectId) {
			sql.append(" and object_id=?");
			parameterValues.add(objectId);
		}
		List<Log> logs = findBySql(sql.toString(), parameterValues);
		if(logs != null && logs.size() > 0)
			return logs.get(0);
		return null;
	}
	
	public <E> PageResult<E>  describeLogs(ApiDescribeLogsMsg msg,  Class<E> clazz) {
		
		StringBuffer sql = new StringBuffer();
		List<Object> values = new ArrayList<>();

		sql.append("select l.* from gc_log l where l.id is not null ");
		
		if(StringUtils.isNotBlank(msg.getUserName())){
			sql.append(" and user_name LIKE ?");
			values.add(msg.getUserName());
		}
		
		if(StringUtils.isNotBlank(msg.getFunName())){
			sql.append(" and fun_name LIKE ?");
			values.add(msg.getFunName());
		}
		
		if(StringUtils.isNotBlank(msg.getStartTime())) {
			sql.append(" and start_time >= ?");
			values.add(msg.getStartTime());
		}
		
		if(StringUtils.isNotBlank(msg.getEndTime())) {
			sql.append(" and start_time <= ?");
			values.add(msg.getEndTime());
		}

		return findBySql(sql.toString(), values, msg.getPageNumber(), msg.getPageSize(), clazz);
	}
}
