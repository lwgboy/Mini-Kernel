package com.gcloud.controller.image.dao;

import com.gcloud.common.util.StringUtils;
import com.gcloud.controller.image.entity.Image;
import com.gcloud.controller.image.entity.enums.ImagePropertyItem;
import com.gcloud.controller.image.model.DescribeImageParams;
import com.gcloud.framework.db.PageResult;
import com.gcloud.framework.db.dao.impl.JdbcBaseDaoImpl;
import com.gcloud.header.api.model.CurrentUser;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
public class ImageDao extends JdbcBaseDaoImpl<Image, String>{

    public <E> PageResult<E> describeDisks(DescribeImageParams params, Class<E> clazz, CurrentUser currentUser){
    	/*IUserResourceFilterPolicy filterPolicy = (IUserResourceFilterPolicy)SpringUtil.getBean(UserResourceFilterPolicy.TYPICAL.getFilterPolicyClazz());
		FilterPolicyModel sqlModel = filterPolicy.filterPolicy(currentUser, "i.");*/
		
        StringBuffer sql = new StringBuffer();
        List<Object> values = new ArrayList<>();
        sql.append("select i.*, i.owner_type as image_owner_alias, data.* from gc_images i left join");
        sql.append(" (select p.image_id, ");

        sql.append(" max(case when p.name = '").append(ImagePropertyItem.ARCHITECTURE.value()).append("' then p.value end) as ").append(ImagePropertyItem.ARCHITECTURE.value()).append(", ");
        sql.append(" max(case when p.name = '").append(ImagePropertyItem.DESCRIPTION.value()).append("' then p.value end) as ").append(ImagePropertyItem.DESCRIPTION.value()).append(", ");
        sql.append(" max(case when p.name = '").append(ImagePropertyItem.OS_TYPE.value()).append("' then p.value end) as ").append(ImagePropertyItem.OS_TYPE.value());

        sql.append(" from gc_image_properties p group by p.image_id) data");
        sql.append(" on i.id = data.image_id");
        sql.append(" where 1 = 1");

        if(StringUtils.isNotBlank(params.getImageId())){
            sql.append(" and i.id = ?");
            values.add(params.getImageId());
        }

        if(StringUtils.isNotBlank(params.getImageName())){
            sql.append(" and i.name like concat('%', ?, '%')");
            values.add(params.getImageName());
        }

        if(StringUtils.isNotBlank(params.getStatus())){
            sql.append(" and i.status = ?");
            values.add(params.getStatus());
        }
        
        if(params.getDisable() != null){
            sql.append(" and i.disable = ?");
            values.add(params.getDisable());
        }
        
        /*sql.append(sqlModel.getWhereSql());
		values.addAll(sqlModel.getParams());*/

        sql.append(" order by i.created_at desc");

        return findBySql(sql.toString(), values, params.getPageNumber(), params.getPageSize(), clazz);

    }
    
    public <E> List<E> imageStatistics(Class<E> clazz){
    	String sql = "SELECT ip.`value` as osType,COUNT(*) as countNum from gc_images i LEFT JOIN gc_image_properties ip on i.id=ip.image_id and ip.`name`='os_type' GROUP BY ip.`value`";
    	return findBySql(sql, clazz);
    }
}
