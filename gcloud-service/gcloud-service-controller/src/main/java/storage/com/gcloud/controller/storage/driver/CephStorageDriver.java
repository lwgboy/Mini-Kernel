
package com.gcloud.controller.storage.driver;

import com.gcloud.common.util.SystemUtil;
import com.gcloud.controller.log.util.LongTaskUtil;
import com.gcloud.controller.storage.dao.VolumeDao;
import com.gcloud.controller.storage.entity.Snapshot;
import com.gcloud.controller.storage.entity.StoragePool;
import com.gcloud.controller.storage.entity.Volume;
import com.gcloud.controller.storage.model.CreateDiskResponse;
import com.gcloud.controller.storage.service.IVolumeService;
import com.gcloud.core.exception.GCloudException;
import com.gcloud.header.compute.enums.StorageType;
import com.gcloud.header.log.enums.LogType;
import com.gcloud.header.storage.StorageErrorCodes;
import com.gcloud.header.storage.enums.VolumeStatus;
import com.gcloud.header.storage.model.StoragePoolInfo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Component
public class CephStorageDriver implements IStorageDriver {

    @Autowired
    private VolumeDao volumeDao;

    @Autowired
    private IVolumeService volumeService;

    @PostConstruct
    private void init() {

    }

    @Override
    public StorageType storageType() {
        return StorageType.DISTRIBUTED;
    }

    @Override
    public void createStoragePool(String poolId, String poolName, String hostname, String taskId) throws GCloudException {
        String[] cmd = new String[] {"ceph", "--user", "gcloud", "osd", "pool", "create", poolName, "64", "64"};
        this.exec(cmd, StorageErrorCodes.FAILED_TO_CREATE_POOL);
    }

    @Override
    public void deleteStoragePool(String poolName) throws GCloudException {
        String[] cmd = new String[] {"ceph", "--user", "gcloud", "osd", "pool", "rm", poolName, poolName, "--yes-i-really-really-mean-it"};
        this.exec(cmd, StorageErrorCodes.FAILED_TO_DELETE_POOL);
    }

    @Override
    public CreateDiskResponse createVolume(String taskId, StoragePool pool, Volume volume) throws GCloudException {
        String[] cmd;
        if (volume.getImageRef() == null) {
            cmd = new String[] {"rbd", "--user", "gcloud", "create", "--pool", volume.getPoolName(), "--image", volume.getProviderRefId(), "--size", volume.getSize() + "G"};
        }
        else {
            // TODO
            cmd = new String[] {"rbd", "--user", "gcloud", "clone", "--pool", volume.getPoolName(), "--image", volume.getProviderRefId(), "--size", volume.getSize() + "G"};
        }
        this.exec(cmd, StorageErrorCodes.FAILED_TO_CREATE_VOLUME);
        this.volumeDao.updateVolumeStatus(volume.getId(), VolumeStatus.AVAILABLE);

        CreateDiskResponse response = new CreateDiskResponse();
        response.setLogType(LogType.SYNC);
        response.setDiskId(volume.getId());
        return response;
    }

    @Override
    public void deleteVolume(String taskId, StoragePool pool, Volume volume) throws GCloudException {
        String[] cmd = new String[] {"rbd", "--user", "gcloud", "rm", "--no-progress", "--pool", volume.getPoolName(), "--image", volume.getProviderRefId()};
        this.exec(cmd, StorageErrorCodes.FAILED_TO_DELETE_VOLUME);
        this.volumeService.handleDeleteVolumeSuccess(volume.getId());
    }

    @Override
    public void resizeVolume(String taskId, StoragePool pool, Volume volume, int newSize) throws GCloudException {
        String[] cmd = new String[] {"rbd", "--user", "gcloud", "resize", "--no-progress", "--pool", volume.getPoolName(), "--image", volume.getProviderRefId(), "--size",
                newSize + "G"};
        this.exec(cmd, StorageErrorCodes.FAILED_TO_RESIZE_VOLUME);
        this.volumeService.handleResizeVolumeSuccess(volume.getId(), newSize);
    }

    @Override
    public void createSnapshot(StoragePool pool, String volumeRefId, Snapshot snapshot) throws GCloudException {
        String[] cmd = new String[] {"rbd", "--user", "gcloud", "snap", "create", "--pool", pool.getPoolName(), "--image", volumeRefId, "--snap", snapshot.getProviderRefId()};
        this.exec(cmd, StorageErrorCodes.FAILED_TO_CREATE_SNAP);
    }

    @Override
    public void deleteSnapshot(StoragePool pool, String volumeRefId, Snapshot snapshot, String taskId) throws GCloudException {
        String[] cmd = new String[] {"rbd", "--user", "gcloud", "snap", "rm", "--no-progress", "--pool", snapshot.getPoolName(), "--image", volumeRefId, "--snap",
                snapshot.getProviderRefId()};
        this.exec(cmd, StorageErrorCodes.FAILED_TO_DELETE_SNAP);
        
        LongTaskUtil.syncSucc(LogType.SYNC, taskId, snapshot.getId());
    }

    @Override
    public void resetSnapshot(StoragePool pool, String volumeRefId, Snapshot snapshot, Integer size) throws GCloudException {
        String[] cmd = new String[] {"rbd", "--user", "gcloud", "snap", "rollback", "--no-progress", "--pool", snapshot.getPoolName(), "--image", volumeRefId, "--snap",
                snapshot.getProviderRefId()};
        this.exec(cmd, StorageErrorCodes.FAILED_TO_RESET_SNAP);
    }

    private void exec(String[] cmd, String errorCode) throws GCloudException {
        int res;
        try {
            res = SystemUtil.runAndGetCode(cmd);
        }
        catch (Exception e) {
            throw new GCloudException(errorCode);
        }
        if (res != 0) {
            throw new GCloudException(errorCode);
        }
    }

	@Override
	public StoragePoolInfo getStoragePool(StoragePool pool) throws GCloudException {
		/*(ceph-mon)[root@control01 /]# ceph df | grep volumes
	    volumes                 6      27479M      1.71         1541G        7077*/
		//第3列为已用，第5列为总容量
		String[] cmd1 = new String[] {"ceph", "df"};
		String result = SystemUtil.run(cmd1);
		String[] records = result.split("\\n");
		for(String record:records) {
			if(record.contains(pool.getPoolName() + " ")) {
				result = record;
				break;
			}
		}
		
		String[] results =  result.trim().split("\\s+");
		StoragePoolInfo info = new StoragePoolInfo();
		info.setTotalSize(sizeConvertToMB(results[4]));
		info.setUsedSize(sizeConvertToMB(results[2]));
		info.setAvailSize(info.getTotalSize() - info.getUsedSize());
		return info;
	}
	
	private long sizeConvertToMB(String size) {
		long result = 0;//M
		String unit = size.substring(size.length() - 1);
		long sizeL = 0;
		char   c   =   unit.charAt(0);
		if(((c>='a' && c<='z')   ||   (c>='A' && c<='Z'))) {
			sizeL = Long.parseLong(size.substring(0, size.length() - 1));
		} else {
			sizeL = Long.parseLong(size);
			unit = "B";
		}
		
		switch(unit){
			case "T":
				result = sizeL * 1024 * 1024;
				break;
			case "G":
				result = sizeL * 1024;
				break;
			case "M":
				result = sizeL;
				break;
			case "K":
				result = sizeL/1024;
				break;
			case "B":
				result = sizeL/1024/1024;
				break;
			default:
		}
		return result;
	}

}
