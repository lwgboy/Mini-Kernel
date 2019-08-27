package com.gcloud.controller.network.dao;

import com.gcloud.controller.network.entity.OvsBridgeUsage;
import com.gcloud.controller.network.enums.OvsBridgeRefType;
import com.gcloud.controller.network.enums.OvsBridgeState;
import com.gcloud.framework.db.dao.impl.JdbcBaseDaoImpl;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
public class OvsBridgeUsageDao extends JdbcBaseDaoImpl<OvsBridgeUsage, String>{

    public int delete(String id, OvsBridgeRefType refType, String refId){

        StringBuffer sql = new StringBuffer();
        sql.append("delete from gc_ovs_bridges_usage where bridge_id = ? and ref_type = ? and ref_id = ?");
        Object[] values = {id, refType.value(), refId};
        return this.jdbcTemplate.update(sql.toString(), values);
    }

    public int delete(OvsBridgeRefType refType, String refId){

        StringBuffer sql = new StringBuffer();
        sql.append("delete from gc_ovs_bridges_usage where ref_type = ? and ref_id = ?");
        Object[] values = {refType.value(), refId};
        return this.jdbcTemplate.update(sql.toString(), values);

    }

    public List<OvsBridgeUsage> getForUpdate(String bridgeId){

        String sql = "select * from gc_ovs_bridges_usage where bridge_id = ? for update";
        List<Object> values = new ArrayList<>();
        values.add(bridgeId);

        return findBySql(sql, values);
    }

    public int allocate(OvsBridgeUsage usage){

        StringBuffer sql = new StringBuffer();
        sql.append("insert into gc_ovs_bridges_usage(bridge_id, ref_type, ref_id, create_time) ");
        sql.append(" select b.id, ?, ?, now() from gc_ovs_bridges b where b.id = ? and b.state = ?");

        Object[] values = {usage.getRefType(), usage.getRefId(), usage.getId(), OvsBridgeState.AVAILABLE.value()};

        return this.jdbcTemplate.update(sql.toString(), values);
    }
}
