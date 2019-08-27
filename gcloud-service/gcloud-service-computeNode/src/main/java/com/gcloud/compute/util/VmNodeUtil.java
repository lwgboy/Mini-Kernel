package com.gcloud.compute.util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.gcloud.common.util.FileUtil;
import com.gcloud.common.util.StringUtils;
import com.gcloud.common.util.SystemUtil;
import com.gcloud.compute.prop.ComputeNodeProp;
import com.gcloud.compute.virtual.IVmVirtual;
import com.gcloud.core.exception.GCloudException;
import com.gcloud.core.service.SpringUtil;
import com.gcloud.header.common.Host;
import com.gcloud.header.compute.enums.CloudPlatform;
import com.gcloud.header.compute.msg.node.vm.model.VmDetail;
import com.gcloud.header.storage.model.VmVolumeDetail;
import com.gcloud.service.common.compute.model.CpuTopology;
import com.gcloud.service.common.compute.model.DomainDetail;
import com.gcloud.service.common.compute.model.DomainInfo;
import com.gcloud.service.common.compute.model.DomainInterface;
import com.gcloud.service.common.compute.uitls.VmUtil;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/*
 * @Date 2015-4-16
 *
 * @Author zhangzj@g-cloud.com.cn
 *
 * @Copyright 2015 www.g-cloud.com.cn Inc. All rights reserved.
 *
 * @Description 获取虚拟机相关属性
 */
@Slf4j
public class VmNodeUtil {

	private static final String LIBVERT_XML_NAME = "libvirt.xml";
	private static final String LIBVERT_XML_TEMP_NAME = "libvirt-temp.xml";
	private static final String INSTANCE_XML_NAME = "instance.xml";
	private static final String AUTOSTART_SH = "autostart.sh";

	public static void startTimes(String instanceId, int times) throws GCloudException {
		_startTimes(instanceId, times, 5000l, false);
	}

	public static void startDesktopTimes(String instanceId, int times) throws GCloudException {
		_startTimes(instanceId, times, 2000l, true);
	}

	private static void _startTimes(String instanceId, int times, long sleepTime, boolean isDesktop) throws GCloudException {
		// 临时解决，重试几次
		// 开机失败
		// error: error from service: CreateMachine: Did not receive a reply.
		// Possible causes include: the remote application did not send a reply,
		// the message bus security policy blocked the reply, the reply timeout
		// expired, or the network connection was broken.
		// libvirt: error : libvirtd quit during handshake: Input/output error

		IVmVirtual virtual = SpringUtil.getBean(IVmVirtual.class);
		for (int i = 1; i <= times; i++) {
			try {
				// 开机
				if (isDesktop) {
					virtual.startDesktop(instanceId);
				} else {
					virtual.start(instanceId);
				}
				break;
			} catch (Exception exx) {
				log.error("start fail", exx);
				if (i == times) {
					if (exx instanceof GCloudException) {
						throw (GCloudException) exx;
					} else {
						throw new GCloudException("::开机失败");
					}

				}
			}
			if (i < times) {
				try {
					Thread.sleep(sleepTime);
				} catch (Exception exb) {
					log.error("sleep fail", exb);
				}
			}
		}
	}

	/**
	 * 如果没有制定cpu配置，则使用默认配置进行配置
	 *
	 * @param vcpu
	 * @param cpuSocket
	 * @param cpuCore
	 * @param cpuThread
	 * @return
	 * @throws GCloudException
	 */
	public static CpuTopology getCpuTopology(Integer vcpu, Integer cpuSocket, Integer cpuCore, Integer cpuThread) throws GCloudException {

		return getCpuTopology(vcpu, cpuSocket, cpuCore, cpuThread, null);

	}

	/**
	 * 如果没有制定cpu配置，则使用默认配置进行配置
	 *
	 * @param vcpu
	 * @return
	 * @throws GCloudException
	 */
	public static CpuTopology getCpuTopology(Integer vcpu) throws GCloudException {

		return getCpuTopology(vcpu, null, null, null, null);

	}

	/**
	 * 如果没有制定cpu配置，则使用传入的topology进行配置
	 *
	 * @param vcpu
	 * @param cpuSocket
	 * @param cpuCore
	 * @param cpuThread
	 * @param topologyConfig
	 * @return
	 * @throws GCloudException
	 */
	public static CpuTopology getCpuTopology(Integer vcpu, Integer cpuSocket, Integer cpuCore, Integer cpuThread, String topologyConfig) throws GCloudException {

		CpuTopology result = null;

		if (cpuSocket == null || cpuSocket <= 0 || cpuCore == null || cpuCore <= 0 || cpuThread == null || cpuThread <= 0) {
			result = VmNodeUtil.getCpuTopology(vcpu, topologyConfig);
		} else {
			result = new CpuTopology();
			result.setSocket(cpuSocket);
			result.setCore(cpuCore);
			result.setThread(cpuThread);

		}
		return result;

	}

	/**
	 * 根据vcpu 和 topologyConfig获取cpu配置 如果 vcpu 除以 cpu配置中的非通配符项，则vcpu取代通配符项，其他项变为1
	 * 会根据计算节点的平台配置设置默认的topologyConfig
	 *
	 * @param vcpu
	 * @param topologyConfig
	 * @return
	 * @throws GCloudException
	 */
	public static CpuTopology getCpuTopology(Integer vcpu, String topologyConfig) throws GCloudException {
		ComputeNodeProp computeNodeProp = (ComputeNodeProp) SpringUtil.getBean("computeNodeProp");
		if (vcpu == null || vcpu < 0) {
			throw new GCloudException("compute_node_base_010006::vcpu should larger than 0");
		}

		String plat = computeNodeProp.getCloudPlatform();
		CloudPlatform platform = CloudPlatform.getPlatformByValue(plat);

		if (StringUtils.isBlank(topologyConfig)) {

			if (platform == null) {
				log.error("topologyConfig and platform can not be null at the same time");
				throw new GCloudException("compute_node_base_010007::topologyConfig and platform can not be null at the same time");
			}

			switch (platform) {
			case DESKTOPGCLOUD:
				topologyConfig = computeNodeProp.getDesktopVmCpuTopologyConfig();
				break;
			case IDCSEVER:
				topologyConfig = computeNodeProp.getVmCpuTopologyConfig();
				break;
			default:
				break;
			}
		}

		return countCpuTopology(vcpu, topologyConfig);

	}

	/**
	 * 根据vcpu 和 topologyConfig获取cpu配置 如果 vcpu 除以 cpu配置中的非通配符项，则vcpu取代通配符项，其他项变为1
	 *
	 * @param vcpu
	 * @param topologyConfig
	 * @return
	 * @throws GCloudException
	 */
	public static CpuTopology countCpuTopology(int vcpu, String topologyConfig) throws GCloudException {

		if (vcpu <= 0) {
			throw new GCloudException("compute_node_base_010008::vcpu should larger than 0");
		}

		if (StringUtils.isBlank(topologyConfig)) {
			log.error("cpu topology can not be null. topologyConfig=" + topologyConfig);
			throw new GCloudException("compute_node_base_010009::cpu topology can not be null");
		}

		String[] topoArr = topologyConfig.split(",");
		if (topoArr == null || topoArr.length != 3) {
			log.error("topology config format error. topologyConfig=" + topologyConfig);
			throw new GCloudException("compute_node_base_010010::topology config format error");
		}

		Integer cpuSocket = null;
		Integer cpuCore = null;
		Integer cpuThread = null;

		// 除了*以外的相乘
		int extTotal = 0;
		int wildcardNum = 0;

		for (int i = 0; i < topoArr.length; i++) {

			String topoValue = topoArr[i];
			Integer cpuTmp = null;
			if ("*".equals(topoValue)) {
				cpuTmp = 0;
			} else if ("0".equals(topoValue)) {
				log.error("topology config can not be 0. topologyConfig=" + topologyConfig);
				throw new GCloudException("compute_node_base_010013::topology config can not be 0");
			} else {
				try {
					cpuTmp = Integer.valueOf(topoValue);
				} catch (Exception tex) {
					log.error("topology config shuold be integer. topologyConfig=" + topologyConfig, tex);
					throw new GCloudException("compute_node_base_010011::topology config shuold be integer");
				}
			}

			if (cpuTmp == 0) {
				wildcardNum++;
				// 只能有一个*
				if (wildcardNum > 1) {
					log.error("there can only be one wildcard. topologyConfig=" + topologyConfig);
					throw new GCloudException("compute_node_base_010012::there can only be one wildcard");
				}
			} else {
				extTotal = extTotal == 0 ? cpuTmp : extTotal * cpuTmp;
			}

			switch (i) {

			case 0:
				cpuSocket = cpuTmp;
				break;
			case 1:
				cpuCore = cpuTmp;
				break;
			case 2:
				cpuThread = cpuTmp;
				break;
			default:
				break;

			}

		}

		int modResult = vcpu % extTotal;

		// 不为0，则默认吧 * 变成vcpu，其他为1
		if (modResult != 0) {
			cpuSocket = cpuSocket == 0 ? vcpu : 1;
			cpuCore = cpuCore == 0 ? vcpu : 1;
			cpuThread = cpuThread == 0 ? vcpu : 1;
		} else {
			int remainderNum = vcpu / extTotal;
			cpuSocket = cpuSocket == 0 ? remainderNum : cpuSocket;
			cpuCore = cpuCore == 0 ? remainderNum : cpuCore;
			cpuThread = cpuThread == 0 ? remainderNum : cpuThread;
		}
		CpuTopology result = new CpuTopology();
		result.setSocket(cpuSocket);
		result.setCore(cpuCore);
		result.setThread(cpuThread);

		return result;

	}

	/**
	 * @param instanceId
	 * @return
	 * @Title: getInstanceDiskPath
	 * @Description: 获取云服务器磁盘路径
	 * @date 2015-5-22 上午9:03:42
	 */
	public static String getInstanceDiskPath(String instanceId) {
		ComputeNodeProp computeNodeProp = (ComputeNodeProp) SpringUtil.getBean("computeNodeProp");
		return computeNodeProp.getInstanceDiskPath() + "/" + instanceId;
	}

	/**
	 * @param instanceId
	 * @return
	 * @Title: getInstancePath
	 * @Description: 获取虚拟机配置文件路径
	 * @date 2015-5-18 上午9:10:16
	 */
	public static String getInstanceConfigPath(String nodeIp, String instanceId) {
		ComputeNodeProp computeNodeProp = (ComputeNodeProp) SpringUtil.getBean("computeNodeProp");
		return VmUtil.getInstanceConfigPath(computeNodeProp.getInstanceConfigPath(), nodeIp, instanceId);
	}

	/**
	 * @param userId
	 * @return
	 * @Title: getInstancePath
	 * @Description: 获取虚拟机配置文件路径
	 * @date 2015-5-18 上午9:10:16
	 */
	public static String getNodeConfigPath(String nodeIp) {
		ComputeNodeProp computeNodeProp = SpringUtil.getBean(ComputeNodeProp.class);
		String instancePath = String.format("%s/%s", computeNodeProp.getInstanceConfigPath(), nodeIp);
		return instancePath;
	}

	/**
	 * @return
	 * @Title: getLibvirtTempletPath
	 * @Description: 获取libvirt模版文件位置
	 * @date 2015-5-18 上午9:09:20
	 */
	public static String getLibvirtTempletPath() {
		ComputeNodeProp computeNodeProp = SpringUtil.getBean(ComputeNodeProp.class);
		String libvirtTemplatePath = "";
		if (CloudPlatform.DESKTOPGCLOUD.getValue().equals(computeNodeProp.getCloudPlatform())) {
			libvirtTemplatePath = computeNodeProp.getConfigurePath() + "/libvirt/desktopcloud-" + computeNodeProp.getHypervisor() + "-libvirt.xml";
		} else {
			libvirtTemplatePath = computeNodeProp.getConfigurePath() + "/libvirt/" + computeNodeProp.getHypervisor() + "-libvirt.xml";
		}

		return libvirtTemplatePath;

	}

	/**
	 * @param instanceId
	 * @return
	 * @Title: getLibvirtXmlTempPath
	 * @Description: 获取libvirt配置文件临时路径
	 * @date 2015-5-18 上午9:11:02
	 */
	public static String getLibvirtXmlTempPath(String nodeIp, String instanceId) {

		String cfgPath = getInstanceConfigPath(nodeIp, instanceId);
		String libvirtPath = FileUtil.getDirectoryString(cfgPath) + LIBVERT_XML_TEMP_NAME;
		return libvirtPath;

	}

	/**
	 * @param instanceId
	 * @return
	 * @Title: getLibvirtXmlPath
	 * @Description: 获取libvirt配置文件路径
	 * @date 2015-5-18 上午9:11:02
	 */
	public static String getLibvirtXmlPath(String nodeIp, String instanceId) {

		String cfgPath = getInstanceConfigPath(nodeIp, instanceId);
		String libvirtPath = FileUtil.getDirectoryString(cfgPath) + LIBVERT_XML_NAME;
		return libvirtPath;

	}

	/**
	 * @param cfgPath
	 * @return
	 * @Title: getLibvirtXmlPath
	 * @Description: 根据虚拟机路径获取libvirt.xml配置文件位置
	 * @date 2015-5-18 上午9:22:25
	 */
	public static String getLibvirtXmlPath(String cfgPath) {
		String libvirtPath = FileUtil.getDirectoryString(cfgPath) + LIBVERT_XML_NAME;
		return libvirtPath;
	}

	/**
	 * @param instanceId
	 * @return
	 * @Title: getInstanceXmlPath
	 * @Description: 获取instance.xml的路径
	 * @date 2015-5-18 上午9:15:00
	 */
	public static String getInstanceXmlPath(String nodeIp, String instanceId) {
		String cfgPath = getInstanceConfigPath(nodeIp, instanceId);
		String path = FileUtil.getDirectoryString(cfgPath) + INSTANCE_XML_NAME;
		return path;
	}

	public static String getDataDiskXmlPath(String nodeIp, String instanceId, String volumeId) throws GCloudException {
		String cfgPath = getInstanceConfigPath(nodeIp, instanceId);
		String path = String.format("%s/volume_%s.xml", cfgPath, volumeId);
		return path;
	}

	/**
	 * @return
	 * @Title: getInstanceXmlPath
	 * @Description: 根据虚拟机路径获取instance.xml配置文件位置
	 * @date 2015-5-18 上午9:23:43
	 */
	public static String getInstanceXmlPath(String cfgPath) {
		String path = FileUtil.getDirectoryString(cfgPath) + INSTANCE_XML_NAME;
		return path;
	}

	public static void deleteDirectoryNoException(String insDiskPath) {
		File diskDirectory = new File(insDiskPath);
		if (diskDirectory.exists()) {
			try {
				VmUtil.rmFile(insDiskPath);
			} catch (Exception be) {
				log.error("1010504::删除配置文件失败", be);
			}

		}
	}

	public static void rebootTimes(String instanceId, int times) throws GCloudException {
		// 临时解决，重试几次
		// 开机失败
		// error: error from service: CreateMachine: Did not receive a reply.
		// Possible causes include: the remote application did not send a reply,
		// the message bus security policy blocked the reply, the reply timeout
		// expired, or the network connection was broken.
		// libvirt: error : libvirtd quit during handshake: Input/output error

		IVmVirtual virtual = SpringUtil.getBean(IVmVirtual.class);

		for (int i = 0; i < times; i++) {

			if (i == times - 1) {
				virtual.reboot(instanceId);
			} else {
				try {
					// 开机
					virtual.reboot(instanceId);
					break;
				} catch (Exception exx) {
					log.error("reboot fail", exx);
				}

				try {
					Thread.sleep(5000L);
				} catch (Exception exb) {
					log.error("sleep fail", exb);
				}
			}

		}

	}

	/**
	 * @param instanceId
	 * @param portId
	 * @return
	 * @Title: getNetworkXmlPath
	 * @Description: 获取网络信息xml路径
	 * @date 2015-6-13 下午3:56:21
	 */
	public static String getNetworkXmlPath(String nodeIp, String instanceId, String portId) {

		String cfgPath = getInstanceConfigPath(nodeIp, instanceId);
		String networkXmlPath = FileUtil.getDirectoryString(cfgPath) + portId + ".xml";
		return networkXmlPath;

	}

	/**
	 * @param instanceId
	 * @param portId
	 * @return
	 * @Title: getNetStartPath
	 * @Description: 获取启动网络脚本路径
	 * @date 2015-6-13 下午3:56:33
	 */
	public static String getNetStartPath(String nodeIp, String instanceId, String portId) {

		String cfgPath = getInstanceConfigPath(nodeIp, instanceId);
		String shPath = FileUtil.getDirectoryString(cfgPath) + "start_net_" + portId + ".sh";
		return shPath;
	}

	public static boolean isVmExist(String instanceId) {
		IVmVirtual virtual = SpringUtil.getBean(IVmVirtual.class);
		String info = virtual.domInfo(instanceId);
		return info != null;
	}

	public static DomainInfo domInfo(String instanceId){
        IVmVirtual virtual = SpringUtil.getBean(IVmVirtual.class);
        DomainInfo info = null;
        String infoStr = virtual.domInfo(instanceId);
        if(StringUtils.isNotBlank(infoStr)){

            String[] infoArr = infoStr.split("\n");
            if(infoArr == null || infoArr.length == 0){
                return null;
            }

            info = new DomainInfo();
            info.setInstanceId(instanceId);
            for(String attr : infoArr){
                String[] attrArr = attr.split(":");
                if(attrArr != null && attrArr.length > 1){

                    String attrKey = attrArr[0].trim();
                    String attrVal = attrArr[1].trim();
                    if("Persistent".equals(attrKey)){
                        info.setPersistent("yes".equals(attrVal));
                    }else if("State".equals(attrKey)){
                        info.setState(attrVal);
                        info.setGcState(virtual.changeStateForGcloud(attrVal));
                    }else if("Autostart".equals(attrKey)){
                        info.setAutostart("enable".equals(attrVal));
                    }

                }
            }
        }
        return info;
    }

	//检查云服务是否存在，后续再决定是否增加判断配置文件，如果配置文件存在就define
	public static DomainInfo checkVm(String instanceId){
	    return domInfo(instanceId);
    }

	public static boolean isInterfaceExistByMacAddress(DomainDetail detail, String macAddress) {

		boolean isExist = false;
		List<DomainInterface> domInterfaces = detail.getDomainInterfaces();
		if (domInterfaces != null && domInterfaces.size() > 0) {
			for (DomainInterface domInterface : domInterfaces) {

				if (domInterface.getMacAddress() != null && domInterface.getMacAddress().equals(macAddress)) {
					isExist = true;
					break;
				}
			}
		}
		return isExist;
	}

	public static void redefineVm(String instanceId, String libvirtPath) {

		IVmVirtual virtual = SpringUtil.getBean(IVmVirtual.class);
		DomainDetail domDetail = virtual.getVmDetail(instanceId);
		if (domDetail.getIsPersistent() != null && domDetail.getIsPersistent()) {
			virtual.undefine(instanceId);
		}
		virtual.define(libvirtPath);

	}

	public static void attachTimes(String instanceId, String diskAttachXmlPath, int times) throws GCloudException {
		// 临时解决，桌面云挂载报错
		// error: internal error: unable to execute QEMU command 'device_add':
		// Device initialization failed.
		IVmVirtual virtual = SpringUtil.getBean(IVmVirtual.class);
		for (int i = 0; i < times; i++) {
			if (i == times - 1) {
				virtual.attachDevice(instanceId, diskAttachXmlPath);
			} else {
				try {
					// 开机
					virtual.attachDevice(instanceId, diskAttachXmlPath);
					break;
				} catch (Exception exx) {
					log.error("挂载失败，第" + (i + 1) + "次", exx);
				}

				try {
					Thread.sleep(1000L);
				} catch (Exception exb) {
					log.error("sleep fail", exb);
				}
			}

		}

	}

	public static VmVolumeDetail getVolumeDetailByVolumeId(VmDetail vmIns, String volumeId) {
		VmVolumeDetail vmDiskDetail = new VmVolumeDetail();
		List<VmVolumeDetail> disks = vmIns.getVmDisks();
		for (VmVolumeDetail disk : disks) {

			if (disk.getVolumeId() != null && disk.getVolumeId().equals(volumeId)) {
				vmDiskDetail = disk;
			}
		}
		return vmDiskDetail;
	}

	/**
	 * @Title: getUserId
	 * @Description: 根据云服务器配置文件路径获取用户id
	 * @date 2015-5-29 下午2:24:27
	 *
	 * @param path
	 * @param instanceId
	 * @return
	 */
	public static String getUserId(String path, String nodeIp, String instanceId) {
		ComputeNodeProp prop = SpringUtil.getBean(ComputeNodeProp.class);
		return path.substring(prop.getInstanceConfigPath().length() + 1 + nodeIp.length() + 1,
				path.indexOf("/" + instanceId));
	}

	/**
	 * @Title: getAutostartShPath
	 * @Description: 获取autostart.sh路径
	 * @date 2015-7-1 上午10:55:23
	 *
	 * @param instanceId
	 * @return
	 */
	public static String getAutostartShPath(String nodeIp, String instanceId) {
		String cfgPath = getInstanceConfigPath(nodeIp, instanceId);
		String path = FileUtil.getDirectoryString(cfgPath) + AUTOSTART_SH;
		return path;
	}

	public static List<Host> getCephMon(){

		List<Host> hosts = null;

		String[] cmd = {"ceph", "mon_status"};
		String ret = SystemUtil.runAndGetValue(cmd);
		if(StringUtils.isNotBlank(ret)){
			try{
				JSONObject jsonObject = JSON.parseObject(ret);
				JSONObject monmap = jsonObject.getJSONObject("monmap");
				JSONArray mons = monmap.getJSONArray("mons");
				//解析成功，才实例化
				hosts = new ArrayList<>();
				if(mons != null && mons.size() > 0){
					for(int i = 0; i < mons.size(); i++){
						JSONObject mon = mons.getJSONObject(i);
						String addr = mon.getString("addr");
						String[] addrArr = addr.split(":");
						Host host = new Host();
						if(addrArr.length > 1){
							host.setHostname(addrArr[0]);
							String portMask = addrArr[1];
							String[] portMaskArr = portMask.split("/");
							host.setPort(portMaskArr[0]);
							hosts.add(host);
						}
					}
				}
			}catch (Exception ex){
				log.error("解析失败, ret=" + ret + ", ex=" + ex, ex);
			}

		}

		return hosts;
	}
}
