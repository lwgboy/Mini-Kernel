package com.gcloud.controller.network.dao;

import com.gcloud.common.util.StringUtils;
import com.gcloud.controller.network.entity.Subnet;
import com.gcloud.controller.network.model.DescribeVSwitchesParams;
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

/**
 * Created by yaowj on 2018/10/25.
 */
@Repository
public class SubnetDao extends JdbcBaseDaoImpl<Subnet, String> {
	public <E> PageResult<E> describeVSwitches(DescribeVSwitchesParams params, Class<E> clazz, CurrentUser currentUser){
		IUserResourceFilterPolicy filterPolicy = (IUserResourceFilterPolicy)SpringUtil.getBean(UserResourceFilterPolicy.TYPICAL.getFilterPolicyClazz());
		FilterPolicyModel sqlModel = filterPolicy.filterPolicy(currentUser, "s.");
		
		StringBuffer sql = new StringBuffer();
		List<Object> listParams = new ArrayList<Object>();

//        sql.append("select s.id as vSwitchId,s.network_id as vpcId,s.cidr as cidrBlock,s.name as vSwitchName,s.zone_id as zoneId,s.router_id as vRouterId from gc_subnets s");
        sql.append("select s.*, rs.router_id from gc_subnets s");
        sql.append(" left join");
        sql.append(" (select group_concat(rp.router_id) router_id, i.subnet_id from gc_router_ports rp ");
        sql.append(" left join gc_ports p on rp.port_id = p.id left join gc_ipallocations i on p.id = i.port_id group by i.subnet_id) rs");
        sql.append(" on s.id = rs.subnet_id");
        sql.append(" where 1=1");

        if(params.getVpcId() != null && StringUtils.isNotBlank(params.getVpcId())) {
            sql.append(" and s.router_id = ?");
            listParams.add(params.getVpcId());
        }
        if(params.getNetworkId() != null && StringUtils.isNotBlank(params.getNetworkId())) {
            sql.append(" and s.network_id = ?");
            listParams.add(params.getNetworkId());
        }
        if(params.getvSwitchId() != null && StringUtils.isNotBlank(params.getvSwitchId())) {
        	sql.append(" and s.id = ?");
        	listParams.add(params.getvSwitchId());
        }
        
        sql.append(sqlModel.getWhereSql());
        listParams.addAll(sqlModel.getParams());
        
        sql.append(" order by s.create_time desc");

        return findBySql(sql.toString(),listParams, params.getPageNumber(), params.getPageSize(), clazz);
	}

	public boolean hasSubnet(String networkId){
	    List<Object> values = new ArrayList<>();

	    String sql = "select * from gc_subnets t where t.network_id = ? limit 1";
        values.add(networkId);
        List<Subnet> subnets = findBySql(sql, values);
        return subnets != null && subnets.size() > 0;

    }
}
