package com.gcloud.controller.storage.dao;

import com.gcloud.common.util.ObjectUtil;
import com.gcloud.common.util.StringUtils;
import com.gcloud.controller.storage.entity.Volume;
import com.gcloud.controller.storage.model.DescribeDisksParams;
import com.gcloud.core.currentUser.policy.enums.UserResourceFilterPolicy;
import com.gcloud.core.currentUser.policy.model.FilterPolicyModel;
import com.gcloud.core.currentUser.policy.service.IUserResourceFilterPolicy;
import com.gcloud.core.exception.GCloudException;
import com.gcloud.core.service.SpringUtil;
import com.gcloud.core.util.SqlUtil;
import com.gcloud.framework.db.PageResult;
import com.gcloud.framework.db.dao.impl.JdbcBaseDaoImpl;
import com.gcloud.header.api.model.CurrentUser;
import com.gcloud.header.storage.StorageErrorCodes;
import com.gcloud.header.storage.enums.DiskTypeParam;
import com.gcloud.header.storage.enums.VolumeStatus;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by yaowj on 2018/9/21.
 */
@Repository
@Slf4j
public class VolumeDao extends JdbcBaseDaoImpl<Volume, String> {
    
    public Volume checkAndGet(String id) throws GCloudException {
        Volume volume = this.getById(id);
        if (volume == null) {
            throw new GCloudException(StorageErrorCodes.FAILED_TO_FIND_VOLUME);
        }
        return volume;
    }
 
    public boolean updateVolumeStatus(String id, VolumeStatus status){

        log.debug(String.format("==volume status== in %s volumeId=%s, status=%s", ObjectUtil.getCalledMethod(), id, status.value()));
        String sql = "update gc_volumes set status = ? where id = ?";
        Object[] values = {status.value(), id};

        return this.jdbcTemplate.update(sql, values) > 0;
    }

    public boolean updateVolumeStatus(String id, String status){

        log.debug(String.format("==volume status== in %s volumeId=%s, status=%s", ObjectUtil.getCalledMethod(), id, status));
        String sql = "update gc_volumes set status = ? where id = ?";
        Object[] values = {status, id};

        return this.jdbcTemplate.update(sql, values) > 0;
    }

    public boolean updateVolumeStatusWithProviderId(String id, String status) {
        String sql = "update gc_volumes set status = ? where provider_ref_id = ?";
        Object[] vals = {status, id};

        return this.jdbcTemplate.update(sql, vals) > 0;
    }

    public boolean syncVolume(Volume vol) {
        StringBuffer sql = new StringBuffer();
        List<Object> values = new ArrayList<>();

        sql.append("update gc_volumes set ");
        sql.append("category = ?, "); values.add(vol.getCategory());
        sql.append("description = ?, "); values.add(vol.getDescription());
        sql.append("display_name = ?, "); values.add(vol.getDisplayName());
        sql.append("size = ?, "); values.add(vol.getSize());
        sql.append("status = ?, "); values.add(vol.getStatus());
        sql.append("updated_at = ? "); values.add(vol.getUpdatedAt());
        sql.append("where id = ? and updated_at < '?'");
        values.add(vol.getId());
        values.add(vol.getUpdatedAt());

        return this.jdbcTemplate.update(sql.toString(), values.toArray()) > 0;
    }

    public <E> PageResult<E> describeDisks(DescribeDisksParams params, Class<E> clazz, CurrentUser currentUser){
    	IUserResourceFilterPolicy filterPolicy = (IUserResourceFilterPolicy)SpringUtil.getBean(UserResourceFilterPolicy.TYPICAL.getFilterPolicyClazz());
		FilterPolicyModel sqlModel = filterPolicy.filterPolicy(currentUser, "v.");
		
        StringBuffer sql = new StringBuffer();
        List<Object> values = new ArrayList<>();

        sql.append("select v.*, aa.instance_uuid from");
        sql.append(" gc_volumes v left join");
        sql.append(" (select a.volume_id, group_concat(a.instance_uuid) instance_uuid from gc_volume_attachments a group by a.volume_id) aa");
        sql.append(" on v.id = aa.volume_id where 1 = 1");

        if(params != null){

            if(StringUtils.isNotBlank(params.getDiskName())){
                sql.append(" and v.display_name like concat('%', ?, '%')");
                values.add(params.getDiskName());
            }

            if(StringUtils.isNotBlank(params.getDiskType())){
                DiskTypeParam diskType = DiskTypeParam.getByValue(params.getDiskType());
                if(diskType != null && diskType != DiskTypeParam.ALL){
                    sql.append(" and v.disk_type = ?");
                    values.add(params.getDiskType());
                }
            }

            if(StringUtils.isNotBlank(params.getStatus())){
                sql.append(" and v.status = ?");
                values.add(params.getStatus());
            }


            if(StringUtils.isNotBlank(params.getInstanceId())){
                sql.append(" and v.id in (select va.volume_id from gc_volume_attachments va where va.instance_uuid = ?)");
                values.add(params.getInstanceId());
            }
            
            if(params.getDiskIds() != null && !params.getDiskIds().isEmpty()) {
            	sql.append(" and v.id in(").append(SqlUtil.inPreStr(params.getDiskIds().size())).append(")");
            	values.addAll(params.getDiskIds());
            }

        }
        
        sql.append(sqlModel.getWhereSql());
		values.addAll(sqlModel.getParams());

        sql.append(" order by v.created_at desc");

        return findBySql(sql.toString(), values, params.getPageNumber(), params.getPageSize(), clazz);
    }


    public <E> List<E> instanceVolume(String instanceId, Class<E> clazz){

        List<Object> values = new ArrayList<>();

        StringBuffer sql = new StringBuffer();
        sql.append("select v.*, a.mountpoint from gc_volumes v, gc_volume_attachments a where v.id = a.volume_id");
        sql.append(" and a.instance_uuid = ?");
        values.add(instanceId);

        return findBySql(sql.toString(), values, clazz);
    }
    
    public <E> List<E> categoryStatistics(String beforeDate, Class<E> clazz){
		 StringBuffer sql = new StringBuffer();
		 sql.append("SELECT dc.id,dc.`name`,COUNT(*) as countNum ");
		 sql.append("from gc_volumes gv left join gc_disk_categories dc on gv.category=dc.id ");
		 sql.append("where gv.created_at < '" + beforeDate + "' ");
		 sql.append("GROUP BY dc.id ");
		 
		return findBySql(sql.toString(), clazz);
	}
    
    public <E> List<E> statusStatistics(String beforeDate, Class<E> clazz){
		 StringBuffer sql = new StringBuffer();
		 sql.append("SELECT status,COUNT(*) as countNum ");
		 sql.append("from gc_volumes gv ");
		 sql.append("where gv.created_at < '" + beforeDate + "' ");
		 sql.append("GROUP BY status ");
		 
		return findBySql(sql.toString(), clazz);
	}
}
