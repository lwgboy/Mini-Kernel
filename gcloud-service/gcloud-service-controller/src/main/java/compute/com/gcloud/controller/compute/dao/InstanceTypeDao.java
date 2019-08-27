package com.gcloud.controller.compute.dao;

import com.gcloud.common.util.StringUtils;
import com.gcloud.controller.compute.entity.InstanceType;
import com.gcloud.controller.compute.handler.api.model.DescribeInstanceTypesParams;
import com.gcloud.controller.compute.model.vm.DetailInstanceTypeParams;
import com.gcloud.framework.db.dao.impl.JdbcBaseDaoImpl;
import com.gcloud.framework.db.jdbc.annotation.Jdbc;
import com.gcloud.header.compute.msg.api.model.InstanceTypeItemType;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Jdbc("controllerJdbcTemplate")
@Repository
public class InstanceTypeDao extends JdbcBaseDaoImpl<InstanceType, String> {

	public List<InstanceTypeItemType> describeInstanceTypes(DescribeInstanceTypesParams params, Class<InstanceTypeItemType> clazz) {
		StringBuffer sql = new StringBuffer();
		List<Object> values = new ArrayList<>();

		sql.append("select id AS instanceTypeId, name AS instanceTypeName,vcpus AS CpuCoreCount,memory_mb AS MemorySize from gc_instance_types where deleted=0");
        if (StringUtils.isNotBlank(params.getZoneId())) {
            sql.append(" AND id in (select z.instance_type_id from gc_zone_instance_types z where z.zone_id = ?)");
            values.add(params.getZoneId());
        }

		return findBySql(sql.toString(), values, clazz);
	}
	
	public <E> List<E> detailInstanceType(DetailInstanceTypeParams params, Class<E> clazz) {
		StringBuffer sql = new StringBuffer();
		List<Object> values = new ArrayList<>();
		sql.append("select it.*, zone.name as zone_name from gc_instance_types as it left join gc_zones as zone on it.zone_id = zone.id where it.id = ?");
		values.add(params.getId());
		
		return findBySql(sql.toString(), values, clazz);
	}
	
	public <E> List<E> getZonesByInstanceType(String instanceTypeId, Class<E> clazz) {
		StringBuffer sql = new StringBuffer();
		List<Object> values = new ArrayList<>();
		
		sql.append("select gz.id as zone_id, gz.name as zone_name from gc_zones as gz, gc_zone_instance_types as gzit where gz.id = gzit.zone_id and gzit.instance_type_id = ?");
		values.add(instanceTypeId);
		
		return findBySql(sql.toString(), values, clazz);
	}
}
