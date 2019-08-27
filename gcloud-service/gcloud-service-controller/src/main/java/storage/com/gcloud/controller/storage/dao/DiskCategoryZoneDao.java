package com.gcloud.controller.storage.dao;

import org.springframework.stereotype.Repository;

import com.gcloud.controller.storage.entity.DiskCategoryZone;
import com.gcloud.framework.db.dao.impl.JdbcBaseDaoImpl;

@Repository
public class DiskCategoryZoneDao extends JdbcBaseDaoImpl<DiskCategoryZone, Integer>{

}
