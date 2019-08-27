package com.gcloud.controller.security.dao;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.gcloud.common.util.StringUtils;
import com.gcloud.controller.compute.dao.InstanceDao;
import com.gcloud.controller.security.entity.SecurityClusterVm;
import com.gcloud.controller.security.enums.SecurityNetworkType;
import com.gcloud.controller.security.model.SecurityClusterAddableInstanceParams;
import com.gcloud.controller.utils.SqlUtil;
import com.gcloud.core.exception.GCloudException;
import com.gcloud.framework.db.PageResult;
import com.gcloud.framework.db.dao.impl.JdbcBaseDaoImpl;
import com.gcloud.framework.db.jdbc.annotation.Jdbc;

import lombok.extern.slf4j.Slf4j;

@Jdbc("controllerJdbcTemplate")
@Repository
@Slf4j
public class SecurityClusterVmDao extends JdbcBaseDaoImpl<SecurityClusterVm, String> {
	
	@Autowired
	private InstanceDao instanceDao;
	
	public <E> PageResult<E> findAddableInstance(SecurityClusterAddableInstanceParams params, Class<E> clazz) {
		StringBuffer sql = new StringBuffer();
		List<Object> values = new ArrayList<>();
		
		//TODO 修改逻辑
//		sql.append("select * from gc_cluster_subnet where cluster_id = ? and network_type = ?");
//		values.add(params.getId());
//		values.add(SecurityNetworkType.PROTECTION.value());
		
		

		Map<String, Object> findParams = new HashMap<String, Object>();
		findParams.put("cluster_id", params.getId());
		List<SecurityClusterVm> list = findByProperties(findParams);
		
		List<String> instanceIdList = new ArrayList<>();
		for (SecurityClusterVm vm : list) {
			instanceIdList.add(vm.getInstanceId());
		}
		
		sql.append("select * from gc_instances where 1=1");
		if(!instanceIdList.isEmpty()) {
			sql.append(" and id not in (select instance_id from gc_security_cluster_vm)");
		}
		
		sql.append(" order by start_time desc");
		
		return instanceDao.findBySql(sql.toString(), values, params.getPageNumber(), params.getPageSize(), clazz);
	}
	
	@Transactional
	public void addInstance(String id, List<String> instanceIds) {
		List<SecurityClusterVm> data = new ArrayList<>();
		for (String instanceId : instanceIds) {
			SecurityClusterVm entity = new SecurityClusterVm();
			entity.setClusterId(id);
			entity.setInstanceId(instanceId);
			data.add(entity);
		}

		try {
			this.saveBatch(data);
		} catch (Exception e) {
			log.error("0160504::添加实例失败");
			throw new GCloudException("0160504::添加实例失败");
		}
	}

	/**
	 * 根据安全集群的ID查询已经添加到该安全集群中的实例信息
	 * @param clusterId 安全集群的ID
	 * @return 实例信息
	 */
	public <E> List<E> findInstancesByClusterId(String clusterId, Class<E> clazz) {
		StringBuffer vmSql = new StringBuffer();
		List<Object> values = new ArrayList<>();
		
		vmSql.append("select * from gc_instances where ");
		if(StringUtils.isNotBlank(clusterId)) {
			vmSql.append(" id in (select instance_id from gc_security_cluster_vm where cluster_id = ?)");
			values.add(clusterId);
		} else {
			vmSql.append(" 1 != 1");
		}
		
		return instanceDao.findBySql(vmSql.toString(), values, clazz);
	}
}
