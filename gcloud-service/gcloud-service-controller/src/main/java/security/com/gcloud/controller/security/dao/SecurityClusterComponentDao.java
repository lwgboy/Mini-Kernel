package com.gcloud.controller.security.dao;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Repository;

import com.gcloud.common.util.StringUtils;
import com.gcloud.controller.security.entity.SecurityClusterComponent;
import com.gcloud.controller.security.enums.SecurityClusterComponentObjectType;
import com.gcloud.controller.security.model.DescribeSecurityClusterComponentParams;
import com.gcloud.controller.utils.SqlUtil;
import com.gcloud.framework.db.PageResult;
import com.gcloud.framework.db.dao.impl.JdbcBaseDaoImpl;
import com.gcloud.framework.db.jdbc.annotation.Jdbc;

@Jdbc("controllerJdbcTemplate")
@Repository
public class SecurityClusterComponentDao extends JdbcBaseDaoImpl<SecurityClusterComponent, String> {
	
    public int deleteByClusterId(String clusterId){
        String sql = "delete from gc_security_cluster_component where cluster_id = ?";
        Object[] values = {clusterId};
        return this.jdbcTemplate.update(sql, values);
    }

    public int deleteHaByClusterId(String clusterId){
        String sql = "delete from gc_security_cluster_component where cluster_id = ? and ha = true";
        Object[] values = {clusterId};
        return this.jdbcTemplate.update(sql, values);
    }
    
    //in语句优化
//    public <E> List<E> findComponentsByClusterIds(List<String> clusters, Class<E> clazz) {
//    	StringBuffer sql = new StringBuffer();
//    	List<Object> values = new ArrayList<>();
//    	
//    	sql.append("select * from gc_security_cluster_component where ");
//    	
//    	if(clusters != null && !clusters.isEmpty()) {
//    		String inPreSql = SqlUtil.inPreStr(clusters.size());
//    		sql.append(" cluster_id in (").append(inPreSql).append(")");
//    		values.addAll(clusters);
//    	} else {
//    		sql.append(" 1 != 1");
//    	}
//    	
//    	return findBySql(sql.toString(), values, clazz);
//    }
    
    public <E> PageResult<E> pageCompontents(DescribeSecurityClusterComponentParams params, Class<E> clazz) {
    	StringBuffer sql = new StringBuffer();
    	List<Object> values = new ArrayList<>();
    	
    	sql.append("select * from gc_security_cluster_component where 1 = 1");
    	
    	if(StringUtils.isNotBlank(params.getId())) {
    		sql.append(" and cluster_id = ?");
    		values.add(params.getId());
    	}
    	
    	if(StringUtils.isNotBlank(params.getType())) {
    		sql.append(" and type = ?");
    		values.add(params.getType());
    	}
    	
    	sql.append(" order by type desc");
    	
    	return findBySql(sql.toString(), values, params.getPageNumber(), params.getPageSize(), clazz);
    }
    
    public <E> List<E> getComponentObjectList(SecurityClusterComponentObjectType scObjectType, Class<E> clazz) {
    	StringBuffer sql = new StringBuffer();
    	List<Object> values = new ArrayList<>();

    	String tableName = "";
    	String objectType = "";
    	if(scObjectType == null) {
    		return new ArrayList<E>();
    	} else if(scObjectType.equals(SecurityClusterComponentObjectType.VM)) {
    		tableName = "gc_instances";
    		objectType = scObjectType.value();
    	} else if(scObjectType.equals(SecurityClusterComponentObjectType.DOCKER_CONTAINER)) {
    		//TODO dc的表名
    		return new ArrayList<E>();
    	}
    	
    	sql.append("select * from ").append(tableName).append(" where ");
    	sql.append(" id in (select object_id from gc_security_cluster_component where object_type = ?)");
    	values.add(objectType);
    	
    	return findBySql(sql.toString(), values, clazz);
    }
}
