package com.gcloud.controller.compute.utils;

import com.gcloud.common.crypto.Crypto;
import com.gcloud.common.util.StringUtils;
import com.gcloud.controller.compute.ControllerComputeProp;
import com.gcloud.controller.compute.model.vm.VmImageInfo;
import com.gcloud.controller.network.dao.OvsBridgeDao;
import com.gcloud.controller.network.dao.PortDao;
import com.gcloud.controller.network.entity.OvsBridge;
import com.gcloud.controller.network.entity.Port;
import com.gcloud.controller.storage.dao.VolumeAttachmentDao;
import com.gcloud.controller.storage.entity.VolumeAttachment;
import com.gcloud.core.exception.GCloudException;
import com.gcloud.core.service.SpringUtil;
import com.gcloud.framework.db.enums.OrderType;
import com.gcloud.header.compute.enums.CreateType;
import com.gcloud.header.compute.enums.ImageStoreType;
import com.gcloud.header.storage.enums.StoragePoolDriver;
import com.gcloud.service.common.Consts;

import java.text.MessageFormat;
import java.util.List;

public class VmControllerUtil {

	public static VmImageInfo getVmImageInfo(StoragePoolDriver storageDriver, StoragePoolDriver imageDriver, String vmImagePoolId, String imageId, String isoId, CreateType createType) throws GCloudException {
		ControllerComputeProp controllerComputeProp = (ControllerComputeProp)SpringUtil.getBean("controllerComputeProp");
		VmImageInfo imageInfo = null;
		String imagePath = "";

		// imagePath都飞分开拼装，防止格式不一致
		if (StoragePoolDriver.RBD == imageDriver) {

			imageInfo = new VmImageInfo();
			imageInfo.setImageStorageType(ImageStoreType.RBD.getValue());
			imageInfo.setImagePoolId("images");//后续改为从配置文件获取

			if (createType == CreateType.ISO) {
				imagePath = String.format(controllerComputeProp.getImagePathFormat(), "iso", imageInfo.getImagePoolId(), isoId);
			} else {
				imagePath = String.format(controllerComputeProp.getCephImagePathFormat(), imageInfo.getImagePoolId(), imageId);
			}
			
			imageInfo.setImagePath(imagePath);
		} else if (StoragePoolDriver.LVM == imageDriver) {
			//后续实现
		} else if (StoragePoolDriver.FILE == imageDriver) {

			imageInfo = new VmImageInfo();
			imageInfo.setImageStorageType(ImageStoreType.LOCAL.getValue());
			imageInfo.setImagePoolId(controllerComputeProp.getDefaultFileImagePath());

			if (controllerComputeProp.isUsedImageCache()) {
				if (createType == CreateType.ISO) {
					imagePath = String.format(controllerComputeProp.getImageCachePath(), "iso", isoId);
				} else {
					imagePath = String.format(controllerComputeProp.getImageCachePath(), "image", imageId);
				}
			} else if (createType == CreateType.ISO) {
				imagePath = String.format(controllerComputeProp.getFileImagePathFormat(), imageInfo.getImagePoolId(), isoId);
			} else {
				imagePath = String.format(controllerComputeProp.getFileImagePathFormat(), imageInfo.getImagePoolId(), imageId);
			}
			imageInfo.setImagePath(imagePath);

		}

		if (imageInfo == null) {
			throw new GCloudException("compute_controller_base_010012::fail to get image info");
		}
        imageInfo.setImageId(imageId);

		return imageInfo;

	}
	
	/**
	 * 
	 * @Title: getDataDiskPath
	 * @Description: 获取数据盘路径
	 * @date 2015-5-18 下午8:11:44
	 * 
	 * @param owenId
	 * @param instanceId
	 * @return
	 */
	public static String getDataDiskPath(String owenId, String instanceId) {
		ControllerComputeProp controllerComputeProp = SpringUtil.getBean(ControllerComputeProp.class);
		return String.format("%s/%s/%s/%s", controllerComputeProp.getInstanceDiskPath(), owenId, instanceId,
				getDataDiskFileName(instanceId));
	}

	/**
	 * 
	 * @Title: getDataDiskFileName
	 * @Description: 获取数据盘名字
	 * @date 2015-5-18 下午8:12:00
	 * 
	 * @param instanceId
	 * @return
	 */
	public static String getDataDiskFileName(String instanceId) {
		return String.format("%s_second", instanceId);
	}

	/**
	 * 
	 * @Title: getDataDiskVolumeFileName
	 * @Description: 获取用作数据盘的云盘的名称
	 * @date 2015-5-18 下午8:12:00
	 * 
	 * @param instanceId
	 * @return
	 */
	public static String getDataDiskVolumeFileName(String instanceId) {
		return String.format("%s_volume", instanceId);
	}

	/**
	 * @Title: getInstanceDiskPath
	 * @Description: 获取云服务器磁盘路径
	 * @date 2015-5-22 上午9:03:42
	 *
	 * @param instanceId
	 * @return
	 */
	public static String getInstanceDiskPath(String instanceId) {
		ControllerComputeProp controllerComputeProp = SpringUtil.getBean(ControllerComputeProp.class);
		return controllerComputeProp.getInstanceDiskPath() + "/" + instanceId;
	}

	public static String getNodesAliveKey(String hostName) {
		return MessageFormat.format(Consts.RedisKey.GCLOUD_CONTROLLER_COMPUTE_NODES_COMPUTE_NODE_ALIVE, hostName);
	}

	public static String getNodesHostNameLock(String hostName) {
		return MessageFormat.format(Consts.RedisKey.GCLOUD_CONTROLLER_COMPUTE_NODES_LOCK_HOSTBANE, hostName);
	}

	public static String getNodesHostNameKey(String hostName) {
		return MessageFormat.format(Consts.RedisKey.GCLOUD_CONTROLLER_COMPUTE_NODES_COMPUTE_NODE_HOSTNAME, hostName);
	}

	public static String getHostNameByHostKey(String hostKey) {

		// 如果Consts.RedisKey.GCLOUD_COMPUTE_CONTROLLER_NODES_COMPUTE_NODE_ALIVE结构修改，需要跟着修改
		int idx = Consts.RedisKey.GCLOUD_CONTROLLER_COMPUTE_NODES_COMPUTE_NODE_HOSTNAME.indexOf("{0}");
		String hostname = hostKey.substring(idx);
		return hostname;

	}

    /**
     * @Title: getHostNameByAliveKey
     * @Description: 根据key来截取hostname，就不用在去获取一次redis
     * @date 2015-6-13 下午3:54:55
     * @param aliveKey
     * @return hostname
     */
    public static String getHostNameByAliveKey(String aliveKey) {
        // 如果Consts.RedisKey.GCLOUD_COMPUTE_CONTROLLER_NODES_COMPUTE_NODE_ALIVE结构修改，需要跟着修改
        int idx = Consts.RedisKey.GCLOUD_CONTROLLER_COMPUTE_NODES_COMPUTE_NODE_ALIVE.indexOf("{0}");
        String hostname = aliveKey.substring(idx);
        return hostname;

    }

    public static String getVmStateSyncKey(String hostname){
        return MessageFormat.format(Consts.RedisKey.GCLOUD_CONTROLLER_COMPUTE_SYNC_INSTANCE_STATE, hostname);

    }
    
	
	/**
	 * @Title: generateNetcardSufId
	 * @Description: 获取用来创建网络的后缀（例如网桥为br+后缀），一张网卡一个后缀（网卡uuid过长，所以取一个较短的唯一的后缀）
	 * @date 2015-6-13 下午3:54:55
	 *
	 * @return
	 */
	public static String generateNetcardSufId() {

		PortDao portDao = SpringUtil.getBean(PortDao.class);

		String sufId = "";

		Port port = null;

		do {
			// suf-xxxxxxxx
			sufId = Crypto.generateResourceIdUpper("suf");
			// 去掉suf-
			sufId = sufId.substring(4);
			port = portDao.findUniqueByProperty(Port.SUF_ID, sufId);

		} while (port != null);

		return sufId;
	}

	public static String generateOvsBridge(){
		OvsBridgeDao ovsBridgeDao = SpringUtil.getBean(OvsBridgeDao.class);

		String brName = "";

		OvsBridge ovsBr = null;

		do {
			// ovsbr-xxxxxxxx
			brName = Crypto.generateResourceIdUpper("ovsbr");
			ovsBr = ovsBridgeDao.findUniqueByProperty(OvsBridge.BRIDGE, brName);

		} while (ovsBr != null);

		return brName;
	}

	public static String getVolumeMountPoint(String instanceId){

        VolumeAttachmentDao attachmentDao = SpringUtil.getBean(VolumeAttachmentDao.class);
        List<VolumeAttachment> attachments = attachmentDao.findByProperty(VolumeAttachment.INSTANCE_UUID, instanceId, VolumeAttachment.MOUNTPOINT, OrderType.ASC.getValue());

        //从vdc 开始， vda 系统盘， hdb cdrom
        String devicePix = "vd";
        char initSuf = 'c';
        char deviceSuf = initSuf;

        if(attachments == null || attachments.size() == 0){
            return devicePix + String.valueOf(initSuf);
        }

        for(VolumeAttachment va : attachments){
            //最后一个字母
            if(StringUtils.isBlank(va.getMountpoint())){
                continue;
            }

            char vaSuf = va.getMountpoint().charAt(va.getMountpoint().length() - 1);
            if(vaSuf < initSuf){
                continue;
            }

            //device 已经存在
            if(vaSuf == deviceSuf){
                deviceSuf++;
            }else{
                //不存在，则返回
                break;
            }
        }

        return devicePix + String.valueOf(deviceSuf);
	}

}
