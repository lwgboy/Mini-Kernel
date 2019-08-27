package com.gcloud.controller.compute.service.vm.storage.impl;

import com.gcloud.controller.compute.dao.InstanceDao;
import com.gcloud.controller.compute.entity.VmInstance;
import com.gcloud.controller.compute.model.node.Node;
import com.gcloud.controller.compute.service.vm.storage.IVmDiskService;
import com.gcloud.controller.compute.utils.RedisNodesUtil;
import com.gcloud.controller.storage.dao.VolumeAttachmentDao;
import com.gcloud.controller.storage.dao.VolumeDao;
import com.gcloud.controller.storage.entity.Volume;
import com.gcloud.controller.storage.entity.VolumeAttachment;
import com.gcloud.controller.storage.service.IVolumeService;
import com.gcloud.core.exception.GCloudException;
import com.gcloud.header.compute.enums.VmTaskState;
import com.gcloud.header.storage.enums.DiskType;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Slf4j
@Transactional
public class VmDiskServiceImpl implements IVmDiskService {

	@Autowired
	private InstanceDao instanceDao;

	@Autowired
	private VolumeDao volumeDao;

	@Autowired
	private VolumeAttachmentDao volumeAttachmentDao;

	@Autowired
    private IVolumeService volumeService;

	public void attachDataDiskInit(String instanceId, String volumeId, boolean inTask) {
		VmInstance vm = instanceDao.getById(instanceId);
		if (vm == null) {
			throw new GCloudException("0010903::云服务器不存在");
		}

		Volume volume = volumeDao.getById(volumeId);
		if (volume == null) {
			throw new GCloudException("0010904::磁盘不存在");
		}

		//暂时不允许多挂载
		if(!volume.getStatus().equals(org.openstack4j.model.storage.block.Volume.Status.AVAILABLE.value())){
			throw new GCloudException("0010905::磁盘当前状态不能挂载");
		}

		Node node = RedisNodesUtil.getComputeNodeByHostName(vm.getHostname());
		if (node == null) {
			throw new GCloudException("0010906::找不到云服务器");
		}

		if (!inTask) {
			if (!instanceDao.updateInstanceTaskState(instanceId, VmTaskState.ATTACH_DISK)) {
				throw new GCloudException("0010907::云服务器当前无法挂载磁盘");
			}
		}

		//保留volume，用于挂载
        volumeService.reserveVolume(volumeId);

	}

	@Override
	public void detachDataDiskInit(String instanceId, String volumeId, boolean inTask) {

		VmInstance vm = instanceDao.getById(instanceId);
		if (vm == null) {
			throw new GCloudException("0011003::云服务器不存在");
		}

		Volume volume = volumeDao.getById(volumeId);
		if (volume == null) {
			throw new GCloudException("0011004::磁盘不存在");
		}

//		if(DiskType.SYSTEM.getValue().equals(volume.getDiskType())){
//			throw new GCloudException("0011008::系统盘不能卸载");
//		}

		List<VolumeAttachment> volumeAttachments = volumeAttachmentDao.findByVolumeIdAndInstanceId(volumeId, instanceId);
		if (volumeAttachments == null || volumeAttachments.size() == 0) {
			throw new GCloudException("0011005::云服务器没有挂载此磁盘");
		}

		Node node = RedisNodesUtil.getComputeNodeByHostName(vm.getHostname());
		if (node == null) {
			throw new GCloudException("0011006::找不到云服务器");
		}

		if (!inTask) {
			if (!instanceDao.updateInstanceTaskState(instanceId, VmTaskState.DETACH_DISK)) {
				throw new GCloudException("0011007::云服务器当前状态不能卸载云盘");
			}
		}

		volumeService.beginDetachingVolume(volumeId);
	}

	@Override
	public void detachDataDiskApiCheck(String volumeId) {
		Volume volume = volumeDao.getById(volumeId);
		if (volume == null) {
			throw new GCloudException("0011009::磁盘不存在");
		}

		if(DiskType.SYSTEM.getValue().equals(volume.getDiskType())){
			throw new GCloudException("0011008::系统盘不能卸载");
		}
	}
}
