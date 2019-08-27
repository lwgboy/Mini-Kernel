package com.gcloud.controller.provider;

import com.gcloud.common.util.StringUtils;
import com.gcloud.controller.storage.entity.StoragePool;
import com.gcloud.core.exception.GCloudException;
import com.gcloud.header.storage.model.StoragePoolInfo;

import lombok.extern.slf4j.Slf4j;
import org.openstack4j.api.Builders;
import org.openstack4j.api.OSClient;
import org.openstack4j.model.common.ActionResponse;
import org.openstack4j.model.common.Identifier;
import org.openstack4j.model.storage.block.ConnectionInfo;
import org.openstack4j.model.storage.block.Connector;
import org.openstack4j.model.storage.block.Volume;
import org.openstack4j.model.storage.block.ext.Service;
import org.openstack4j.model.storage.block.VolumeSnapshot;
import org.openstack4j.model.storage.block.VolumeType;
import org.openstack4j.model.storage.block.builder.ConnectorBuilder;
import org.openstack4j.model.storage.block.builder.VolumeTypeBuilder;
import org.openstack4j.openstack.OSFactory;
import org.openstack4j.openstack.storage.block.domain.CinderConnectionInfo;
import org.openstack4j.openstack.storage.block.domain.CinderVolumeType.VolumeTypes;
import org.openstack4j.openstack.storage.block.domain.VolumeBackendPool;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
@Slf4j
public class CinderProviderProxy extends OpenstackProviderProxy {
	@Autowired
	// CinderProvider provider;
	public void setProvider(CinderProvider provider) {
		super.setProvider(provider);
	}

	public static void main(String[] args) {
		OSClient.OSClientV3 client = OSFactory.builderV3().endpoint("http://192.168.215.31:35357/v3").credentials("admin", "zMHsTAhu5Mzpnm28Rt31qnwG9rVLul1PhSUnHk49", Identifier.byName("Default")).scopeToProject(Identifier.byName("admin"), Identifier.byName("Default")).authenticate();

		/*List<Volume> vols = (List<Volume>)client.blockStorage().volumes().list();
		System.out.println(vols.toString());*/
		List<VolumeBackendPool> backends = (List<VolumeBackendPool>)client.blockStorage().schedulerStatsPools().poolsDetail();
		
		List<VolumeType> types = (List<VolumeType>)client.blockStorage().volumes().listVolumeTypes();
		System.out.println(backends.size());
		/*Volume volume = client.blockStorage().volumes().get("8b016a00-6362-4b2a-b873-4d9d26cc7213");
		if(null == volume) {
			return;
		}
		ActionResponse response = client.blockStorage().volumes().attach(volume.getId(), "80d61903-9247-451c-ae0f-ce6b0eed5dff",
				"volumes/volume-8b016a00-6362-4b2a-b873-4d9d26cc7213", "control01");
		ActionResponse response = client.blockStorage().volumes().attach(volume.getId(), "80d61903-9247-451c-ae0f-ce6b0eed5dff",
				"/dev/vda", "control01");
		if (!response.isSuccess()) {
			log.error(String.format("挂载到vm失败,message=%s,errorCode=%s,code=%s,volumeId=%s", response.getFault(), response.getFault(), response.getCode(), volume.getId()));
		}*/

		/*ConnectorBuilder vol = Builders.connector().ip("192.168.198.200").platform("x86_64").host("control01").doLocalAttach(false).multipath(false);
		CinderConnectionInfo response = client.blockStorage().volumes().initializeConnection("027b0bc5-d425-49da-962d-745fc70e19d1", vol.build());
		if (response == null) {
			System.out.println("error");
			// log.error(String.format("挂载到vm失败,message=%s,errorCode=%s,code=%s,volumeId=%s",
			// response.getFault(), response.getFault(), response.getCode(),
			// "db0c2f62-f146-4052-b10b-7761ca2fe484"));
		}
		System.out.println(response.getDriverVolumeType());*/

	}

	public List<Volume> getVolumeList(Map<String, String> filterParams) {
		List<Volume> volList;

		try {
			if (filterParams == null) {
				volList = (List<Volume>)getClient().blockStorage().volumes().list();
			}
			else {
				volList = (List<Volume>)getClient().blockStorage().volumes().list(filterParams);
			}
		} catch (Exception ex) {
			throw new GCloudException("::获取volume列表失败:" + ex.getMessage());
		}

		return volList;
	}

	public List<VolumeSnapshot> getSnapshotList(Map<String, String> filterParams) {
		List<VolumeSnapshot> snapList;
		try {
			if (filterParams == null)
				snapList = (List<VolumeSnapshot>)getClient().blockStorage().snapshots().list();
			else
				snapList = (List<VolumeSnapshot>)getClient().blockStorage().snapshots().list(filterParams);
		} catch (Exception ex) {
			throw new GCloudException("::获取卷快照列表失败:" + ex.getMessage());
		}

		return snapList;
	}

	public Volume getVolume(String id) {
		Volume volume = null;
		try {
			volume = getClient().blockStorage().volumes().get(id);
		} catch (Exception ex) {
			throw new GCloudException("::获取volume失败");
		}
		return volume;
	}

	/**
	 * @param size
	 * @throws GCloudException
	 * @Title: checkPoolAvailSizeIsEnough
	 * @Description: 检测存储空间是否充足
	 * @date 2015-8-31 下午7:44:54
	 */
	public void checkPoolAvailSizeIsEnough(String poolName, int size) {
		log.debug("checkPoolAvailSizeIsEnough poolName = " + poolName);
		List<VolumeBackendPool> backends = (List<VolumeBackendPool>) getClient().blockStorage().schedulerStatsPools().poolsDetail();
		for (VolumeBackendPool backend : backends) {
			if (backend.getName().equals(poolName)) {
				if (backend.getCapabilities().getFreeCapacityGb() < size) {
					log.error("存储池空间不足");
					throw new GCloudException("::存储空间不足");
				}
				break;
			}
		}
	}

	public void checkPoolAvailSizeIsEnough(int size) {
		List<VolumeBackendPool> backends = (List<VolumeBackendPool>) getClient().blockStorage().schedulerStatsPools().poolsDetail();
		for (VolumeBackendPool backend : backends) {
			if (backend.getCapabilities().getFreeCapacityGb() > size) {
				return;
			}
		}
		log.error("存储池空间不足");
		throw new GCloudException("::存储空间不足");
	}

	/**
	 * @param volumeId
	 * @param newSize
	 * @return
	 * @throws GCloudException
	 * @Title: extend
	 * @Description: 卷扩容
	 * @date 2015-6-3 上午11:15:07
	 */
	public void resizeVolume(String volumeId, Integer newSize) {

		log.debug("extend volume volumeId = " + volumeId + "newSize = " + newSize);
		ActionResponse response = null;
		try {
			response = getClient().blockStorage().volumes().extend(volumeId, newSize);
		} catch (Exception ex) {
			log.error("块设备扩容失败" + ex, ex);
			throw new GCloudException("::块设备扩容失败");
		}

		if (response == null) {
			log.error(String.format("块设备扩容失败,response为空,volumeId=%s,size=%s", volumeId, newSize));
			throw new GCloudException("::块设备扩容失败");
		}

		if (!response.isSuccess()) {
			log.error(String.format("块设备扩容失败,message=%s,code=%s,volumeId=%s,size=%s", response.getFault(), response.getCode(), volumeId, newSize));
			throw new GCloudException("::块设备扩容失败");
		}

	}

	public void attachVolume(String volumeId, String instanceUuid, String mountpoint, String hostName) {

		log.debug("attachToVm volumeId = " + volumeId + " instanceUuid = " + instanceUuid);
		ActionResponse response = null;
		try {
			response = getClient().blockStorage().volumes().attach(volumeId, instanceUuid, mountpoint, hostName);
		} catch (Exception ex) {
			log.error("挂载到vm失败" + ex, ex);
			throw new GCloudException("::挂载到vm失败");
		}

		if (response == null) {
			log.error(String.format("挂载到vm失败,response为空,volumeId=%s", volumeId));
			throw new GCloudException("::挂载到vm失败");
		}

		if (!response.isSuccess()) {
			log.error(String.format("挂载到vm失败,message=%s,code=%s,volumeId=%s", response.getFault(), response.getCode(), volumeId));
			throw new GCloudException("::挂载到vm失败");
		}

	}

	public Volume createVolume(Volume vol) {
		Volume volume = null;

		try {
			volume = getClient().blockStorage().volumes().create(vol);
		} catch (Exception ex) {
			log.error("::创建块设备失败" + ex, ex);
			throw new GCloudException("::创建块设备失败");
		}

		if (volume == null) {
			log.error("创建块设备失败，volume 为 null");
			throw new GCloudException("::创建块设备失败");
		}

		return volume;
	}

	public void deleteVolume(String volumeId) {

		log.debug("deleteVolume volumeId = " + volumeId);
		ActionResponse response = null;
		try {
			response = getClient().blockStorage().volumes().delete(volumeId);
		} catch (Exception ex) {
			log.error("删除volume失败" + ex, ex);
			throw new GCloudException("::删除volume失败");
		}

		if (response == null) {
			log.error(String.format("删除volume失败,response为空,volumeId=%s", volumeId));
			throw new GCloudException("::删除volume失败");
		}

		if (!response.isSuccess()) {
			log.error(String.format("删除volume失败,message=%s,code=%s,volumeId=%s", response.getFault(), response.getCode(), volumeId));
			throw new GCloudException("::删除volume失败");
		}

	}

	public void updateVolume(String volumeId, String name, String description) {

		log.debug("update volumeId = " + volumeId + " name = " + name);

		ActionResponse response = null;
		try {
			response = getClient().blockStorage().volumes().update(volumeId, name, description);
		} catch (Exception ex) {
			log.error("更新volume失败" + ex, ex);
			throw new GCloudException("::更新volume失败");
		}

		if (response == null) {
			log.error(String.format("更新volume失败,response为空,volumeId=%s", volumeId));
			throw new GCloudException("::更新volume失败");
		}

		if (!response.isSuccess()) {
			log.error(String.format("更新volume失败,message=%s,code=%s,volumeId=%s", response.getFault(), response.getCode(), volumeId));
			throw new GCloudException("::更新volume失败");
		}

	}

	public VolumeSnapshot createSnapshot(VolumeSnapshot createSnapshot) {

		VolumeSnapshot snapshot = null;

		try {
			snapshot = getClient().blockStorage().snapshots().create(createSnapshot);
		} catch (Exception ex) {
			log.error("::创建快照失败" + ex, ex);
			throw new GCloudException("::创建快照失败");
		}

		if (snapshot == null) {
			log.error("创建快照失败，snapshot 为 null");
			throw new GCloudException("::创建快照失败");
		}

		return snapshot;

	}

	public void updateSnapshot(String snapshotId, String name, String description) {

		log.debug("update snapshotId = " + snapshotId + " name = " + name);

		ActionResponse response = null;
		try {
			response = getClient().blockStorage().snapshots().update(snapshotId, name, description);
		} catch (Exception ex) {
			log.error("更新快照失败" + ex, ex);
			throw new GCloudException("::更新快照失败");
		}

		if (response == null) {
			log.error(String.format("更新快照失败,response为空,snapshotId=%s", snapshotId));
			throw new GCloudException("::更新快照失败");
		}

		if (!response.isSuccess()) {
			log.error(String.format("更新快照失败,message=%s,code=%s,snapshotId=%s", response.getFault(), response.getCode(), snapshotId));
			throw new GCloudException("::更新快照失败");
		}
	}

	public void deleteSnapshot(String snapshotId) {
		log.debug("delete snapshot snapshotId = " + snapshotId);
		ActionResponse response = null;
		try {
			response = getClient().blockStorage().snapshots().delete(snapshotId);
		} catch (Exception ex) {
			log.error("删除快照失败" + ex, ex);
			throw new GCloudException("::删除快照失败");
		}

		if (response == null) {
			log.error(String.format("删除快照失败,response为空,snapshotId=%s", snapshotId));
			throw new GCloudException("::删除快照失败");
		}

		if (!response.isSuccess()) {
			log.error(String.format("删除快照失败,message=%s,code=%s,snapshotId=%s", response.getFault(), response.getCode(), snapshotId));
			throw new GCloudException("::删除快照失败");
		}
	}

	public void resetVolumeSnapshot(String volumeId, String snapshotId) {

		log.debug("revert volumeId snapshot, volumeId=%s, snapshotId=%s", volumeId, snapshotId);
		ActionResponse response = null;
		try {
			response = getClient().blockStorage().volumes().revert(volumeId, snapshotId);
		} catch (Exception ex) {
			log.error("恢复快照失败" + ex, ex);
			throw new GCloudException("::恢复快照失败");
		}

		if (response == null) {
			log.error(String.format("恢复快照失败,response为空,volumeId=%s, snapshotId=%s", volumeId, snapshotId));
			throw new GCloudException("::恢复快照失败");
		}

		if (!response.isSuccess()) {
			log.error(String.format("恢复快照失败,message=%s,code=%s,volumeId=%s, snapshotId=%s", response.getFault(), response.getCode(), volumeId, snapshotId));
			throw new GCloudException("::恢复快照失败");
		}

	}

	public VolumeSnapshot getVolumeSnapshot(String id) {
		VolumeSnapshot snapshot = null;
		try {
			snapshot = getClient().blockStorage().snapshots().get(id);
		} catch (Exception ex) {
			throw new GCloudException("::获取快照失败");
		}
		return snapshot;
	}

	public ConnectionInfo initializeConnection(String volumeId, Connector connector) {

		log.debug("initializeConnection volumeId = " + volumeId);
		ConnectionInfo response = null;
		try {
			response = getClient().blockStorage().volumes().initializeConnection(volumeId, connector);
		} catch (Exception ex) {
			log.error("initialize connection error" + ex, ex);
			throw new GCloudException("::initialize connection error");
		}

		if (response == null) {
			log.error(String.format("initialize connection null, volumeId=%s", volumeId));
			throw new GCloudException("::initialize connection null");
		}
		
		return response;
	}


    public void reserveVolume(String volumeId){

        log.debug("revert volumeId， volumeId=%s", volumeId);
        ActionResponse response = null;
        try {
            response = getClient().blockStorage().volumes().reserve(volumeId);
        } catch (Exception ex) {
            log.error("保留块设备失败" + ex, ex);
            throw new GCloudException("::保留块设备失败");
        }

        if (response == null) {
            log.error(String.format("保留块设备失败,response为空,volumeId=%s", volumeId));
            throw new GCloudException("::保留块设备失败");
        }

        if (!response.isSuccess()) {
            log.error(String.format("保留块设备失败,message=%s,code=%s,volumeId=%s", response.getFault(), response.getCode(), volumeId));
            throw new GCloudException("::保留块设备失败");
        }

    }


    public void unreserveVolume(String volumeId){

        log.debug("unreserve volumeId， volumeId=%s", volumeId);
        ActionResponse response = null;
        try {
            response = getClient().blockStorage().volumes().unreserve(volumeId);
        } catch (Exception ex) {
            log.error("取消保留块设备失败" + ex, ex);
            throw new GCloudException("::取消保留块设备失败");
        }

        if (response == null) {
            log.error(String.format("取消保留块设备失败,response为空,volumeId=%s", volumeId));
            throw new GCloudException("::取消保留块设备失败");
        }

        if (!response.isSuccess()) {
            log.error(String.format("取消保留块设备失败,message=%s,code=%s,volumeId=%s", response.getFault(), response.getCode(), volumeId));
            throw new GCloudException("::取消保留块设备失败");
        }

    }

    public void beginDetachingVolume(String volumeId){

        log.debug("beginDetaching volumeId， volumeId=%s", volumeId);
        ActionResponse response = null;
        try {
            response = getClient().blockStorage().volumes().beginDetaching(volumeId);
        } catch (Exception ex) {
            log.error("开始卸载块设备失败" + ex, ex);
            throw new GCloudException("::开始卸载块设备失败");
        }

        if (response == null) {
            log.error(String.format("开始卸载块设备失败,response为空,volumeId=%s", volumeId));
            throw new GCloudException("::开始卸载块设备失败");
        }

        if (!response.isSuccess()) {
            log.error(String.format("开始卸载块设备失败,message=%s,code=%s,volumeId=%s", response.getFault(), response.getCode(), volumeId));
            throw new GCloudException("::开始卸载块设备失败");
        }

    }

    public void detachVolume(String volumeId, String attachmentId){

        log.debug("detach volumeId， volumeId=%s, attachmentId=%s", volumeId, attachmentId);
        ActionResponse response = null;
        try {
            response = getClient().blockStorage().volumes().detach(volumeId, attachmentId);
        } catch (Exception ex) {
            log.error("卸载块设备失败" + ex, ex);
            throw new GCloudException("::卸载块设备失败");
        }

        if (response == null) {
            log.error(String.format("卸载块设备失败,response为空,volumeId=%s, attachmentId=%s", volumeId, attachmentId));
            throw new GCloudException("::卸载块设备失败");
        }

        if (!response.isSuccess()) {
            log.error(String.format("卸载块设备失败,message=%s,code=%s,volumeId=%s, attachmentId=%s", response.getFault(), response.getCode(), volumeId, attachmentId));
            throw new GCloudException("::卸载块设备失败");
        }

    }

    public void rollDetachingVolume(String volumeId){

        log.debug("rollDetaching volumeId， volumeId=%s", volumeId);
        ActionResponse response = null;
        try {
            response = getClient().blockStorage().volumes().rollDetaching(volumeId);
        } catch (Exception ex) {
            log.error("回滚正则挂载块设备失败" + ex, ex);
            throw new GCloudException("::回滚正则挂载块设备失败");
        }

        if (response == null) {
            log.error(String.format("回滚正则挂载块设备失败,response为空,volumeId=%s", volumeId));
            throw new GCloudException("::回滚正则挂载块设备失败");
        }

        if (!response.isSuccess()) {
            log.error(String.format("回滚正则挂载块设备失败,message=%s,code=%s,volumeId=%s", response.getFault(), response.getCode(), volumeId));
            throw new GCloudException("::回滚正则挂载块设备失败");
        }

    }

    public String createVolumeType(String volumeTypeName, String backendName) {
        VolumeType response = null;
        try {
            Map<String, String> extraSpecs = new HashMap<>();
            extraSpecs.put("volume_backend_name", backendName);
            VolumeType volumeType = Builders.volumeType().name(volumeTypeName).extraSpecs(extraSpecs).build();
            response = getClient().blockStorage().volumes().createVolumeType(volumeType);
        }
        catch (Exception ex) {
            log.error("创建VolumeType失败：" + ex, ex);
            throw new GCloudException("::创建VolumeType失败");
        }
        if (response == null) {
            log.error("创建VolumeType失败,response为空");
            throw new GCloudException("::创建VolumeType失败");
        }
        return response.getId();
    }

    public void deleteVolumeType(String volumeTypeId) {
        try {
            getClient().blockStorage().volumes().deleteVolumeType(volumeTypeId);
        }
        catch (Exception ex) {
            log.error("删除VolumeType失败：" + ex, ex);
            throw new GCloudException("::删除VolumeType失败");
        }
    }
    
    public StoragePoolInfo getPoolInfo(StoragePool pool) {
		log.debug("getPoolInfo poolName = " + pool.getPoolName());
		VolumeBackendPool result = null;
		List<VolumeType> volumeTypes = (List<VolumeType>) getClient().blockStorage().volumes().listVolumeTypes();
		Map<String, Map<String, String>> volumeTypeMap = volumeTypes.stream().collect(Collectors.toMap(VolumeType::getId, VolumeType::getExtraSpecs));
		String backendName = "";
		if(volumeTypeMap.get(pool.getProviderRefId()) != null) {
			backendName = volumeTypeMap.get(pool.getProviderRefId()).get("volume_backend_name");
		}
		List<VolumeBackendPool> backends = (List<VolumeBackendPool>) getClient().blockStorage().schedulerStatsPools().poolsDetail();
		for (VolumeBackendPool backend : backends) {
			if(StringUtils.isNotBlank(backendName)) {
				if(backend.getCapabilities().getVolumeBackendName().equals(backendName) 
						&& (StringUtils.isBlank(pool.getHostname()) || backend.getName().substring(0, backend.getName().indexOf("@")).equals(pool.getHostname()))) {
					result = backend;
					break;
				}
			} else if (StringUtils.isNotBlank(backend.getCapabilities().getPoolname())) {
				if(backend.getCapabilities().getPoolname().equals(pool.getPoolName())
						&& (StringUtils.isBlank(pool.getHostname()) || backend.getName().substring(0, backend.getName().indexOf("@")).equals(pool.getHostname()))) {
					result = backend;
					break;
				}
			} else {
				//根据 locationinfo	"ceph:/etc/ceph/ceph.conf:cc5fbead-de56-4146-9e29-0a7b7866b462:cinder:volumes" 中的poolname比较？
			}
		}
		StoragePoolInfo info = new StoragePoolInfo();
		if(result!=null) {
			info.setTotalSize(result.getCapabilities().getTotalCapacityGb() * 1024);
			info.setAvailSize(result.getCapabilities().getFreeCapacityGb() * 1024);
			info.setUsedSize(info.getTotalSize() - info.getAvailSize());
		}
		return info;
	}

}
