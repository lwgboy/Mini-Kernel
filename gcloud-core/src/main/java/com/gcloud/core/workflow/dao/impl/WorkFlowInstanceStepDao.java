package com.gcloud.core.workflow.dao.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Repository;

import com.alibaba.fastjson.JSONObject;
import com.gcloud.core.workflow.dao.IWorkFlowInstanceStepDao;
import com.gcloud.core.workflow.entity.WorkFlowInstanceStep;
import com.gcloud.core.workflow.enums.FlowStepStatus;
import com.gcloud.core.workflow.model.FirstStepRes;
import com.gcloud.framework.db.dao.impl.JdbcBaseDaoImpl;

@Repository
public class WorkFlowInstanceStepDao extends JdbcBaseDaoImpl<WorkFlowInstanceStep, Long> implements IWorkFlowInstanceStepDao {
	public List<WorkFlowInstanceStep> getStepsByIds(Long flowId, String toIds) {
		if(StringUtils.isBlank(toIds)) {
			return new ArrayList<WorkFlowInstanceStep>();
		}
		String[] ids = toIds.split(",");
		List<Object> valueList = new ArrayList<Object>();
		StringBuffer sql = new StringBuffer();
		sql.append("select * from gc_work_flow_instance_step w where w.flow_id=?");
		valueList.add(flowId);
		if (ids.length > 0) {
			sql.append(" and w.template_step_id in (");
			sql.append(Arrays.asList(ids).stream().collect(Collectors.joining(",")));
			sql.append(") ");
		}
		
		return this.findBySql(sql.toString(), valueList);
	}

	public List<WorkFlowInstanceStep> getNextRollBackSteps(Long flowId, boolean rollbackFailContinue) {
		List<Object> parameterValues = new ArrayList<Object>();
		/*StringBuffer sql = new StringBuffer();
		sql.append("select fis.* from gc_work_flow_instance_step fis where fis.flow_id=? and fis.state=? ");
		sql.append("and EXISTS(SELECT s.* from gc_work_flow_instance_step s where s.flow_id=? ");
		sql.append("and ((fis.y_to_ids is not null and find_in_set(s.template_step_id,fis.y_to_ids) != 0) or (fis.n_to_ids is not null and find_in_set(s.template_step_id,fis.n_to_ids) != 0)) ");
		sql.append("and s.state in('ROLLBACKED', 'ROLLBACKED_SKIP', 'ROLLBACKED_FAIL', 'FAILURE'))");
		parameterValues.add(flowId);
		parameterValues.add(FlowStepStatus.SUCCESS.name());
		parameterValues.add(flowId);
		
		SELECT template_step_id,getPriority(template_step_id, 20) as priority
		FROM gc_work_flow_instance_step where flow_id=20 
		ORDER BY (LENGTH(priority)-LENGTH(REPLACE(priority,'.',''))) DESC,priority DESC
		 */
		StringBuffer sql = new StringBuffer();
		sql.append("select *,getPriority(template_step_id, ?) as priority from gc_work_flow_instance_step ");
		sql.append("where flow_id=? and state=? ");
		if(rollbackFailContinue) {
			sql.append("and rollback_fail_continue=1 ");
		}
		sql.append("ORDER BY (LENGTH(priority)-LENGTH(REPLACE(priority,'.',''))) DESC,priority DESC");
		parameterValues.add(flowId);
		parameterValues.add(flowId);
		parameterValues.add(FlowStepStatus.SUCCESS.name());
		List<WorkFlowInstanceStep> list = findBySql(sql.toString(), parameterValues);
		List<WorkFlowInstanceStep> result = new ArrayList<WorkFlowInstanceStep>();
		if(list.size() > 0) {
			result.add(list.get(0));
		}
		return result;
	}
	
	@Override
	public WorkFlowInstanceStep getLastedSuccessStep(Long flowId) {
		// TODO Auto-generated method stub
		List<Object> parameterValues = new ArrayList<Object>();
		String sql = "select * from gc_work_flow_instance_step where flow_id=? and state=? order by template_step_id desc";
		parameterValues.add(flowId);
		parameterValues.add(FlowStepStatus.SUCCESS.name());
		List<WorkFlowInstanceStep> list = findBySql(sql, parameterValues);
		return list.size() > 0 ? list.get(0) : null;
	}

	@Override
	public List<WorkFlowInstanceStep> getAllNotFinishedStep() {
		// TODO Auto-generated method stub
		StringBuffer sql = new StringBuffer();
		sql.append("select * from gc_work_flow_instance_step ");
		sql.append("where step_type = 'command' and state in (");
		sql.append("'" + FlowStepStatus.EXECUTING.name() + "','" + FlowStepStatus.ROLLBACKING.name() + "'");
		sql.append(") ");
		return findBySql(sql.toString());
	}

	@Override
	public List<String> getFirstStepRess(Long flowTaskId) {
		List<Object> valueList = new ArrayList<Object>();
		valueList.add(flowTaskId);
		StringBuffer sql = new StringBuffer();
		sql.append("select s.res_json, ins.task_id from gc_work_flow_instance_step s LEFT JOIN gc_work_flow_instance ins on s.flow_id=ins.id ");
		sql.append("where s.from_ids is null and exists (SELECT b.id from gc_work_flow_task f LEFT JOIN gc_work_flow_batch b on f.task_id=b.ptask_id where f.id=? and s.flow_id=b.flow_id)");
		List<FirstStepRes> ress = this.findBySql(sql.toString(), valueList, FirstStepRes.class);
		List<String> result = new ArrayList<String>();
		for (FirstStepRes res : ress) {
			if (StringUtils.isNotBlank(res.getResJson())) {
				JSONObject resObj = JSONObject.parseObject(res.getResJson());
				resObj.put("taskId", res.getTaskId());
				result.add(resObj.toJSONString());
			}
		}
		return result;
	}

	@Override
	public List<String> getLastStepRess(Long flowTaskId) {
		List<Object> valueList = new ArrayList<Object>();
		valueList.add(flowTaskId);
		StringBuffer sql = new StringBuffer();
		sql.append("select * from gc_work_flow_instance_step s ");
		sql.append("where s.y_to_ids is null and s.n_to_ids is null and exists (SELECT b.id from gc_work_flow_task f LEFT JOIN gc_work_flow_batch b on f.task_id=b.ptask_id where f.id=? and s.flow_id=b.flow_id)");
		List<WorkFlowInstanceStep> steps = this.findBySql(sql.toString(), valueList);
		List<String> result = new ArrayList<String>();
		for (WorkFlowInstanceStep step : steps) {
			if (StringUtils.isNotBlank(step.getResJson())) {
				result.add(step.getResJson());
			}
		}
		return result;
	}
	
	@Override
	public List<WorkFlowInstanceStep> getStepsByTaskId(String taskId){
		List<Object> valueList = new ArrayList<Object>();
		String sql = "select * from gc_work_flow_instance_step w where w.task_id=? or w.rollback_task_id=?";
		valueList.add(taskId);
		valueList.add(taskId);

		return this.findBySql(sql, valueList);
	}
	
	public List<WorkFlowInstanceStep> getRepeatSteps(Long flowId, Integer stepId) {
		int startId = stepId * 10000;
		int endId = stepId * 100000;
		List<Object> valueList = new ArrayList<Object>();
		String sql = "select * from gc_work_flow_instance_step w where w.flow_id=? and (w.template_step_id=? or (w.template_step_id>=? and w.template_step_id<?))";
		valueList.add(flowId);
		valueList.add(stepId);
		valueList.add(startId);
		valueList.add(endId);
		return this.findBySql(sql, valueList);
	}

	@Override
	public WorkFlowInstanceStep getFirstStep(Long flowId) {
		List<Object> valueList = new ArrayList<Object>();
		String sql = "select * from gc_work_flow_instance_step w where w.flow_id=? and w.from_ids is null";
		valueList.add(flowId);
		List<WorkFlowInstanceStep> steps = this.findBySql(sql, valueList);
		return steps.size()>0?steps.get(0):null;
	}

	@Override
	public void deleteExpireData(String endTime) {
		String sql ="delete from gc_work_flow_instance_step where start_time <= '" + endTime + "'";
		this.jdbcTemplate.execute(sql);
	}

}
