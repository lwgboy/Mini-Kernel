package com.gcloud.controller.storage.dao;

import com.gcloud.controller.storage.entity.StoragePoolZone;
import com.gcloud.framework.db.dao.impl.JdbcBaseDaoImpl;
import org.springframework.stereotype.Repository;

@Repository
public class StoragePoolZoneDao extends JdbcBaseDaoImpl<StoragePoolZone, Integer> {
}
