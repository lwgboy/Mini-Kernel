package com.gcloud.controller.network.dao;

import com.gcloud.controller.network.entity.QosPortPolicyBinding;
import com.gcloud.framework.db.dao.impl.JdbcBaseDaoImpl;
import org.springframework.stereotype.Repository;

/**
 * Created by yaowj on 2018/10/30.
 */
@Repository
public class QosPortPolicyBindingDao extends JdbcBaseDaoImpl<QosPortPolicyBinding, Long> {

    public int deleteByPortId(String portId){

        String sql = "delete from gc_qos_port_policy_bindings where port_id = ?";
        Object[] values = {portId};
        return this.jdbcTemplate.update(sql, values);
    }

    public int deleteByPolicyId(String policyId){

        String sql = "delete from gc_qos_port_policy_bindings where policy_id = ?";
        Object[] values = {policyId};
        return this.jdbcTemplate.update(sql, values);
    }

}
