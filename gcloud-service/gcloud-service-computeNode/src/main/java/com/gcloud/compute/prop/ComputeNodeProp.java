package com.gcloud.compute.prop;

import com.gcloud.common.util.StringUtils;
import com.gcloud.header.compute.enums.BooleanType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

/**
 * Created by yaowj on 2018/9/7.
 */
@Component
// @ConfigurationProperties(prefix = "gcloud.spring.computeNode")
public class ComputeNodeProp {

	@Value("${gcloud.computeNode.controller}")
	private String controller;

	@Value("${gcloud.computeNode.maxCores:0}")
	private int maxCores; // 物理机最大限制cpu数
	@Value("${gcloud.computeNode.maxMem:0}")
	private int maxMem; // 物理机最大限制内存数
	@Value("${gcloud.computeNode.maxLocalAverage:0}")
	private double maxLocalAverage;
	@Value("${gcloud.computeNode.maxSleepCount:0}")
	private int maxSleepCount;
	@Value("${gcloud.computeNode.vmStartSleepTime:20}")
	private int vmStartSleepTime;
	@Value("${gcloud.computeNode.configurePath:/usr/share/gcloud}")
	private String configurePath;
	@Value("${gcloud.computeNode.vmStartMaxNum:5}")
	private int vmStartMaxNum;
	@Value("${gcloud.computeNode.nodeIp:127.0.0.1}")
	private String nodeIp;
	@Value("${gcloud.computeNode.cloudPlatform:IDCSever}")
	private String cloudPlatform;
	@Value("${gcloud.computeNode.desktopVmCpuTopologyConfig:1,*,1}")
	private String desktopVmCpuTopologyConfig;
	@Value("${gcloud.computeNode.vmCpuTopologyConfig:*,1,1}")
	private String vmCpuTopologyConfig;
	@Value("${gcloud.computeNode.instanceDiskPath:/instances/disk}")
	private String instanceDiskPath;
	@Value("${gcloud.computeNode.instanceConfigPath:/cephFileSystem/instances/config}")
	private String instanceConfigPath;
	@Value("${gcloud.computeNode.cephileSystem:true}")
	private boolean cephFileSystem;
	@Value("${gcloud.computeNode.cephFileSystemPath:/cephFileSystem}")
	private String cephFileSystemPath;
	@Value("${gcloud.computeNode.rbdUserName:}")
	private String rbdUserName;
	@Value("${gcloud.computeNode.rbdSecretUuid:}")
	private String rbdSecretUuid;
	@Value("${spring.computeNode.hypervisor:kvm}")
	private String hypervisor;
	@Value("${spring.computeNode.agentType:qemu-guest-agent}")
	private String agentType;
	@Value("${spring.computeNode.graphicsPassword:}")
	private String graphicsPassword;
	@Value("${spring.computeNode.remoteType:vnc}")
	private String remoteType;
	@Value("${spring.computeNode.qemuPath:}")
	private String qemuPath;

	@Value("${spring.computeNode.fileDiskCacheType:}")
	private String fileDiskCacheType;

	@Value("${spring.computeNode.rbdDiskCacheType:}")
	private String rbdDiskCacheType;

	@Value("${spring.computeNode.lvmDiskCacheType:}")
	private String lvmDiskCacheType;

	@Value("${spring.computeNode.reportFrequency:30}")
	private int reportFrequency;

	@Value("${spring.computeNode.desktopQemuPath:/usr/bin/qemu-system-x86_64}")
	private String desktopQemuPath;
	@Value("${spring.computeNode.desktopCfgPath:/usr/etc/qemu}")
	private String desktopCfgPath;
	@Value("${spring.computeNode.desktopCfgTemplate:lci_5900.cfg}")
	private String desktopCfgTemplate;

	@Value("${spring.computeNode.hostname:}")
	private String hostname;

	@Value("${gcloud.computeNode.zxAuthPath:}")
	private String zxAuthPath;

	private boolean cpuBinding;

	private int ftNode;

	@Autowired
	private Environment environment;

	@PostConstruct
	public void initProperties() {
		String envFtNode = environment.getProperty("spring.computeNode.ftNode");
		if (StringUtils.isBlank(envFtNode) || envFtNode.trim().equals("N")) {
			this.setFtNode(BooleanType.FALSE.getValue());
		} else {
			this.setFtNode(BooleanType.TRUE.getValue());
		}

		String envCpuBinding = environment.getProperty("spring.computeNode.cpuBinding");
		if (StringUtils.isBlank(envCpuBinding) || !envCpuBinding.trim().equals("Y")) {
			this.setCpuBinding(false);
		} else {
			this.setCpuBinding(true);
		}

	}

	public String getDesktopCfgPath() {
		return desktopCfgPath;
	}

	public void setDesktopCfgPath(String desktopCfgPath) {
		this.desktopCfgPath = desktopCfgPath;
	}

	public String getDesktopCfgTemplate() {
		return desktopCfgTemplate;
	}

	public void setDesktopCfgTemplate(String desktopCfgTemplate) {
		this.desktopCfgTemplate = desktopCfgTemplate;
	}

	public String getDesktopQemuPath() {
		return desktopQemuPath;
	}

	public void setDesktopQemuPath(String desktopQemuPath) {
		this.desktopQemuPath = desktopQemuPath;
	}

	public double getMaxLocalAverage() {
		return maxLocalAverage;
	}

	public void setMaxLocalAverage(double maxLocalAverage) {
		this.maxLocalAverage = maxLocalAverage;
	}

	public int getMaxSleepCount() {
		return maxSleepCount;
	}

	public void setMaxSleepCount(int maxSleepCount) {
		this.maxSleepCount = maxSleepCount;
	}

	public int getVmStartSleepTime() {
		return vmStartSleepTime;
	}

	public void setVmStartSleepTime(int vmStartSleepTime) {
		this.vmStartSleepTime = vmStartSleepTime;
	}

	public String getConfigurePath() {
		return configurePath;
	}

	public void setConfigurePath(String configurePath) {
		this.configurePath = configurePath;
	}

	public int getVmStartMaxNum() {
		return vmStartMaxNum;
	}

	public void setVmStartMaxNum(int vmStartMaxNum) {
		this.vmStartMaxNum = vmStartMaxNum;
	}

	public String getController() {
		return controller;
	}

	public void setController(String controller) {
		this.controller = controller;
	}

	public String getNodeIp() {
		return nodeIp;
	}

	public void setNodeIp(String nodeIp) {
		this.nodeIp = nodeIp;
	}

	public String getCloudPlatform() {
		return cloudPlatform;
	}

	public void setCloudPlatform(String cloudPlatform) {
		this.cloudPlatform = cloudPlatform;
	}

	public String getDesktopVmCpuTopologyConfig() {
		return desktopVmCpuTopologyConfig;
	}

	public void setDesktopVmCpuTopologyConfig(String desktopVmCpuTopologyConfig) {
		this.desktopVmCpuTopologyConfig = desktopVmCpuTopologyConfig;
	}

	public String getVmCpuTopologyConfig() {
		return vmCpuTopologyConfig;
	}

	public void setVmCpuTopologyConfig(String vmCpuTopologyConfig) {
		this.vmCpuTopologyConfig = vmCpuTopologyConfig;
	}

	public String getInstanceDiskPath() {
		return instanceDiskPath;
	}

	public void setInstanceDiskPath(String instanceDiskPath) {
		this.instanceDiskPath = instanceDiskPath;
	}

	public boolean isCephFileSystem() {
		return cephFileSystem;
	}

	public void setCephFileSystem(boolean cephFileSystem) {
		this.cephFileSystem = cephFileSystem;
	}

	public String getCephFileSystemPath() {
		return cephFileSystemPath;
	}

	public void setCephFileSystemPath(String cephFileSystemPath) {
		this.cephFileSystemPath = cephFileSystemPath;
	}

	public String getInstanceConfigPath() {
		return instanceConfigPath;
	}

	public void setInstanceConfigPath(String instanceConfigPath) {
		this.instanceConfigPath = instanceConfigPath;
	}

	public String getHypervisor() {
		return hypervisor;
	}

	public void setHypervisor(String hypervisor) {
		this.hypervisor = hypervisor;
	}

	public String getAgentType() {
		return agentType;
	}

	public void setAgentType(String agentType) {
		this.agentType = agentType;
	}

	public String getGraphicsPassword() {
		return graphicsPassword;
	}

	public void setGraphicsPassword(String graphicsPassword) {
		this.graphicsPassword = graphicsPassword;
	}

	public String getRemoteType() {
		return remoteType;
	}

	public void setRemoteType(String remoteType) {
		this.remoteType = remoteType;
	}

	public String getQemuPath() {
		return qemuPath;
	}

	public void setQemuPath(String qemuPath) {
		this.qemuPath = qemuPath;
	}

	public String getRbdUserName() {
		return rbdUserName;
	}

	public void setRbdUserName(String rbdUserName) {
		this.rbdUserName = rbdUserName;
	}

	public String getRbdSecretUuid() {
		return rbdSecretUuid;
	}

	public void setRbdSecretUuid(String rbdSecretUuid) {
		this.rbdSecretUuid = rbdSecretUuid;
	}

	public int getFtNode() {
		return ftNode;
	}

	public void setFtNode(int ftNode) {
		this.ftNode = ftNode;
	}

	public boolean getCpuBinding() {
		return cpuBinding;
	}

	public void setCpuBinding(boolean cpuBinding) {
		this.cpuBinding = cpuBinding;
	}

	public int getMaxCores() {
		return maxCores;
	}

	public void setMaxCores(int maxCores) {
		this.maxCores = maxCores;
	}

	public int getMaxMem() {
		return maxMem;
	}

	public void setMaxMem(int maxMem) {
		this.maxMem = maxMem;
	}

	public String getHostname() {
		return hostname;
	}

	public void setHostname(String hostname) {
		this.hostname = hostname;
	}

	public int getReportFrequency() {
		return reportFrequency;
	}

	public void setReportFrequency(int reportFrequency) {
		this.reportFrequency = reportFrequency;
	}

	public String getZxAuthPath() {
		return zxAuthPath;
	}

	public void setZxAuthPath(String zxAuthPath) {
		this.zxAuthPath = zxAuthPath;
	}

	public String getFileDiskCacheType() {
		return fileDiskCacheType;
	}

	public void setFileDiskCacheType(String fileDiskCacheType) {
		this.fileDiskCacheType = fileDiskCacheType;
	}

	public String getRbdDiskCacheType() {
		return rbdDiskCacheType;
	}

	public void setRbdDiskCacheType(String rbdDiskCacheType) {
		this.rbdDiskCacheType = rbdDiskCacheType;
	}

	public String getLvmDiskCacheType() {
		return lvmDiskCacheType;
	}

	public void setLvmDiskCacheType(String lvmDiskCacheType) {
		this.lvmDiskCacheType = lvmDiskCacheType;
	}
}
