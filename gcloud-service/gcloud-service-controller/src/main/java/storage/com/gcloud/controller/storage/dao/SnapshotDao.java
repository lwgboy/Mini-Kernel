package com.gcloud.controller.storage.dao;

import com.gcloud.common.util.StringUtils;
import com.gcloud.controller.storage.entity.Snapshot;
import com.gcloud.controller.storage.model.DescribeSnapshotsParams;
import com.gcloud.controller.utils.SqlUtil;
import com.gcloud.core.currentUser.policy.enums.UserResourceFilterPolicy;
import com.gcloud.core.currentUser.policy.model.FilterPolicyModel;
import com.gcloud.core.currentUser.policy.service.IUserResourceFilterPolicy;
import com.gcloud.core.exception.GCloudException;
import com.gcloud.core.service.SpringUtil;
import com.gcloud.framework.db.PageResult;
import com.gcloud.framework.db.dao.impl.JdbcBaseDaoImpl;
import com.gcloud.header.api.model.CurrentUser;
import com.gcloud.header.storage.StorageErrorCodes;

import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by yaowj on 2018/9/21.
 */
@Repository
public class SnapshotDao extends JdbcBaseDaoImpl<Snapshot, String> {

    public Snapshot checkAndGet(String id) {
        Snapshot snapshot = this.getById(id);
        if (snapshot == null) {
            throw new GCloudException(StorageErrorCodes.SNAPSHOT_NOT_FOUND);
        }
        return snapshot;
    }

    public int updateStatus(String id, String status) {

        String sql = "update gc_snapshots set status = ? where id = ?";
        Object[] values = {status, id};

        return this.jdbcTemplate.update(sql, values);
    }

    public <E> PageResult<E> describeSnapshots(DescribeSnapshotsParams params, Class<E> clazz, CurrentUser currentUser) {
        IUserResourceFilterPolicy filterPolicy = (IUserResourceFilterPolicy)SpringUtil.getBean(UserResourceFilterPolicy.TYPICAL.getFilterPolicyClazz());
        FilterPolicyModel sqlModel = filterPolicy.filterPolicy(currentUser, "t.");

        StringBuffer sql = new StringBuffer();
        List<Object> values = new ArrayList<>();

        sql.append("select * from gc_snapshots t");
        sql.append(" where 1 = 1");

        if (params != null) {

            if (StringUtils.isNotBlank(params.getDiskId())) {
                sql.append(" and t.volume_id = ? ");
                values.add(params.getDiskId());
            }

            if (params.getSnapshotIdList() != null && params.getSnapshotIdList().size() > 0) {

                String inPreSql = SqlUtil.inPreStr(params.getSnapshotIdList().size());
                sql.append(" and t.id in (").append(inPreSql).append(")");
                //Arrays.asList使用与数组转List，但是如果是List的话，会给List再加一层List
                //values.addAll(Arrays.asList(params.getSnapshotIdList()));
                values.addAll(params.getSnapshotIdList());
            }
        }

        sql.append(sqlModel.getWhereSql());
        values.addAll(sqlModel.getParams());

        sql.append(" order by t.created_at desc");

        return findBySql(sql.toString(), values, params.getPageNumber(), params.getPageSize(), clazz);
    }

    public boolean syncSnapshot(Snapshot snap) {
        StringBuffer sql = new StringBuffer("update gc_snapshots set ");
        List<Object> values = new ArrayList<>();

        sql.append("display_description = ?, ");
        values.add(snap.getDisplayDescription());
        sql.append("display_name = ?, ");
        values.add(snap.getDisplayName());
        sql.append("status = ?, ");
        values.add(snap.getStatus());
        sql.append("volume_size = ?, ");
        values.add(snap.getVolumeSize());
        sql.append("updated_at = '?' ");
        values.add(snap.getUpdatedAt());
        sql.append("where id = ? and updated_at < '?'");
        values.add(snap.getId());
        values.add(snap.getUpdatedAt());

        return this.jdbcTemplate.update(sql.toString(), values.toArray()) > 0;
    }

    public List<Snapshot> findByVolume(String volumeId) {
        return this.findByProperty("volume_id", volumeId);
    }

}
