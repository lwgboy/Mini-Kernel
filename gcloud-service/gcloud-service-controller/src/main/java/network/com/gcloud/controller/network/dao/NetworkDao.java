package com.gcloud.controller.network.dao;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Repository;

import com.gcloud.controller.network.entity.Network;
import com.gcloud.controller.utils.SqlUtil;
import com.gcloud.core.currentUser.policy.enums.UserResourceFilterPolicy;
import com.gcloud.core.currentUser.policy.model.FilterPolicyModel;
import com.gcloud.core.currentUser.policy.service.IUserResourceFilterPolicy;
import com.gcloud.core.service.SpringUtil;
import com.gcloud.framework.db.PageResult;
import com.gcloud.framework.db.dao.impl.JdbcBaseDaoImpl;
import com.gcloud.header.network.model.ExternalNetworkSetType;
import com.gcloud.header.network.model.VpcsItemType;
import com.gcloud.header.network.msg.api.DescribeExternalNetworksMsg;
import com.gcloud.header.network.msg.api.DescribeVpcsMsg;

@Repository
public class NetworkDao extends JdbcBaseDaoImpl<Network,String>{
	public PageResult<VpcsItemType> describeVpcs(DescribeVpcsMsg msg){
		IUserResourceFilterPolicy filterPolicy = (IUserResourceFilterPolicy)SpringUtil.getBean(UserResourceFilterPolicy.TYPICAL.getFilterPolicyClazz());
		FilterPolicyModel sqlModel = filterPolicy.filterPolicy(msg.getCurrentUser(), "");
		
		String sql="select * from gc_networks where 1=1";
		List<Object> params=new ArrayList<Object>();
		if(msg.getVpcIds()!=null&&msg.getVpcIds().size()>0){
	        String inStr = SqlUtil.inPreStr(msg.getVpcIds().size());
			sql+=" and id in (" + inStr + ")";
			params.addAll(msg.getVpcIds());
		}
		sql += sqlModel.getWhereSql();
		params.addAll(sqlModel.getParams());
		return findBySql(sql,params,msg.getPageNumber(),msg.getPageSize(),msg.getSort(),msg.getDir(),VpcsItemType.class);
	}
	
	public PageResult<ExternalNetworkSetType> describeNetworks(DescribeExternalNetworksMsg msg){
		IUserResourceFilterPolicy filterPolicy = (IUserResourceFilterPolicy)SpringUtil.getBean(UserResourceFilterPolicy.TYPICAL.getFilterPolicyClazz());
		FilterPolicyModel sqlModel = filterPolicy.filterPolicy(msg.getCurrentUser(), "n.");
		StringBuffer sql = new StringBuffer();
		
		sql.append("select n.*, group_concat(s.id) subnetIds from `gc_networks` n "
				+ " left join `gc_subnets` s on n.id = s.network_id");
		
		sql.append(" where 1 = 1");
		
		List<Object> params=new ArrayList<Object>();
		if(msg.getNetworkIds()!=null&&msg.getNetworkIds().size()>0){
	        String inStr = SqlUtil.inPreStr(msg.getNetworkIds().size());
			sql.append(" and n.id in (" + inStr + ")");
			params.addAll(msg.getNetworkIds());
		}
		if(msg.getType() != null){
			sql.append(" and n.type = ?");
			params.add(msg.getType());
		}
		
		sql.append(sqlModel.getWhereSql());
		params.addAll(sqlModel.getParams());
		
		sql.append(" group by n.id");
		
		sql.append(" order by n.create_time desc");
		
		return findBySql(sql.toString(),params,msg.getPageNumber(),msg.getPageSize(),msg.getSort(),msg.getDir(),ExternalNetworkSetType.class);
	}
}
