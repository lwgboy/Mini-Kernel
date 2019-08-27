package com.gcloud.controller.compute.dao;

import com.gcloud.common.util.StringUtils;
import com.gcloud.controller.compute.entity.VmInstance;
import com.gcloud.controller.compute.handler.api.model.DescribeInstancesParams;
import com.gcloud.controller.utils.SqlUtil;
import com.gcloud.core.currentUser.policy.enums.UserResourceFilterPolicy;
import com.gcloud.core.currentUser.policy.model.FilterPolicyModel;
import com.gcloud.core.currentUser.policy.service.IUserResourceFilterPolicy;
import com.gcloud.core.service.SpringUtil;
import com.gcloud.framework.db.PageResult;
import com.gcloud.framework.db.dao.impl.JdbcBaseDaoImpl;
import com.gcloud.framework.db.jdbc.annotation.Jdbc;
import com.gcloud.header.api.model.CurrentUser;
import com.gcloud.header.compute.enums.VmState;
import com.gcloud.header.compute.enums.VmStepState;
import com.gcloud.header.compute.enums.VmTaskState;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/*
 * @Date 2015-4-14
 * 
 * @Author yaowj@g-cloud.com.cn
 * 
 * @Copyright 2015 www.g-cloud.com.cn Inc. All rights reserved.
 * 
 * @Description gc_instances dao 实现类
 */
@Jdbc("controllerJdbcTemplate")
@Repository
public class InstanceDao extends JdbcBaseDaoImpl<VmInstance, String>{

	public boolean isVmAliasExist(String alias, String id) {
		StringBuffer sb = new StringBuffer();
		Object[] values = null;
		sb.append("SELECT COUNT(*) FROM gc_instances vm WHERE vm.alias = ? AND (vm.trashed is null OR vm.trashed = ?) ");
		if (id != null) {
			values = new Object[3];
			values[0] = alias;
			values[1] = VmTaskState.LOGIC_DELETEING.value();
			values[2] = id;
			sb.append("AND vm.id != ?");
		} else {
			values = new Object[2];
			values[0] = alias;
			values[1] = VmTaskState.LOGIC_DELETEING.value();
		}
		int num = this.jdbcTemplate.queryForObject(sb.toString(), values,
				Integer.class);
		return num > 0;
	}


	public void deleteByInstanceId(String instanceId) {

		String sql = "delete from gc_instances where instanceId = ?";

		Object[] values = { instanceId };

		this.jdbcTemplate.update(sql, values);

	}
	
	public boolean updateInstanceStepState(String id, VmStepState stepState, boolean inTask, VmState... inVmStates) {
		List<Object> values = new ArrayList<>();
		StringBuffer sql = new StringBuffer();
		sql.append("update gc_instances t set step_state = ? where id = ?");
		sql.append(" and (step_state is null or step_state = '') ");
		if(inTask){
			sql.append(" and task_state is not null and task_state <> ''");
		}else{
			sql.append(" and (task_state is null or task_state = '') ");
		}

		values.add(stepState.value());
		values.add(id);

		if(inVmStates != null && inVmStates.length > 0){
			String inStr = SqlUtil.inPreStr(inVmStates.length);
			sql.append(" and state in (").append(inStr).append(")");
			values.addAll(Arrays.stream(inVmStates).map(VmState::value).collect(Collectors.toList()));
		}


		return this.jdbcTemplate.update(sql.toString(), values.toArray()) > 0;
	}

	public boolean updateInstanceTaskState(String id, VmTaskState taskState){
		List<Object> values = new ArrayList<>();
		StringBuffer sql = new StringBuffer();
		sql.append("update gc_instances t set task_state = ? where id = ?");
		sql.append(" and (task_state is null or task_state = '') ");
		sql.append(" and (step_state is null or step_state = '') ");

		values.add(taskState.value());
		values.add(id);


		return this.jdbcTemplate.update(sql.toString(), values.toArray()) > 0;
	}

	public boolean updateInstanceTaskStateAndStepState(String id, VmTaskState taskState, VmStepState stepState){

		List<Object> values = new ArrayList<>();
		StringBuffer sql = new StringBuffer();
		sql.append("update gc_instances t set task_state = ?, step_state = ? where id = ?");
		sql.append(" and (task_state is null or task_state = '') ");
		sql.append(" and (step_state is null or step_state = '') ");

		values.add(taskState.value());
		values.add(stepState.value());
		values.add(id);

		return this.jdbcTemplate.update(sql.toString(), values.toArray()) > 0;

	}

	public boolean hasImageInstance(String imageId){

		String sql = "select * from gc_instances t where t.image_id = ? limit 1";

		List<Object> values = new ArrayList<>();
		values.add(imageId);


		List<VmInstance> instances = this.findBySql(sql, values);
		return instances != null && instances.size() > 0;
	}

	public int cleanState(String instanceId){

		String sql = "update gc_instances set task_state = null, step_state = null where id = ?";
		Object[] values = {instanceId};

		return this.jdbcTemplate.update(sql, values);
	}

	public int cleanStepState(String instanceId){

		String sql = "update gc_instances set step_state = null where id = ?";
		Object[] values = {instanceId};

		return this.jdbcTemplate.update(sql, values);
	}
	
	public <E> PageResult<E>  describeInstances(DescribeInstancesParams params, CurrentUser currentUser, Class<E> clazz) {
		IUserResourceFilterPolicy filterPolicy = (IUserResourceFilterPolicy)SpringUtil.getBean(UserResourceFilterPolicy.TYPICAL.getFilterPolicyClazz());
		FilterPolicyModel sqlModel = filterPolicy.filterPolicy(currentUser, "i.");
		
		StringBuffer sql = new StringBuffer();
		List<Object> values = new ArrayList<>();

		sql.append("select i.* from gc_instances i where i.trashed is null ");
		
		if(StringUtils.isNotBlank(params.getInstanceName())){
			sql.append(" and alias LIKE ?");
			values.add(params.getInstanceName());
		}
		
		if(StringUtils.isNotBlank(params.getStatus())){
			sql.append(" and state = ?");
			values.add(params.getStatus());
		}
		
		if(StringUtils.isNotBlank(params.getZoneId())){
			sql.append(" and zone_id = ?");
			values.add(params.getZoneId());
		}

		if(params.getInstanceIds() != null && params.getInstanceIds().size() > 0){
			String inStr = SqlUtil.inPreStr(params.getInstanceIds().size());
			sql.append(" and id in (").append(inStr).append(")");
			values.addAll(params.getInstanceIds());
		}
		
		sql.append(sqlModel.getWhereSql());
		values.addAll(sqlModel.getParams());
		
		return findBySql(sql.toString(), values, params.getPageNumber(), params.getPageSize(), clazz);
	}

	public List<VmInstance> getByIds(List<String> ids){

		if(ids == null || ids.size() == 0){
			return new ArrayList<>();
		}

		List<Object> values = new ArrayList<>();

		StringBuffer sql = new StringBuffer();
		sql.append("select * from gc_instances where ");
		String inStr = SqlUtil.inPreStr(ids.size());
		sql.append(" id in (").append(inStr).append(")");
		values.addAll(ids);


		return findBySql(sql.toString(), values);
	}
	
	public <E> List<E> instanceStatistics(Class<E> clazz){
		String sql = "select state, COUNT(*) as countNum from gc_instances  where trashed is null GROUP BY state";
		return findBySql(sql, clazz);
	}
	
	public <E> List<E> instanceStatisticsByZoneState(Class<E> clazz){
		String sql = "SELECT zone_id as zoneId,state,COUNT(*) as countNum from gc_instances where trashed is null GROUP BY zone_id,state";
		return findBySql(sql, clazz);
	}
	
	public <E> List<E> computeNodeUsedResource(Class<E> clazz){
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT hostname,SUM(core) as usedCore,SUM(memory) as usedMemory from gc_instances ");
		sql.append("where trashed is null and state != 'stopped' ");
		sql.append("GROUP BY hostname ");
		return findBySql(sql.toString(), clazz);
	}
}
