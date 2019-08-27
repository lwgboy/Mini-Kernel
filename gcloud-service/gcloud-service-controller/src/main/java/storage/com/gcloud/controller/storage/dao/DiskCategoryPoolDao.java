package com.gcloud.controller.storage.dao;

import org.springframework.stereotype.Repository;

import com.gcloud.controller.storage.entity.DiskCategoryPool;
import com.gcloud.framework.db.dao.impl.JdbcBaseDaoImpl;

@Repository
public class DiskCategoryPoolDao extends JdbcBaseDaoImpl<DiskCategoryPool, Integer> {
}
