package com.gcloud.controller.storage.service;

import com.gcloud.controller.storage.entity.Volume;
import com.gcloud.controller.storage.model.CreateDiskParams;
import com.gcloud.controller.storage.model.CreateVolumeParams;
import com.gcloud.controller.storage.model.DescribeDisksParams;
import com.gcloud.framework.db.PageResult;
import com.gcloud.header.api.model.CurrentUser;
import com.gcloud.header.compute.enums.Device;
import com.gcloud.header.compute.msg.api.model.DiskInfo;
import com.gcloud.header.storage.model.DiskItemType;
import com.gcloud.header.storage.model.VmVolumeDetail;
import com.gcloud.header.storage.msg.api.volume.ApiDisksStatisticsReplyMsg;

import java.util.List;

/**
 * Created by yaowj on 2018/9/21.
 */
public interface IVolumeService {

	String create(CreateDiskParams params, CurrentUser currentUser);

	String createVolume(CreateVolumeParams params, CurrentUser currentUser);

	void deleteVolume(String id, String taskId);

	PageResult<DiskItemType> describeDisks(DescribeDisksParams params, CurrentUser currentUser);

	void updateVolume(String diskId, String diskName);

	void reserveVolume(String volumeId);

	void unreserveVolume(String volumeId);

	String attachVolume(String volumeId, String instanceUuid, String mountPoint, String hostname, String taskId);

    void beginDetachingVolume(String volumeId);

    void rollDetachingVolume(String volumeId);

	void detachVolume(String volumeId, String attachmentId);

	void resizeVolume(String diskId, Integer newSize, String taskId);

	Volume getVolume(String volumeId);

	List<VmVolumeDetail> getVolumeInfo(String instanceId);

    VmVolumeDetail volumeDetail(String volumeId, String instanceId);

    VmVolumeDetail volumeDetail(String instanceId, Device device);



	void handleDeleteVolumeSuccess(String volumeId);

    void handleDeleteVolumeFailed(String errorCode, String volumeId);

	void handleAttachVolumeSuccess(String volumeId, String attachmentId, String instanceId, String mountPoint, String hostname);

	void handleResizeVolumeSuccess(String volumeId, int newSize);

    void handleResizeVolumeFailed(String errorCode, String volumeId);

	void checkCategory(List<DiskInfo> diskInfos);

	void checkCategory(String category, Integer size);
	
	ApiDisksStatisticsReplyMsg diskStatistics();
}
