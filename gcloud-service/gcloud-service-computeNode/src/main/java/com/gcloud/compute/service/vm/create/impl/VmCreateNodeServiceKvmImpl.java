package com.gcloud.compute.service.vm.create.impl;

import com.gcloud.common.util.StringUtils;
import com.gcloud.compute.cache.cache.VmInstancesCache;
import com.gcloud.compute.prop.ComputeNodeProp;
import com.gcloud.compute.service.vm.create.IVmCreateNodeService;
import com.gcloud.compute.util.ConfigFileUtil;
import com.gcloud.compute.util.VmNodeUtil;
import com.gcloud.compute.virtual.IVmVirtual;
import com.gcloud.core.condition.ConditionalHypervisor;
import com.gcloud.core.exception.GCloudException;
import com.gcloud.core.service.SpringUtil;
import com.gcloud.header.compute.enums.AgentType;
import com.gcloud.header.compute.enums.BusType;
import com.gcloud.header.compute.enums.CloudPlatform;
import com.gcloud.header.compute.enums.Device;
import com.gcloud.header.compute.enums.DiskProtocol;
import com.gcloud.header.compute.enums.FileFormat;
import com.gcloud.header.compute.enums.FtState;
import com.gcloud.header.compute.enums.GrapPasswdType;
import com.gcloud.header.compute.enums.RemoteType;
import com.gcloud.header.compute.enums.StorageType;
import com.gcloud.header.compute.msg.node.vm.model.VmDetail;
import com.gcloud.header.enums.ProviderType;
import com.gcloud.header.storage.enums.DiskType;
import com.gcloud.header.storage.enums.StoragePoolDriver;
import com.gcloud.header.storage.model.VmVolumeDetail;
import com.gcloud.service.common.compute.model.XmlElement;
import com.gcloud.service.common.compute.uitls.VmUtil;
import lombok.extern.slf4j.Slf4j;
import org.dom4j.Document;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/*
 * @Date Oct 31, 2018
 * 
 * @Author zhangzj
 * 
 * @Copyright 2018 www.g-cloud.com.cn Inc. All rights reserved.
 * 
 * @Description TODO
 */
@ConditionalHypervisor
@Slf4j
public class VmCreateNodeServiceKvmImpl implements IVmCreateNodeService {

	@Autowired
	private ComputeNodeProp computeNodeProp;

	@Autowired
	private IVmVirtual vmVirtual;

	@Override
	public void checkCreateVmNodeEnv(String userId, String instanceId) {
		String cfgPath = VmNodeUtil.getInstanceConfigPath(computeNodeProp.getNodeIp(), instanceId);
		try {
			// 还要检测文件系统是否已经挂载
			// TODO EnvironmentUtils.checkCephFileSystem();

			// 检查云服务配置文件目录是否存在，不存在则创建
			VmUtil.checkPath(cfgPath);

			// 创建虚拟机文件锁
			ConfigFileUtil.createLockFile(cfgPath);
		} catch (Exception ex) {
			// 删除配置文件
			VmNodeUtil.deleteDirectoryNoException(cfgPath);

			String errorCode = "1010101::create fail";
			if (ex instanceof GCloudException) {
				errorCode = ex.getMessage();
			}

			log.error(errorCode, ex);
			throw new GCloudException(errorCode);
		}

	}


	@Override
	public void connectVolume() {
		// TODO Auto-generated method stub

	}

	@Override
	public void createDomain(String instanceId, String userId) {
		String nodeIp = computeNodeProp.getNodeIp();
		String cfgPath = VmNodeUtil.getInstanceConfigPath(nodeIp, instanceId);

		try {

			log.debug(String.format("虚拟机%s define开始", instanceId));
			// define
//			vmVirtual.define(libXmlPath);
			log.debug(String.format("虚拟机%s define结束", instanceId));

			log.debug(String.format("虚拟机%s 开机开始", instanceId));
			VmNodeUtil.startTimes(instanceId, 5);
			log.debug(String.format("虚拟机%s 开机结束", instanceId));

			ConfigFileUtil.forceDeleteLockFile(cfgPath);

		} catch (Exception ex) {

			String errorCode = "1010102::创建实例错误";
			if (ex instanceof GCloudException) {
				errorCode = ex.getMessage();
			}

			log.error(errorCode, ex);
			throw new GCloudException(errorCode);
		}

	}

	@Override
	public void buildVmConfig(VmDetail vmIns, String userId) {
		String nodeIp = computeNodeProp.getNodeIp();
		String cfgPath = VmNodeUtil.getInstanceConfigPath(nodeIp, vmIns.getId());

		try {
			// 配置libvirt.xml instance.xml
			log.debug(String.format("虚拟机%s 创建libvirt文件开始", vmIns.getId()));
			buildLibvirtXml(vmIns, nodeIp);
			log.debug(String.format("虚拟机%s 创建libvirt文件结束", vmIns.getId()));

			log.debug(String.format("虚拟机%s 创建instance文件开始", vmIns.getId()));
			buildInstanceXml(vmIns, nodeIp);
			log.debug(String.format("虚拟机%s 创建instance文件结束", vmIns.getId()));

//			// 配置网络信息
//			if (vmIns.getVmNetwork() != null && vmIns.getVmNetwork().size() > 0) {
//				VmNetworkDetail vmNetwork = vmIns.getVmNetwork().get(0);
//				log.debug(String.format("虚拟机%s 配置网络开始", vmIns.getId()));
//				// VmNetcardAbstract netAbs = (VmNetcardAbstract)
//				// SpringUtil.getBean("vmNetcardKvmImpl");
//				//
//				// // 配置文件，如果失败，自带回滚
//				// netAbs.changeConfigFile(vmIns.getInstanceId(), vmIns.getId(),
//				// userId, vmNetwork.getPortId(), vmNetwork.getBrName(),
//				// vmNetwork.getPreName(), vmNetwork.getAftName(), vmNetwork);
//
//				log.debug(String.format("虚拟机%s 配置网络结束", vmIns.getId()));
//			}
			String libXmlPath = VmNodeUtil.getLibvirtXmlPath(nodeIp, vmIns.getId());
			vmVirtual.define(libXmlPath);
			VmInstancesCache.add(vmIns.getId(), vmIns);
		} catch (Exception e) {
			log.error("build vm error, ex:" + e, e);

			try{
				VmInstancesCache.remove(vmIns.getId());
				// 如果已经define虚拟机，则undefined
				vmVirtual.undefine(vmIns.getId());

				// 删除配置文件
				VmNodeUtil.deleteDirectoryNoException(cfgPath);
			}catch (Exception ex){
				log.error("build vm rollback error, ex:" + ex);
			}

			throw e;

		}
	}

	private void buildLibvirtXml(VmDetail vmIns, String nodeIp) throws GCloudException {

		String templetPath = VmNodeUtil.getLibvirtTempletPath();

		String libXmlPath = VmNodeUtil.getLibvirtXmlPath(nodeIp, vmIns.getId());

		String core = String.valueOf(vmIns.getCore());

		String memoryKb = String.valueOf(vmIns.getMemory() * 1024L);
		List<VmVolumeDetail> vmDisks = vmIns.getVmDisks();

		try {

			Document doc = ConfigFileUtil.readXml(templetPath);

			ConfigFileUtil.changeLibvirtName(doc, vmIns.getId());
			ConfigFileUtil.changeLibvirtDescription(doc, "GCloud");
			ConfigFileUtil.changeLibvirtUuid(doc, vmIns.getId());
			ConfigFileUtil.changeLibvirtCpu(doc, core);
			// 这里写修改cpu类型的代码
			ConfigFileUtil.changeLibvirtCpuType(doc, vmIns.getCpuCore(), vmIns.getCpuThread(), vmIns.getCpuSocket());

			ConfigFileUtil.changeLibvirtMemory(doc, memoryKb);

			for (int i = 0; i < vmDisks.size(); i++) {
				ConfigFileUtil.addDiskDevice(doc, vmDisks.get(i), false);
			}

			if (AgentType.QEMU_GUEST_AGENT.getValue().equals(computeNodeProp.getAgentType())) {
				ConfigFileUtil.addQemuAgentChannel(doc, vmIns.getId());
			}

			if (CloudPlatform.DESKTOPGCLOUD.getValue().equals(computeNodeProp.getCloudPlatform())) {
				ConfigFileUtil.changeEmulator(doc, computeNodeProp.getDesktopQemuPath());
				ConfigFileUtil.prepareDeskTopCfgFile(vmIns.getId());
			} else {
				ConfigFileUtil.changeEmulator(doc, computeNodeProp.getQemuPath());
			}

			String remoteType = computeNodeProp.getRemoteType();
			if (RemoteType.SPICE.getValue().equals(remoteType)) {
				ConfigFileUtil.addLibvirtGraphicsType(doc, "spice");
				ConfigFileUtil.setLibvirtDestop(doc, vmIns);
			}
			// 设置远程连接密码
			String grapPasswd = computeNodeProp.getGraphicsPassword();
			if (!StringUtils.isBlank(grapPasswd)) {
				if (GrapPasswdType.RONDOM.getPasswd().equalsIgnoreCase(grapPasswd)) {
					grapPasswd = UUID.randomUUID().toString().substring(0, 8);
					ConfigFileUtil.addLibvirtGraphicsPassword(doc, grapPasswd);
				} else {
					ConfigFileUtil.addLibvirtGraphicsPassword(doc, grapPasswd);
				}

			}

			if(vmIns.getZxAuth() != null && vmIns.getZxAuth()){
				//迁移过来，暂时写死vdz，后续维护外部文件
				ComputeNodeProp computeNodeProp = SpringUtil.getBean(ComputeNodeProp.class);
				VmVolumeDetail systemDiskDetail = new VmVolumeDetail(null, null, FileFormat.RAW.getValue(), computeNodeProp.getZxAuthPath(), Device.VDZ.getValue(), BusType.VIRTIO.getValue(), DiskType.DATA.getValue(), null, null, null, ProviderType.GCLOUD.getValue(), DiskProtocol.FILE.value(), StoragePoolDriver.FILE.name());
				systemDiskDetail.setStorageType(StorageType.LOCAL.getValue());
				ConfigFileUtil.addDiskDevice(doc, systemDiskDetail, false);
			}

			ConfigFileUtil.doc2XmlFile(doc, libXmlPath);

		} catch (Exception ex) {
			log.error("配置libvirt.xml失败", ex);
			throw new GCloudException("compute_node_vm_010018::fail to create libvirt.xml");
		}

	}

	private void buildInstanceXml(VmDetail vmIns, String nodeIp) throws GCloudException {

		String insXmlPath = VmNodeUtil.getInstanceXmlPath(nodeIp, vmIns.getId());

		try {

			Document doc = ConfigFileUtil.initInstanceXml();
			String parentNode = ConfigFileUtil.INSTANCE_XML_ROOT;

			// os xml
			String platform = vmIns.getPlatform() == null ? "" : vmIns.getPlatform();
			Map<String, String> osAttr = new HashMap<String, String>();
			osAttr.put("platform", platform);
			XmlElement osXmlE = new XmlElement("os", null, osAttr, null);
			ConfigFileUtil.addElementForXml(doc, parentNode, null, null, osXmlE);

			// imageId xml
			XmlElement imageXmlE = new XmlElement("imageId", vmIns.getImageId(), null, null);
			ConfigFileUtil.addElementForXml(doc, parentNode, null, null, imageXmlE);

			// imagePath xml
			XmlElement imagePathE = new XmlElement("imagePath", vmIns.getImagePath(), null, null);
			ConfigFileUtil.addElementForXml(doc, parentNode, null, null, imagePathE);

			// imagePoolId xml
			XmlElement imagePoolIdE = new XmlElement("imagePoolId", vmIns.getImagePoolId(), null, null);
			ConfigFileUtil.addElementForXml(doc, parentNode, null, null, imagePoolIdE);

			// imageStorageType xml
			XmlElement imageStorageTypeE = new XmlElement("imageStorageType", String.valueOf(vmIns.getImageStorageType()), null, null);
			ConfigFileUtil.addElementForXml(doc, parentNode, null, null, imageStorageTypeE);

			// ft xml
			Map<String, String> att = new HashMap<String, String>();
			att.put("state", String.valueOf(FtState.NO_FT.getValue()));
			XmlElement ftE = new XmlElement("faultTolreant", null, att, null);
			ConfigFileUtil.addElementForXml(doc, parentNode, null, null, ftE);

			// volume xml
			List<VmVolumeDetail> diskDetail = vmIns.getVmDisks();
			for (VmVolumeDetail vmDisk : diskDetail) {
				ConfigFileUtil.addInstanceXmlVolumeInfo(doc, vmDisk);
			}

			ConfigFileUtil.doc2XmlFile(doc, insXmlPath);

		} catch (Exception ex) {
			log.error("配置instance.xml失败", ex);
			throw new GCloudException("compute_node_vm_010018::fail to craete instance.xml");
		}

	}

}
