package com.gcloud.compute.service.vm.storage;

import com.gcloud.header.storage.model.VmVolumeDetail;

public interface IVmVolumeNodeService {

	void configDataDiskFile(String instanceId, VmVolumeDetail vmVolumeDetail);

	void cleanDataDiskFile(String instanceId, VmVolumeDetail vmVolumeDetail);

	void forceCleanDataDiskFile(String instanceId, String volumeId);

}
