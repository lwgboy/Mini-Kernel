package com.gcloud.controller.network.dao;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Repository;

import com.gcloud.common.util.StringUtils;
import com.gcloud.controller.network.entity.FloatingIp;
import com.gcloud.core.currentUser.policy.enums.UserResourceFilterPolicy;
import com.gcloud.core.currentUser.policy.model.FilterPolicyModel;
import com.gcloud.core.currentUser.policy.service.IUserResourceFilterPolicy;
import com.gcloud.core.service.SpringUtil;
import com.gcloud.framework.db.PageResult;
import com.gcloud.framework.db.dao.impl.JdbcBaseDaoImpl;
import com.gcloud.header.api.model.CurrentUser;
import com.gcloud.header.network.model.EipAddressSetType;
import com.gcloud.header.network.msg.api.DescribeEipAddressesMsg;
@Repository
public class FloatingIpDao extends JdbcBaseDaoImpl<FloatingIp, String>{
	public PageResult<EipAddressSetType> getByPage(DescribeEipAddressesMsg param) {
		IUserResourceFilterPolicy filterPolicy = (IUserResourceFilterPolicy)SpringUtil.getBean(UserResourceFilterPolicy.TYPICAL.getFilterPolicyClazz());
		FilterPolicyModel sqlModel = filterPolicy.filterPolicy(param.getCurrentUser(), "f.");
		
		List<Object> values = new ArrayList<Object>();

		StringBuffer sb = new StringBuffer();
		sb.append("SELECT f.region_id as regionId,f.floating_ip_address as ipAddress,f.id as allocationId,f.status as status,");
		sb.append("f.instance_id as instanceId,f.create_time as allocationTime,f.floating_network_id as externalNetworkId,");
		sb.append("n.name AS networkName, i.alias AS instanceName FROM gc_floating_ips f");
		sb.append(" LEFT JOIN gc_networks n ");
		sb.append(" ON f.floating_network_id = n.id");
		sb.append(" LEFT JOIN gc_instances i");
		sb.append(" ON f.instance_id = i.id");
		sb.append(" WHERE 1=1");
		
		if(StringUtils.isNotBlank(param.getAllocationId())) {
			sb.append(" AND f.id = ?");
			values.add(param.getAllocationId());
		}
		if(StringUtils.isNotBlank(param.getEipAddress())) {
			sb.append(" AND f.floating_ip_address = ?");
			values.add(param.getEipAddress());
		}
		if(StringUtils.isNotBlank(param.getStatus())) {
			sb.append(" AND f.status = ?");
			values.add(param.getStatus());
		}
		List<String> fields = new ArrayList<>();
		fields.add("n.name");
		fields.add("i.alias");
		fields.add("f.floating_ip_address");
		if (StringUtils.isNotBlank(param.getKey())) {
			sb.append(" AND (");
			for (int i = 0; i < fields.size(); i++) {
				if (i != 0) {
					sb.append(" OR ");
				}
				sb.append(String.format(" %s LIKE ?", fields.get(i)));
				values.add("%" + param.getKey() + "%");
			}
			sb.append(")");
		}
		
		sb.append(sqlModel.getWhereSql());
		values.addAll(sqlModel.getParams());

		sb.append(" ORDER BY f.create_time DESC");
		return this.findBySql(sb.toString(), values, param.getPageNumber(), param.getPageSize(), EipAddressSetType.class);
	}
}
