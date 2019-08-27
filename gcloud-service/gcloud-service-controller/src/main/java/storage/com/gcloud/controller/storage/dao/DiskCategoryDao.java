
package com.gcloud.controller.storage.dao;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Repository;

import com.gcloud.common.util.StringUtils;
import com.gcloud.controller.storage.entity.DiskCategory;
import com.gcloud.controller.storage.model.DescribeDiskCategoriesParams;
import com.gcloud.controller.storage.model.DetailDiskCategoryParams;
import com.gcloud.core.currentUser.policy.enums.UserResourceFilterPolicy;
import com.gcloud.core.currentUser.policy.model.FilterPolicyModel;
import com.gcloud.core.currentUser.policy.service.IUserResourceFilterPolicy;
import com.gcloud.core.service.SpringUtil;
import com.gcloud.framework.db.PageResult;
import com.gcloud.framework.db.dao.impl.JdbcBaseDaoImpl;
import com.gcloud.header.api.model.CurrentUser;

@Repository
public class DiskCategoryDao extends JdbcBaseDaoImpl<DiskCategory, String> {
	public <E> List<E> detailDiskCategory(DetailDiskCategoryParams params, Class<E> clazz) {
		StringBuffer sql = new StringBuffer();
		List<Object> values = new ArrayList<>();
		
		sql.append("select c.*, zone.id as zone_id, zone.name as zone_name, "
				+ "pool.id as pool_id, pool.display_name as pool_name "
				+ "from gc_disk_categories as c "
				+ "left join gc_disk_category_pools as cp on c.id = cp.disk_category_id "
				+ "left join gc_zones as zone on cp.zone_id = zone.id "
				+ "left join gc_storage_pools as pool on cp.storage_pool_id = pool.id "
				+ "where c.id = ?");
		values.add(params.getId());
		
		return findBySql(sql.toString(), values, clazz);
	}
	
    public <E> List<E> diskCategoryList(String zoneId, Class<E> clazz){

        StringBuffer sql = new StringBuffer();
        //TODO 有可能会重复
        sql.append("select * from gc_disk_categories c where c.id in");
        sql.append(" (select p.disk_category_id p from gc_disk_category_pools p where p.zone_id = ?)");

        List<Object> values = new ArrayList<>();
        values.add(zoneId);

        return findBySql(sql.toString(), values, clazz);
    }
    
    public <E> PageResult<E> describeDiskCategories(DescribeDiskCategoriesParams params, Class<E> clazz) {
    	StringBuffer sql = new StringBuffer();
    	List<Object> values = new ArrayList<>();
    	
    	sql.append("select c.*, zone.id as zone_id, zone.name as zone_name, "
				+ "pool.id as pool_id, pool.display_name as pool_name "
				+ "from gc_disk_categories as c "
				+ "left join gc_disk_category_pools as cp on c.id = cp.disk_category_id "
				+ "left join gc_zones as zone on cp.zone_id = zone.id "
				+ "left join gc_storage_pools as pool on cp.storage_pool_id = pool.id "
				+ "where 1 = 1 ");
    	
    	if(StringUtils.isNotBlank(params.getZoneId())) {
    		sql.append(" and zone.id = ?");
    		values.add(params.getZoneId());
    	}
    	
    	return findBySql(sql.toString(), values, params.getPageNumber(), params.getPageSize(), clazz);
    }

}
