package com.gcloud.controller.compute.dao;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Repository;

import com.gcloud.common.model.BaseParams;
import com.gcloud.controller.compute.entity.ComputeNode;
import com.gcloud.controller.compute.model.node.DescribeNodesParams;
import com.gcloud.framework.db.PageResult;
import com.gcloud.framework.db.dao.impl.JdbcBaseDaoImpl;
import com.gcloud.framework.db.jdbc.annotation.Jdbc;


/*
 * @Date 2015-4-16
 * 
 * @Author yaowj@g-cloud.com.cn
 * 
 * @Copyright 2015 www.g-cloud.com.cn Inc. All rights reserved.
 * 
 * @Description TODO
 */
@Jdbc("controllerJdbcTemplate")
@Repository
public class ComputeNodeDao extends JdbcBaseDaoImpl<ComputeNode, Integer> {

	public int hasActiveNodeByGroupId(String groupId) {

		String sql = "select count(1) nodeNum from gc_compute_nodes t where t.state = 1";
		sql += " and t.hostName in (select n.hostName from gc_group_node n where groupId = ?) limit 1";

		Object[] values = { groupId };

		int num = this.jdbcTemplate.queryForObject(sql, values, Integer.class);

		return num;

	}

	public List<ComputeNode> getUngroupNode() {

		String sql = "select * from gc_compute_nodes t where t.state = 1 and t.hostName not in";
		sql += " (select hostName from gc_group_node)";

		return this.findBySql(sql);

	}

	public void updateBatch(String whereField, List<String> ids,
			String setField, Object value) {
		if (ids.size() == 0) {
			return;
		}
		StringBuffer sb = new StringBuffer();
		sb.append("UPDATE gc_compute_nodes SET `");
		sb.append(setField);
		sb.append("` = '");
		sb.append(value);
		sb.append("'  WHERE ");
		int l = ids.size();
		Object[] os = new Object[l];
		for (int i = 0; i < l; i++) {
			if (i > 0) {
				sb.append(" OR ");
			}
			sb.append(" `");
			sb.append(whereField);
			sb.append("` = ? ");

			os[i] = ids.get(i);
		}
		jdbcTemplate.update(sb.toString(), os);
	}


	public PageResult getNodePage(BaseParams p, String groupId) {

		List<Object> valueList = new ArrayList<Object>();

		String sql = "select * from gc_compute_nodes where state = 1 and hostName in";
		sql += " (select hostName from gc_group_node where groupId = ?)";

		valueList.add(groupId);

		if (!StringUtils.isBlank(p.getKey())) {

			String likeKey = "%" + p.getKey() + "%";

			sql += " and (hostName like ? or nodeIp like ? or type like ?)";

			valueList.add(likeKey);
			valueList.add(likeKey);
			valueList.add(likeKey);
		}

		if (!StringUtils.isBlank(p.getSort())
				&& !StringUtils.isBlank(p.getDir())) {
			sql += " ORDER BY " + p.getSort() + " " + p.getDir();
		}

		// 等待架构提供接口，先自己改BaseDao方法
		return this.findPageBySql(sql, valueList, p.getPage(), p.getPageSize());
	}


	public List<ComputeNode> getGroupActiveNode(String groupId) {

		String sql = "select * from gc_compute_nodes where state = 1 and hostName in";
		sql += " (select n.hostName from gc_group_node n where n.groupId = ?)";

		List<Object> valueList = new ArrayList<Object>();
		valueList.add(groupId);

		return this.findBySql(sql, valueList);

	}

	public PageResult getUngroupNodePage(BaseParams p) {

		List<Object> valueList = new ArrayList<Object>();

		String sql = "select * from gc_compute_nodes t where t.state = 1 and t.hostName not in";
		sql += " (select hostName from gc_group_node)";

		if (!StringUtils.isBlank(p.getKey())) {

			String likeKey = "%" + p.getKey() + "%";

			sql += " and (hostName like ? or nodeIp like ? or type like ?)";

			valueList.add(likeKey);
			valueList.add(likeKey);
			valueList.add(likeKey);

		}

		if (!StringUtils.isBlank(p.getSort())
				&& !StringUtils.isBlank(p.getDir())) {
			sql += " ORDER BY " + p.getSort() + " " + p.getDir();
		}

		// 等待架构提供接口，先自己改BaseDao方法
		return this.findPageBySql(sql, valueList, p.getPage(), p.getPageSize());
	}

	public List<ComputeNode> findNodeByGroup(String groupId) {

		List<Object> valueList = new ArrayList<Object>();

		String sql = "select * from gc_compute_nodes where state = 1 and hostName in";
		sql += " (select hostName from gc_group_node where groupId = ?)";

		valueList.add(groupId);

		return this.findBySql(sql, valueList);
	}

	public List<Map<String, Object>> getNodeMap(String groupId, Boolean isAll) {
		String sql = "select cn.state,cn.cloudPlatform,cn.ftNode,cn.hostName,gn.groupId,gn.clusterGroupId,rg.groupName,cn.hypervisorType,cn.nodeIp ";
		sql += " from gc_compute_nodes cn LEFT JOIN gc_group_node gn on cn.hostName = gn.hostName LEFT JOIN gc_resource_group rg on gn.groupId = rg.groupId ";
		sql += " where 1 = 1 ";
		if (isAll == null || isAll == false) {
			sql += " and cn.state = 1 ";
		}
		if (!StringUtils.isBlank(groupId)) {
			if (groupId.trim().equals("0")) {
				sql += " and gn.groupId is null ";
			} else {
				sql += " and gn.groupId = '" + groupId + "'";
			}
		}
		return this.jdbcTemplate.queryForList(sql);
	}
	
	public List<ComputeNode> getUngroupNodeNotFt() {

		String sql = "select * from gc_compute_nodes t where t.state = 1 and t.ftNode = 0 and t.hostName not in";
		sql += " (select hostName from gc_group_node) order by t.id ";

		return this.findBySql(sql);

	}
	
	public List<ComputeNode> findNodeByGroupNotFt(String groupId) {

		List<Object> valueList = new ArrayList<Object>();

		String sql = "select * from gc_compute_nodes where state = 1 and ftNode = 0 and hostName in";
		sql += " (select hostName from gc_group_node where groupId = ?) order by id";

		valueList.add(groupId);

		return this.findBySql(sql, valueList);
	}
	
	public <E> PageResult<E> pageNodeGroupOrNot(DescribeNodesParams params, boolean isGroup, Class<E> clazz) {
		StringBuffer sql = new StringBuffer();
		List<Object> values = new ArrayList<>();
		
		sql.append("select t.node_ip as ip, t.hostname, t.type from gc_compute_nodes t where t.state = 0 and t.hostname");
		if(!isGroup) {
			sql.append(" not ");
		}
		sql.append(" in (select hostname from gc_group_node ");
		
		if(isGroup) {
			sql.append(" where group_id = ?");
			values.add(params.getGroupId());
		}
		sql.append(")");
		if(StringUtils.isNotBlank(params.getKey())) {
			sql.append(" and (hostname like concat('%', ?, '%') or node_ip like concat('%', ?, '%') or type like concat('%', ?, '%')) ");
			values.add(params.getKey());
			values.add(params.getKey());
			values.add(params.getKey());
		}
		
		return findBySql(sql.toString(), values, params.getPageNumber(), params.getPageSize(), clazz);
	}
	
	public <E> List<E> computeNodeTotalResource(Class<E> clazz){
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT hostname,cpu_total as totalCore,memory_total as totalMemory ");
		sql.append("from gc_compute_nodes ");
		sql.append("where state=1 ");
		return findBySql(sql.toString(), clazz);
	}
}
