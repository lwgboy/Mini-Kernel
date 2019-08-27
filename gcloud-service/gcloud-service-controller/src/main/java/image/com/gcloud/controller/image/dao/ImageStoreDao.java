package com.gcloud.controller.image.dao;

import org.springframework.stereotype.Repository;

import com.gcloud.controller.image.entity.ImageStore;
import com.gcloud.framework.db.dao.impl.JdbcBaseDaoImpl;

@Repository
public class ImageStoreDao extends JdbcBaseDaoImpl<ImageStore, Long>{
	public int deleteByImageId(String imageId){

        String sql = "delete from gc_image_stores where image_id = ?";
        Object[] values = {imageId};
        return this.jdbcTemplate.update(sql, values);
    }
}
