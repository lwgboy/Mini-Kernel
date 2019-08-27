package com.gcloud.compute.util;


import com.gcloud.common.util.FileUtil;
import com.gcloud.common.util.StringUtils;
import com.gcloud.common.util.SystemUtil;
import com.gcloud.compute.prop.ComputeNodeProp;
import com.gcloud.compute.virtual.libvirt.volume.IVmVolume;
import com.gcloud.compute.virtual.libvirt.volume.VmVolumeImpls;
import com.gcloud.core.exception.GCloudException;
import com.gcloud.core.service.SpringUtil;
import com.gcloud.header.compute.enums.DeviceOwner;
import com.gcloud.header.compute.enums.DiskProtocol;
import com.gcloud.header.compute.enums.FtState;
import com.gcloud.header.compute.msg.node.vm.model.VmDetail;
import com.gcloud.header.compute.msg.node.vm.model.VmNetworkDetail;
import com.gcloud.header.storage.model.VmVolumeDetail;
import com.gcloud.service.common.compute.model.QemuInfo;
import com.gcloud.service.common.compute.model.XmlElement;
import com.gcloud.service.common.compute.uitls.DdUtil;
import com.gcloud.service.common.compute.uitls.DiskQemuImgUtil;
import com.gcloud.service.common.compute.uitls.DiskUtil;
import com.gcloud.service.common.compute.uitls.VmUtil;
import lombok.extern.slf4j.Slf4j;
import org.dom4j.Attribute;
import org.dom4j.Branch;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.SAXReader;
import org.dom4j.io.XMLWriter;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;


/*
 * @Date 2015-5-18
 *
 * @Author yaowj@g-cloud.com.cn
 *
 * @Copyright 2015 www.g-cloud.com.cn Inc. All rights reserved.
 *
 * @Description 配置文件工具类
 */
@Slf4j
public class ConfigFileUtil {

	public static final String LOCK_FILE = "/createLock";
	public static final String NET_START_SCRIPT = "network/net_start.sh";
	public static final String INSTANCE_XML_ROOT = "instance";
	public static final String CONTAINER_XML_ROOT = "container";
	public static final String LXC_XML_ROOT = "lxc";
	public static final String DOCKER_NET_START_SCRIPT = "network/docker/set_net_interface.sh";

	/**
	 * @Title: changeLibvirtName
	 * @Description: 修改libvirt.xml的name
	 * @date 2015-5-18 上午8:38:18
	 *
	 * @param doc
	 * @param value
	 */
	public static void changeLibvirtName(Document doc, String value) {
		doc.selectSingleNode("/domain/name").setText(value);
	}

	/**
	 * @Title: changeLibvirtDescription
	 * @Description: 修改libvirt.xml的description
	 * @date 2015-5-18 上午8:38:31
	 *
	 * @param doc
	 * @param value
	 */
	public static void changeLibvirtDescription(Document doc, String value) {
		doc.selectSingleNode("/domain/description").setText(value);

	}

	/**
	 * @Title: changeLibvirtUuid
	 * @Description: 修改libvirt.xml的uuid
	 * @date 2015-5-18 上午8:38:52
	 *
	 * @param doc
	 * @param value
	 */
	public static void changeLibvirtUuid(Document doc, String value) {
		doc.selectSingleNode("/domain/uuid").setText(value);
	}

	/**
	 * @Title: changeLibvirtCpu
	 * @Description: 修改libvirt.xml的cpu
	 * @date 2015-5-18 上午8:39:16
	 *
	 * @param doc
	 * @param value
	 */
	public static void changeLibvirtCpu(Document doc, String value) {

		doc.selectSingleNode("/domain/vcpu").setText(value);
		Element vcpu = (Element) doc.selectSingleNode("/domain/vcpu");
		vcpu.addAttribute("current", value);

	}

	/**
	 *
	 * @Title: addLibvirtCpuType
	 * @Description: 添加对cpu的要求
	 * @date 2016年12月23日 上午10:02:49
	 *
	 * @param doc
	 * @param cpuCore
	 * @param cpuThread
	 * @param cpuSocket
	 */
	public static void changeLibvirtCpuType(Document doc, Integer cpuCore, Integer cpuThread, Integer cpuSocket) {
		if (cpuCore != null && cpuCore != 0 && cpuThread != null && cpuThread != 0 && cpuSocket != null && cpuSocket != 0) {
			String cpuCoreStr = String.valueOf(cpuCore);
			String cpuThreadStr = String.valueOf(cpuThread);
			String cpuSocketStr = String.valueOf(cpuSocket);
			Element domain = (Element) doc.selectSingleNode("/domain");
			if (doc.selectSingleNode("/domain/cpu") == null) {
				Element cpu = domain.addElement("cpu");
				Element topology = cpu.addElement("topology");
				topology.addAttribute("sockets", cpuSocketStr);
				topology.addAttribute("cores", cpuCoreStr);
				topology.addAttribute("threads", cpuThreadStr);
			} else {
				Element cpu = (Element) doc.selectSingleNode("/domain/cpu");
				Element topology = (Element) doc.selectSingleNode("/domain/cpu/topology");
				if (topology != null) {
					topology.addAttribute("sockets", cpuSocketStr);
					topology.addAttribute("cores", cpuCoreStr);
					topology.addAttribute("threads", cpuThreadStr);
				} else {
					topology = cpu.addElement("topology");
					topology.addAttribute("sockets", cpuSocketStr);
					topology.addAttribute("cores", cpuCoreStr);
					topology.addAttribute("threads", cpuThreadStr);
				}

			}
		}
	}

	public static void changeInstanceImage(Document doc, String value) {

	}

	/**
	 * @Title: changeLibvirtMemory
	 * @Description: 修改libvirt.xml的menory
	 * @date 2015-5-18 上午8:39:37
	 *
	 * @param doc
	 * @param value
	 */
	public static void changeLibvirtMemory(Document doc, String value) {
		doc.selectSingleNode("/domain/memory").setText(value);
		doc.selectSingleNode("/domain/currentMemory").setText(value);
	}

	/**
	 * @Title: changeEmulator
	 * @Description: 改变qemu路径
	 * @date 2015-7-8 下午5:15:01
	 *
	 * @param doc
	 * @param value
	 */
	public static void changeEmulator(Document doc, String value) {

		if (!StringUtils.isBlank(value)) {
			doc.selectSingleNode("/domain/devices/emulator").setText(value);
		}

	}

	/**
	 * @Title: addQemuAgentChannel
	 * @Description: 修改libvirt.xml，增加qemu-guest-agent通道
	 * @date 2015-5-18 上午8:40:25
	 *
	 * @param doc
	 */
	public static void addQemuAgentChannel(Document doc, String instanceId) {

		Element devices = (Element) doc.selectSingleNode("/domain/devices");
		Element diskE = devices.addElement("channel");
		diskE.addAttribute("type", "unix");

		Element sourceE = diskE.addElement("source");
		sourceE.addAttribute("mode", "bind");
		sourceE.addAttribute("path", String.format("/var/lib/libvirt/qemu/%s.org.qemu.guest_agent.0", instanceId));

		Element driverE = diskE.addElement("target");
		driverE.addAttribute("type", "virtio");
		driverE.addAttribute("name", "org.qemu.guest_agent.0");
	}

	/**
	 * @Title: addLibvirtGraphicsType
	 * @Description: 修改libvirt.xml，增加graphics
	 * @date 2015-5-18 上午8:41:59
	 *
	 * @param doc
	 * @param value
	 */
	public static void addLibvirtGraphicsType(Document doc, String value) {

		Element graphics = (Element) doc.selectSingleNode("/domain/devices/graphics");
		graphics.addAttribute("type", value);
	}

	public static void addLibvirtGraphicsPassword(Document doc, String value) {
		Element graphics = (Element) doc.selectSingleNode("/domain/devices/graphics");
		if (StringUtils.isNotBlank(value)) {
			graphics.addAttribute("passwd", value);
		} else {
			Attribute passwd = graphics.attribute("passwd");
			graphics.remove(passwd);
		}
	}

	/**
	 * @Title: initInstanceXml
	 * @Description: instance.xml写入platform熟悉
	 * @date 2015-5-18 上午9:39:45
	 *
	 */
	public static Document initInstanceXml() {

		Document document = DocumentHelper.createDocument();
		document.addElement("instance");
		return document;

	}

	/**
	 * @Title: initDockerXml
	 * @Description:
	 * @date 2015-5-18 上午9:39:45
	 *
	 */
	public static Document initDockerXml() {

		Document document = DocumentHelper.createDocument();
		document.addElement(CONTAINER_XML_ROOT);
		return document;

	}

	/**
	 * @Title: addInstanceXmlOs
	 * @Description: 增加虚拟机系统信息
	 * @date 2015-5-21 上午8:53:44
	 *
	 * @param doc
	 * @param platform
	 */
	public static void addInstanceXmlOs(Document doc, String platform) {

		Element devices = (Element) doc.selectSingleNode("/instance");
		Element osE = devices.addElement("os");
		osE.addAttribute("platform", platform);

	}

	/**
	 * @Title: addElementToInstanceXml
	 * @Description: 添加新元素到instance.xml
	 * @date 2015-5-21 上午8:54:23
	 *
	 * @param doc
	 * @param node
	 * @param field
	 * @param value
	 */
	public static void addElementToInstanceXml(Document doc, String node, String field, String value) {
		Element devices = (Element) doc.selectSingleNode(node);
		Element imageIdE = devices.addElement(field);
		imageIdE.setText(value);
	}

	/**
	 * @Title: changeElementToInstanceXml
	 * @Description: 修改instance，xml元素
	 * @date 2015-5-30 下午4:59:48
	 *
	 * @param doc
	 * @param node
	 * @param field
	 * @param value
	 * @return
	 */
	public static void changeElementToInstanceXml(Document doc, String node, String field, String value) {
		Element instance = (Element) doc.selectSingleNode("/" + node + "/" + field);
		instance.setText(value);
	}

	/**
	 * @Title: addContent
	 * @Description: 增加文件信息
	 * @date 2015-5-18 上午9:44:37
	 *
	 * @param filePath
	 */
	public static boolean addContent(String filePath, String content) {
		boolean isSucc = true;
		FileWriter writer = null;
		try {

			File File = new File(filePath);

			if (File.exists() == false) {
				File.createNewFile();
			}
			writer = new FileWriter(filePath, false);
			writer.write(content);

		} catch (Exception e) {
			log.error("::修改文件失败", e);
			throw new GCloudException("::修改文件失败");
		} finally {
			try {
				writer.close();
			} catch (Exception e) {
				log.error("流关闭失败", e);
			}
		}
		return isSucc;
	}

	/**
	 * @Title: updateNodeInfoWithCreateLock
	 * @Description: 修改nodeinfo信息,增加createLock收养锁
	 * @date 2015-5-18 上午9:44:37
	 *
	 */
	public static void updateNodeIpWithCreateLock(String instanceId, String sourceIp, String targetIp) {
		log.debug(String.format("修改nodeinfo 开始， instanceId：%s,sourceIp：%s,targetIp：%s", instanceId, sourceIp, targetIp));
		// 获取云服务器配置原来的根目录路径
		String sourceInstanceConfigPath = VmNodeUtil.getInstanceConfigPath(sourceIp, instanceId);
		String targetInstanceConfigPath = VmNodeUtil.getInstanceConfigPath(targetIp, instanceId);

		try {
			// 创建createLock锁文件
			createLockFile(sourceInstanceConfigPath);

			String userConfigPath = VmNodeUtil.getNodeConfigPath(targetIp);
			VmUtil.checkPath(userConfigPath);

			// 移动虚拟机文件夹路径
			String[] cmd = new String[] { "mv", sourceInstanceConfigPath, targetInstanceConfigPath };
			int res = SystemUtil.runAndGetCode(cmd);
			if (res != 0) {
				throw new GCloudException("移动虚拟机目录错误");
			}
			// 强制删除createLock锁文件
			forceDeleteLockFile(targetInstanceConfigPath);
		} catch (Exception e) {
			log.error("修改nodeinfo 错误", e);
			// 强制删除createLock锁文件
			forceDeleteLockFile(sourceInstanceConfigPath);
			throw new GCloudException(e.getMessage());
		}
		log.debug(String.format("修改nodeinfo 结束， instanceId：%s,sourceIp：%s,targetIp：%s", instanceId, sourceIp, targetIp));
	}

	public static void createLockFile(String path) {

		try {
			File createLockFile = new File(path + LOCK_FILE);
			createLockFile.createNewFile();
		} catch (IOException e) {
			log.error("1010503:fail to create create lock", e);
			throw new GCloudException("1010503:fail to create create lock");
		}
	}

	public static void forceDeleteLockFile(String path) {
		File createLockFile = new File(path + LOCK_FILE);
		while (createLockFile.delete()) {
			try {
				Thread.sleep(200);
			} catch (InterruptedException e) {
				log.error("::删除文件sleep失败", e);
				throw new GCloudException("::删除文件sleep失败");
			}
		}
	}

	/**
	 *
	 * @Title: updateCDDisk
	 * @Description: 更改光盘
	 * @date 2015-5-25 上午11:40:58
	 *
	 * @param doc
	 * @param filePath
	 * @throws GCloudException
	 */
	public static void updateCDDisk(Document doc, String filePath) {
		Element domain = (Element) doc.selectSingleNode("/domain");
		String hyper = domain.attributeValue("type");
		boolean isSuccess = false;
		log.debug("updateCDDisk: update cdrom device, source=" + filePath);
		List<Element> disks = (List<Element>) doc.selectNodes("/domain/devices/disk");
		for (Iterator<Element> i = disks.iterator(); i.hasNext();) {
			Element disk = i.next();
			String attValue = disk.attributeValue("device");
			if (attValue.equals("cdrom")) {
				Element source = (Element) disk.selectSingleNode("source");
				if (hyper.equals("kvm")) {
					Attribute att = source.attribute("dev");
					if (att != null) {
						source.remove(att);
					}
					source.addAttribute("file", filePath);
				}

				isSuccess = true;
				break;
			}
		}
		if (!isSuccess) {
			throw new GCloudException("::更新文件失败");
		}
	}

	/**
	 *
	 * @Title: updateBootItems
	 * @Description: 更改os启动选项
	 * @date 2015-5-25 下午2:55:59
	 *
	 * @param doc
	 * @param bootItem
	 * @throws GCloudException
	 */
	public static void updateBootItems(Document doc, String bootItem) {
		Element os = (Element) doc.selectSingleNode("/domain/os");
		Element boot = os.addElement("boot");
		boot.addAttribute("dev", bootItem);
	}

	/**
	 * 生成迁移状态临时文件
	 *
	 * @param instanceId
	 *            虚拟机实例路径
	 * @param migrateStatus
	 *            节点标识信息
	 * @param sourceIp
	 *            源节点Ip
	 * @param targetIp
	 *            目标节点Ip
	 * @return String relativeMigratePath
	 * @throws Exception
	 */
	public static String generateMigrateStatus(String instanceId, String relativePath, String migrateStatus, String sourceIp, String targetIp) throws Exception {
		return generateMigrateStatus(instanceId, relativePath, migrateStatus, sourceIp, targetIp, null);
	}

	public static String generateMigrateStatus(String instanceId, String relativePath, String migrateStatus, String sourceIp, String targetIp, String networkInfo) throws Exception {
		String info = String.format("status:%s,sourcIp:%s,targetIp:%s", migrateStatus, sourceIp, targetIp);
		if (!StringUtils.isBlank(networkInfo)) {
			info += "," + networkInfo;
		}
		return generateMigrateStatus(instanceId, relativePath, info);
	}

	private static String generateMigrateStatus(String instanceId, String relativePath, String info) throws Exception {
		// System.out.println("instancePath" + instancePath);
		String tmpDirPath = "/tmp/" + relativePath;
		String migrateTempInfoPath = tmpDirPath + "/" + instanceId + "_migrateStatus";

		String mdkirCmd[] = new String[] { "mkdir", "-p", tmpDirPath };
		if (SystemUtil.runAndGetCode(mdkirCmd) != 0) {
			throw new Exception(String.format("mkdir %s error", tmpDirPath));
		}
		File migrateInfoFile = new File(migrateTempInfoPath);
		if (migrateInfoFile.exists() == false) {
			migrateInfoFile.createNewFile();
		}
		// 打开一个写文件器，构造函数中的第二个参数true表示以追加形式写文件
		FileWriter writer = new FileWriter(migrateInfoFile, false);
		writer.write(info);
		writer.close();
		return relativePath + "/" + instanceId + "_migrateStatus";
	}

	public static void removeNetworkInfo(Document doc) {

	}

	public static void addQemuCommandline(Document doc, String value) {
		Element qemuCommandline = (Element) doc.selectSingleNode("/domain/qemu:commandline");
		Element qemuArg = qemuCommandline.addElement("qemu:arg");
		qemuArg.addAttribute("value", value);
	}

	public static String getTargetDev(String portId) {
		return "tap" + portId.substring(0, 11);
	}

	/**
	 *
	 * @param bridge
	 * @return
	 */
	public static Document generateOvsNetcardDom(String portId, String mac, String bridge) {
		Document document = DocumentHelper.createDocument();
		Element interE = document.addElement("interface");
		interE.addAttribute("type", "bridge");

		Element macE = interE.addElement("mac");
		macE.addAttribute("address", mac);

		Element virtualPort = interE.addElement("virtualport");
		virtualPort.addAttribute("type", "openvswitch");
		Element targetE = interE.addElement("target");
		String targetDev = getTargetDev(portId);
		targetE.addAttribute("dev", targetDev);

		Element sourceE = interE.addElement("source");
		sourceE.addAttribute("bridge", bridge);

		Element modelE = interE.addElement("model");
		modelE.addAttribute("type", "virtio");

		Element driverE = interE.addElement("driver");
		driverE.addAttribute("name", "qemu");

		/*
		 * if(!DeviceOwner.FOREIGN.getValue().equals(port.getDeviceOwner())) {
		 * Element filterrefE = interE.addElement("filterref");
		 * filterrefE.addAttribute("filter", "no-arp-spoofing");
		 * 
		 * Element parameterE1 = filterrefE.addElement("parameter");
		 * parameterE1.addAttribute("name", "IP");
		 * parameterE1.addAttribute("value", ip);
		 * 
		 * Element parameterE2 = filterrefE.addElement("parameter");
		 * parameterE2.addAttribute("name", "MAC");
		 * parameterE2.addAttribute("value", port.getMacAddress());
		 * 
		 * }
		 */

		return document;

	}

	/**
	 * @Title: deleteInterface
	 * @Description: 删除libvirt.xml里面的对应网络信息
	 * @date 2015-6-13 下午3:59:54
	 *
	 * @param doc
	 * @param macAddress
	 */
	public static void deleteInterface(Document doc, String macAddress) {

		Element devices = (Element) doc.selectSingleNode("/domain/devices");
		List<Element> interfaceList = (List<Element>) doc.selectNodes("/domain/devices/interface");
		if (interfaceList != null) {

			for (Element inter : interfaceList) {

				Element macE = (Element) inter.selectSingleNode("mac");
				if (macE != null) {
					String address = macE.attributeValue("address");
					if (address != null && address.equals(macAddress)) {
						devices.remove(inter);
						break;
					}
				}
			}
		}

	}

	/**
	 * @Title: addNetInfoToInsXml
	 * @Description: 把网络信息增加到instancexml里面
	 * @date 2015-6-13 下午3:59:39
	 *
	 * @param portId
	 * @param brName
	 * @param preName
	 * @param aftName
	 * @param macAddress
	 */
	public static void addNetInfoToInsXml(String instanceId, String filePath, String portId, String brName, String preName, String aftName, String macAddress) {

		SAXReader reader = new SAXReader();

		List<XmlElement> netChildren = new ArrayList<XmlElement>();

		Map<String, String> netAttr = new HashMap<String, String>();
		netAttr.put("id", portId);

		XmlElement portIdE = new XmlElement("portId", portId, null, null);
		XmlElement brE = new XmlElement("brName", brName, null, null);
		XmlElement preE = new XmlElement("preName", preName, null, null);
		XmlElement aftE = new XmlElement("aftName", aftName, null, null);
		XmlElement macE = new XmlElement("macAddress", macAddress, null, null);

		netChildren.add(portIdE);
		netChildren.add(brE);
		netChildren.add(preE);
		netChildren.add(aftE);
		netChildren.add(macE);

		XmlElement netE = new XmlElement("network", null, netAttr, netChildren);

		ConfigFileUtil.addElementForXmlAndSave(instanceId, INSTANCE_XML_ROOT, null, null, netE, filePath);

	}

	/**
	 * @Title: addDockerNetInfoToXml
	 *
	 * @date 2016年11月22日 下午5:27:26
	 *
	 * @param dockerId
	 * @param filePath
	 * @param portId
	 * @param preName
	 * @param aftName
	 * @param ifname
	 * @param gateway
	 * @throws GCloudException
	 */
	public static void addDockerNetInfoToXml(String dockerId, String filePath, String portId, String preName, String aftName, String ifname, String gateway, String macAddr) {

		List<XmlElement> netChildren = new ArrayList<XmlElement>();

		Map<String, String> netAttr = new HashMap<String, String>();
		netAttr.put("id", portId);

		XmlElement portIdE = new XmlElement("portId", portId, null, null);
		XmlElement preE = new XmlElement("preName", preName, null, null);
		XmlElement aftE = new XmlElement("aftName", aftName, null, null);
		XmlElement ifnameE = new XmlElement("ifname", ifname, null, null);
		XmlElement gatewayE = new XmlElement("gateway", gateway, null, null);
		XmlElement macAddrE = new XmlElement("macAddr", macAddr, null, null);

		netChildren.add(portIdE);
		netChildren.add(preE);
		netChildren.add(aftE);
		netChildren.add(ifnameE);
		netChildren.add(gatewayE);
		netChildren.add(macAddrE);

		XmlElement netE = new XmlElement("network", null, netAttr, netChildren);

		ConfigFileUtil.addDockerElementForXmlAndSave(dockerId, CONTAINER_XML_ROOT, null, null, netE, filePath);

	}

	/**
	 * @Title: createUsbAttachXml
	 *
	 * @date 2015-6-16 下午3:53:07
	 *
	 * @param xmlPath
	 * @throws GCloudException
	 */
	public static void createUsbAttachXml(String vid, String pid, String bus, String device, String xmlPath) {

		Document document = DocumentHelper.createDocument();

		Element hostdevE = document.addElement("hostdev");

		hostdevE.addAttribute("mode", "subsystem");
		hostdevE.addAttribute("type", "usb");
		hostdevE.addAttribute("type", "usb");

		Element sourceE = hostdevE.addElement("source");

		Element vendorE = sourceE.addElement("vendor");
		vendorE.addAttribute("id", "0x" + vid);

		Element productE = sourceE.addElement("product");
		productE.addAttribute("id", "0x" + pid);

		Element addressE = sourceE.addElement("address");
		addressE.addAttribute("bus", bus);
		addressE.addAttribute("device", device);

		doc2XmlFile(document, xmlPath);
	}

	/**
	 *
	 * @Title: createIsoAttachXml
	 *
	 * @date 2016-7-20 上午10:36:39
	 *
	 * @param isoPath
	 * @param device
	 * @param xmlPath
	 * @throws GCloudException
	 */
	public static void createIsoAttachXml(String isoPath, String device, String xmlPath) {

		// 删除挂载xml文件
		File xmlFile = new File(xmlPath);
		if (xmlFile.exists()) {
			xmlFile.delete();
		}

		Document document = DocumentHelper.createDocument();

		Element diskE = document.addElement("disk");

		diskE.addAttribute("device", "cdrom");
		diskE.addAttribute("type", "file");

		Element sourceE = diskE.addElement("source");
		sourceE.addAttribute("file", isoPath);

		Element targetE = diskE.addElement("target");
		targetE.addAttribute("dev", device);
		targetE.addAttribute("bus", "ide");

		doc2XmlFile(document, xmlPath);
	}

	/**
	 *
	 * @Title: updateIsoAttachXml
	 *
	 * @date 2016-7-20 上午10:36:39
	 *
	 * @param isoPath
	 * @param device
	 * @param xmlPath
	 * @throws GCloudException
	 */
	public static void updateIsoAttachXml(String isoPath, String device, String xmlPath) {

		Document document = DocumentHelper.createDocument();

		Element diskE = document.addElement("disk");

		diskE.addAttribute("device", "cdrom");
		diskE.addAttribute("type", "file");

		Element sourceE = diskE.addElement("source");
		sourceE.addAttribute("file", isoPath);

		Element targetE = diskE.addElement("target");
		targetE.addAttribute("dev", device);
		targetE.addAttribute("bus", "ide");

		doc2XmlFile(document, xmlPath);
	}

	/**
	 * @Title: deleteLibvirtDiskBySerial
	 * @Description: 根据source 的 file属性删除对应的xml
	 * @date 2015-6-16 下午3:53:04
	 *
	 * @param doc
	 */
	public static void deleteLibvirtDiskBySerial(Document doc, String serial) {

		Element devices = (Element) doc.selectSingleNode("/domain/devices");
		List<Element> diskList = (List<Element>) doc.selectNodes("/domain/devices/disk");
		if (diskList != null) {

			for (Element inter : diskList) {

				Element serialE = (Element) inter.selectSingleNode("serial");
				if (serialE != null && serialE.getText().equals(serial)) {
					devices.remove(inter);
					break;
				}
			}
		}

	}

	public static void deleteLibvirtDisk(Document doc, DiskProtocol protocol, String sourcePath) {

		Element devices = (Element) doc.selectSingleNode("/domain/devices");
		List<Element> diskList = (List<Element>) doc.selectNodes("/domain/devices/disk");
		if (diskList != null) {

			for (Element inter : diskList) {
				Element sourceE = (Element) inter.selectSingleNode("source");
                Attribute protocolAttr = sourceE.attribute("protocol");
                DiskProtocol xmlProtocol = DiskProtocol.FILE;
                if(protocolAttr != null){
                    xmlProtocol = DiskProtocol.value(protocolAttr.getValue());
                }
                if(xmlProtocol == null){
                    log.debug("不支持此协议,protocol=" + xmlProtocol);
                    continue;
                }

                if(!xmlProtocol.equals(protocol)){
                    continue;
                }

                if(DiskProtocol.RBD == protocol){
                    String name = sourceE.attributeValue("name");
                    if(sourcePath.equals(name)){
                        devices.remove(inter);
                        break;
                    }
                }else if(DiskProtocol.FILE == protocol){
                    String file = sourceE.attributeValue("file");
                    if(sourcePath.equals(file)){
                        devices.remove(inter);
                        break;
                    }
                }

			}
		}

	}

	public static void checkFileExist(String filePath) {
		File instanceXmlFile = new File(filePath);
		if (!instanceXmlFile.exists()) {
			throw new GCloudException("::文件不存在");
		}
	}

	// =================xml操作 =========
	/**
	 *
	 * @Title: addElementForXmlAndSave
	 * @Description: 添加xml元素，根据xPath和属性查找要修改的元素，添加children根据XmlElement的name属性往下查找
	 * @date 2015-6-19 上午9:53:06
	 *
	 * @param instanceId
	 * @param xPath
	 * @param attributeKey
	 * @param attributeValue
	 * @param setElement
	 * @param filePath
	 * @throws Exception
	 */
	public static void addElementForXmlAndSave(String instanceId, String xPath, String attributeKey, String attributeValue, XmlElement setElement, String filePath) {
		Document doc = readXml(filePath);
		addElementForXml(doc, xPath, attributeKey, attributeValue, setElement);
		doc2XmlFile(doc, filePath);
	}

	public static void addDockerElementForXmlAndSave(String dockerId, String xPath, String attributeKey, String attributeValue, XmlElement setElement, String filePath) {
		// TODO
		// 收养完成后 修改 docker 锁

		Document doc = readXml(filePath);
		addElementForXml(doc, xPath, attributeKey, attributeValue, setElement);
		doc2XmlFile(doc, filePath);
	}

	/**
	 *
	 * @Title: addElementForXml(不修改文件)
	 * @Description: 添加xml元素，根据xPath和属性查找要修改的元素，添加children根据XmlElement的name属性往下查找
	 * @date 2015-6-19 上午9:53:06
	 *
	 * @param xPath
	 * @param attributeKey
	 * @param attributeValue
	 * @param setElement
	 * @throws Exception
	 */
	public static Document addElementForXml(Document doc, String xPath, String attributeKey, String attributeValue, XmlElement setElement) {
		return addElementForXml(doc, xPath, attributeKey, attributeValue, setElement, null);
	}

	/**
	 *
	 * @Title: updateElementForXmlAndSave
	 * @Description: 修改xml元素，根据xPath和属性查找要修改的元素，修改children根据XmlElement的name属性往下查找
	 * @date 2015-6-19 上午9:53:06
	 *
	 * @param instanceId
	 * @param xPath
	 * @param attributeKey
	 * @param attributeValue
	 * @param setElement
	 * @param filePath
	 * @throws Exception
	 */
	public static void updateElementForXmlAndSave(String instanceId, String xPath, String attributeKey, String attributeValue, XmlElement setElement, String filePath) {
		Document doc = readXml(filePath);
		updateElementForXml(doc, xPath, attributeKey, attributeValue, setElement);
		doc2XmlFile(doc, filePath);
	}

	public static void updateDockerElementForXmlAndSave(String dockerId, String xPath, String attributeKey, String attributeValue, XmlElement setElement, String filePath) {
		// TODO
		// 收养完成后 修改 docker 锁

		Document doc = readXml(filePath);
		updateElementForXml(doc, xPath, attributeKey, attributeValue, setElement);
		doc2XmlFile(doc, filePath);
	}

	private static Document updateElementForXml(Document doc, String xPath, String attributeKey, String attributeValue, XmlElement setElement) {
		return updateElementForXml(doc, xPath, attributeKey, attributeValue, setElement, null);
	}

	/**
	 *
	 * @Title: deleteElementForXmlAndSave
	 * @Description: 删除xml元素，根据xPath和属性查找要修改的元素删除
	 * @date 2015-6-19 上午9:53:43
	 *
	 * @param instanceId
	 * @param xPath
	 * @param attributeKey
	 * @param attributeValue
	 * @param filePath
	 * @throws Exception
	 */
	public static void deleteElementForXmlAndSave(String instanceId, String xPath, String attributeKey, String attributeValue, String filePath) {
		Document doc = readXml(filePath);
		deleteElementForXml(doc, xPath, attributeKey, attributeValue);
		doc2XmlFile(doc, filePath);
	}

	private static Document deleteElementForXml(Document doc, String xPath, String attributeKey, String attributeValue) {
		Element updateElement = null;

		try {
			updateElement = findNodeByXpathAndAttribute(doc, xPath, attributeKey, attributeValue, null, XmlOption.DELETE);
		} catch (Exception e) {
			log.info("查找不到节点来删除" + e.getMessage(), e);
		}

		if (updateElement != null) {
			updateElement.detach();
		}
		return doc;
	}

	/**
	 *
	 * @Title: findNodeByXpathAndAttribute
	 * @Description: 根据Xpath和Attribute查找节点
	 * @date 2015-6-18 下午10:48:11
	 *
	 * @param doc
	 * @param xPath
	 * @param attributeKey
	 * @param attributeValue
	 * @param parentElement
	 * @return
	 * @throws GCloudException
	 */
	private static Element findNodeByXpathAndAttribute(Document doc, String xPath, String attributeKey, String attributeValue, Element parentElement, XmlOption option) {
		Element updateElement = null;
		try {
			List<Element> result = null;
			if (parentElement != null) {
				if (option == XmlOption.UPDATA || option == XmlOption.DELETE || option == XmlOption.FIND) {
					result = parentElement.selectNodes(xPath);
				} else if (option == XmlOption.ADD) {
					return parentElement;
				}
			} else {
				result = doc.selectNodes(xPath);
			}

			if (result != null) {
				if (StringUtils.isNotBlank(attributeKey)) {
					for (Element e : result) {
						Attribute attr = e.attribute(attributeKey);
						if (attr != null && attr.getValue().equals(attributeValue)) {
							updateElement = e;
							break;
						}
					}
				} else {
					updateElement = result.get(0);
				}
			}

			if (updateElement == null) {
				throw new GCloudException("");
			}
		} catch (Exception e) {
			throw new GCloudException("::Can't find update node.");
		}
		return updateElement;
	}

	private static Document addElementForXml(Document doc, String xPath, String attributeKey, String attributeValue, XmlElement addElement, Element parentElement) {
		Element nodeElement = findNodeByXpathAndAttribute(doc, xPath, attributeKey, attributeValue, parentElement, XmlOption.ADD);

		Element temp = null;

		if (StringUtils.isNotBlank(addElement.getName())) {
			temp = nodeElement.addElement(addElement.getName());
		}

		if (addElement.getText() != null && (addElement.getChildrens() == null || addElement.getChildrens().size() == 0)) {
			temp.setText(addElement.getText());
		}

		if (addElement.getAttributes() != null && addElement.getAttributes().size() > 0) {
			for (Map.Entry<String, String> attributeMap : addElement.getAttributes().entrySet()) {
				String key = attributeMap.getKey();
				String value = attributeMap.getValue();
				temp.addAttribute(key, value);
			}
		}

		if (addElement.getChildrens() != null && addElement.getChildrens().size() > 0) {
			for (XmlElement children : addElement.getChildrens()) {
				addElementForXml(doc, temp.getName(), null, null, children, temp);
			}
		}
		return doc;
	}

	private static Document updateElementForXml(Document doc, String xPath, String attributeKey, String attributeValue, XmlElement setElement, Element parentElement) {
		Element updateElement = findNodeByXpathAndAttribute(doc, xPath, attributeKey, attributeValue, parentElement, XmlOption.UPDATA);

		if (StringUtils.isNotBlank(setElement.getName()) && setElement.getName().equals(updateElement.getName())) {
			updateElement.setName(setElement.getName());
		}

		if (setElement.getText() != null && (setElement.getChildrens() == null || setElement.getChildrens().size() == 0)) {
			updateElement.setText(setElement.getText());
		}

		if (setElement.getAttributes() != null && setElement.getAttributes().size() > 0) {
			for (Map.Entry<String, String> attributeMap : setElement.getAttributes().entrySet()) {
				String key = attributeMap.getKey();
				String value = attributeMap.getValue();
				updateElement.addAttribute(key, value);
			}
		}

		if (setElement.getChildrens() != null && setElement.getChildrens().size() > 0) {
			for (XmlElement children : setElement.getChildrens()) {
				updateElementForXml(doc, children.getName(), null, null, children, updateElement);
			}
		}
		return doc;
	}

	/**
	 *
	 * @Title: findElementForXml
	 * @Description: 根据Xpath和Attribute查找节点
	 * @date 2015-7-21 下午3:45:13
	 *
	 * @param filePath
	 *            文件路径
	 * @param xPath
	 *            xml路径
	 * @param attributeKey
	 *            属性key
	 * @param attributeValue
	 *            属性value
	 * @return
	 * @throws GCloudException
	 */
	public static Element findElementForXml(String filePath, String xPath, String attributeKey, String attributeValue) {
		try {
			Document doc = readXml(filePath);
			Element element = findNodeByXpathAndAttribute(doc, xPath, attributeKey, attributeValue, null, XmlOption.FIND);
			return element;
		} catch (Exception e) {
			log.error(String.format("查找xml元素出错，错误:%s, filePath:%s, xPath:%s,  attributeKey:%s, attributeValue:%s", e.getMessage(), filePath, xPath, attributeKey, attributeValue), e);
			return null;
		}
	}

	public enum XmlOption {
		ADD, FIND, UPDATA, DELETE
	}

	public static boolean doc2XmlFile(Document document, String filename) {

		boolean flag = true;

		XMLWriter writer = null;
		try {
			OutputFormat format = OutputFormat.createPrettyPrint();
			format.setEncoding("UTF-8");

			File file = new File(filename);
			FileWriter fw = new FileWriter(file);

			writer = new XMLWriter(fw, format);
			writer.write(document);

		} catch (Exception ex) {

			flag = false;
			ex.printStackTrace();

		} finally {

			try {
				writer.close();
			} catch (Exception ex) {
			}

		}

		return flag;

	}


	// end =====================xml操作==============

	/**
	 *
	 * @Title: prepareDeskTopCfgFile
	 * @Description: 准备桌面云配置文件
	 * @date 2015-5-19 上午9:21:43
	 *
	 * @param instanceId
	 * @throws GCloudException
	 */
	public static void prepareDeskTopCfgFile(String instanceId) {
		ComputeNodeProp computeNodeProp = (ComputeNodeProp) SpringUtil.getBean(ComputeNodeProp.class);
		VmUtil.checkPath(computeNodeProp.getDesktopCfgPath());
		String deskTopCfgTemplatePath = computeNodeProp.getConfigurePath() + "/" + computeNodeProp.getDesktopCfgTemplate();
		if (!new File(deskTopCfgTemplatePath).exists()) {
			log.error(String.format("M01_BU01_UC01_IMPL_9100::deskTopCfgTemplate=%s isn't exists !", computeNodeProp.getDesktopCfgTemplate()));
			throw new GCloudException("M01_BU01_UC01_IMPL_9100");
		}
		DdUtil.ddCopyFile(deskTopCfgTemplatePath, computeNodeProp.getDesktopCfgPath() + "/" + instanceId + ".cfg");
	}

	/**
	 * @Title: setLibvirtDestop
	 * @Description: 修改libvirt.xml，桌面云格式
	 * @date 2015-5-18 上午8:42:24
	 *
	 * @param doc
	 * @param vmIns
	 */
	public static void setLibvirtDestop(Document doc, VmDetail vmIns) {
		ComputeNodeProp computeNodeProp = (ComputeNodeProp) SpringUtil.getBean(ComputeNodeProp.class);
		Element graphics = (Element) doc.selectSingleNode("/domain/devices/graphics");
		String listenAttr = graphics.attributeValue("listen");

		if (listenAttr.indexOf("lci-cfg-path=") != -1) {
			String postFix = listenAttr.substring(listenAttr.lastIndexOf("."));
			listenAttr = listenAttr.substring(0, listenAttr.indexOf("lci-cfg-path=") + 13 + 1);
			listenAttr = listenAttr + computeNodeProp.getDesktopCfgPath() + "/" + vmIns.getId() + postFix;
		} else {
			listenAttr = listenAttr + ",lci-cfg-path=" + computeNodeProp.getDesktopCfgPath() + "/" + vmIns.getId() + ".cfg";
		}

		// graphics.setAttributeValue("listen", listenAttr);
		graphics.addAttribute("listen", listenAttr);

	}

	/**
	 * @Title: getHotNetcardDom
	 * @Description: 获取网络xml的dom对象
	 * @date 2015-6-13 下午4:00:15
	 *
	 * @return
	 */
	public static Document getHotNetcardDom(VmNetworkDetail vmNetwork) {

		Document document = DocumentHelper.createDocument();
		networkInterfaceConfig(document, vmNetwork);
		return document;

	}

	/**
	 * @Title: addNetworkInfo
	 * @Description: 把网络信息增加到libvirt.xml里面，几时虚拟机被undefined，也能够生效
	 * @date 2015-6-13 下午4:00:30
	 *
	 * @param doc
	 * @throws GCloudException
	 */
	public static void addNetworkInfo(Document doc, VmNetworkDetail vmNetwork) {
		Element devices = (Element) doc.selectSingleNode("/domain/devices");
		networkInterfaceConfig(devices, vmNetwork);
	}

	public static void networkInterfaceConfig(Branch branch, VmNetworkDetail networkDetail) {
		String ip = networkDetail.getIp();

		Element interE = branch.addElement("interface");
		interE.addAttribute("type", "bridge");

		Element macE = interE.addElement("mac");
		macE.addAttribute("address", networkDetail.getMacAddress());

		Element targetE = interE.addElement("target");
		String targetDev = getTargetDev(networkDetail.getPortId());
		targetE.addAttribute("dev", targetDev);

		String bridge = StringUtils.isBlank(networkDetail.getCustomOvsBr()) ? networkDetail.getBrName() : networkDetail.getCustomOvsBr();
		Element sourceE = interE.addElement("source");
		sourceE.addAttribute("bridge", bridge);

		Element modelE = interE.addElement("model");
		modelE.addAttribute("type", "virtio");

		if (!(networkDetail.getNoArpLimit() != null && networkDetail.getNoArpLimit()) && (networkDetail.getDeviceOwner() != null && !DeviceOwner.FOREIGN.getValue().equals(networkDetail.getDeviceOwner()))) {
			Element filterrefE = interE.addElement("filterref");
			filterrefE.addAttribute("filter", "no-arp-spoofing");

			Element parameterE1 = filterrefE.addElement("parameter");
			parameterE1.addAttribute("name", "IP");
			parameterE1.addAttribute("value", ip);

			Element parameterE2 = filterrefE.addElement("parameter");
			parameterE2.addAttribute("name", "MAC");
			parameterE2.addAttribute("value", networkDetail.getMacAddress());
		}

		if (StringUtils.isNotBlank(networkDetail.getCustomOvsBr())) {
			Element virtualPortE = interE.addElement("virtualport");
			virtualPortE.addAttribute("type", "openvswitch");
		}
	}

	/**
	 * @Title: addAutoStartNetSh
	 * @Description: 添加启动网络脚本
	 * @date 2015-6-13 下午3:59:14
	 *
	 * @param shPath
	 * @param portId
	 * @param brName
	 * @param preName
	 * @param aftName
	 * @param macAddress
	 * @throws GCloudException
	 */
	public static void addAutoStartNetSh(String shPath, String portId, String instanceUuid, String brName, String preName, String aftName, String macAddress) {
		ComputeNodeProp computeNodeProp = (ComputeNodeProp) SpringUtil.getBean(ComputeNodeProp.class);
		String scriptPath = computeNodeProp.getConfigurePath();
		String script = FileUtil.getDirectoryString(scriptPath) + NET_START_SCRIPT;

		FileWriter writer = null;
		try {

			File file = new File(shPath);
			if (!file.exists()) {
				file.createNewFile();
			}

			String context = script + " " + portId + " " + instanceUuid + " " + brName + " " + preName + " " + aftName + " " + macAddress;

			writer = new FileWriter(shPath, false);
			writer.write(context);

		} catch (Exception ex) {
			log.error("创建网络启动脚本失败", ex);
			throw new GCloudException("::创建文件失败");

		} finally {
			try {
				writer.close();
			} catch (Exception e) {
				log.error("流关闭失败", e);
			}
		}

	}

	public static void addDiskDevice(Document doc, VmVolumeDetail vmDiskDetail, boolean isCreateElement) {
		DiskProtocol diskProtocol = DiskProtocol.value(vmDiskDetail.getProtocol());

		if(StringUtils.isBlank(vmDiskDetail.getFileFormat())){
			String convertSource = DiskUtil.getDiskPath(vmDiskDetail.getSourcePath(), diskProtocol);
			QemuInfo qemuInfo = DiskQemuImgUtil.info(convertSource);
			vmDiskDetail.setFileFormat(qemuInfo.getFormat());
		}

		IVmVolume vmVolume = VmVolumeImpls.vmVolumeImpl(diskProtocol);
		vmVolume.addDiskDevice(doc, vmDiskDetail, isCreateElement);
	}

	public static void addCustomOvsBrToInsXml(String instanceId, String filePath, String portId, String customOvsBr, String macAddress) {

		List<XmlElement> netChildren = new ArrayList<XmlElement>();

		Map<String, String> netAttr = new HashMap<String, String>();
		netAttr.put("id", portId);

		XmlElement portIdE = new XmlElement("portId", portId, null, null);
		XmlElement customOvsBrE = new XmlElement("customOvsBr", customOvsBr, null, null);
		XmlElement macE = new XmlElement("macAddress", macAddress, null, null);

		netChildren.add(portIdE);
		netChildren.add(customOvsBrE);
		netChildren.add(macE);

		XmlElement netE = new XmlElement("network", null, netAttr, netChildren);

		ConfigFileUtil.addElementForXmlAndSave(instanceId, INSTANCE_XML_ROOT, null, null, netE, filePath);

	}

	public static void addCustomOvsBrStartNetSh(String shPath, String customOvsBr) {

		FileWriter writer = null;
		try {

			File file = new File(shPath);
			if (!file.exists()) {
				file.createNewFile();
			}
			String context = "ovs-vsctl add-br " + customOvsBr;
			writer = new FileWriter(shPath, false);
			writer.write(context);
		} catch (Exception ex) {
			log.error("创建网络启动脚本失败", ex);
			throw new GCloudException("::创建网络启动脚本失败");
		} finally {
			try {
				if (writer != null) {
					writer.close();
				}
			} catch (Exception e) {
				log.error("流关闭失败", e);
			}
		}
	}

	public static Document readXml(String path) {

		File file = new File(path);
		return readXml(file);
	}

	public static Document readXml(File file) {
		SAXReader reader = new SAXReader();
		Document doc = null;
		try {
			doc = reader.read(file);
		} catch (Exception ex) {
			throw new GCloudException("::读取xml文件失败");
		}

		return doc;
	}

	public static Integer getFtState(String instanceXmlPath) {
		try {
			Element faultTolreant = ConfigFileUtil.findElementForXml(instanceXmlPath, "/instance/faultTolreant", null, null);
			if (faultTolreant == null) {
				return FtState.NO_FT.getValue();
			} else {
				Attribute att = faultTolreant.attribute("state");
				if (att != null) {
					return Integer.parseInt(att.getValue());
				}
			}
		} catch (GCloudException e) {
			log.error("获取是否容错配置错误", e);
		}
		return FtState.NO_FT.getValue();
	}


    public static void addInstanceXmlVolumeInfo(Document doc, VmVolumeDetail vmDisk){

        List<XmlElement> diskChildren = new ArrayList<XmlElement>();
        String volumeId = vmDisk.getVolumeId() == null ? "" : vmDisk.getVolumeId();
        String volumePath = vmDisk.getSourcePath() == null ? "" : vmDisk.getSourcePath();
        String targetDev = vmDisk.getTargetDev() == null ? "" : vmDisk.getTargetDev();
        String storageType = vmDisk.getStorageType() == null ? "" : vmDisk.getStorageType();
        String protocol = vmDisk.getProtocol() == null ? "" : vmDisk.getProtocol();

        Map<String, String> diskAttr = new HashMap<String, String>();
        diskAttr.put("id", volumeId);

        XmlElement volIdE = new XmlElement("volumeId", volumeId, null, null);
        XmlElement diskPathE = new XmlElement("volumePath", volumePath, null, null);
        XmlElement devE = new XmlElement("targetDev", targetDev, null, null);
        XmlElement storageTypeE = new XmlElement("storageType", storageType, null, null);
        XmlElement protocolE = new XmlElement("protocol", protocol, null, null);


        diskChildren.add(volIdE);
        diskChildren.add(diskPathE);
        diskChildren.add(devE);
        diskChildren.add(storageTypeE);
        diskChildren.add(protocolE);


        XmlElement diskE = new XmlElement("volume", null, diskAttr, diskChildren);

        ConfigFileUtil.addElementForXml(doc, ConfigFileUtil.INSTANCE_XML_ROOT, null, null, diskE);

    }

    public static void addInstanceXmlVolumeInfoAndSave(String filePath, VmVolumeDetail volumeDetail){
        Document doc = readXml(filePath);
        addInstanceXmlVolumeInfo(doc, volumeDetail);
        doc2XmlFile(doc, filePath);
    }
}
