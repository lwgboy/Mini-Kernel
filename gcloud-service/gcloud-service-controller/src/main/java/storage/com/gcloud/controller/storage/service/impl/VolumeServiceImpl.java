package com.gcloud.controller.storage.service.impl;

import com.gcloud.common.util.StringUtils;
import com.gcloud.controller.ResourceProviders;
import com.gcloud.controller.compute.utils.VmControllerUtil;
import com.gcloud.controller.image.dao.ImageDao;
import com.gcloud.controller.image.entity.Image;
import com.gcloud.controller.log.util.LongTaskUtil;
import com.gcloud.controller.storage.dao.DiskCategoryDao;
import com.gcloud.controller.storage.dao.SnapshotDao;
import com.gcloud.controller.storage.dao.StoragePoolDao;
import com.gcloud.controller.storage.dao.VolumeAttachmentDao;
import com.gcloud.controller.storage.dao.VolumeDao;
import com.gcloud.controller.storage.entity.DiskCategory;
import com.gcloud.controller.storage.entity.Snapshot;
import com.gcloud.controller.storage.entity.StoragePool;
import com.gcloud.controller.storage.entity.Volume;
import com.gcloud.controller.storage.entity.VolumeAttachment;
import com.gcloud.controller.storage.model.CreateDiskParams;
import com.gcloud.controller.storage.model.CreateDiskResponse;
import com.gcloud.controller.storage.model.CreateVolumeParams;
import com.gcloud.controller.storage.model.DescribeDisksParams;
import com.gcloud.controller.storage.provider.IVolumeProvider;
import com.gcloud.controller.storage.service.IVolumeService;
import com.gcloud.core.cache.container.CacheContainer;
import com.gcloud.core.cache.enums.CacheType;
import com.gcloud.core.exception.GCloudException;
import com.gcloud.core.util.BeanUtil;
import com.gcloud.framework.db.PageResult;
import com.gcloud.header.api.model.CurrentUser;
import com.gcloud.header.compute.enums.Device;
import com.gcloud.header.compute.enums.DiskProtocol;
import com.gcloud.header.compute.enums.StorageDiskProtocol;
import com.gcloud.header.compute.enums.StorageType;
import com.gcloud.header.compute.msg.api.model.DiskInfo;
import com.gcloud.header.enums.ResourceType;
import com.gcloud.header.image.model.ImageStatisticsItem;
import com.gcloud.header.storage.StorageErrorCodes;
import com.gcloud.header.storage.enums.DiskType;
import com.gcloud.header.storage.enums.DiskTypeParam;
import com.gcloud.header.storage.enums.VolumeStatus;
import com.gcloud.header.storage.model.DiskCategoryStatisticsItem;
import com.gcloud.header.storage.model.DiskCategoryStatisticsResponse;
import com.gcloud.header.storage.model.DiskItemType;
import com.gcloud.header.storage.model.DiskStatusStatisticsItem;
import com.gcloud.header.storage.model.DiskStatusStatisticsResponse;
import com.gcloud.header.storage.model.VmVolumeDetail;
import com.gcloud.header.storage.msg.api.volume.ApiDisksStatisticsReplyMsg;
import com.gcloud.service.common.Consts;
import com.gcloud.service.common.compute.uitls.DiskUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * Created by yaowj on 2018/12/4.
 */
@Component
@Transactional(propagation = Propagation.REQUIRED)
@Slf4j
public class VolumeServiceImpl implements IVolumeService {

    @Autowired
    private VolumeDao volumeDao;

    @Autowired
    private StoragePoolDao poolDao;

    @Autowired
    private VolumeAttachmentDao volumeAttachmentDao;

    @Autowired
    private SnapshotDao snapshotDao;

    @Autowired
    private StoragePoolDao storagePoolDao;

    @Autowired
    private DiskCategoryDao diskCategoryDao;

    @Autowired
    private ImageDao imageDao;

    @Override
    public String create(CreateDiskParams cdp, CurrentUser currentUser) {

        CreateVolumeParams params = BeanUtil.copyProperties(cdp, CreateVolumeParams.class);
        params.setDiskType(DiskType.DATA);

        if (StringUtils.isNotBlank(params.getSnapshotId())) {
            Snapshot snapshot = snapshotDao.getById(params.getSnapshotId());
            if (snapshot == null) {
                throw new GCloudException(StorageErrorCodes.SNAPSHOT_NOT_FOUND);
            }
        }

        return createVolume(params, currentUser);
    }

    @Override
    public String createVolume(CreateVolumeParams params, CurrentUser currentUser) {
        params.setPool(this.poolDao.checkAndGet(params.getZoneId(), params.getDiskCategory()));
        if (StringUtils.isNotBlank(params.getImageRef())) {
            Image image = imageDao.getById(params.getImageRef());
            if (image == null) {
                throw new GCloudException(StorageErrorCodes.REF_IMAGE_IS_NOT_FOUND);
            }

            params.setImageProvider(image.getProvider());
            params.setImageProviderRefId(image.getProviderRefId());
        }

        String volumeId = StringUtils.genUuid();
        CreateDiskResponse response = this.getProvider(params.getPool().getProvider()).createVolume(volumeId, params, currentUser);

        LongTaskUtil.syncSucc(response.getLogType(), params.getTaskId(), volumeId);

        return volumeId;
    }

    @Override
    public void deleteVolume(String volumeId, String taskId) {

        Volume volume;
        synchronized (StringUtils.syncStringObject(volumeId)) {
            volume = this.volumeDao.getById(volumeId);
            if (volume == null) {
                return;
            }

            List<Snapshot> snapshots = snapshotDao.findByProperty(Snapshot.VOLUME_ID, volumeId);
            if(snapshots != null && snapshots.size() > 0){
                throw new GCloudException("0060315::请清除快照再删除卷");
            }

            this.checkAvailable(volume);
            this.volumeDao.updateVolumeStatus(volumeId, VolumeStatus.DELETING);
        }
        try {
            StoragePool pool = this.poolDao.getById(volume.getPoolId());
            this.getProvider(volume.getProvider()).deleteVolume(pool, volume, taskId);
        }
        catch (Exception e) {
            this.handleDeleteVolumeFailed(e.getMessage(), volumeId);
        }
    }

    @Override
    public PageResult<DiskItemType> describeDisks(DescribeDisksParams params, CurrentUser currentUser) {

        if (params == null) {
            params = new DescribeDisksParams();
        }

        if (StringUtils.isNotBlank(params.getDiskType())) {
            DiskTypeParam diskType = DiskTypeParam.getByValue(params.getDiskType());
            if (diskType == null) {
                throw new GCloudException(StorageErrorCodes.NO_SUCH_DISK_TYPE);
            }
        }
        return volumeDao.describeDisks(params, DiskItemType.class, currentUser);
    }

    @Override
    public void updateVolume(String volumeId, String name) {
        if (StringUtils.isBlank(name)) {
            return;
        }
        Volume volume = volumeDao.checkAndGet(volumeId);

        Volume updateVol = new Volume();
        List<String> updateField = new ArrayList<>();
        updateVol.setId(volumeId);
        updateField.add(updateVol.updateDisplayName(name));
        volumeDao.update(updateVol, updateField);
        CacheContainer.getInstance().put(CacheType.VOLUME_NAME, volumeId, name);

        this.getProvider(volume.getProvider()).updateVolume(volume.getProviderRefId(), name, null);
    }

    @Override
    public void reserveVolume(String volumeId) {
        Volume volume;
        synchronized (StringUtils.syncStringObject(volumeId)) {
            volume = volumeDao.checkAndGet(volumeId);
            if (volume.getStatus().equals(VolumeStatus.ATTACHING.value())) {
                return;
            }
            this.checkAvailableOrInUse(volume);
            this.volumeDao.updateVolumeStatus(volumeId, VolumeStatus.ATTACHING.value());
        }
        this.getProvider(volume.getProvider()).reserveVolume(volume.getProviderRefId());
    }

    @Override
    public void unreserveVolume(String volumeId) {
        Volume volume;
        synchronized (StringUtils.syncStringObject(volumeId)) {
            volume = volumeDao.checkAndGet(volumeId);
            if (!volume.getStatus().equals(VolumeStatus.ATTACHING.value())) {
                return;
            }
            volumeDao.updateVolumeStatus(volumeId, this.volumeAttachmentDao.isAttach(volumeId) ? VolumeStatus.IN_USE.value() : VolumeStatus.AVAILABLE.value());
        }
        this.getProvider(volume.getProvider()).unreserveVolume(volume.getProviderRefId());
    }

    @Override
    public String attachVolume(String volumeId, String instanceId, String mountPoint, String hostname, String taskId) {
        Volume volume;
        String attachmentId = UUID.randomUUID().toString();
        synchronized (StringUtils.syncStringObject(volumeId)) {
            volume = volumeDao.checkAndGet(volumeId);
//            if (!volume.getStatus().equals(VolumeStatus.ATTACHING.getValue())) {
//                throw new GCloudException(StorageErrorCodes.VOLUME_IS_NOT_ATTACHING);
//            }
            if (this.volumeAttachmentDao.findByVolumeIdAndInstanceId(volumeId, instanceId).isEmpty()) {
                String attachmentRefId = this.getProvider(volume.getProvider()).attachVolume(volume, attachmentId, instanceId, mountPoint, hostname, taskId);
                VolumeAttachment volumeAttachment = new VolumeAttachment();
                volumeAttachment.setId(attachmentId);
                volumeAttachment.setProvider(volume.getProvider());
                volumeAttachment.setProviderRefId(attachmentRefId);
                volumeAttachment.setInstanceUuid(instanceId);
                volumeAttachment.setMountpoint(mountPoint);
                volumeAttachment.setVolumeId(volumeId);
                volumeAttachment.setAttachedHost(hostname);
                volumeAttachmentDao.save(volumeAttachment);
            }
            this.volumeDao.updateVolumeStatus(volumeId, VolumeStatus.IN_USE.value());
        }

        return attachmentId;
    }

    @Override
    public void beginDetachingVolume(String volumeId) {
        Volume volume;
        synchronized (StringUtils.syncStringObject(volumeId)) {
            volume = volumeDao.checkAndGet(volumeId);
            this.checkInUse(volume);
            volumeDao.updateVolumeStatus(volumeId, VolumeStatus.DETACHING.value());
        }
        this.getProvider(volume.getProvider()).beginDetachingVolume(volume);
    }

    @Override
    public void rollDetachingVolume(String volumeId) {
        Volume volume;
        synchronized (StringUtils.syncStringObject(volumeId)) {
            volume = volumeDao.checkAndGet(volumeId);
            if (!volume.getStatus().equals(VolumeStatus.DETACHING.value())) {
                return;
            }
            this.volumeDao.updateVolumeStatus(volumeId, this.volumeAttachmentDao.isAttach(volumeId) ? VolumeStatus.IN_USE.value() : VolumeStatus.AVAILABLE.value());
        }
        this.getProvider(volume.getProvider()).rollDetachingVolume(volume);
    }

    @Override
    public void detachVolume(String volumeId, String attachmentId) {
        Volume volume;
        synchronized (StringUtils.syncStringObject(volumeId)) {
            volume = volumeDao.checkAndGet(volumeId);
            if (!volume.getStatus().equals(VolumeStatus.DETACHING.value())) {
                throw new GCloudException(StorageErrorCodes.VOLUME_IS_NOT_DETACHING);
            }
            VolumeAttachment attachment = this.volumeAttachmentDao.getById(attachmentId);
            if (attachment != null) {
                this.getProvider(volume.getProvider()).detachVolume(volume, attachment);
                this.volumeAttachmentDao.deleteById(attachmentId);
            }
            this.volumeDao.updateVolumeStatus(volumeId, this.volumeAttachmentDao.isAttach(volumeId) ? VolumeStatus.IN_USE.value() : VolumeStatus.AVAILABLE.value());
        }
    }

    @Override
    public void resizeVolume(String volumeId, Integer newSize, String taskId) {
        Volume volume;
        synchronized (StringUtils.syncStringObject(volumeId)) {
            volume = this.volumeDao.getById(volumeId);
            if (volume == null) {
                throw new GCloudException(StorageErrorCodes.FAILED_TO_FIND_VOLUME);
            }
            if (volume.getSize() >= newSize) {
                throw new GCloudException(StorageErrorCodes.NEW_SIZE_CANNOT_BE_SMALLER);
            }
            this.checkAvailable(volume);
            if (this.volumeAttachmentDao.isAttach(volumeId)) {
                throw new GCloudException(StorageErrorCodes.VOLUME_IS_ATTACHED);
            }
            this.volumeDao.updateVolumeStatus(volumeId, VolumeStatus.RESIZING);
        }
        try {
            StoragePool pool = this.poolDao.getById(volume.getPoolId());
            this.getProvider(volume.getProvider()).resizeVolume(pool, volume, newSize, taskId);
        }
        catch (Exception e) {
            this.handleResizeVolumeFailed(e.getMessage(), volumeId);
        }
    }

    @Override
    public Volume getVolume(String volumeId) {
        return volumeDao.findUniqueByProperty("id", volumeId);
    }

    @Override
    public List<VmVolumeDetail> getVolumeInfo(String instanceId) {

        return null;
    }

    @Override
    public VmVolumeDetail volumeDetail(String volumeId, String instanceId) {

        Volume volume = volumeDao.getById(volumeId);
        if (volume == null) {
            return null;
        }

        return volumeDetail(volume, null, instanceId);

    }

    @Override
    public VmVolumeDetail volumeDetail(String instanceId, Device device) {

        Map<String, Object> values = new HashMap<>();
        values.put(VolumeAttachment.INSTANCE_UUID, instanceId);
        values.put(VolumeAttachment.MOUNTPOINT, device.getMountPath());
        VolumeAttachment va = volumeAttachmentDao.findUniqueByProperties(values);
        if (va == null) {
            return null;
        }

        Volume volume = volumeDao.getById(va.getVolumeId());

        return volumeDetail(volume, va, null);
    }

    private VmVolumeDetail volumeDetail(Volume volume, VolumeAttachment volumeAttachment, String instanceId) {

        VmVolumeDetail detail = new VmVolumeDetail();

        if (volumeAttachment == null && StringUtils.isNotBlank(instanceId)) {
            List<VolumeAttachment> volumeAttachments = volumeAttachmentDao.findByVolumeIdAndInstanceId(volume.getId(), instanceId);
            volumeAttachment = volumeAttachments != null && volumeAttachments.size() > 0 ? volumeAttachments.get(0) : null;
        }

        if (volumeAttachment != null) {
            String mountPoint = volumeAttachment.getMountpoint();
            String targetDev = "";
            if (StringUtils.isNotBlank(mountPoint) && mountPoint.lastIndexOf("/") != -1 && !mountPoint.endsWith("/")) {
                targetDev = mountPoint.substring(mountPoint.lastIndexOf("/") + 1);
            }
            detail.setTargetDev(targetDev);

        }
        else if (StringUtils.isNotBlank(instanceId)) {
            String targetDev = VmControllerUtil.getVolumeMountPoint(instanceId);
            detail.setTargetDev(targetDev);
        }

        StoragePool pool = this.storagePoolDao.checkAndGet(volume.getZoneId(), volume.getCategory());

        StorageType st = StorageType.value(volume.getStorageType());

        DiskProtocol diskProtocol = DiskProtocol.value(pool.getConnectProtocol());
        if (diskProtocol == null) {
            StorageDiskProtocol storageDiskProtocol = StorageDiskProtocol.getByProviderAndStorageType(volume.getProvider(), pool.getDriver());
            if (storageDiskProtocol == null) {
                throw new GCloudException("0060314::找不到磁盘协议");
            }
            diskProtocol = storageDiskProtocol.getDiskProtocol();
        }

        detail.setBusType(Consts.DEFAULT_DISK_DRIVER);
        detail.setDiskType(volume.getDiskType());
        detail.setVolumeSize(volume.getSize());
        detail.setVolumeId(volume.getId());
        detail.setVolumeRefId(volume.getProviderRefId());
        detail.setProvider(volume.getProvider());
        detail.setCategory(volume.getCategory());
        detail.setProtocol(diskProtocol.value());

        detail.setStorageType(st.getValue());
        detail.setSourcePath(DiskUtil.volumeSourcePath(volume.getProviderRefId(), pool.getPoolName(), diskProtocol));

        return detail;

    }

    private void checkAvailable(Volume volume) throws GCloudException {
        if (!StringUtils.equals(volume.getStatus(), VolumeStatus.AVAILABLE.value())) {
            throw new GCloudException(StorageErrorCodes.VOLUME_IS_NOT_AVAILABLE);
        }
    }

    private void checkAvailableOrInUse(Volume volume) throws GCloudException {
        if (!StringUtils.equals(volume.getStatus(), VolumeStatus.AVAILABLE.value()) && !StringUtils.equals(volume.getStatus(), VolumeStatus.IN_USE.value())) {
            throw new GCloudException(StorageErrorCodes.VOLUME_IS_NOT_AVAILABLE);
        }
    }

    private void checkInUse(Volume volume) throws GCloudException {
        if (!StringUtils.equals(volume.getStatus(), VolumeStatus.IN_USE.value())) {
            log.debug(String.format("==volume status==volume check in user, volumeId=%s, status=%s", volume.getId(), volume.getStatus()));
            throw new GCloudException(StorageErrorCodes.VOLUME_IS_NOT_IN_USE);
        }
    }

    @Override
    public void handleDeleteVolumeSuccess(String volumeId) {
        this.volumeDao.deleteById(volumeId);
        for (Snapshot snapshot : this.snapshotDao.findByVolume(volumeId)) {
            this.snapshotDao.deleteById(snapshot.getId());
        }
        log.info("删除存储卷成功：{}", volumeId);
    }

    @Override
    public void handleDeleteVolumeFailed(String errorCode, String volumeId) {
        this.volumeDao.updateVolumeStatus(volumeId, VolumeStatus.AVAILABLE);
        log.info("删除存储卷失败：{} -> {}", volumeId, errorCode);
    }

    @Override
    public void handleAttachVolumeSuccess(String volumeId, String attachmentId, String instanceId, String mountPoint, String hostname) {
        Volume volume = volumeDao.getById(volumeId);
        volumeDao.updateVolumeStatus(volumeId, VolumeStatus.IN_USE.value());
        VolumeAttachment volumeAttachment = new VolumeAttachment();
        volumeAttachment.setId(UUID.randomUUID().toString());
        volumeAttachment.setProvider(volume.getProvider());
        volumeAttachment.setProviderRefId(attachmentId);
        volumeAttachment.setInstanceUuid(instanceId);
        volumeAttachment.setMountpoint(mountPoint);
        volumeAttachment.setVolumeId(volumeId);
        volumeAttachment.setAttachedHost(hostname);
        volumeAttachmentDao.save(volumeAttachment);
    }

    @Override
    public void handleResizeVolumeSuccess(String volumeId, int newSize) {
        Volume updateVol = new Volume();
        List<String> updateField = new ArrayList<>();
        updateVol.setId(volumeId);
        updateField.add(updateVol.updateStatus(VolumeStatus.AVAILABLE.value()));
        updateField.add(updateVol.updateSize(newSize));
        this.volumeDao.update(updateVol, updateField);
        log.info("resize存储卷成功：{}", volumeId);
    }

    @Override
    public void handleResizeVolumeFailed(String errorCode, String volumeId) {
        this.volumeDao.updateVolumeStatus(volumeId, VolumeStatus.AVAILABLE);
        log.info("resize存储卷失败：{} -> {}", volumeId, errorCode);
    }

    private IVolumeProvider getProvider(int providerType) {
        IVolumeProvider provider = ResourceProviders.checkAndGet(ResourceType.VOLUME, providerType);
        return provider;
    }

    public void checkCategory(List<DiskInfo> diskInfos) {
        if (diskInfos != null && diskInfos.size() > 0) {
            Map<String, DiskCategory> categoryMap = new HashMap<>();
            for (DiskInfo diskInfo : diskInfos) {
                checkCategory(diskInfo.getCategory(), diskInfo.getSize(), categoryMap);
            }
        }

    }

    public void checkCategory(String category, Integer size) {
        checkCategory(category, size, null);
    }

    private void checkCategory(String category, Integer size, Map<String, DiskCategory> categoryMap) {

        DiskCategory diskCategory = categoryMap == null ? null : categoryMap.get(category);
        if (diskCategory == null) {
            diskCategory = diskCategoryDao.getById(category);
            if (diskCategory == null) {
                throw new GCloudException("0060107::找不到磁盘类型");
            }
            categoryMap.put(category, diskCategory);
        }

        if ((diskCategory.getMaxSize() != null && size > diskCategory.getMaxSize()) || (diskCategory.getMinSize() != null && size < diskCategory.getMinSize())) {
            throw new GCloudException("0060108::磁盘大小不在磁盘类型允许范围内");
        }

    }

	@Override
	public ApiDisksStatisticsReplyMsg diskStatistics() {
		//为保持磁盘总数一致，统计当前时间之前的disk
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String beforeDate = sdf.format(new Date());
		ApiDisksStatisticsReplyMsg reply = new ApiDisksStatisticsReplyMsg();
		
		List<DiskCategoryStatisticsItem> categoryStatistics = volumeDao.categoryStatistics(beforeDate, DiskCategoryStatisticsItem.class);
		List<DiskStatusStatisticsItem> statusStatistics = volumeDao.statusStatistics(beforeDate, DiskStatusStatisticsItem.class);
		reply.setAllNum(statusStatistics.stream().map(DiskStatusStatisticsItem::getCountNum).reduce(0, Integer::sum));
		
		DiskCategoryStatisticsResponse categoryResponse  = new DiskCategoryStatisticsResponse();
		categoryResponse.setItem(categoryStatistics);
		DiskStatusStatisticsResponse statusResponse  = new DiskStatusStatisticsResponse();
		statusResponse.setItem(statusStatistics);
		
		reply.setCategoryStatisticsItems(categoryResponse);
		reply.setStatusStatisticsItems(statusResponse);
		return reply;
	}

}
