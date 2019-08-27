package com.gcloud.compute.service.vm.senior.impl;

import com.gcloud.common.util.StringUtils;
import com.gcloud.compute.prop.ComputeNodeProp;
import com.gcloud.compute.service.vm.senior.IVmSeniorNodeService;
import com.gcloud.compute.util.VmNodeUtil;
import com.gcloud.core.condition.ConditionalHypervisor;
import com.gcloud.core.exception.GCloudException;
import com.gcloud.header.compute.enums.DiskProtocol;
import com.gcloud.header.compute.enums.FileFormat;
import com.gcloud.header.compute.enums.VmState;
import com.gcloud.header.storage.model.VmVolumeDetail;
import com.gcloud.service.common.compute.model.DomainInfo;
import com.gcloud.service.common.compute.model.QemuInfo;
import com.gcloud.service.common.compute.uitls.DiskQemuImgUtil;
import com.gcloud.service.common.compute.uitls.DiskUtil;
import com.gcloud.service.common.compute.uitls.VmUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.File;

/**
 * Created by yaowj on 2018/11/30.
 */
@ConditionalHypervisor
@Slf4j
public class VmSeniorNodeServiceKvmImpl implements IVmSeniorNodeService {

    @Autowired
    private ComputeNodeProp prop;

    private final FileFormat DEFAULT_FORMAT = FileFormat.QCOW2;

    @Override
    public void convertToImage(String instanceId, FileFormat targetFormat, VmVolumeDetail volumeDetail) {

        DomainInfo domInfo = VmNodeUtil.checkVm(instanceId);
        if (domInfo == null) {
            throw new GCloudException("1011202::云服务器不存在");
        }

        //不是关机, 则抛错
        if(!VmState.STOPPED.value().equals(domInfo.getGcState())){
            throw new GCloudException("1011205::云服务器需要为关机状态");
        }

        String targetPath = VmUtil.getBundleTargetPath(VmNodeUtil.getInstanceConfigPath(prop.getNodeIp(), instanceId));

        String systemDiskPath = volumeDetail.getSourcePath();
        String systemDiskType = volumeDetail.getFileFormat();
        String systemDiskProtocol = volumeDetail.getProtocol();

        if(StringUtils.isBlank(systemDiskPath)){
            throw new GCloudException("1011203::找不到系统盘");
        }

        String convertSource = DiskUtil.getDiskPath(systemDiskPath, DiskProtocol.value(systemDiskProtocol));

        if(StringUtils.isBlank(systemDiskType)){
            QemuInfo qemuInfo = DiskQemuImgUtil.info(convertSource);
            systemDiskType = qemuInfo.getFormat();
        }

        FileFormat targetImageFormat = targetFormat == null ? DEFAULT_FORMAT : targetFormat;

        boolean isCompress = targetImageFormat.equals(FileFormat.QCOW2);

        try{
            DiskQemuImgUtil.convert(convertSource, targetPath, systemDiskType, targetImageFormat.getValue(), isCompress);
        }catch (Exception ex){
            File file = new File(targetPath);
            if(file.exists()){
                file.delete();
            }
            throw ex;
        }


    }

    @Override
    public void deleteBundle(String instanceId, String nodeIp) {

        nodeIp = StringUtils.isBlank(nodeIp) ? prop.getNodeIp() : nodeIp;


        String targetPath = VmUtil.getBundleTargetPath(VmNodeUtil.getInstanceConfigPath(nodeIp, instanceId));

        File file = new File(targetPath);
        if(file.exists()){
            file.delete();
        }
    }
}
