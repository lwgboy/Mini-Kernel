package com.gcloud.controller.compute.dao;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Repository;

import com.gcloud.controller.compute.entity.VmUsb;
import com.gcloud.controller.utils.SqlUtil;
import com.gcloud.framework.db.dao.impl.JdbcBaseDaoImpl;
import com.gcloud.framework.db.jdbc.annotation.Jdbc;
@Jdbc("controllerJdbcTemplate")
@Repository
public class VmUsbDao extends JdbcBaseDaoImpl<VmUsb, Integer>{
	
	public List<VmUsb> getVmUsbByInsIds(List<String> instanceIds) {
		
		String inPreSql = SqlUtil.inPreStr(instanceIds.size());
    	String sql = "select * from gc_vm_usb where instanceId in (" + inPreSql + ")";
    	List<Object> values = new ArrayList<Object>();
    	values.addAll(instanceIds);
    	
    	return this.findBySql(sql, values);
	}
	
}
