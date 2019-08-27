package com.gcloud.controller.network.dao;

import com.gcloud.controller.network.entity.Router;
import com.gcloud.controller.network.model.DescribeVRoutersParams;
import com.gcloud.core.currentUser.policy.enums.UserResourceFilterPolicy;
import com.gcloud.core.currentUser.policy.model.FilterPolicyModel;
import com.gcloud.core.currentUser.policy.service.IUserResourceFilterPolicy;
import com.gcloud.core.service.SpringUtil;
import com.gcloud.framework.db.PageResult;
import com.gcloud.framework.db.dao.impl.JdbcBaseDaoImpl;
import com.gcloud.header.api.model.CurrentUser;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
public class RouterDao  extends JdbcBaseDaoImpl<Router, String>{

	public <E> PageResult<E> describeVRouters(DescribeVRoutersParams params, Class<E> clazz, CurrentUser currentUser){
		IUserResourceFilterPolicy filterPolicy = (IUserResourceFilterPolicy)SpringUtil.getBean(UserResourceFilterPolicy.TYPICAL.getFilterPolicyClazz());
		FilterPolicyModel sqlModel = filterPolicy.filterPolicy(currentUser, "r.");
		List<Object> values = new ArrayList<>();
		
		StringBuffer sql = new StringBuffer();

//        sql.append("select r.id as vRouterId,r.name as vRouterName,r.region_id,r.status as regionId from gc_routers r where 1 = 1 ");
		sql.append("select r.id, max(r.external_gateway_network_id) external_gateway_network_id, max(r.status) status, max(r.region_id) region_id, max(r.name) name, group_concat(distinct i.subnet_id) subnets");
		sql.append(" from gc_routers r left join gc_router_ports rp on r.id = rp.router_id");
		sql.append(" left join gc_ipallocations i on rp.port_id = i.port_id");
		sql.append(" where 1 = 1");   
        sql.append(sqlModel.getWhereSql());
		values.addAll(sqlModel.getParams());
		sql.append(" group by r.id");
        sql.append(" order by r.create_time desc");

        return findBySql(sql.toString(), values, params.getPageNumber(), params.getPageSize(), clazz);
	}
	
	public int routerStatistics() {
		List<Object> values = new ArrayList<>();
		String sql = "select count(*) from gc_routers";
		return countBySql(sql, values);
	}
}
