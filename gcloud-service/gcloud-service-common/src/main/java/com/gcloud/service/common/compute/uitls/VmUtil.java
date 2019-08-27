/*
 * @Date 2015-5-20
 * 
 * @Author chenyu1@g-cloud.com.cn
 * 
 * @Copyright 2015 www.g-cloud.com.cn Inc. All rights reserved.
 * 
 * @Description TODO
 */
package com.gcloud.service.common.compute.uitls;

import com.gcloud.common.util.SystemUtil;
import com.gcloud.core.exception.GCloudException;
import com.gcloud.header.compute.enums.VmState;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;


/*
 * @Date 2016-8-15
 * 
 * @Author zhangzj@g-cloud.com.cn
 * 
 * @Copyright 2016 www.g-cloud.com.cn Inc. All rights reserved.
 * 
 * @Description TODO
 */
@Slf4j
public class VmUtil {
	
	public static boolean isStateException(String state) {
		
		boolean isStateEx = false;
		
		if (state.equals(VmState.CRASHED.value())
				|| state.equals(VmState.PAUSED.value())
				|| !isInVmState(state)) {
			isStateEx = true;
		} 
		
		return isStateEx;
		
	}
	
	/**
	  * @Title: isInVmState
	  * @Description: 判断是否在已知的状态中
	  * @date 2015-5-25 上午11:47:45
	  *
	  * @param state
	  * @return    
	  */
	public static boolean isInVmState(String state){
		
		boolean isIn = false;
		for(VmState vmS : VmState.values()){
			
			if(vmS.value().equals(state)){
				isIn = true;
				break;
			}
			
		}
		
		return isIn;
	}
	
	 /**
     * @Title: checkPath
     * @Description: 检查云服务器磁盘文件目录，如果没有则创建
     * @date 2015-5-20 下午3:09:32
     *    
     */
	public static void checkPath(String path) throws GCloudException {
		try {
			File file = new File(path);
			if (!file.exists()) {
				file.mkdirs();
			}
		} catch (Exception e) {
			log.error("1010502::fail to check instance config path", e);
			throw new GCloudException("1010502::fail to check instance config path");
		}
	}
   
   /**
	 * 获取 配置文件中参数 的属性值，如果没有配置或者参数配置值没有设置，返回值默认值
	 * @param prop 配置属性集
	 * @param key 属性key
	 * @param defaultStr 默认值
	 * @return value
	 */
	public static String getProperty(Properties prop, String key, String defaultStr) {
		String value = prop.getProperty(key);
		if (StringUtils.isBlank(value)) {
			return defaultStr;
		}

		else {
			return value.trim();
		}
	}

	//=================网卡相关
	
	/**
	  * @Title: getBrName
	  * @Description: 获取网桥名称
	  * @date 2015-6-13 下午3:52:33
	  *
	  * @param sufId
	  * @return    
	  */
	public static String getBrName(String sufId){
		return "br" + sufId;
	}
	
	/**
	  * @Title: getPreName
	  * @Description: 获取pair br端的名称
	  * @date 2015-6-13 下午3:53:27
	  *
	  * @param sufId
	  * @return    
	  */
	public static String getPreName(String sufId){
		return "pre" + sufId;
	}
	
	/**
	  * @Title: getAftName
	  * @Description: 获取pair ovs端名称
	  * @date 2015-6-13 下午3:53:45
	  *
	  * @param sufId
	  * @return    
	  */
	public static String getAftName(String sufId){
		return "aft" + sufId;
	}
	//=================网卡相关
	
	public static String getHostName() throws GCloudException {
		boolean isWin = System.getProperty("os.name").toLowerCase().indexOf("windows") < 0 ? false : true;
		if (isWin) {
			log.error("获取本机hostName错误,非linux系统");
			return null;
		} else {
			String[] cmd = new String[]{ "hostname"};
			try {
				String hostName = SystemUtil.runAndGetValue(cmd);
				log.debug("获取本机hostName" + hostName);
				return hostName.replace("\n", "");
			} catch (Exception e) {
				log.error("获取本机hostName错误", e);
				throw new GCloudException("::获取本机host出错");
			}
		}
	}
	/*
	public static Integer getImageSize(String imageId) throws GCloudException{

        Image imageInfo = ImageRestApi.details(imageId,
                "compute_node_vm_341003::fail to get image info");
        if (imageInfo == null) {
            throw new GCloudException(
                    "compute_node_vm_341004::can not find the image");
        }
        Long imSize = imageInfo.getSize();
        Integer imageSize = (int) Math.ceil(imSize / 1024.0 / 1024.0 / 1024.0);
        
        if (imageSize == null || imageSize == 0) {
            throw new GCloudException(
                    "compute_node_vm_341005::size of image must larger than 0");
        }
        return imageSize;
	}
	*/
	/**
	  * @Title: rmFile
	  * @Description: 用rm -rf 删除文件
	  * @date 2015-7-2 下午4:58:02
	  *
	  * @param filePath
	  */
	public static void rmFile(String filePath) throws GCloudException{

		if(StringUtils.isBlank(filePath)){
			log.error("::file path is null");
			throw new GCloudException("::file path is null");
		}

		String[] cmd = new String[]{"rm", filePath, "-rf"};
		int res =  SystemUtil.runAndGetCode(cmd);

		LogUtil.handleLog(cmd, res, "::remove file error");
		
	}
	
	/*
	public static boolean isVolumeAttach(String volumeId, String hostName, String attachPath) throws GCloudException {

		Volume volume = VolumeRestApi.detail(volumeId, errorCode);
		boolean isAttach = VmUtil.isVolumeAttach(volume, hostName, attachPath, errorCode);

		return isAttach;

	}
	
	public static boolean isVolumeAttach(Volume volume, String hostName, String attachPath) throws GCloudException {

		List<VolumeAttachment> attachs = (List<VolumeAttachment>) volume.getAttachments();
		
		boolean isAttach = false;
		if (attachs != null && attachs.size() > 0) {

			VolumeAttachment volAtt = attachs.get(0);
			if (volAtt != null) {
				String device = volAtt.getDevice();
				if (!StringUtils.isBlank(device)) {
					String[] devices = device.split(";");
					for (String dev : devices) {
						
						if(attachPath == null){
							
							String bindedHost = "";
							String[] devInfo = dev.split("@");
							if(devInfo.length > 0){
								bindedHost = devInfo[0];
							}
							if(!StringUtils.isBlank(bindedHost) && bindedHost.equals(hostName)){
								isAttach = true;
								break;
							}
							
						}else{
							String attStr = VmUtil.getVolumeAttach(hostName, attachPath);
							if (!StringUtils.isBlank(dev) && dev.equals(attStr)) {
								isAttach = true;
								break;
							}
						}
						
					}
				}
			}
		}

		return isAttach;

	}

	public static String getVolumeAttach(String hostName, String attachPath) {

		return String.format("%s@%s", hostName, attachPath);

	}
	
	public static String getIpByPort(Port port){
		
		String ip = "";
		Set<IP> ipSet = (Set<IP>) port.getFixedIps();
		for (IP nIp : ipSet) {
			ip = nIp.getIpAddress();
			break;
		}
		return ip;
		
	}
	
	public static List<String> getAttachHost(Volume volume){
		List<String> hosts = new ArrayList<String>();
		List<VolumeAttachment> attachs = (List<VolumeAttachment>) volume.getAttachments();
		if(attachs != null && attachs.size() > 0){
		
			VolumeAttachment volAtt = attachs.get(0);
			if (volAtt != null) {
				String device = volAtt.getDevice();
				if (!StringUtils.isBlank(device)) {
					String[] devices = device.split(";");
					for (String dev : devices) {
						
						String bindedHost = "";
						String[] devInfo = dev.split("@");
						if(devInfo.length > 0){
							bindedHost = devInfo[0];
							hosts.add(bindedHost);
						}
					}
				}
			}
			
		}
		
		return hosts;
	}
	*/
	public static String dateToString(Date date, String format){
		
		String dateStr = "";

		try{
			
			if(date != null){
				SimpleDateFormat dateFormat = new SimpleDateFormat(format);
				dateStr = dateFormat.format(date);
			}
			
		}catch(Exception ex){
			log.error("时间转换失败", ex);
		}
		
		return dateStr;
	}
	/*
	public static void forceDeletePort(String portId){
		
		try {
			Port backPort = Builders.port().id(portId).deviceId("").deviceOwner("").bindingHostId("").usedStatus(UsedStatus.UNSED).build();
			NeutronRestApi.updatePort(backPort, "删除网卡失败");
		} catch (Exception be) {
			log.error("删除前更新网卡网卡失败", be);
		}
		try {
			NeutronRestApi.deltePort(portId, "删除网卡失败");
		} catch (Exception be) {
			log.error("删除网卡失败", be);
		}
		
	}
	*/
	public static String getVolumeHostname(String volumeHost){
		
		String hostName = null;
		if(!StringUtils.isBlank(volumeHost)){
			String[] hostArr = volumeHost.split("@");
			hostName = hostArr[0];
		}
		return hostName;
		
	}
	
	public static double getNowLoadAverage(String title) {
		double result = 0.0;
		try {
			String[] topCmd = new String[] { "uptime" };
			String topRes = SystemUtil.run(topCmd);
			String[] res = topRes.split("load average: ");
			if (res != null && res.length > 1) {
				if (res[1] != null) {
					String[] load = res[1].split(",");
					result = Double.parseDouble(load[0].replace(" ", ""));
				}
			}
		} catch (Exception e) {
			log.error(title, e);
			result = 0.0;
		}
		return result;
	}
	
	/**
	 * 
	  * @Title: getProcessCpuUsed
	  * @Description: TODO
	  * @date 2016-8-15 上午9:34:12
	  *
	  * @param key
	  * @return  eg:20% 返回 20
	 */
	public static double getProcessCpuUsed(String key) {
		double result = 0.0;
		try {
			String[] topCmd = new String[] { "bash" , "-c", String.format("ps aux|grep -v ^'USER\\|grep'|sort -rn -k3|grep %s | awk '{print $3}' | awk '{sum+=$1}END{print sum}'", key)};
			String topRes = SystemUtil.run(topCmd);
			if (StringUtils.isNotBlank(topRes)) {
				result = Double.parseDouble(topRes);
			}
		} catch (Exception e) {
			log.error("获取进程cpu使用率失败", e);
			result = 0.0;
		}
		return result;
	}
	
	/**
	 * 
	  * @Title: getProcessMemoryUsed
	  * @Description: TODO
	  * @date 2016-8-15 上午9:33:42
	  *
	  * @param key
	  * @return  单位kb
	 */
	public static double getProcessMemoryUsed(String key) {
		double result = 0.0;
		try {
			String[] topCmd = new String[] { "bash" , "-c", String.format("ps aux|grep -v ^'USER\\|grep'|sort -rn -k3|grep %s | awk '{print $6}' | awk '{sum+=$1}END{print sum}'", key)};
			String topRes = SystemUtil.run(topCmd);
			if (StringUtils.isNotBlank(topRes)) {
				result = Double.parseDouble(topRes);
			}
		} catch (Exception e) {
			log.error("获取进程memory使用率失败", e);
			result = 0.0;
		}
		return result;
	}
	
	public static void checkCpu(Integer core, Integer cpuCore, Integer cpuThread, Integer cpuSocket) throws GCloudException{
		// 对cpu类型进行判断。
		if (cpuCore != null && cpuSocket != null && cpuThread != null) {
			if (cpuCore <= 0 || cpuSocket <= 0 || cpuThread <= 0) {
				throw new GCloudException("compute_controller_vm_011045::cpu and cpu topology config can not be null");
			}
			if (core != (cpuCore * cpuSocket * cpuThread)) {
				throw new GCloudException("compute_controller_vm_011047::vcpu num should equeal with socket * core * thread");
			}
		} else if (!(cpuCore == null && cpuSocket == null && cpuThread == null)) {
			throw new GCloudException("compute_controller_vm_011046::cpu and cpu topology config can not be null");
		}
	}
	
	/**
	 * 根据vcpu 和 topologyConfig获取cpu配置
	 * 如果 vcpu 除以 cpu配置中的非通配符项，则vcpu取代通配符项，其他项变为1
	 * @param vcpu
	 * @param topologyConfig
	 * @return
	 * @throws GCloudException
	 */
	/*
	public static CpuTopology getCpuTopology(int vcpu, String topologyConfig) throws GCloudException{
		
		if(vcpu <= 0){
			throw new GCloudException("compute_node_base_010008::vcpu should larger than 0");
		}
		
		if(StringUtils.isBlank(topologyConfig)){
			log.error("cpu topology can not be null. topologyConfig=" + topologyConfig);
			throw new GCloudException("compute_node_base_010009::cpu topology can not be null");
		}
		
		String[] topoArr = topologyConfig.split(",");
		if(topoArr == null || topoArr.length != 3){
			log.error("topology config format error. topologyConfig=" + topologyConfig);
			throw new GCloudException("compute_node_base_010010::topology config format error");
		}
		
		Integer cpuSocket = null;
		Integer cpuCore = null;
		Integer cpuThread = null;
		
		//除了*以外的相乘
		int extTotal = 0;
		int wildcardNum = 0;
		
		for(int i = 0; i < topoArr.length; i ++){
			
			String topoValue = topoArr[i];
			Integer cpuTmp = null;
			if("*".equals(topoValue)){
				cpuTmp = 0;
			}else if("0".equals(topoValue)){
				log.error("topology config can not be 0. topologyConfig=" + topologyConfig);
				throw new GCloudException("compute_node_base_010013::topology config can not be 0");
			}else{
				try{
					cpuTmp = Integer.valueOf(topoValue);
				}catch(Exception tex){
					log.error("topology config shuold be integer. topologyConfig=" + topologyConfig, tex);
					throw new GCloudException("compute_node_base_010011::topology config shuold be integer");
				}
			}

			if(cpuTmp == 0){
				wildcardNum ++;
				//只能有一个*
				if(wildcardNum > 1){
					log.error("there can only be one wildcard. topologyConfig=" + topologyConfig);
					throw new GCloudException("compute_node_base_010012::there can only be one wildcard");
				}
			}else{
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
		
		//不为0，则默认吧 * 变成vcpu，其他为1
		if(modResult != 0){
			cpuSocket = cpuSocket == 0 ? vcpu : 1;
			cpuCore = cpuCore == 0 ? vcpu : 1;
			cpuThread = cpuThread == 0 ? vcpu : 1;
		}else{
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
	*/

	/**
	 * @param instanceId
	 * @return
	 * @Title: getInstancePath
	 * @Description: 获取虚拟机配置文件路径
	 * @date 2015-5-18 上午9:10:16
	 */
	public static String getInstanceConfigPath(String configPath, String nodeIp, String instanceId) {
		String instancePath = String.format("%s/%s/%s", configPath, nodeIp, instanceId);
		return instancePath;
	}

	public static String getBundleTargetPath(String instanceConfigPath){
		return instanceConfigPath + "/bundle";
	}
	
}
