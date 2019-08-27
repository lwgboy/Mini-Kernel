package com.gcloud.controller.network.dao;

import com.gcloud.controller.network.entity.QosBandwidthLimitRule;
import com.gcloud.framework.db.dao.impl.JdbcBaseDaoImpl;
import org.springframework.stereotype.Repository;

/**
 * Created by yaowj on 2018/10/30.
 */
@Repository
public class QosBandwidthLimitRuleDao extends JdbcBaseDaoImpl<QosBandwidthLimitRule, String> {

    public int deleteByPolicyId(String policyId){

        String sql = "delete from gc_qos_bandwidth_limit_rules where qos_policy_id = ?";
        Object[] values = {policyId};
        return this.jdbcTemplate.update(sql, values);
    }

}
