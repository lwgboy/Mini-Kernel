package com.gcloud.compute.service.vm.senior;

import com.gcloud.header.compute.enums.FileFormat;
import com.gcloud.header.storage.model.VmVolumeDetail;

/**
 * Created by yaowj on 2018/11/30.
 */
public interface IVmSeniorNodeService {

    void convertToImage(String instanceId, FileFormat targetFormat, VmVolumeDetail vmVolumeDetail);

    void deleteBundle(String instanceId, String nodeIp);
}
