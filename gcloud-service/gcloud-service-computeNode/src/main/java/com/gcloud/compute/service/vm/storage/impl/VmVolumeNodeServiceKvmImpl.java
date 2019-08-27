package com.gcloud.compute.service.vm.storage.impl;

import com.gcloud.compute.prop.ComputeNodeProp;
import com.gcloud.compute.service.vm.base.IVmBaseNodeService;
import com.gcloud.compute.service.vm.storage.IVmVolumeNodeService;
import com.gcloud.compute.util.ConfigFileUtil;
import com.gcloud.compute.util.VmNodeUtil;
import com.gcloud.compute.virtual.IVmVirtual;
import com.gcloud.core.condition.ConditionalHypervisor;
import com.gcloud.core.exception.GCloudException;
import com.gcloud.core.simpleflow.Flow;
import com.gcloud.core.simpleflow.SimpleFlowChain;
import com.gcloud.header.compute.enums.DiskProtocol;
import com.gcloud.header.compute.enums.VmState;
import com.gcloud.header.storage.model.VmVolumeDetail;
import com.gcloud.service.common.compute.model.DomainInfo;
import lombok.extern.slf4j.Slf4j;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.File;

@ConditionalHypervisor
@Slf4j
public class VmVolumeNodeServiceKvmImpl implements IVmVolumeNodeService {

	@Autowired
	private IVmVirtual vmVirtual;

	@Autowired
	private IVmBaseNodeService vmBaseNodeService;

	@Autowired
	private ComputeNodeProp nodeProp;

	@Override
	public void configDataDiskFile(String instanceId, VmVolumeDetail vmVolumeDetail) {
		DomainInfo domInfo = VmNodeUtil.checkVm(instanceId);
		if (domInfo == null) {
			throw new GCloudException("1010902::云服务器不存在");
		}

		String libvirtPath = VmNodeUtil.getLibvirtXmlPath(nodeProp.getNodeIp(), instanceId);
		String instanceXmlPath = VmNodeUtil.getInstanceXmlPath(nodeProp.getNodeIp(), instanceId);
		String volumeXmlPath = VmNodeUtil.getDataDiskXmlPath(nodeProp.getNodeIp(), instanceId, vmVolumeDetail.getVolumeId());


        SimpleFlowChain<String, String> chain = new SimpleFlowChain<>("config data disk file");

		chain.then(new Flow<String>() {
            @Override
            public void run(SimpleFlowChain chain, String data) {
                // 修改instance.xml
                ConfigFileUtil.addInstanceXmlVolumeInfoAndSave(instanceXmlPath, vmVolumeDetail);
                chain.next();
            }

            @Override
            public void rollback(SimpleFlowChain chain, String data) {
                ConfigFileUtil.deleteElementForXmlAndSave(instanceId, "/instance/volume", "id", vmVolumeDetail.getVolumeId(), instanceXmlPath);
            }
        }).then(new Flow<String>() {
            @Override
            public void run(SimpleFlowChain chain, String data) {
                Document libvirtDoc = ConfigFileUtil.readXml(libvirtPath);
                // 修改libvirt.xml
                attachDataDiskUpdateLivirtXml(vmVolumeDetail, libvirtPath, libvirtDoc);
                chain.next();
            }

            @Override
            public void rollback(SimpleFlowChain chain, String data) {
                Document libvirtDoc = ConfigFileUtil.readXml(libvirtPath);
                DiskProtocol protocol = DiskProtocol.value(vmVolumeDetail.getProtocol());
                ConfigFileUtil.deleteLibvirtDisk(libvirtDoc, protocol, vmVolumeDetail.getSourcePath());
                ConfigFileUtil.doc2XmlFile(libvirtDoc, libvirtPath);
            }

        }).then(new Flow<String>() {
            @Override
            public void run(SimpleFlowChain chain, String data) {
                // 新增volume.xml
                attachDataDiskCreateVolumeXml(volumeXmlPath, vmVolumeDetail);
                chain.next();
            }

            @Override
            public void rollback(SimpleFlowChain chain, String data) {
                File volumeXmlFile = new File(volumeXmlPath);
                if(volumeXmlFile.exists()){
                    volumeXmlFile.delete();
                }

            }
        }).then(new Flow<String>() {
            @Override
            public void run(SimpleFlowChain chain, String data) {
                if(VmState.RUNNING.value().equals(domInfo.getGcState())){
                    // 挂载volume.xml
                    vmVirtual.attachDevice(instanceId, volumeXmlPath);
                }

                chain.next();
            }

            @Override
            public void rollback(SimpleFlowChain chain, String data) {
                if(VmState.RUNNING.value().equals(domInfo.getGcState())){
                    vmVirtual.detachDevice(instanceId, volumeXmlPath);
                }

            }
        }).then(new Flow() {
            @Override
            public void run(SimpleFlowChain chain, Object data) {
                // 更新demain
                VmNodeUtil.redefineVm(instanceId, libvirtPath);
                chain.next();
            }

            @Override
            public void rollback(SimpleFlowChain chain, Object data) {

            }
        }).start();

        if (chain.getErrorCode() != null) {
            throw new GCloudException(chain.getErrorCode());
        }

	}

	private void attachDataDiskCreateVolumeXml(String volumeXmlPath, VmVolumeDetail vmVolumeDetail) {
		Document volumeDoc = DocumentHelper.createDocument();
		ConfigFileUtil.addDiskDevice(volumeDoc, vmVolumeDetail, true);
		ConfigFileUtil.doc2XmlFile(volumeDoc, volumeXmlPath);
	}

	private void attachDataDiskUpdateLivirtXml(VmVolumeDetail vmVolumeDetail, String libvirtPath, Document libvirtDoc) {
		ConfigFileUtil.addDiskDevice(libvirtDoc, vmVolumeDetail, false);
		ConfigFileUtil.doc2XmlFile(libvirtDoc, libvirtPath);
	}


	@Override
	public void cleanDataDiskFile(String instanceId, VmVolumeDetail dataDiskDetail) {

        DomainInfo domInfo = VmNodeUtil.checkVm(instanceId);
        if (domInfo == null) {
            throw new GCloudException("1011002::云服务器不存在");
        }

        String state = domInfo.getGcState();
        String volumeId = dataDiskDetail.getVolumeId();

        String libvirtPath = VmNodeUtil.getLibvirtXmlPath(nodeProp.getNodeIp(), instanceId);
        String instanceXmlPath = VmNodeUtil.getInstanceXmlPath(nodeProp.getNodeIp(), instanceId);
        String volumeXmlPath = VmNodeUtil.getDataDiskXmlPath(nodeProp.getNodeIp(), instanceId, volumeId);
        File libvirtFile = new File(libvirtPath);

        SimpleFlowChain<String, String> chain = new SimpleFlowChain<>();
        chain.then(new Flow<String>() {
            @Override
            public void run(SimpleFlowChain chain, String data) {
                if (VmState.RUNNING.value().equals(state)) {
                    vmVirtual.detachDevice(instanceId, volumeXmlPath);
                }
                chain.next();
            }

            @Override
            public void rollback(SimpleFlowChain chain, String data) {
                if (VmState.RUNNING.value().equals(state)) {
                    VmNodeUtil.attachTimes(instanceId, volumeXmlPath, 3);
                }
            }
        }).then(new Flow<String>() {
            @Override
            public void run(SimpleFlowChain chain, String data) {
                ConfigFileUtil.deleteElementForXmlAndSave(instanceId, "/instance/volume", "id", String.valueOf(volumeId), instanceXmlPath);
                chain.next();
            }

            @Override
            public void rollback(SimpleFlowChain chain, String data) {
                ConfigFileUtil.addInstanceXmlVolumeInfoAndSave(instanceXmlPath, dataDiskDetail);

            }
        }).then(new Flow<String>() {
            @Override
            public void run(SimpleFlowChain chain, String data) {
                Document libvirtDoc = ConfigFileUtil.readXml(libvirtFile);
                DiskProtocol protocol = DiskProtocol.value(dataDiskDetail.getProtocol());
                ConfigFileUtil.deleteLibvirtDisk(libvirtDoc, protocol, dataDiskDetail.getSourcePath());
                ConfigFileUtil.doc2XmlFile(libvirtDoc, libvirtPath);
                chain.next();
            }

            @Override
            public void rollback(SimpleFlowChain chain, String data) {
                Document libvirtDoc = ConfigFileUtil.readXml(libvirtFile);

                ConfigFileUtil.addDiskDevice(libvirtDoc, dataDiskDetail, false);
                ConfigFileUtil.doc2XmlFile(libvirtDoc, libvirtPath);
            }
        }).then(new Flow<String>() {
            @Override
            public void run(SimpleFlowChain chain, String data) {
                File aFile = new File(volumeXmlPath);
                if (aFile.exists()) {
                    aFile.delete();
                }
                chain.next();
            }

            @Override
            public void rollback(SimpleFlowChain chain, String data) {
                File aFile = new File(volumeXmlPath);
                if (!aFile.exists()) {
                    attachDataDiskCreateVolumeXml(volumeXmlPath, dataDiskDetail);
                }
            }
        }).then(new Flow() {
            @Override
            public void run(SimpleFlowChain chain, Object data) {
                // 更新demain
                VmNodeUtil.redefineVm(instanceId, libvirtPath);
                chain.next();
            }

            @Override
            public void rollback(SimpleFlowChain chain, Object data) {

            }
        }).start();

        if (chain.getErrorCode() != null) {
            throw new GCloudException(chain.getErrorCode());
        }

    }

	public void forceCleanDataDiskFile(String instanceId, String volumeId) {

		String libvirtPath = VmNodeUtil.getLibvirtXmlPath(nodeProp.getNodeIp(), instanceId);
		String instanceXmlPath = VmNodeUtil.getInstanceXmlPath(nodeProp.getNodeIp(), instanceId);
		String volumeXmlPath = VmNodeUtil.getDataDiskXmlPath(nodeProp.getNodeIp(), instanceId, volumeId);

		try{
			ConfigFileUtil.deleteElementForXmlAndSave(instanceId, "/instance/volume", "id", String.valueOf(volumeId), instanceXmlPath);
		}catch (Exception ex){
			log.error("1010803::清除instance.xml失败");
		}

		try{
			Document libvirtDoc = ConfigFileUtil.readXml(libvirtPath);
			ConfigFileUtil.deleteLibvirtDiskBySerial(libvirtDoc, volumeId);
			ConfigFileUtil.doc2XmlFile(libvirtDoc, libvirtPath);
		}catch (Exception ex){
			log.error("1010804::清除libvirt配置失败");
		}

		try{
			File aFile = new File(volumeXmlPath);
			if (aFile.exists()) {
				aFile.delete();
			}
		}catch (Exception ex){
			log.error("1010805::清除挂载文件失败");
		}

	}

}
