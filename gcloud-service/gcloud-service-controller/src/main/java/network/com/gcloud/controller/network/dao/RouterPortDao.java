package com.gcloud.controller.network.dao;

import com.gcloud.common.util.StringUtils;
import com.gcloud.controller.network.entity.RouterPort;
import com.gcloud.controller.network.model.RouterSubnetInfo;
import com.gcloud.framework.db.dao.impl.JdbcBaseDaoImpl;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
public class RouterPortDao extends JdbcBaseDaoImpl<RouterPort, Long> {

    public int delete(String routerId, String portId){
        String sql = "delete from gc_router_ports where router_id = ? and port_id = ?";
        Object[] values = {routerId, portId};
        return this.jdbcTemplate.update(sql, values);
    }

    public List<RouterSubnetInfo> routerSubnetInfos(String routerId, String subnetId){

        List<Object> values = new ArrayList<>();

        StringBuffer sql = new StringBuffer();
        sql.append("select distinct rp.router_id, rp.port_id, i.subnet_id from gc_router_ports rp");
        sql.append(" left join gc_ports p on rp.port_id = p.id");
        sql.append(" left join gc_ipallocations i on p.id = i.port_id");
        sql.append(" where 1 = 1");

        if(StringUtils.isNotBlank(routerId)){
            sql.append(" and rp.router_id = ?");
            values.add(routerId);
        }

        if(StringUtils.isNotBlank(subnetId)){
            sql.append(" and i.subnet_id = ?");
            values.add(subnetId);
        }

        return this.findBySql(sql.toString(), values, RouterSubnetInfo.class);

    }

    public int deleteByRouter(String routerId){

        String sql = "delete from gc_router_ports where router_id = ?";
        Object[] values = {routerId};
        return this.jdbcTemplate.update(sql, values);
    }

    public boolean hasPort(String routerId, String portType){

        String sql = "select * from gc_router_ports t where t.router_id = ? and t.port_type = ? limit 1";
        List<Object> values = new ArrayList<>();
        values.add(routerId);
        values.add(portType);

        List<RouterPort> routerPorts = findBySql(sql, values);

        return routerPorts != null && routerPorts.size() > 0;
    }

}
