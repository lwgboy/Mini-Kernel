package com.gcloud.controller.image.dao;

import com.gcloud.controller.image.entity.ImageProperty;
import com.gcloud.framework.db.dao.impl.JdbcBaseDaoImpl;
import org.springframework.stereotype.Repository;

@Repository
public class ImagePropertyDao extends JdbcBaseDaoImpl<ImageProperty, Long>{

    public int deleteByImageId(String imageId){

        String sql = "delete from gc_image_properties where image_id = ?";
        Object[] values = {imageId};
        return this.jdbcTemplate.update(sql, values);
    }

}
