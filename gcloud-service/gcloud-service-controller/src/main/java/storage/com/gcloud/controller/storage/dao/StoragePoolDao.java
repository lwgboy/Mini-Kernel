
package com.gcloud.controller.storage.dao;

import com.gcloud.controller.storage.entity.StoragePool;
import com.gcloud.core.exception.GCloudException;
import com.gcloud.framework.db.PageResult;
import com.gcloud.framework.db.dao.impl.JdbcBaseDaoImpl;
import com.gcloud.header.compute.enums.StorageType;
import com.gcloud.header.storage.StorageErrorCodes;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class StoragePoolDao extends JdbcBaseDaoImpl<StoragePool, String> {

    public StoragePool checkAndGet(String zoneId, String categoryId) throws GCloudException {

        StringBuffer sql = new StringBuffer();
        sql.append("select p.* from gc_storage_pools p, gc_disk_category_pools t");
        sql.append(" where p.id = t.storage_pool_id");
        sql.append(" and t.zone_id = ? and t.disk_category_id = ?");

        List<Object> values = new ArrayList<>();
        values.add(zoneId);
        values.add(categoryId);

        List<StoragePool> res = this.findBySql(sql.toString(), values);
        if (res.isEmpty()) {
            throw new GCloudException(StorageErrorCodes.FAILED_TO_FIND_POOL);
        }
        return res.get(0);
    }

    public StoragePool checkAndGet(String poolId) throws GCloudException {
        StoragePool pool = this.getById(poolId);
        if (pool == null) {
            throw new GCloudException(StorageErrorCodes.FAILED_TO_FIND_POOL);
        }
        return pool;
    }

    public <E> PageResult<E> describeStoragePools(int pageNumber, int pageSize, Class<E> clazz) {
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT * FROM gc_storage_pools");
        sql.append(" ORDER BY pool_name ASC");
        return this.findBySql(sql.toString(), new ArrayList<>(), pageNumber, pageSize, clazz);
    }

    public StoragePool find(Integer provider, String storageType, String poolName, String hostname) {
        Map<String, Object> props = new HashMap<>();
        props.put(StoragePool.PROVIDER, provider);
        props.put(StoragePool.STORAGE_TYPE, storageType);
        props.put(StoragePool.POOL_NAME, poolName);
        if(StorageType.LOCAL.getValue().equals(storageType)){
            props.put(StoragePool.HOSTNAME, hostname);
        }
        List<StoragePool> res = this.findByProperties(props);
        return res.isEmpty() ? null : res.get(0);
    }

}
