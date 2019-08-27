package com.gcloud.controller.storage.dao;

import com.alibaba.fastjson.JSONObject;
import com.gcloud.controller.storage.entity.VolumeAttachment;
import com.gcloud.framework.db.dao.impl.JdbcBaseDaoImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
@Slf4j
public class VolumeAttachmentDao extends JdbcBaseDaoImpl<VolumeAttachment, String> {

	public List<VolumeAttachment> findByVolumeIdAndInstanceId(String volumeId, String instanceId) {
		String sql = "select * from gc_volume_attachments where volume_id = ? and instance_uuid = ?";

		List<Object> valueList = new ArrayList<Object>();
		valueList.add(volumeId);
		valueList.add(instanceId);
		return this.findBySql(sql, valueList);
	}

	public boolean isAttach(String volumeId){

		String sql = "select * from gc_volume_attachments where volume_id = ? limit 1";
		List<Object> values = new ArrayList<>();
		values.add(volumeId);

		List<VolumeAttachment> attachments = findBySql(sql, values);
		if(attachments != null && attachments.size() > 0){
			log.debug(String.format("==volume status== is volume attach, volumeId=%s, attachment=%s", volumeId, JSONObject.toJSONString(attachments)));
		}
		return attachments != null && attachments.size() > 0;

	}

	public List<VolumeAttachment> getInstanceRefAttach(String instanceId){

		StringBuffer sql = new StringBuffer();
		sql.append("select * from gc_volume_attachments t where t.volume_id in ");
		sql.append(" (select a.volume_id from gc_volume_attachments a where a.instance_uuid = ?)");

		List<Object> values = new ArrayList<>();
		values.add(instanceId);

		return this.findBySql(sql.toString(), values);
	}

}
