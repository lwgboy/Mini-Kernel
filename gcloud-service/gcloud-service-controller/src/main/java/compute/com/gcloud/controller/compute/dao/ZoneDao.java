
package com.gcloud.controller.compute.dao;

import com.gcloud.controller.compute.entity.AvailableZoneEntity;
import com.gcloud.framework.db.PageResult;
import com.gcloud.framework.db.dao.impl.JdbcBaseDaoImpl;
import com.gcloud.framework.db.jdbc.annotation.Jdbc;
import org.springframework.stereotype.Repository;

@Jdbc("controllerJdbcTemplate")
@Repository
public class ZoneDao extends JdbcBaseDaoImpl<AvailableZoneEntity, String> {

    public PageResult<AvailableZoneEntity> page(Integer pageNumber, Integer pageSize){

        String sql = "select * from gc_zones";

        return findBySql(sql, pageNumber, pageSize);

    }

}
