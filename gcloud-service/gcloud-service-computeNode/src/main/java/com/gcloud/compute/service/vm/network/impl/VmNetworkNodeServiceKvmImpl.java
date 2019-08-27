package com.gcloud.compute.service.vm.network.impl;

import com.gcloud.common.util.StringUtils;
import com.gcloud.compute.prop.ComputeNodeProp;
import com.gcloud.compute.service.vm.base.IVmBaseNodeService;
import com.gcloud.compute.service.vm.network.IVmNetworkNodeService;
import com.gcloud.compute.util.ComputeNetworkUtil;
import com.gcloud.compute.util.ConfigFileUtil;
import com.gcloud.compute.util.VmNodeUtil;
import com.gcloud.compute.virtual.IVmVirtual;
import com.gcloud.core.condition.ConditionalHypervisor;
import com.gcloud.core.exception.GCloudException;
import com.gcloud.core.simpleflow.Flow;
import com.gcloud.core.simpleflow.FlowDoneHandler;
import com.gcloud.core.simpleflow.SimpleFlowChain;
import com.gcloud.core.util.ErrorCodeUtil;
import com.gcloud.header.compute.enums.VmState;
import com.gcloud.header.compute.msg.node.vm.model.VmNetworkDetail;
import com.gcloud.service.common.compute.model.DomainDetail;
import com.gcloud.service.common.compute.model.DomainInfo;
import com.gcloud.service.common.util.NetworkNodeUtil;
import lombok.extern.slf4j.Slf4j;
import org.dom4j.Document;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.File;

/**
 * Created by yaowj on 2018/11/13.
 */
@ConditionalHypervisor
@Slf4j
public class VmNetworkNodeServiceKvmImpl implements IVmNetworkNodeService {

    @Autowired
    private IVmVirtual vmVirtual;

    @Autowired
    private IVmBaseNodeService vmBaseNodeService;

    @Autowired
    private ComputeNodeProp nodeProp;


    private final int MAX_LOOP_TIME = 5;
    private final int SLEEP_TIME = 1000;

    @Override
    public void detachPort(String instanceId, VmNetworkDetail networkDetail) {

        DomainInfo domInfo = VmNodeUtil.checkVm(instanceId);
        if (domInfo == null) {
            throw new GCloudException("1010702::云服务器不存在");
        }
        String networkXmlPath = VmNodeUtil.getNetworkXmlPath(nodeProp.getNodeIp(), instanceId, networkDetail.getPortId());
        String libvirtPath = VmNodeUtil.getLibvirtXmlPath(nodeProp.getNodeIp(), instanceId);

        SimpleFlowChain<String, String> chain = new SimpleFlowChain<>("::detach port");
        chain.then(new Flow<String>("virsh attach port") {
            @Override
            public void run(SimpleFlowChain chain, String data) {

                String vmState = vmBaseNodeService.vmGcloudState(instanceId);
                if(VmState.RUNNING.value().equals(vmState)){
                    //卸载网卡
                    vmVirtual.detachDevice(instanceId, networkXmlPath);

                    boolean isDSucc = false;
                    for (int i = 0; i < MAX_LOOP_TIME; i++) {
                        DomainDetail domDetail = vmVirtual.getVmDetail(instanceId);
                        boolean isExist = VmNodeUtil.isInterfaceExistByMacAddress(domDetail, networkDetail.getMacAddress());
                        // 不存在，则已经卸载
                        if (!isExist) {
                            isDSucc = true;
                            break;
                        }
                        try{
                            Thread.sleep(SLEEP_TIME);
                        }catch (InterruptedException iex){
                            log.error("sleep error");
                        }

                    }

                    //没有卸载成功，抛出错误
                    if(!isDSucc){
                        log.error("detach-device execute successfully, but it didn't detach successfully");
                        throw new GCloudException("1010703::卸载命令执行成功，但是无法从云服务上卸载网卡");
                    }


                }

                chain.next();

            }

            @Override
            public void rollback(SimpleFlowChain chain, String data) {
                vmVirtual.attachDevice(instanceId, networkXmlPath);
            }

        }).then(new Flow<String>("change libvirt network interface") {
            @Override
            public void run(SimpleFlowChain chain, String data) {

                Document libvirtDoc = ConfigFileUtil.readXml(libvirtPath);
                ConfigFileUtil.deleteInterface(libvirtDoc, networkDetail.getMacAddress());
                ConfigFileUtil.doc2XmlFile(libvirtDoc, libvirtPath);
                chain.next();
            }

            @Override
            public void rollback(SimpleFlowChain chain, String data) {
                Document libvirtDoc = ConfigFileUtil.readXml(libvirtPath);
                ConfigFileUtil.addNetworkInfo(libvirtDoc, networkDetail);
                ConfigFileUtil.doc2XmlFile(libvirtDoc, libvirtPath);
                VmNodeUtil.redefineVm(instanceId, libvirtPath);
            }
        }).then(new Flow<String>() {
            @Override
            public void run(SimpleFlowChain chain, String data) {
                VmNodeUtil.redefineVm(instanceId, libvirtPath);
            }

            @Override
            public void rollback(SimpleFlowChain chain, String data) {

            }
        }).done(new FlowDoneHandler<String>() {
            @Override
            public void handle(String data) {
                //TODO 看收养如何处理
//                VmNodeUtil.removeNetworkDetail(vmIns, netcardId);
            }
        }).start();

        if (chain.getErrorCode() != null) {
            throw new GCloudException(chain.getErrorCode());
        }


    }

    @Override
    public void forceDetach(String instanceId, String portId, String macAddress) {
        String networkXmlPath = VmNodeUtil.getNetworkXmlPath(nodeProp.getNodeIp(), instanceId, portId);
        String libvirtPath = VmNodeUtil.getLibvirtXmlPath(nodeProp.getNodeIp(), instanceId);

        String vmState = vmBaseNodeService.vmGcloudState(instanceId);
        if(VmState.RUNNING.value().equals(vmState)){
            try{
                vmVirtual.detachDevice(instanceId, networkXmlPath);
            }catch (Exception ex){
                log.error("::卸载失败", ex);
            }
        }

        try{
            Document libvirtDoc = ConfigFileUtil.readXml(libvirtPath);
            ConfigFileUtil.deleteInterface(libvirtDoc, macAddress);
            ConfigFileUtil.doc2XmlFile(libvirtDoc, libvirtPath);
        }catch (Exception ex){
            log.error("::删除xml失败", ex);
        }

        try{
            VmNodeUtil.redefineVm(instanceId, libvirtPath);
        }catch (Exception ex){
            log.error("::redefine 失败", ex);
        }

    }

    @Override
    public void cleanNetEnvConfig(String instanceId, VmNetworkDetail networkDetail) {

        String brName = networkDetail.getBrName();
        String preName = networkDetail.getPreName();
        String aftName = networkDetail.getAftName();

        boolean isBrExist = ComputeNetworkUtil.isNetExist(brName);
        boolean isPairExist = ComputeNetworkUtil.isNetExist(preName);
        boolean isOvsExist = ComputeNetworkUtil.isOvsExist(aftName);

        SimpleFlowChain<String, String> chain = new SimpleFlowChain<>("config net env config");
        chain.then(new Flow<String>() {
            @Override
            public void run(SimpleFlowChain chain, String data) {
                //删除ovs
                if(isOvsExist){
                    NetworkNodeUtil.deleteOvs(aftName);
                }
                chain.next();
            }

            @Override
            public void rollback(SimpleFlowChain chain, String data) {
                NetworkNodeUtil.addOvs(networkDetail.getPortRefId(), instanceId, networkDetail.getMacAddress(), aftName);
            }
        }).then(new Flow<String>() {
            @Override
            public void run(SimpleFlowChain chain, String data) {
                if(isPairExist){
                    //删除bridge和veth
                    //删除pair一端，会自动删除另一端
                    NetworkNodeUtil.delIpLink(preName);
                }

                chain.next();
            }

            @Override
            public void rollback(SimpleFlowChain chain, String data) {
                NetworkNodeUtil.ipLinkVethPeer(preName, aftName);
                NetworkNodeUtil.ifconfigUp(preName);
                NetworkNodeUtil.ifconfigUp(aftName);
                NetworkNodeUtil.brAddIf(brName, preName);
            }
        }).then(new Flow<String>() {
            @Override
            public void run(SimpleFlowChain chain, String data) {
                if(isBrExist){
                    NetworkNodeUtil.ifconfigDown(brName);
                }
                chain.next();
            }

            @Override
            public void rollback(SimpleFlowChain chain, String data) {
                NetworkNodeUtil.ifconfigUp(brName);
            }
        }).then(new Flow<String>() {
            @Override
            public void run(SimpleFlowChain chain, String data) {
                if(isBrExist){
                    NetworkNodeUtil.delBr(brName);
                }
                chain.next();
            }

            @Override
            public void rollback(SimpleFlowChain chain, String data) {
                NetworkNodeUtil.addBr(brName);
            }
        }).start();

    }

    @Override
    public void forceCleanNetEnvConfig(String portId, VmNetworkDetail networkDetail) {
        String brName = networkDetail.getBrName();
        String preName = networkDetail.getPreName();
        String aftName = networkDetail.getAftName();

        boolean isBrExist = ComputeNetworkUtil.isNetExist(brName);
        boolean isPairExist = ComputeNetworkUtil.isNetExist(preName);
        boolean isOvsExist = ComputeNetworkUtil.isOvsExist(aftName);

        try{
            if(isOvsExist){
                NetworkNodeUtil.deleteOvs(aftName);
            }
        }catch (Exception ex){
            log.error("删除ovs失败", ex);
        }

        try{
            if(isPairExist){
                //删除bridge和veth
                //删除pair一端，会自动删除另一端
                NetworkNodeUtil.delIpLink(preName);
            }
        }catch (Exception ex){
            log.error("删除veth失败", ex);
        }

        try{
            if(isBrExist){
                NetworkNodeUtil.ifconfigDown(brName);
            }
        }catch (Exception ex){
            log.error("网桥down失败", ex);
        }

        try{
            if(isBrExist){
                NetworkNodeUtil.delBr(brName);
            }
        }catch (Exception ex){
            log.error("删除网桥失败", ex);
        }


    }

    @Override
    public void cleanNetConfigFile(String instanceId, VmNetworkDetail networkDetail) {

        String brName = networkDetail.getBrName();
        String preName = networkDetail.getPreName();
        String aftName = networkDetail.getAftName();

        String portId = networkDetail.getPortId();

        DomainInfo domInfo = VmNodeUtil.checkVm(instanceId);
        if (domInfo == null) {
            throw new GCloudException("1010706::云服务器不存在");
        }

        String networkXmlPath = VmNodeUtil.getNetworkXmlPath(nodeProp.getNodeIp(), instanceId, portId);
        String instanceXmlPath = VmNodeUtil.getInstanceXmlPath(nodeProp.getNodeIp(), instanceId);
        String networkStartShPath = VmNodeUtil.getNetStartPath(nodeProp.getNodeIp(), instanceId, portId);

        SimpleFlowChain<String, String> chain = new SimpleFlowChain<>("clean net config file");

        chain.then(new Flow<String>() {
            @Override
            public void run(SimpleFlowChain chain, String data) {
                File shFile = new File(networkStartShPath);
                if(shFile.exists()){
                    shFile.delete();
                }
                chain.next();
            }

            @Override
            public void rollback(SimpleFlowChain chain, String data) {

                ConfigFileUtil.addAutoStartNetSh(networkStartShPath, portId, instanceId, brName, preName, aftName, networkDetail.getMacAddress());

            }
        }).then(new Flow<String>() {
            @Override
            public void run(SimpleFlowChain chain, String data) {
                //删除instancexml信息
                ConfigFileUtil.deleteElementForXmlAndSave(instanceId, "/instance/network", "id", portId, instanceXmlPath);
                chain.next();
            }

            @Override
            public void rollback(SimpleFlowChain chain, String data) {
                /*写入配置文件，用于物理机重启时重新配置网络*/
                ConfigFileUtil.addNetInfoToInsXml(instanceId, instanceXmlPath, networkDetail.getPortId(), brName, preName, aftName, networkDetail.getMacAddress());
            }
        }).then(new Flow<String>() {
            @Override
            public void run(SimpleFlowChain chain, String data) {
                //删除网卡配置文件
                File file = new File(networkXmlPath);
                if(file.exists()){
                    file.delete();
                }
                chain.next();
            }

            @Override
            public void rollback(SimpleFlowChain chain, String data) {
                Document networkDoc = ConfigFileUtil.getHotNetcardDom(networkDetail);
                ConfigFileUtil.doc2XmlFile(networkDoc, networkXmlPath);
            }
        }).start();

    }

    @Override
    public void forceCleanNetConfigFile(String instanceId, VmNetworkDetail networkDetail) {

        String portId = networkDetail.getPortId();


        String networkXmlPath = VmNodeUtil.getNetworkXmlPath(nodeProp.getNodeIp(), instanceId, portId);
        String instanceXmlPath = VmNodeUtil.getInstanceXmlPath(nodeProp.getNodeIp(), instanceId);
        String networkStartShPath = VmNodeUtil.getNetStartPath(nodeProp.getNodeIp(), instanceId, portId);

        try{
            File shFile = new File(networkStartShPath);
            if(shFile.exists()){
                shFile.delete();
            }
        }catch (Exception ex){
            log.error("删除网络启动脚本失败", ex);
        }

        try{
            ConfigFileUtil.deleteElementForXmlAndSave(instanceId, "/instance/network", "id", portId, instanceXmlPath);
        }catch (Exception ex){
            log.error("修改instance配置失败", ex);
        }

        try{
            File file = new File(networkXmlPath);
            if(file.exists()){
                file.delete();
            }
        }catch (Exception ex){
            log.error("删除网络脚本失败", ex);
        }
    }

    @Override
    public void configNetEnv(String instanceUuid, VmNetworkDetail vmNetworkDetail) {

        if(StringUtils.isNotBlank(vmNetworkDetail.getCustomOvsBr())){
            configOvsBrNetEnv(vmNetworkDetail.getCustomOvsBr());
        }else{
            configPortNetEnv(instanceUuid, vmNetworkDetail);
        }

    }

    private void configOvsBrNetEnv(String customOvsBr){
//        try{
//            if(!NetworkNodeUtil.isOvsBrExist(customOvsBr)){
//                NetworkNodeUtil.addOvsBr(customOvsBr);
//            }
//        }catch (Exception ex){
//            if(!NetworkNodeUtil.isOvsBrExist(customOvsBr)){
//                log.error("1010604::创建ovs网桥失败", ex);
//                throw new GCloudException("1010604::创建ovs网桥失败");
//            }
//        }

        //创建ovs网桥交给 网络去做
        if(!NetworkNodeUtil.isOvsBrExist(customOvsBr)){
            throw new GCloudException("::ovs网桥不存在");
        }
    }

    private void configPortNetEnv(String instanceUuid, VmNetworkDetail vmNetworkDetail){

        String brName = vmNetworkDetail.getBrName();
        String aftName = vmNetworkDetail.getAftName();
        String preName = vmNetworkDetail.getPreName();

        try{
            boolean isBrExist = ComputeNetworkUtil.isNetExist(brName);
            if(!isBrExist){
                NetworkNodeUtil.addBr(brName);
            }

            NetworkNodeUtil.ifconfigUp(brName);

            boolean isPairExist = ComputeNetworkUtil.isNetExist(preName);
            if(!isPairExist){
                NetworkNodeUtil.ipLinkVethPeer(preName, aftName);

                NetworkNodeUtil.ifconfigUp(preName);
                NetworkNodeUtil.ifconfigUp(aftName);
                NetworkNodeUtil.brAddIf(brName, preName);
            }

            boolean isOvsExist = ComputeNetworkUtil.isOvsExist(aftName);
            if(!isOvsExist){
                NetworkNodeUtil.addOvs(vmNetworkDetail.getPortRefId(), instanceUuid, vmNetworkDetail.getMacAddress(), aftName);
            }
        }catch (Exception ex){
            rollbackPortNetConfig(brName, aftName, preName);
            throw new GCloudException(ErrorCodeUtil.getErrorCode(ex, "1010605::配置网络失败"));
        }

    }

    private void rollbackPortNetConfig(String brName, String aftName, String preName){

        boolean isBrExist = ComputeNetworkUtil.isNetExist(brName);
        boolean isPairExist = ComputeNetworkUtil.isNetExist(preName);
        boolean isOvsExist = ComputeNetworkUtil.isOvsExist(aftName);

        //删除一端，会自动删除另一端
        if(isPairExist){

            try{
                NetworkNodeUtil.delIpLink(preName);
            }catch(Exception be){
                log.error("回滚删除pair失败", be);
            }

        }

        if(isBrExist){
            try{
                NetworkNodeUtil.ifconfigDown(brName);
                NetworkNodeUtil.delBr(brName);
            }catch(Exception be){
                log.error("回滚删除br失败", be);
            }
        }

        if(isOvsExist){
            try{
                NetworkNodeUtil.deleteOvs(aftName);
            }catch(Exception be){
                log.error("回滚删除ovs失败", be);
            }
        }

    }


    @Override
    public void configNetFile(String instanceId, VmNetworkDetail vmNetworkDetail) {

        if(StringUtils.isNotBlank(vmNetworkDetail.getCustomOvsBr())){
            configOvsBrNetFile(instanceId, vmNetworkDetail);
        }else{
            configPortNetFile(instanceId, vmNetworkDetail);
        }
    }


    private void configOvsBrNetFile(String instanceId, VmNetworkDetail vmNetworkDetail){

        DomainInfo domInfo = VmNodeUtil.checkVm(instanceId);
        if (domInfo == null) {
            throw new GCloudException("1010607::云服务器不存在");
        }

        String networkXmlPath = VmNodeUtil.getNetworkXmlPath(nodeProp.getNodeIp(), instanceId, vmNetworkDetail.getPortId());
        String instanceXmlPath = VmNodeUtil.getInstanceXmlPath(nodeProp.getNodeIp(), instanceId);
        String networkStartShPath = VmNodeUtil.getNetStartPath(nodeProp.getNodeIp(), instanceId, vmNetworkDetail.getPortId());

        try{
            Document networkDoc = ConfigFileUtil.getHotNetcardDom(vmNetworkDetail);
            ConfigFileUtil.doc2XmlFile(networkDoc, networkXmlPath);

            /*写入配置文件，用于物理机重启时重新配置网络*/
            ConfigFileUtil.addCustomOvsBrToInsXml(instanceId, instanceXmlPath, vmNetworkDetail.getPortId(), vmNetworkDetail.getCustomOvsBr(), vmNetworkDetail.getMacAddress());

            ConfigFileUtil.addCustomOvsBrStartNetSh(networkStartShPath, vmNetworkDetail.getCustomOvsBr());

        }catch (Exception ex){
            rollbackConfigFile(instanceId, vmNetworkDetail.getPortId());
            throw new GCloudException(ErrorCodeUtil.getErrorCode(ex, "1010608::配置网卡配置文件失败"));
        }

    }

    private void configPortNetFile(String instanceId, VmNetworkDetail vmNetworkDetail){

        DomainInfo domInfo = VmNodeUtil.checkVm(instanceId);
        if (domInfo == null) {
            throw new GCloudException("1010609::云服务器不存在");
        }

        String brName = vmNetworkDetail.getBrName();
        String aftName = vmNetworkDetail.getAftName();
        String preName = vmNetworkDetail.getPreName();

        String networkXmlPath = VmNodeUtil.getNetworkXmlPath(nodeProp.getNodeIp(), instanceId, vmNetworkDetail.getPortId());
        String instanceXmlPath = VmNodeUtil.getInstanceXmlPath(nodeProp.getNodeIp(), instanceId);
        String networkStartShPath = VmNodeUtil.getNetStartPath(nodeProp.getNodeIp(), instanceId, vmNetworkDetail.getPortId());

        try{
            Document networkDoc = ConfigFileUtil.getHotNetcardDom(vmNetworkDetail);
            ConfigFileUtil.doc2XmlFile(networkDoc, networkXmlPath);

            /*写入配置文件，用于物理机重启时重新配置网络*/
            ConfigFileUtil.addNetInfoToInsXml(instanceId, instanceXmlPath, vmNetworkDetail.getPortId(), brName, preName, aftName, vmNetworkDetail.getMacAddress());

            ConfigFileUtil.addAutoStartNetSh(networkStartShPath, vmNetworkDetail.getPortId(), instanceId, brName, preName, aftName, vmNetworkDetail.getMacAddress());

        }catch (Exception ex){
            rollbackConfigFile(instanceId, vmNetworkDetail.getPortId());
            throw new GCloudException(ErrorCodeUtil.getErrorCode(ex, "1010610::配置网卡配置文件失败"));
        }


    }

    private void rollbackConfigFile(String instanceId, String portId){

        String nodeIp = nodeProp.getNodeIp();
        String networkXmlPath = VmNodeUtil.getNetworkXmlPath(nodeIp, instanceId, portId);
        String instanceXmlPath = VmNodeUtil.getInstanceXmlPath(nodeIp, instanceId);
        String networkStartShPath = VmNodeUtil.getNetStartPath(nodeIp, instanceId, portId);

        try{
            ConfigFileUtil.deleteElementForXmlAndSave(instanceId, "/instance/network", "id", portId, instanceXmlPath);
        }catch(Exception be){
            log.error("回滚删除insxml信息失败", be);
        }


        try{
            File file = new File(networkXmlPath);
            if(file.exists()){
                file.delete();
            }
        }catch(Exception be){
            log.error("回滚删除网络配置文件失败", be);
        }


        try{
            File file = new File(networkStartShPath);
            if(file.exists()){
                file.delete();
            }
        }catch(Exception be){
            log.error("回滚删除网络配置文件失败", be);
        }
    }



    @Override
    public void attachPort(String instanceId, VmNetworkDetail vmNetworkDetail) {

        DomainInfo domInfo = VmNodeUtil.checkVm(instanceId);
        if (domInfo == null) {
            throw new GCloudException("1010602::云服务器不存在");
        }

        String libvirtPath = VmNodeUtil.getLibvirtXmlPath(nodeProp.getNodeIp(), instanceId);
        String networkXmlPath = VmNodeUtil.getNetworkXmlPath(nodeProp.getNodeIp(), instanceId, vmNetworkDetail.getPortId());

        SimpleFlowChain<String, String> chain = new SimpleFlowChain<>();
        chain.then(new Flow<String>() {
            @Override
            public void run(SimpleFlowChain chain, String data) {
                Document libvirtDoc = ConfigFileUtil.readXml(libvirtPath);
                ConfigFileUtil.addNetworkInfo(libvirtDoc, vmNetworkDetail);
                ConfigFileUtil.doc2XmlFile(libvirtDoc, libvirtPath);
                chain.next();
            }

            @Override
            public void rollback(SimpleFlowChain chain, String data) {

                Document libvirtDoc = ConfigFileUtil.readXml(libvirtPath);
                ConfigFileUtil.deleteInterface(libvirtDoc, vmNetworkDetail.getMacAddress());
                ConfigFileUtil.doc2XmlFile(libvirtDoc, libvirtPath);

                VmNodeUtil.redefineVm(instanceId, libvirtPath);

            }
        }).then(new Flow<String>() {
            @Override
            public void run(SimpleFlowChain chain, String data) {

                String state = vmBaseNodeService.vmGcloudState(instanceId);
                if(state != null && VmState.RUNNING.value().equals(state)){
                    vmVirtual.attachDevice(instanceId, networkXmlPath);
                }
                chain.next();
            }

            @Override
            public void rollback(SimpleFlowChain chain, String data) {
                vmVirtual.detachDevice(instanceId, networkXmlPath);
            }
        }).then(new Flow<String>() {
            @Override
            public void run(SimpleFlowChain chain, String data) {
                VmNodeUtil.redefineVm(instanceId, libvirtPath);
                chain.next();
            }

            @Override
            public void rollback(SimpleFlowChain chain, String data) {

            }
        }).start();

        if (chain.getErrorCode() != null) {
            throw new GCloudException(chain.getErrorCode());
        }

    }


}
