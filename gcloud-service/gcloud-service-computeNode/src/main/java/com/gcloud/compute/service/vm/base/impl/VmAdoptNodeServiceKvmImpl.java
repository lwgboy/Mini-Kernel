package com.gcloud.compute.service.vm.base.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.gcloud.common.util.FileUtil;
import com.gcloud.common.util.NetworkUtil;
import com.gcloud.common.util.StringUtils;
import com.gcloud.compute.cache.cache.VmExceptionCache;
import com.gcloud.compute.cache.cache.VmInstancesCache;
import com.gcloud.compute.prop.ComputeNodeProp;
import com.gcloud.compute.service.vm.base.IVmAdoptNodeService;
import com.gcloud.compute.util.ComputeNetworkUtil;
import com.gcloud.compute.util.ConfigFileUtil;
import com.gcloud.compute.util.EnvironmentUtils;
import com.gcloud.compute.util.VmNodeUtil;
import com.gcloud.compute.virtual.IVmVirtual;
import com.gcloud.core.condition.ConditionalHypervisor;
import com.gcloud.core.exception.GCloudException;
import com.gcloud.header.compute.enums.AutostartType;
import com.gcloud.header.compute.enums.BooleanType;
import com.gcloud.header.compute.enums.BusType;
import com.gcloud.header.compute.enums.CloudPlatform;
import com.gcloud.header.compute.enums.Device;
import com.gcloud.header.compute.enums.DeviceOwner;
import com.gcloud.header.compute.enums.DiskProtocol;
import com.gcloud.header.compute.enums.FtState;
import com.gcloud.header.compute.msg.node.vm.model.VmCdromDetail;
import com.gcloud.header.compute.msg.node.vm.model.VmDetail;
import com.gcloud.header.compute.msg.node.vm.model.VmNetworkDetail;
import com.gcloud.header.compute.msg.node.vm.model.VmStateInfo;
import com.gcloud.header.storage.enums.DiskType;
import com.gcloud.header.storage.model.VmVolumeDetail;
import com.gcloud.service.common.compute.enums.VmDisableType;
import com.gcloud.service.common.compute.model.DomainDetail;
import com.gcloud.service.common.compute.model.DomainListInfo;
import com.gcloud.service.common.compute.model.QemuInfo;
import com.gcloud.service.common.compute.uitls.DiskQemuImgUtil;
import com.gcloud.service.common.compute.uitls.DiskUtil;
import com.gcloud.service.common.util.NetworkNodeUtil;
import lombok.extern.slf4j.Slf4j;
import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

@Slf4j
@ConditionalHypervisor
public class VmAdoptNodeServiceKvmImpl implements IVmAdoptNodeService {

    @Autowired
    private IVmVirtual vmVirtual;

    @Autowired
    private ComputeNodeProp prop;

    @Override
    public boolean adoptVms(boolean isInit) {
        if (isInit) {
            log.debug(String.format("执行clearVmPathTrash 开始"));
            clearVmPathTrash(prop.getInstanceConfigPath());// 清除云服务器配置文件路径系统垃圾
            log.debug(String.format("执行clearVmPathTrash 结束"));

            try {
                log.debug(String.format("执行undefineVms 开始"));
                EnvironmentUtils.checkCephFileSystem();
                undefineVms(prop.getInstanceConfigPath());// undefine不属于这个节点的云服务器
                log.debug(String.format("执行undefineVms 结束"));
            } catch (Exception e) {
                log.error("收养undefine出错:" + e.getMessage(), e);
            }

            isInit = false;
        }
        log.debug(String.format("执行scanVmByDirs 开始"));
        scanVmByDirs(prop.getInstanceConfigPath());// 扫描云服务器配置路径的文件
        log.debug(String.format("执行scanVmByDirs 结束"));

        return isInit;
    }

    /**
     * @param instanceConfigurePath
     * @Title: scanVmByDirs
     * @Description: 扫描目录
     * @date 2015-6-18 下午2:23:17
     */
    public void scanVmByDirs(String instanceConfigurePath) {

        String configPath = FileUtil.pathString(instanceConfigurePath, prop.getNodeIp());
        File dir = new File(configPath);
        log.debug(String.format("scan path %s begin", configPath));
        if (dir.exists() && dir.isDirectory()) {
            File userDirs[] = dir.listFiles();
            // 遍历用户实例目录
            for (int a = 0; a < userDirs.length; a++) {
                String userFileName = userDirs[a].getName();
                log.debug(String.format("userDir name %s is scan begin", userFileName));
                // 遍历用户的云服务器实例目录
                File instanceIds[] = userDirs[a].listFiles();
                for (int j = 0; j < instanceIds.length; j++) {
                    String instanceIdFileName = instanceIds[j].getName();
                    log.debug(String.format("instanceId %s scan begin", instanceIdFileName));
                    scanOneVm(instanceIds[j]);
                    log.debug(String.format("instanceId %s scan end", instanceIdFileName));
                }
                log.debug(String.format("userDir name %s is scan end", userFileName));
            }
        }
        log.debug(String.format("scan path %s end", configPath));
    }

    /**
     * @param instanceFile
     * @Title: scanOneVm
     * @Description: 单个云服务器路径扫描
     * @date 2015-5-18 下午3:19:02
     */
    public void scanOneVm(File instanceFile) {
        try {

            String nodeIp = prop.getNodeIp();
            if (StringUtils.isBlank(nodeIp)) {
                log.error("nodeIp  is null");
                return;
            }

            String instanceId = instanceFile.getName();
            String instancePath = instanceFile.getPath();

            //存在内存中，更新状态
            VmDetail vm = VmInstancesCache.get(instanceFile.getName());
            if(vm != null){
                if(vm.getVmDisableType() == null){
                    String state = vmVirtual.queryStateForGcloud(vm.getId());
                    if (StringUtils.isNotBlank(state)) {
                        vm.setState(state);
                        updateRemotePort(instanceFile, vm);
                        return;
                    } else {
                        VmInstancesCache.remove(vm.getId());
                        vm = null;
                    }

                }else{
                    VmInstancesCache.remove(instanceId);
                    vm = null;
                }
            }


            if (instanceFile.isDirectory() == false) {
                log.error(String.format("instanceId %s isn't directory,continue next ", instanceFile.getName()));
                return;
            }

            File instFilesList[] = instanceFile.listFiles();
            // 检测云服务器文件夹下面的文件数目
            if (instFilesList.length < 2) {
                VmDetail disableVm = new VmDetail();
                disableVm.setId(instanceFile.getName());
                disableVm.setVmDisableType(VmDisableType.DISABLE11.getValue());
                VmExceptionCache.add(instanceFile.getName(), disableVm);
                log.error(String.format("instancePath %s  file less than 2,continue next ", instanceFile.getName()));
                return;
            }

            // 检测instance.xml,libvirt.xml和nodeInfo文件是否存在
            if (!isFileExists(VmNodeUtil.getLibvirtXmlPath(instancePath))) {
                VmDetail disableVm = new VmDetail();
                log.error(String.format("instancePath %s libvirt file isn't exist,continue next ", instanceFile.getName()));
                disableVm.setVmDisableType(VmDisableType.DISABLE12.getValue());
                VmExceptionCache.add(instanceFile.getName(), disableVm);
                return;
            }
            if (!isFileExists(VmNodeUtil.getInstanceXmlPath(instancePath))) {
                VmDetail disableVm = new VmDetail();
                log.error(String.format("instancePath %s instance file isn't exist,continue next ", instanceFile.getName()));
                disableVm.setVmDisableType(VmDisableType.DISABLE12.getValue());
                VmExceptionCache.add(instanceFile.getName(), disableVm);
                return;
            }


            // 检测云服务器是否正在创建云服务器中
            File createLockFile = new File(instancePath + "/createLock");
            if (createLockFile.exists()) {
                log.error("the instance is Penging,path = " + instancePath + "/createLock");
                return;
            }

            // 读取配置文件获取云服务器信息
            vm = generateInstance(instanceFile, vm, nodeIp);
            if (vm == null) {
                log.error(String.format("读取配置文件内容出错,instancePath %s", instancePath));
                return;
            }
            // start networks TODO
            // 如果配置文件信息都齐全，但是不存在这台服务器且不在故障恢复中就define看看，存在就收养
            if (vm.getVmDisableType() != null) {
                log.error(String.format("instanceId %s is exception", new File(instancePath).getName()));
                VmExceptionCache.add(vm.getId(), vm);
                return;
            }


            boolean isVmExist = VmNodeUtil.isVmExist(vm.getId());
            boolean isDomPersistent = vmVirtual.isDomPersistent(vm.getId());
            Integer ftState = ConfigFileUtil.getFtState(VmNodeUtil.getInstanceXmlPath(instancePath));
            if ((!isVmExist || !isDomPersistent) && !ftState.equals(FtState.CHECKPOINTING.getValue())) {
                vmVirtual.define(VmNodeUtil.getLibvirtXmlPath(instancePath), "define失败");
                isVmExist = VmNodeUtil.isVmExist(vm.getId());
            }

            if (isVmExist) {
                String state = vmVirtual.queryStateForGcloud(vm.getId());
                if (StringUtils.isNotBlank(state)) {
                    vm.setState(state);
                }
                if (VmExceptionCache.get(vm.getId()) != null) {
                    VmExceptionCache.remove(vm.getId());
                }

                log.debug(String.format("instanceId %s add cache success，%s", new File(instancePath).getName(), JSON.toJSONString(vm, SerializerFeature.WriteMapNullValue)));
                VmInstancesCache.add(vm.getId(), vm);
            }

        } catch (Exception e) {
            log.error(String.format("scan instanceId %s ", instanceFile.getName()), e);
            return;
        }

    }

    public void clearVmPathTrash(String instanceConfigurePath) {
        log.debug("clearVmPathTrash begin:" + instanceConfigurePath);
        File dir = new File(instanceConfigurePath);
        if (dir.exists() && dir.isDirectory()) {
            File userDirs[] = dir.listFiles();
            // 遍历用户目录
            for (int i = 0; i < userDirs.length; i++) {
                if (userDirs[i].getName().equals(".") || userDirs[i].isDirectory() == false) {
                    log.debug(String.format("clearVmPathTrash userDir name %s is not statified,continue next ", userDirs[i].getName()));
                    continue;
                }
                log.debug(String.format("clearVmPathTrash userDir name %s is scanned", userDirs[i].getName()));
                File instanceIds[] = userDirs[i].listFiles();
                // 遍历用户的云服务器实例目录
                for (int j = 0; j < instanceIds.length; j++) {
                    File instanceFile = instanceIds[j];
                    // 检测云服务器文件夹下面的文件数目
                    if (!instanceFile.isDirectory()) {
                        instanceIds[j].delete();
                        log.debug(String.format("clearVmPathTrash instanceId %s  fileNumber is 0,now delete ", instanceIds[j].getName()));
                        continue;
                    }
                }
            }
        }
        log.debug("clearVmPathTrash end:" + instanceConfigurePath);
    }


    public void undefineVms(String instanceConfigurePath) {
        log.debug("undefine Instances:" + instanceConfigurePath + " start");

        List<String> nodeInstanceIds = new ArrayList<String>();

        try {
            // 遍历instanceConfigurePath下的instance实例
            File dir = new File(instanceConfigurePath);
            if (dir.exists() && dir.isDirectory()) {
                File nodeIpDirs[] = dir.listFiles();
                for (int a = 0; a < nodeIpDirs.length; a++) {
                    String nodeIpFileName = nodeIpDirs[a].getName();
                    if (StringUtils.isBlank(nodeIpFileName) || nodeIpFileName.equals(".") || (!prop.getNodeIp().equals(nodeIpFileName)) || (!NetworkUtil.isIpv4(nodeIpFileName)) || nodeIpDirs[a].isDirectory() == false) {
                        log.debug(String.format("nodeIpDir name %s is not undefine,continue next ", nodeIpDirs[a].getName()));
                        continue;
                    }

                    log.debug(String.format("nodeIpDir name %s is undefine begin", nodeIpFileName));
                    // 遍历用户目录
                    File userDirs[] = nodeIpDirs[a].listFiles();
                    for (int i = 0; i < userDirs.length; i++) {
                        if (userDirs[i].getName().equals(".") || userDirs[i].getName().equals("cache") || userDirs[i].isDirectory() == false) {
                            log.debug(String.format("userDir name %s is not statified,continue next ", userDirs[i].getName()));
                            continue;
                        }
                        log.debug(String.format("userDir name %s undefine begin", userDirs[i].getName()));
                        File instanceIds[] = userDirs[i].listFiles();
                        // 遍历用户的云服务器实例目录
                        for (int j = 0; j < instanceIds.length; j++) {
                            File instanceFile = instanceIds[j];

                            nodeInstanceIds.add(instanceFile.getName());

                        }
                        log.debug(String.format("userDir name %s undefine end", userDirs[i].getName()));
                    }
                    log.debug(String.format("nodeIpDir name %s is undefine end", nodeIpFileName));
                }
            }

            List<DomainListInfo> virshList = vmVirtual.listVm(null, true, true);
            if (virshList != null) {
                for (DomainListInfo vm : virshList) {
                    log.debug(String.format("instanceId %s check list start", vm.getDomainAlias()));
                    if (!nodeInstanceIds.contains(vm.getDomainAlias())) {

                        try {
                            log.debug(String.format("instanceId %s is not in the node", vm.getDomainAlias()));
                            DomainDetail detail = vmVirtual.getVmDetail(vm.getDomainAlias());
                            if (detail != null) {
                                // 排除容错监听的虚拟机
                                List<String> args = detail.getQemuArgs();
                                if (args != null) {
                                    if (args.contains("-incoming")) {
                                        log.debug(String.format("instanceId%s is ft vm", vm.getDomainAlias()));
                                        continue;
                                    }
                                }
                            }
                            log.debug(String.format("instanceId %s  destroy and undefine start", vm.getDomainAlias()));
                            //VmNodeUtil.destroyNoException(vm.getDomainAlias(), virMng);
                            vmVirtual.undefine(vm.getDomainAlias());
                            log.debug(String.format("instanceId %s  destroy and undefine end", vm.getDomainAlias()));
                        } catch (Exception ex) {
                            log.error("清理虚拟机，undefine失败", ex);
                        }

                    } else {
                        log.debug(String.format("instanceId %s is in the node", vm.getDomainAlias()));
                    }
                    log.debug(String.format("instanceId %s check list end", vm.getDomainAlias()));
                }
            }
        } catch (Exception e) {
            log.error("undefine Instances error:" + instanceConfigurePath + " end");
        } finally {
            log.debug("undefine Instances:" + instanceConfigurePath + " end");
        }
    }

    private void updateRemotePort(File instanceFile, VmDetail vm) {
        try {
            if (VmNodeUtil.isVmExist(instanceFile.getName())) {
                DomainDetail domainDetail = vmVirtual.getVmDetail(instanceFile.getName());
                if (domainDetail != null && (!"-1".equals(domainDetail.getRemotePort()))) {
                    vm.setRemotePort(domainDetail.getRemotePort());
                } else {
                    vm.setRemotePort(null);
                }
            }
        } catch (Exception e) {
            log.error("获取端口错误", e);
        }
    }

    private boolean isFileExists(String path) {
        File file = new File(path);
        if (!file.exists()) {
            log.error("the file isn't exist,path = " + path);
        }
        return file.exists();
    }

    /**
     * @return
     * @throws GCloudException
     * @Title: generateInstance
     * @Description: 通过配置文件生成云服务器实例对象
     * @date 2015-6-18 下午2:24:40
     */
    private VmDetail generateInstance(File instanceFile, VmDetail vm, String nodeIp) throws GCloudException {

        String instancePath = instanceFile.getPath();
        Date vmStart = new Date();
        log.debug(String.format("generateInstance instancePath %s begin ", instancePath));
        // 读取libvirt文件的内容
        Date start = new Date();
        log.debug(String.format("read libvirtXmlContent instancePath %s begin ", instancePath));
        vm = libvirtXmlContent(instanceFile);
        Date end = new Date();
        log.debug(String.format("read libvirtXmlContent instancePath %s end,耗时(ms):%s", instancePath, (end.getTime() - start.getTime())));
        if (vm.getVmDisableType() != null) {
            return vm;
        }
        // 读取instance文件的内容
        start = new Date();
        log.debug(String.format("read instanceXmlContent instancePath %s begin ", instancePath));
        vm = instanceXmlContent(vm, instancePath);
        end = new Date();
        log.debug(String.format("read instanceXmlContent instancePath %s end,耗时(ms):%s", instancePath, (end.getTime() - start.getTime())));
        if (vm.getVmDisableType() != null) {
            return vm;
        }
        // 获取userid
        vm.setUserId(VmNodeUtil.getUserId(instancePath, nodeIp, vm.getId()));
        // 查看是否有autostart.sh的脚本，有的话则是自启动，没有就不是自启动
        String startShPath = VmNodeUtil.getAutostartShPath(nodeIp, vm.getId());
        File file = new File(startShPath);
        if (file.exists()) {
            vm.setAutoStart(AutostartType.ENABLE.getValue());
        } else {
            vm.setAutoStart(AutostartType.DISABLE.getValue());
        }

        updateRemotePort(instanceFile, vm);

        Date vmEnd = new Date();
        log.debug(String.format("generateInstance instancePath %s finish end,耗时(ms):%s", instancePath, (vmEnd.getTime() - vmStart.getTime())));
        return vm;

    }

    /**
     * @return
     * @throws GCloudException
     * @Title: libvirtXmlContent2Bean
     * @Description: 读取libvirt，xml配置文件信息
     * @date 2015-5-18 下午3:46:17
     */
    private VmDetail libvirtXmlContent(File instanceFile) throws GCloudException {
        VmDetail vm = new VmDetail();
        vm.setId(instanceFile.getName());

        String instancePath = instanceFile.getPath();
        File libvirtXmlFile = new File(VmNodeUtil.getLibvirtXmlPath(instancePath));
        if (!libvirtXmlFile.exists() || !libvirtXmlFile.canRead()) {
            vm.setVmDisableType(VmDisableType.DISABLE2.getValue());
            return vm;
            // throw new GCloudException("libvirt配置文件不存在或者不可读");
        }

        List<VmVolumeDetail> vmDisks = new ArrayList<>();
        try {
            SAXReader reader = new SAXReader();
            Document doc = reader.read(libvirtXmlFile);
            vm.setId(doc.selectSingleNode("/domain/name").getText());
            vm.setId(doc.selectSingleNode("/domain/uuid").getText());

            if (doc.selectSingleNode("/domain/vcpu").getText() != null) {
                vm.setCore(Integer.parseInt(doc.selectSingleNode("/domain/vcpu").getText()));
            } else {
                Element vcpu = (Element) doc.selectSingleNode("/domain/vcpu");
                vm.setCore(Integer.parseInt(vcpu.attributeValue("current")));
            }

            if (doc.selectSingleNode("/domain/cpu") != null && doc.selectSingleNode("/domain/cpu/topology") != null) {
                Element cpuTopology = (Element) doc.selectSingleNode("/domain/cpu/topology");
                vm.setCpuCore(Integer.parseInt(cpuTopology.attributeValue("cores")));
                vm.setCpuSocket(Integer.parseInt(cpuTopology.attributeValue("sockets")));
                vm.setCpuThread(Integer.parseInt(cpuTopology.attributeValue("threads")));
            } else {
                //没有设置topology，libvirt默认是 *,1,1
                vm.setCpuSocket(vm.getCore());
                vm.setCpuCore(1);
                vm.setCpuThread(1);

            }

            if (doc.selectSingleNode("/domain/memory").getText() != null) {
                vm.setMemory(Integer.parseInt(doc.selectSingleNode("/domain/memory").getText()) / 1024);
            } else {
                vm.setMemory(Integer.parseInt(doc.selectSingleNode("/domain/currentMemory").getText()) / 1024);
            }

            if (CloudPlatform.DESKTOPGCLOUD.getValue().equals(prop.getCloudPlatform())) {

                List<Element> usbReDirE = (List<Element>) doc.selectNodes("/domain/devices/redirdev");
                Element graphic = (Element) doc.selectSingleNode("/domain/devices/graphics");
                if (usbReDirE != null && usbReDirE.size() > 0 && graphic != null && "spice".equals(graphic.attributeValue("type"))) {
                    vm.setIsUsbRedir(BooleanType.TRUE.getValue());
                } else {
                    vm.setIsUsbRedir(BooleanType.FALSE.getValue());
                }
            }

        } catch (Exception e) {
            log.error("libvirtXmlContent:", e);
            vm.setVmDisableType(VmDisableType.DISABLE2.getValue());
            return vm;
            // if(e instanceof GCloudException){
            // throw new GCloudException(e.getMessage());
            // }else{
            // throw new GCloudException("获取libvirt配置文件内容失败。");
            // }
        }
        vm.setVmDisks(vmDisks);
        return vm;
    }

    /**
     * @param vm
     * @param instancePath
     * @return
     * @throws GCloudException
     * @Title: instanceXmlContent
     * @Description: 读取instance.xml配置文件
     * @date 2015-6-18 下午2:27:36
     */
    private VmDetail instanceXmlContent(VmDetail vm, String instancePath) throws GCloudException {
        File instanceXmlFile = new File(VmNodeUtil.getInstanceXmlPath(instancePath));
        List<VmVolumeDetail> vmDisks = vm.getVmDisks();
        List<VmNetworkDetail> vmNetworks = vm.getVmNetwork();
        List<VmCdromDetail> vmCdroms = vm.getVmCdrom();
        int disk = 0;
        String storageType = null;

        if (!instanceXmlFile.exists() || !instanceXmlFile.canRead()) {
            log.error("instance配置文件不存在或者不可读。");
            vm.setVmDisableType(VmDisableType.DISABLE3.getValue());
            return vm;
            // throw new GCloudException("instance配置文件不存在或者不可读。");
        }
        try {
            SAXReader reader = new SAXReader();
            Document document;
            document = reader.read(instanceXmlFile);
            Element os = (Element) document.selectSingleNode("/instance/os");
            vm.setPlatform(os.attributeValue("platform"));
            // 镜像创建的时候
            Element imageIdE = (Element) document.selectSingleNode("/instance/imageId");
            String imageId = imageIdE == null ? null : imageIdE.getText();
            if(StringUtils.isNotBlank(imageId)){
                log.debug(String.format("获取镜像详情  instancePath = %s , imageId = %s  begin ", instancePath, imageId));
                vm.setImageId(imageId);
                Element imagePoolId = (Element) document.selectSingleNode("/instance/imagePoolId");
                if (imagePoolId != null) {
                    vm.setImagePoolId(imagePoolId.getText());
                }
                Element imageStorageType = (Element) document.selectSingleNode("/instance/imageStorageType");
                if (imageStorageType != null) {
                    vm.setImageStorageType(imageStorageType.getText());
                }

                Element imagePath = (Element) document.selectSingleNode("/instance/imagePath");
                if (imagePath != null) {
                    vm.setImagePath(imagePath.getText());
                    //判断镜像是否存在
                    try{
                        DiskQemuImgUtil.info(vm.getImagePath());
                    }catch (Exception ex){
                        log.error(String.format("镜像不存在，image path = %s", vm.getImagePath()), ex);
                        vm.setVmDisableType(VmDisableType.DISABLE4.getValue());
                        return vm;
                    }
                }
                log.debug(String.format("获取镜像详情  instancePath = %s , imageId = %s  end ", instancePath, imageId));

            }else{
                // iso创建的时候
                Element isoIdE = (Element) document.selectSingleNode("/instance/isoId");
                String isoId = isoIdE == null ? null : isoIdE.getText();
                vm.setIsoId(isoId);

                //iso 为空， imageId也是空
                if(StringUtils.isBlank(isoId)){
                    vm.setVmDisableType(VmDisableType.DISABLE4.getValue());
                    return vm;
                }

                Element isoPath = (Element) document.selectSingleNode("/instance/isoPath");
                if (isoPath != null) {
                }
                Element isoPoolId = (Element) document.selectSingleNode("/instance/isoPoolId");
                if (isoPoolId != null) {
                    vm.setIsoPoolId(isoPoolId.getText());
                }
                Element isoStorageType = (Element) document.selectSingleNode("/instance/isoStorageType");
                if (isoStorageType != null) {
                    vm.setIsoStorageType(isoStorageType.getText());
                }
            }


            // Element autostart = (Element)
            // document.selectSingleNode("/instance/autostart");
            // if(autostart != null){
            // if(autostart.getText().equals(AutostartType.ENABLE.getName())){
            // vm.setAutoStart(AutostartType.ENABLE.getValue());
            // }else{
            // vm.setAutoStart(AutostartType.DISABLE.getValue());
            // }
            // }
            // 获取磁盘信息
            List<Element> disksElement = (List<Element>) document.selectNodes("/instance/volume");
            for (Iterator<Element> i = disksElement.iterator(); i.hasNext(); ) {

                Element diskElement = i.next();
                String volumeId = diskElement.selectSingleNode("volumeId").getText();
                String volumePath = diskElement.selectSingleNode("volumePath").getText();
                String dev = diskElement.selectSingleNode("targetDev").getText();
                String protocol = diskElement.selectSingleNode("protocol").getText();

                String diskType = null;
                log.debug(String.format("获取块设备详细  instancePath = %s , volumeId = %s  begin ", instancePath, volumeId));

                log.debug(String.format("获取块设备的虚拟大小  instancePath = %s , volumePath = %s  begin ", instancePath, volumePath));
                try {
                    String diskPath = DiskUtil.getDiskPath(volumePath, DiskProtocol.value(protocol));
                    QemuInfo info = DiskQemuImgUtil.info(diskPath);
//                    Integer virtualSize = DiskUtil.getRoundDiskSizeMb(info.getVirtualSize());
                    if (dev.equals(Device.VDA.getValue())) {
                        diskType = DiskType.SYSTEM.getValue();
                    } else {
                        diskType = DiskType.DATA.getValue();
                    }
                    VmVolumeDetail diskDetail = new VmVolumeDetail();
                    diskDetail.setVirtualSize(info.virtualSizeGb());
                    diskDetail.setFileFormat(info.getFormat());
                    diskDetail.setSourcePath("volumePath");
                    diskDetail.setTargetDev(dev);
                    diskDetail.setBusType(BusType.VIRTIO.getValue());
                    diskDetail.setVolumeId(volumeId);
                    diskDetail.setDiskType(diskType);

                    vmDisks.add(diskDetail);
                } catch (Exception e) {
                    log.error("", e);
                    vm.setVmDisableType(VmDisableType.DISABLE8.getValue());
                    return vm;
                }
                log.debug(String.format("获取块设备的虚拟大小  instancePath = %s , volumePath = %s  end ", instancePath, volumePath));
            }

            vm.setDisk(disk);
            // 获取网络信息
            List<Element> networksElement = (List<Element>) document.selectNodes("/instance/network");
            for (Iterator<Element> i = networksElement.iterator(); i.hasNext(); ) {

                Element networkElement = i.next();
                String portId = networkElement.selectSingleNode("portId").getText();

                String brName = networkElement.selectSingleNode("brName") == null ? null : networkElement.selectSingleNode("brName").getText();
                String preName = networkElement.selectSingleNode("preName") == null ? null : networkElement.selectSingleNode("preName").getText();
                String aftName = networkElement.selectSingleNode("aftName") == null ? null : networkElement.selectSingleNode("aftName").getText();
                String customOvsBr = networkElement.selectSingleNode("customOvsBr") == null ? null : networkElement.selectSingleNode("customOvsBr").getText();

                String macAddress = networkElement.selectSingleNode("macAddress").getText();

                log.debug(String.format("判断 br pre aft instancePath = %s   begin ", instancePath));
                if (StringUtils.isNotBlank(customOvsBr)) {
                    if (!NetworkNodeUtil.isOvsBrExist(customOvsBr)) {
                        log.error(String.format("instanceId %s ovs 网桥不存在", vm.getId()));
                        vm.setVmDisableType(VmDisableType.DISABLE14.getValue());
                        return vm;
                    }
                } else {
                    if (!ComputeNetworkUtil.isNetExist(brName) || !ComputeNetworkUtil.isNetExist(preName) || !ComputeNetworkUtil.isOvsExist(aftName)) {
                        log.error(String.format("instanceId %s br不存在或者pre不存在或者aft不存在", vm.getId()));
                        vm.setVmDisableType(VmDisableType.DISABLE9.getValue());
                        return vm;
                    }
                }
                log.debug(String.format("判断 br pre aft instancePath = %s   end ", instancePath));

                log.debug(String.format("获取网卡信息 instancePath = %s ,portId = %s  begin ", instancePath, portId));
                VmNetworkDetail networkDetail = new VmNetworkDetail();
                networkDetail.setPortId(portId);
                networkDetail.setBrName(brName);
                networkDetail.setPreName(preName);
                networkDetail.setAftName(aftName);
                networkDetail.setCustomOvsBr(customOvsBr);
                networkDetail.setMacAddress(macAddress);
                networkDetail.setDeviceOwner(DeviceOwner.COMPUTE.getValue());

                vmNetworks.add(networkDetail);

                log.debug(String.format("获取网卡信息 instancePath = %s ,portId = %s  end ", instancePath, portId));

            }

            // 获取光盘信息
            List<Element> cdromsElement = (List<Element>) document.selectNodes("/instance/cdrom");
            for (Iterator<Element> i = cdromsElement.iterator(); i.hasNext(); ) {
                Element cdromElement = i.next();
                String cdromId = cdromElement.selectSingleNode("isoId").getText();
                String cdromPoolId = cdromElement.selectSingleNode("isoPoolId").getText();
                String cdromPath = cdromElement.selectSingleNode("isoPath").getText();
                String cdromStorageTypeStr = cdromElement.selectSingleNode("isoStorageType").getText();
                String cdromTargetDevice = cdromElement.selectSingleNode("isoTargetDevice").getText();
                Integer cdromStorageType = Integer.parseInt(cdromStorageTypeStr);
                log.debug(String.format("actvice cdrom instancePath = %s ,cdromId = %s  begin ", instancePath, cdromId));

                log.debug(String.format("actvice cdrom instancePath = %s ,cdromId = %s  end ", instancePath, cdromId));

                VmCdromDetail vmCdrom = new VmCdromDetail(cdromId, cdromPath, cdromPoolId, cdromStorageType, cdromTargetDevice);
                vmCdroms.add(vmCdrom);
            }
            vm.setVmCdrom(vmCdroms);
        } catch (Exception e) {
            log.error("InstanceXmlContent:", e);
            if (e instanceof GCloudException) {
                throw new GCloudException(e.getMessage());
            } else {
                throw new GCloudException("获取instance配置文件内容失败。");
            }
        }
        return vm;
    }

    @Override
    public Map<String, VmStateInfo> stateInfo() {

        Map<String, VmStateInfo> stateInfoMap = new HashMap<>();
        List<DomainListInfo> domainInfos = vmVirtual.listVm(null, true, false);
        if(domainInfos != null && domainInfos.size() > 0){
            for(DomainListInfo info : domainInfos){
                VmStateInfo state = new VmStateInfo();
                state.setInstanceId(info.getDomainAlias());
                state.setState(info.getState());
                state.setGcState(vmVirtual.changeStateForGcloud(info.getState()));
                stateInfoMap.put(info.getDomainAlias(), state);
            }
        }

        return stateInfoMap;
    }
}