package com.gcloud.controller.network.dao;

import com.gcloud.common.util.StringUtils;
import com.gcloud.controller.network.entity.Port;
import com.gcloud.controller.network.model.DescribeNetworkInterfacesParams;
import com.gcloud.controller.utils.SqlUtil;
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
public class PortDao extends JdbcBaseDaoImpl<Port, String> {

    public <E> PageResult<E> describePorts(DescribeNetworkInterfacesParams params, Class<E> clazz, CurrentUser currentUser){
    	IUserResourceFilterPolicy filterPolicy = (IUserResourceFilterPolicy)SpringUtil.getBean(UserResourceFilterPolicy.TYPICAL.getFilterPolicyClazz());
		FilterPolicyModel sqlModel = filterPolicy.filterPolicy(currentUser, "p.");
		
        List<Object> values = new ArrayList<>();
        StringBuffer sql = new StringBuffer();
        sql.append("select p.*, it.subnet_id, it.ip_address, it.router_id, psg.security_group_ids_str from gc_ports p")
                .append(" left join")
                .append(" (select i.port_id, rs.router_id, group_concat(ifnull(i.subnet_id, '')) subnet_id, group_concat(ifnull(i.ip_address, '')) ip_address, group_concat(ifnull(i.network_id, '')) network_id")
                .append(" from gc_ipallocations i left join gc_subnets s on i.subnet_id = s.id")
                .append(" left join")
                .append(" (select  group_concat(rp.router_id) router_id, i.subnet_id from gc_router_ports rp left join gc_ports p")
                .append(" on rp.port_id = p.id left join gc_ipallocations i on p.id = i.port_id group by i.subnet_id")
                .append(" ) rs")
                .append(" on i.subnet_id = rs.subnet_id ")
                .append(" group by i.port_id) it")
                .append(" on p.id = it.port_id")
                .append(" left join")
                .append(" (select p2.id, group_concat(sb.security_group_id) security_group_ids_str  ")
                .append(" from gc_ports p2, gc_security_group_port_bindings sb")
                .append(" where p2.id = sb.port_id group by p2.id) psg")
                .append(" on p.id = psg.id")
                .append(" where 1 = 1");


        if(StringUtils.isNotBlank(params.getvSwitchId())){
            sql.append(" and i.subnet_id = ?");
            values.add(params.getvSwitchId());
        }

        if(StringUtils.isNotBlank(params.getPrimaryIpAddress())){
            sql.append(" and i.ip_address = ?");
            values.add(params.getPrimaryIpAddress());
        }

        if(StringUtils.isNotBlank(params.getNetworkInterfaceName())){
            sql.append(" and p.name like concat('%', ?, '%')");
            values.add(params.getNetworkInterfaceName());
        }

        if(StringUtils.isNotBlank(params.getInstanceId())){
            sql.append(" and p.device_id = ?");
            values.add(params.getInstanceId());
        }

        if(StringUtils.isNotBlank(params.getSecurityGroupId())){
            sql.append(" and p.id in (select sgb.port_id from gc_security_group_port_bindings sgb where sgb.security_group_id = ?)");
            values.add(params.getSecurityGroupId());
        }

        if(params.getNetworkInterfaceIds() != null && params.getNetworkInterfaceIds().size() > 0){
            String inPreSql = SqlUtil.inPreStr(params.getNetworkInterfaceIds().size());
            sql.append(" and p.id in (").append(inPreSql).append(")");
            values.addAll(params.getNetworkInterfaceIds());
        }

        if(params.getDeviceOwners() != null && params.getDeviceOwners().size() > 0){
            String inPreSql = SqlUtil.inPreStr(params.getDeviceOwners().size());
            if(params.getIncludeOwnerless() != null && params.getIncludeOwnerless()){
                sql.append(" and (p.device_owner in (").append(inPreSql).append(") or p.device_owner is null or p.device_owner = '' )");
            }else{
                sql.append(" and p.device_owner in (").append(inPreSql).append(")");
            }

            values.addAll(params.getDeviceOwners());

        }
        
        sql.append(sqlModel.getWhereSql());
        values.addAll(sqlModel.getParams());

        sql.append(" order by create_time desc");

        return findBySql(sql.toString(), values, params.getPageNumber(), params.getPageSize(), clazz);

    }

    public int attachPort(String portId, String deviceId, String deviceOwner, String sufId, String preName, String aftName, String brName, String customOvsBr, Boolean noArpLimit){

        StringBuffer sql = new StringBuffer();
        sql.append("update gc_ports set device_id = ?, device_owner = ?,");
        sql.append(" suf_id = ?, pre_name = ?, aft_name = ?, br_name = ?, ovs_bridge_id = ?, no_arp_limit = ?");
        sql.append(" where id = ? and (device_id is null or device_id = '' )");

        Object[] values = {deviceId, deviceOwner, sufId, preName, aftName, brName, customOvsBr, noArpLimit, portId};

        return this.jdbcTemplate.update(sql.toString(), values);

    }
    
    public <E> List<E> getInstanceNetworkInterfaces(String instanceId, Class<E> clazz) {
    	String sql = "select p.id as networkInterfaceId, p.mac_address as macAddress, i.ip_address as primaryIpAddress from gc_ports p left join gc_ipallocations i on p.id = i.port_id where p.device_owner='compute:node' and p.device_id = '" + instanceId + "'";
    	return findBySql(sql, clazz);
    }

    public <E> List<E> getInstancePortAndIp(String instanceId, Class<E> clazz){

        List<Object> values = new ArrayList<>();
        StringBuffer sql = new StringBuffer();
        sql.append("select p.*, i.ip_address, i.subnet_id from gc_ports p left join gc_ipallocations i on p.id = i.port_id where p.device_id = ?");
        values.add(instanceId);

        return findBySql(sql.toString(), values, clazz);

    }

    public List<Port> subnetPorts(String subnetId){

        StringBuffer sql = new StringBuffer();
        List<Object> values = new ArrayList<>();

        sql.append("select * from gc_ports t where t.id in");
        sql.append(" (select i.port_id from gc_ipallocations i where i.subnet_id = ?)");
        values.add(subnetId);

        return findBySql(sql.toString(), values);
    }

}
