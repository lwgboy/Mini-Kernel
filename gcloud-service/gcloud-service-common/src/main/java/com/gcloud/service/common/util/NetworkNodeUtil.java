package com.gcloud.service.common.util;

import com.gcloud.common.util.StringUtils;
import com.gcloud.common.util.SystemUtil;
import com.gcloud.core.exception.GCloudException;
import lombok.extern.slf4j.Slf4j;
@Slf4j
public class NetworkNodeUtil {

	// start=============网络相关工具类==================

	/**
	 * @Title: addBr
	 * @Description: 创建网桥
	 * @date 2015-6-13 下午3:57:16
	 * 
	 * @param brName
	 * @throws GCloudException
	 */
	public static void addBr(String brName) {

		String[] cmd = new String[] { "brctl", "addbr", brName };
		int ret = SystemUtil.runAndGetCode(cmd);
		LogUtil.handleLog(cmd, ret, "1010611::创建网桥失败");

	}

	/**
	 * @Title: addovsBr
	 * @Description: 创建网桥
	 * @date 2017-11-13 下午3:57:16
	 * 
	 * @param brName
	 * @throws GCloudException
	 */
	public static void addOvsBr(String brName) {
		String[] cmd = new String[] { "ovs-vsctl", "add-br", brName };
		int ret = SystemUtil.runAndGetCode(cmd);
		LogUtil.handleLog(cmd, ret, "1010612::创建ovs网桥失败");
	}
	
	public static void setOvsPortTag(String tapName,String tag) {

		String[] cmd = new String[] { "ovs-vsctl", "set", "port",tapName,"tag="+tag };
		int ret = SystemUtil.runAndGetCode(cmd);
		LogUtil.handleLog(cmd, ret, "1010613::设置ovs tag失败");

	}
	
	public static String getOvsPortTag(String tapName )throws GCloudException {

		String tag=null;
		String ret;
		String[] cmd = new String[] { "ovs-vsctl", "--columns=tag","find","port","name="+tapName };
		ret = SystemUtil.run(cmd);
		if(!StringUtils.isBlank(ret)){
			tag=ret.split(":")[1].substring(1, 2);

		}
		LogUtil.handleLog(cmd, 0, "1010614::获取ovs port tag 失败");
		return tag;

	}


	/**
	 * @Title: brAddIf
	 * @Description: 把网络设备桥接到网桥上
	 * @date 2015-6-13 下午3:57:25
	 * 
	 * @param brName
	 * @param netName
	 * @throws GCloudException
	 */
	public static void brAddIf(String brName, String netName) {

		String[] cmd = new String[] { "brctl", "addif", brName, netName };
		int ret = SystemUtil.runAndGetCode(cmd);
		LogUtil.handleLog(cmd, ret, "1010615::把网络设备桥接到网桥失败");

	}

	/**
	 * @Title: ipLinkVethPeer
	 * @Description: 创建和连接pair两端
	 * @date 2015-6-13 下午3:57:49
	 * 
	 * @param preName
	 * @param aftName
	 * @throws GCloudException
	 */
	public static void ipLinkVethPeer(String preName, String aftName) {

		String[] cmd = new String[] { "ip", "link", "add", preName, "type", "veth", "peer", "name", aftName };
		int ret = SystemUtil.runAndGetCode(cmd);
		LogUtil.handleLog(cmd, ret, "1010616::创建veth失败");

	}

	/**
	 * @Title: ifconfigUp
	 * @Description: 启动网络设备
	 * @date 2015-6-13 下午3:58:03
	 * 
	 * @param net
	 * @throws GCloudException
	 */
	public static void ifconfigUp(String net) {
		String[] cmd = new String[] { "ifconfig", net, "up" };
		int ret = SystemUtil.runAndGetCode(cmd);
		LogUtil.handleLog(cmd, ret, "1010617::启动网络设备失败");

	}

	/**
	 * @Title: ifconfigDown
	 * @Description: 关闭网络设备
	 * @date 2015-6-13 下午3:58:10
	 * 
	 * @param net
	 * @throws GCloudException
	 */
	public static void ifconfigDown(String net) {

		String[] cmd = new String[] { "ifconfig", net, "down" };
		int ret = SystemUtil.runAndGetCode(cmd);
		LogUtil.handleLog(cmd, ret, "1010707::关闭网络设备失败");

	}

	/**
	 * @Title: delBr
	 * @Description: 删除网桥
	 * @date 2015-6-13 下午3:58:23
	 * 
	 * @param brName
	 * @throws GCloudException
	 */
	public static void delBr(String brName) {

		String[] cmd = new String[] { "brctl", "delbr", brName };
		int ret = SystemUtil.runAndGetCode(cmd);
		LogUtil.handleLog(cmd, ret, "1010708::删除网桥失败");

	}

	/**
	 * @Title: addOvs
	 * @Description: 增加ovs
	 * @date 2015-6-13 下午3:58:32
	 * 
	 * @param portId
	 * @param objectId(虚拟机为uuid)
	 * @param macAddress
	 * @param aftName
	 * @throws GCloudException
	 */
	public static void addOvs(String portId, String objectId, String macAddress, String aftName) {
		String[] cmd = new String[] { "ovs-vsctl", "--timeout=120", "--", "--if-exists", "del-port", aftName, "--", "add-port", "br-int", aftName, "--", "set", "Interface", aftName, "external-ids:iface-id=" + portId, "external-ids:iface-status=active", "external-ids:attached-mac=" + macAddress, "external-ids:vm-uuid=" + objectId };
		int ret = SystemUtil.runAndGetCode(cmd);
		LogUtil.handleLog(cmd, ret, "1010618::添加ovs失败");

	}

	/**
	 * @Title: deleteOvs
	 * @Description: 删除ovs
	 * @date 2015-6-13 下午3:58:40
	 * 
	 * @param aftName
	 * @throws GCloudException
	 */
	public static void deleteOvs(String aftName) {

		String[] cmd = new String[] { "ovs-vsctl", "del-port", "br-int", aftName };
		int ret = SystemUtil.runAndGetCode(cmd);
		LogUtil.handleLog(cmd, ret, "1010709::删除ovs失败");

	}

	/**
	 * @Title: delIpLink
	 * @Description: 删除pair
	 * @date 2015-6-13 下午3:58:51
	 * 
	 * @param pairName
	 * @throws GCloudException
	 */
	public static void delIpLink(String pairName) {

		int ret = 0;
		String[] cmd = new String[] { "ip", "link", "del", pairName };
		ret = SystemUtil.runAndGetCode(cmd);
		LogUtil.handleLog(cmd, ret, "1010710::删除ip link失败");

	}

	// end=============网络相关工具类==================
	
	
	public static void setNetin(String pid, String net){

		String[] cmd = new String[] {"ip", "link", "set", net, "netns", pid};
		int ret = SystemUtil.runAndGetCode(cmd);
		LogUtil.handleLog(cmd, ret, "::netns 设置失败");

	}
	
	public static void nsIpLinkSetDev(String pid, String net, String ifname){

		String[] cmd = new String[] {"nsenter", "-t", pid, "-n", "ip", "link", "set", "dev", net, "name", ifname};
		int ret = SystemUtil.runAndGetCode(cmd);
		LogUtil.handleLog(cmd, ret, "::设置ns ip link失败");
		
	}
	
	public static void nsIpLinkUp(String pid, String ifname){

		String[] cmd = new String[] {"nsenter", "-t", pid, "-n", "ip", "link", "set", ifname,"up", };
		int ret = SystemUtil.runAndGetCode(cmd);
		LogUtil.handleLog(cmd, ret, "::启动ip link 失败");
		
	}
	

	/**
	  * @Title: nsIpLinkSetIp
	  * @Description: netmask 以位数表示,例如24
	  * @date 2016年11月22日 下午3:37:02
	  *
	  * @param pid
	  * @param ifname
	  * @param ip
	  * @param netmask
	  * @throws GCloudException    
	  */
	public static void nsSetIp(String pid, String ifname, String ip, String netmask){

		String ipStr = String.format("%s/%s", ip, netmask);
		int ret = 0;
		String[] cmd = new String[] {"nsenter", "-t", pid, "-n", "ip", "addr", "add", ipStr, "dev", ifname};
		ret = SystemUtil.runAndGetCode(cmd);
		LogUtil.handleLog(cmd, ret, "::nsenter设置ip失败");
		
	}
	
	public static void nsDelDefaultRoute(String pid){

		int ret = 0;
		String[] cmd = new String[] {"nsenter", "-t", pid, "-n", "ip", "route", "del", "default"};
		ret = SystemUtil.runAndGetCode(cmd);
		LogUtil.handleLog(cmd, ret, "::nsenter 设置默认路由失败");
		
	}
	
	public static void nsDelSetDefaultRoute(String pid, String ifname, String gateway){

		int ret = 0;
		String[] cmd = new String[] {"nsenter", "-t", pid, "-n", "ip", "route", "add", "default", "via", gateway, "dev", ifname};
		ret = SystemUtil.runAndGetCode(cmd);
		LogUtil.handleLog(cmd, ret, "::nsenter 删除默认路由失败");
		
	}

	public static boolean isOvsBrExist(String brName) {

		boolean isExist = false;

		try {

			int ret = 0;
			String[] cmd = new String[] { "ovs-vsctl", "br-exists", brName };
			ret = SystemUtil.runAndGetCode(cmd);
			isExist = ret == 0;

		} catch (Exception e) {
			log.error("检查ovs br失败", e);
			isExist = false;
		}

		return isExist;
	}

	public static void deleteOvsBr(String brName) throws GCloudException{

		String[] cmd = new String[] { "ovs-vsctl", "del-br", brName };
		int ret = SystemUtil.runAndGetCode(cmd);
		LogUtil.handleLog(cmd, ret, "::删除ovs bridge 失败");

	}
}
