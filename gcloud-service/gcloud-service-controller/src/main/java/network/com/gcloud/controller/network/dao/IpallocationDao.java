package com.gcloud.controller.network.dao;

import com.gcloud.controller.network.entity.Ipallocation;
import com.gcloud.framework.db.dao.impl.JdbcBaseDaoImpl;
import org.springframework.stereotype.Repository;

/**
 * Created by yaowj on 2018/10/25.
 */
@Repository
public class IpallocationDao extends JdbcBaseDaoImpl<Ipallocation, Long> {

    public int deleteByPortId(String portId){

        String sql = "delete from gc_ipallocations where port_id = ?";
        Object[] values = {portId};
        return this.jdbcTemplate.update(sql, values);
    }

}
