package com.gcloud.compute.service.vm.base.impl;

import com.gcloud.compute.prop.ComputeNodeProp;
import com.gcloud.compute.service.vm.base.IVmBaseNodeService;
import com.gcloud.compute.util.ConfigFileUtil;
import com.gcloud.compute.util.VmNodeUtil;
import com.gcloud.compute.virtual.IVmVirtual;
import com.gcloud.core.condition.ConditionalHypervisor;
import com.gcloud.core.exception.GCloudException;
import com.gcloud.core.simpleflow.Flow;
import com.gcloud.core.simpleflow.SimpleFlowChain;
import com.gcloud.header.compute.enums.PlatformType;
import com.gcloud.header.compute.enums.VmState;
import com.gcloud.service.common.compute.model.CpuTopology;
import com.gcloud.service.common.compute.model.DomainInfo;
import com.gcloud.service.common.compute.uitls.VmUtil;
import lombok.extern.slf4j.Slf4j;
import org.dom4j.Document;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.File;

/**
 * Created by yaowj on 2018/9/7.
 */
@Slf4j
@ConditionalHypervisor
public class VmBaseNodeServiceKvmImpl implements IVmBaseNodeService {

	@Autowired
	private IVmVirtual vmVirtual;

	@Autowired
	private ComputeNodeProp prop;

	@Override
	public void startup(String instanceId) throws GCloudException {
		this._startup(instanceId, false);
	}

	@Override
	public void startupDesktop(String instanceId) throws GCloudException {
		this._startup(instanceId, true);
	}

	private void _startup(String instanceId, boolean isDesktop) throws GCloudException {
		try {
			DomainInfo domInfo = VmNodeUtil.checkVm(instanceId);
			if (domInfo == null) {
				throw new GCloudException("1010201::找不到云服务器");// 没找到该云服务器
			}

			String state = domInfo.getGcState();

			// 开放状态异常操作权限
			if (VmUtil.isStateException(state) || (!state.equals(VmState.RUNNING.value())) && !state.equals(VmState.STOPPED.value())) {
				log.error("云服务器不是关闭状态:" + instanceId);
				throw new GCloudException("1010202::云服务器当前状态不支持开机");// 云服务器不是关机的状态
			}

			// 已经是开机，则不处理
			if (!state.equals(VmState.RUNNING.value())) {
				if (isDesktop) {
					VmNodeUtil.startDesktopTimes(instanceId, 2);
				} else {
					VmNodeUtil.startTimes(instanceId, 5);
				}
			}

		} catch (Exception e) {
			log.error("云服务器开机失败", e);
			if (e instanceof GCloudException) {
				throw new GCloudException(e.getMessage());
			} else {
				throw new GCloudException("1010205::云服务器开机异常");// 开机失败
			}
		}
	}

	@Override
	public void reboot(String instanceId, Boolean forceStop) throws GCloudException {

        DomainInfo domInfo = VmNodeUtil.checkVm(instanceId);
        if (domInfo == null) {
			throw new GCloudException("1010401::云服务器不存在");// 该云服务器不存在
		}
		String state = domInfo.getGcState();
		// 开放状态异常操作权限
		if (VmUtil.isStateException(state) || !state.equals(VmState.RUNNING.value())) {
			log.debug("云服务器不是运行状态" + instanceId);
			throw new GCloudException("1010402::云服务器当前状态不支持重启");// 云服务器的状态不是运行的状态
		}

		if (forceStop != null && forceStop) {
			forceReboot(instanceId);
		} else {
			reboot(instanceId);
		}

	}

	private void forceReboot(String instanceId) throws GCloudException {

        DomainInfo domInfo = VmNodeUtil.checkVm(instanceId);
        if (domInfo == null) {
            throw new GCloudException("::云服务器不存在");
        }

		vmVirtual.destroy(instanceId);

		VmNodeUtil.startTimes(instanceId, 5);
	}

	private void reboot(String instanceId) throws GCloudException {
		VmNodeUtil.rebootTimes(instanceId, 5);
	}

	@Override
	public void stop(String instanceId) throws GCloudException {
        DomainInfo domInfo = VmNodeUtil.checkVm(instanceId);
        if (domInfo == null) {
			throw new GCloudException("1010301::找不到云服务器");// 没找到该云服务器
		}

		String state = vmVirtual.queryStateForGcloud(instanceId);
		// 开放状态异常操作权限
		if (VmUtil.isStateException(state) || (!state.equals(VmState.RUNNING.value())) && !state.equals(VmState.STOPPED.value())) {
			log.debug("云服务器不是运行状态" + instanceId);
			throw new GCloudException("1010302::云服务器当前状态不支持关机");// 云服务器的状态不是运行的状态
		}

		// 如果已经关机，则不处理
		if (!state.equals(VmState.STOPPED.value())) {

			String result = null;

			boolean timeout = false;
			Long startTime = System.currentTimeMillis();
			do {
				try {
					vmVirtual.shutdown(instanceId);
				} catch (Exception e) {
					log.error("执行shutdown关机命令失败，云服务器可能已经被关闭。", e);
				}

				result = vmVirtual.queryStateForGcloud(instanceId);
				if (result.equals(VmState.STOPPED.value())) {
					log.debug("云服务器正常关机成功:" + instanceId + result);
					break;
				}

				try {
					Thread.sleep(3000);
				} catch (Exception ex) {
					log.error("sleep exception", ex);
					result = vmVirtual.queryStateForGcloud(instanceId);
					break;
				}

				result = vmVirtual.queryStateForGcloud(instanceId);
				if (result.equals(VmState.STOPPED.value())) {
					log.debug("云服务器正常关机成功:" + instanceId + result);
					break;
				}

				Long endTime = System.currentTimeMillis();
				timeout = endTime - startTime > 180000L;
				if (timeout) {
					log.debug("云服务器正常关机超时:" + instanceId + result);
					break;
				}
			} while (true);

			if (!result.equals(VmState.STOPPED.value())) {
				throw new GCloudException("1010304::云服务器关机失败");
			}

		}

	}

	@Override
	public String vmGcloudState(String instanceId) throws GCloudException {
		String vmState = null;
		try {
			vmState = vmVirtual.queryStateForGcloud(instanceId);
		} catch (Exception ex) {
			log.error("获取云服务器状态信息失败", ex);
		}

		return vmState;
	}

	@Override
	public void destroy(String instanceId) {
        DomainInfo domInfo = VmNodeUtil.checkVm(instanceId);
        if (domInfo == null) {
			throw new GCloudException("::找不到云服务器");// 没找到该云服务器
		}

		String state = vmVirtual.queryStateForGcloud(instanceId);
		// 关机不处理
		if (!state.equals(VmState.STOPPED.value())) {
			vmVirtual.destroy(instanceId);
		}
	}

	@Override
	public void configInstanceResource(String instanceId, Integer cpu, Integer memory, Integer orgCpu, Integer orgMemory) {
        DomainInfo domInfo = VmNodeUtil.checkVm(instanceId);
        if (domInfo == null) {
			throw new GCloudException("1011302::云服务器不存在");// 没找到该云服务器
		}

		String libvirtPath = VmNodeUtil.getLibvirtXmlPath(prop.getNodeIp(), instanceId);

		SimpleFlowChain<String, String> chain = new SimpleFlowChain<>("config Instance Resource");
		chain.then(new Flow<String>() {
			File file = new File(libvirtPath);

			@Override
			public void run(SimpleFlowChain chain, String data) {
				Document doc = ConfigFileUtil.readXml(file);
				CpuTopology cpuTopo = VmNodeUtil.getCpuTopology(cpu);
				ConfigFileUtil.changeLibvirtCpuType(doc, cpuTopo.getCore(), cpuTopo.getThread(), cpuTopo.getSocket());

				ConfigFileUtil.changeLibvirtCpu(doc, String.valueOf(cpu));
				ConfigFileUtil.changeLibvirtMemory(doc, String.valueOf(memory * 1024L));
				ConfigFileUtil.doc2XmlFile(doc, libvirtPath);

				chain.next();
			}

			@Override
			public void rollback(SimpleFlowChain chain, String data) {
				Document doc = ConfigFileUtil.readXml(file);
				if(orgCpu != null && orgMemory != null){
					CpuTopology cpuTopo = VmNodeUtil.getCpuTopology(orgCpu);
					ConfigFileUtil.changeLibvirtCpuType(doc, cpuTopo.getCore(), cpuTopo.getThread(), cpuTopo.getSocket());
					ConfigFileUtil.changeLibvirtCpu(doc, String.valueOf(orgCpu));
				}


			}
		}).then(new Flow<String>() {
			@Override
			public void run(SimpleFlowChain chain, String data) {
				log.debug(String.format("【修改配置-%s】修改libvirt-save FilePath:%s", instanceId, libvirtPath));
				VmNodeUtil.redefineVm(instanceId, libvirtPath);
				log.debug(String.format("【修改配置-%s】修改libvirt-redefineVm", instanceId));

				chain.next();
			}

			@Override
			public void rollback(SimpleFlowChain chain, String data) {

			}
		}).start();
	}

	@Override
	public void destroyIfExist(String instanceId) {
		if(VmNodeUtil.isVmExist(instanceId)){
			String state = vmVirtual.queryStateForGcloud(instanceId);
			// 关机不处理
			if (!state.equals(VmState.STOPPED.value())) {
				vmVirtual.destroy(instanceId);
			}
		}
	}
	
	@Override
	public void changePassword(String instanceId, String password) throws GCloudException{
        DomainInfo domInfo = VmNodeUtil.checkVm(instanceId);
        if (domInfo == null) {
            throw new GCloudException("::找不到该云服务器");
        }

        String state = domInfo.getGcState();
        if(VmUtil.isStateException(state) == false && !state.equals(VmState.RUNNING.value())){
            throw new GCloudException("::操作失败，云服务器不是开机状态");
        }
        
        boolean isReady = checkVmReady(instanceId, 30);
        if(isReady) {
	        String username = "";
	        if(vmVirtual.getSystemType(instanceId).getValue().equals(PlatformType.LINUX.getValue())) {
	        	username = "root";
	        } else {
	        	username = "Administrator";
	        }
	        
	        boolean ret = vmVirtual.agentGuestSetUserPassword(instanceId, username, password, false);
	        if (!ret) {
	            throw new GCloudException("::修改云服务器密码失败");
	        }
        } else {
        	throw new GCloudException("vm change password error,can not connect, instanceId:" + instanceId);
        }
	}

	@Override
	public String queryVnc(String instanceId) {
		DomainInfo info = VmNodeUtil.domInfo(instanceId);
		if (info == null) {
			throw new GCloudException("::找不到该云服务器");
		}
		String state = info.getGcState();
		if (VmUtil.isStateException(state) == false && !state.equals(VmState.RUNNING.value())){
			throw new GCloudException("::操作失败，云服务器不是开机状态");
		}

		int port = vmVirtual.queryVncPort(instanceId);
//		NoVncUtil.addToken(instanceId, prop.getNodeIp(), port);
//		return NoVncUtil.generateVncUrl(instanceId);
		return String.valueOf(port);
	}

	/* 
	 * 虚拟机重命名
	 */
	@Override
	public void changeHostName(String instanceId, String hostname) throws GCloudException {
		DomainInfo info = VmNodeUtil.domInfo(instanceId);
		boolean ret = false;
		if (info == null) {
			throw new GCloudException("::找不到该云服务器");
		}
		boolean isReady = checkVmReady(instanceId, 30);
        if(isReady) {
			String platformType = vmVirtual.getSystemType(instanceId).getValue();
			if (platformType.equals(PlatformType.WINDOWS.getValue())) {
				log.debug("rename: for windows instanceId:" + instanceId);
				String[] cmd = new String[] { "c:/Program Files/qemu-ga/gcloud-rename.bat", hostname };
				ret = vmVirtual.agentGuestExec(instanceId, cmd, 30);
				if(!ret) {
					throw new GCloudException("vm rename error instanceId:" + instanceId);
				}
			} else if (platformType.equals(PlatformType.LINUX.getValue())) {
				log.debug("rename: for linux instanceId:" + instanceId);
				
				String hostfile = "/etc/sysconfig/network";
				String[] cmd1 = new String[] { "bash", "-c", "sed -i '/^HOSTNAME/d' " + hostfile };
				String[] cmd2 = new String[] { "bash", "-c", "echo \\\"HOSTNAME=" + hostname + "\\\" >> " + hostfile };
				ret = vmVirtual.agentGuestExec(instanceId, cmd1, 30);
				log.debug("修改/etc/sysconfig/network sed HOSTNAME:" + instanceId + " 是否成功 = " + ret);
				if(!ret) {
					ret = vmVirtual.agentGuestExec(instanceId, cmd2, 30);
					log.debug("修改/etc/sysconfig/network echo HOSTNAME :" + instanceId + " 是否成功 = " + ret);
					if(!ret) {
						throw new GCloudException("vm rename error instanceId:" + instanceId);
					}
				}
				
				boolean isHostnamectl = vmVirtual.agentGuestExec(instanceId, new String[] { "hostnamectl" }, 30);
				String[] cmd3 = null;
				if (isHostnamectl) {
					cmd3 = new String[] { "bash", "-c", "hostnamectl set-hostname " + hostname };
				} else {
					cmd3 = new String[] { "bash", "-c", "hostname " + hostname };
				}
				ret = vmVirtual.agentGuestExec(instanceId, cmd3, 30);
				log.debug("hostname 命令 :" + instanceId + " 是否成功 = " + ret);
				if(!ret) {
					throw new GCloudException("vm rename error instanceId:" + instanceId);
				}
			}
        } else {
        	throw new GCloudException("vm rename error,can not connect, instanceId:" + instanceId);
        }
	}
	
	@Override
	public Boolean checkVmReady(String instanceId, int timeout) throws GCloudException {
		long timeoutMill = timeout * 1000;
		long startTime = System.currentTimeMillis();
		long sleepInterval = 5000L;
		long useTime = 0;
		boolean result = false;
		if(timeoutMill <= sleepInterval) {
			sleepInterval = 2000L;
		}
		while (useTime <= timeoutMill) {
			long stepStartTime = System.currentTimeMillis();
			result = vmVirtual.agentGuestInfo(instanceId);
			if (result) {
				break;
			}
			
			try {
				Thread.sleep(sleepInterval - (System.currentTimeMillis() - stepStartTime));
			} catch (InterruptedException e) {
				log.error(e.getMessage());
			}

			useTime = System.currentTimeMillis() - startTime;
		}
		return result;
	}
}
