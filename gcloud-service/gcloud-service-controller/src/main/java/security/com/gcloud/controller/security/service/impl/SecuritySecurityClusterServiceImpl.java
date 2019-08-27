package com.gcloud.controller.security.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.gcloud.common.util.StringUtils;
import com.gcloud.controller.compute.dao.ZoneDao;
import com.gcloud.controller.compute.dispatcher.Dispatcher;
import com.gcloud.controller.compute.entity.AvailableZoneEntity;
import com.gcloud.controller.compute.entity.VmInstance;
import com.gcloud.controller.network.dao.SecurityGroupDao;
import com.gcloud.controller.network.dao.SubnetDao;
import com.gcloud.controller.network.entity.SecurityGroup;
import com.gcloud.controller.network.entity.Subnet;
import com.gcloud.controller.network.enums.PortType;
import com.gcloud.controller.security.dao.SecurityClusterComponentDao;
import com.gcloud.controller.security.dao.SecurityClusterDao;
import com.gcloud.controller.security.dao.SecurityClusterInfoDao;
import com.gcloud.controller.security.dao.SecurityClusterNetworkConfigDao;
import com.gcloud.controller.security.dao.SecurityClusterOvsBridgeDao;
import com.gcloud.controller.security.dao.SecurityClusterSecurityGroupDao;
import com.gcloud.controller.security.dao.SecurityClusterSubnetDao;
import com.gcloud.controller.security.dao.SecurityClusterVmDao;
import com.gcloud.controller.security.entity.SecurityCluster;
import com.gcloud.controller.security.entity.SecurityClusterComponent;
import com.gcloud.controller.security.entity.SecurityClusterInfo;
import com.gcloud.controller.security.entity.SecurityClusterNetworkConfig;
import com.gcloud.controller.security.entity.SecurityClusterOvsBridge;
import com.gcloud.controller.security.entity.SecurityClusterSecurityGroup;
import com.gcloud.controller.security.entity.SecurityClusterSubnet;
import com.gcloud.controller.security.entity.SecurityClusterVm;
import com.gcloud.controller.security.enums.SecurityBaseState;
import com.gcloud.controller.security.enums.SecurityClusterComponentObjectType;
import com.gcloud.controller.security.enums.SecurityClusterComponentState;
import com.gcloud.controller.security.enums.SecurityClusterState;
import com.gcloud.controller.security.enums.SecurityComponent;
import com.gcloud.controller.security.enums.SecurityNetcardConfigType;
import com.gcloud.controller.security.enums.SecurityNetworkType;
import com.gcloud.controller.security.model.ApiListSecurityCluseterParams;
import com.gcloud.controller.security.model.ClusterCreateNetInfo;
import com.gcloud.controller.security.model.ClusterCreateNetcardInfo;
import com.gcloud.controller.security.model.ClusterCreateOvsBridgeInfo;
import com.gcloud.controller.security.model.ComponentResource;
import com.gcloud.controller.security.model.ComputeClusterCreateObjectInfo;
import com.gcloud.controller.security.model.CreateClusterParams;
import com.gcloud.controller.security.model.CreateClusterResponse;
import com.gcloud.controller.security.model.SecurityClusterAddableInstanceParams;
import com.gcloud.controller.security.model.DescribeSecurityClusterComponentParams;
import com.gcloud.controller.security.model.DescribeSecurityClusterParams;
import com.gcloud.controller.security.model.EnableClusterHaParams;
import com.gcloud.controller.security.model.EnableClusterHaResponse;
import com.gcloud.controller.security.model.ModifySecurityClusterParams;
import com.gcloud.controller.security.model.SecurityClusterAddInstanceParams;
import com.gcloud.controller.security.model.SecurityClusterDetailParams;
import com.gcloud.controller.security.model.SecurityClusterRemoveInstanceParams;
import com.gcloud.controller.security.model.SecurityClusterTopologyParams;
import com.gcloud.controller.security.model.workflow.CreateClusterComponentObjectInfo;
import com.gcloud.controller.security.model.workflow.SecurityClusterComponentHaNetcardInfo;
import com.gcloud.controller.security.service.ISecurityClusterComponentService;
import com.gcloud.controller.security.service.ISecurityClusterService;
import com.gcloud.controller.utils.OrderBy;
import com.gcloud.core.exception.GCloudException;
import com.gcloud.framework.db.PageResult;
import com.gcloud.header.api.model.CurrentUser;
import com.gcloud.header.security.model.SecurityClusterComponentType;
import com.gcloud.header.security.model.SecurityClusterDetailResponse;
import com.gcloud.header.security.model.SecurityClusterInfoType;
import com.gcloud.header.security.model.SecurityClusterInstanceType;
import com.gcloud.header.security.model.SecurityClusterListType;
import com.gcloud.header.security.model.SecurityClusterSubnetType;
import com.gcloud.header.security.model.SecurityClusterTopologyResponse;
import com.gcloud.header.security.model.SecurityClusterType;
import com.gcloud.header.security.msg.model.CreateClusterInfoParams;
import com.gcloud.header.security.msg.model.CreateClusterObjectParams;
import com.gcloud.header.security.msg.model.CreateNetworkParams;

import lombok.extern.slf4j.Slf4j;

@Service
@Transactional
@Slf4j
public class SecuritySecurityClusterServiceImpl implements ISecurityClusterService {

    @Autowired
    private SecurityClusterDao securityClusterDao;
    
    @Autowired
    private SecurityClusterVmDao securityClusterVmDao;

    @Autowired
    private SecurityClusterNetworkConfigDao securityClusterNetworkConfigDao;

    @Autowired
    private SecurityClusterSubnetDao securityClusterSubnetDao;

    @Autowired
    private SecurityClusterInfoDao securityClusterInfoDao;

    @Autowired
    private SubnetDao subnetDao;

    @Autowired
    private SecurityGroupDao securityGroupDao;

    @Autowired
    private SecurityClusterSecurityGroupDao securityClusterSecurityGroupDao;

    @Autowired
    private ISecurityClusterComponentService clusterComponentService;

    @Autowired
    private SecurityClusterOvsBridgeDao securityClusterOvsBridgeDao;

    @Autowired
    private SecurityClusterComponentDao securityClusterComponentDao;

    @Autowired
    private ZoneDao zoneDao;

    @Override
    public CreateClusterResponse createCluster(CreateClusterParams ccp, CurrentUser loginUser) {

        Map<String, Subnet> subnetMap = new HashMap<>();
        Map<String, SecurityGroup> securityGroupMap = new HashMap<>();
        Map<SecurityNetworkType, String> networkTypeSubnetMap = new HashMap<>();
        Map<SecurityNetworkType, String> networkTypeSecurityGroupMap = new HashMap<>();

        CreateNetworkParams protNet = ccp.getProtectionNet();
        if(protNet == null){
            throw new GCloudException("security_controller_cluster_010004::protection net can not be null");
        }

        checkNetwork(ccp.getProtectionNet(), SecurityNetworkType.PROTECTION, subnetMap, securityGroupMap);
        checkNetwork(ccp.getManagementNet(), SecurityNetworkType.MANAGEMENT, subnetMap, securityGroupMap);
        checkNetwork(ccp.getOuterNet(), SecurityNetworkType.OUTER, subnetMap, securityGroupMap);

        networkTypeSubnetMap.put(SecurityNetworkType.PROTECTION, ccp.getProtectionNet().getSubnetId());
        networkTypeSubnetMap.put(SecurityNetworkType.MANAGEMENT, ccp.getManagementNet().getSubnetId());
        networkTypeSubnetMap.put(SecurityNetworkType.OUTER, ccp.getOuterNet().getSubnetId());

        networkTypeSecurityGroupMap.put(SecurityNetworkType.PROTECTION, ccp.getProtectionNet().getSecurityGroupId());
        networkTypeSecurityGroupMap.put(SecurityNetworkType.MANAGEMENT, ccp.getManagementNet().getSecurityGroupId());
        networkTypeSecurityGroupMap.put(SecurityNetworkType.OUTER, ccp.getOuterNet().getSecurityGroupId());

        List<SecurityClusterSubnet> exitSubnet = securityClusterSubnetDao.findByProperty("subnetId", protNet.getSubnetId());
        if(exitSubnet != null && exitSubnet.size() > 0){
            throw new GCloudException("security_controller_cluster_010005::protection net can be only linked to a cluster");
        }

        boolean isHa = ccp.getHa() != null && ccp.getHa();


        Set<String> subnetIds = new HashSet<String>();

        if(ccp.getCreateInfo() == null){
            throw new GCloudException("security_controller_cluster_010031::create info cannot be null");
        }

        if(StringUtils.isBlank(ccp.getCreateInfo().getZoneId())){
            throw new GCloudException("security_controller_cluster_010031::target host cannot be null");
        }

        if(StringUtils.isBlank(ccp.getCreateInfo().getHostName())){
            throw new GCloudException("security_controller_cluster_010031::target host cannot be null");
        }

        if(ccp.getProtectionNet() == null){
            throw new GCloudException("security_controller_cluster_010027::protection net cannot be null");
        }

        if(!subnetIds.add(ccp.getProtectionNet().getSubnetId())){
            throw new GCloudException("security_controller_cluster_010023::protection net cannot be the same");
        }


        if(ccp.getOuterNet() == null){
            throw new GCloudException("security_controller_cluster_010028::outer net cannot be null");
        }

        if(!subnetIds.add(ccp.getOuterNet().getSubnetId())){
            throw new GCloudException("security_controller_cluster_010024::outer net cannot be the same");
        }

        if(ccp.getManagementNet() == null){
            throw new GCloudException("security_controller_cluster_010029::management net cannot be null");
        }

        if(!subnetIds.add(ccp.getManagementNet().getSubnetId())){
            throw new GCloudException("security_controller_cluster_010025::management net cannot be the same");
        }

        if(isHa){
            if(ccp.getHaInfo() == null || ccp.getHaInfo().getNet() == null){
                throw new GCloudException("security_controller_cluster_010030::ha net cannot be null");
            }
            if(!subnetIds.add(ccp.getHaInfo().getNet().getSubnetId())){
                throw new GCloudException("security_controller_cluster_010026::ha net cannot be the same");
            }
        }

        AvailableZoneEntity zone = zoneDao.getById(ccp.getCreateInfo().getZoneId());
        if(zone == null){
            throw new GCloudException("::zone not found");
        }


        SecurityCluster cluster = new SecurityCluster();
        cluster.setId(UUID.randomUUID().toString());
        cluster.setName(ccp.getName());
        cluster.setDescription(ccp.getDescription());
        cluster.setState(SecurityClusterState.CREATING.value());
        cluster.setCreateTime(new Date());
        cluster.setCreateUser(loginUser.getId());
        cluster.setCreateTime(new Date());
        cluster.setUpdateTime(new Date());
        cluster.setHa(isHa);
        cluster.setZoneId(ccp.getCreateInfo().getZoneId());


        securityClusterDao.save(cluster);

        SecurityClusterInfo clusterInfo = new SecurityClusterInfo();
        clusterInfo.setClusterId(cluster.getId());
        clusterInfo.setHostname(ccp.getCreateInfo().getHostName());
        clusterInfo.setHa(false);

        securityClusterInfoDao.save(clusterInfo);

        saveNetwork(cluster.getId(), ccp.getProtectionNet(), SecurityNetworkType.PROTECTION, loginUser.getId(), subnetMap, securityGroupMap);
        saveNetwork(cluster.getId(), ccp.getManagementNet(), SecurityNetworkType.MANAGEMENT, loginUser.getId(), subnetMap, securityGroupMap);
        saveNetwork(cluster.getId(), ccp.getOuterNet(), SecurityNetworkType.OUTER, loginUser.getId(), subnetMap, securityGroupMap);

        if(isHa){
            if(ccp.getHaInfo() == null){
                throw new GCloudException("security_controller_cluster_010006::ha configuration can not be null");
            }

            if(ccp.getHaInfo().getCreateInfo() == null || StringUtils.isBlank(ccp.getHaInfo().getCreateInfo().getHostName())){
                throw new GCloudException("security_controller_cluster_010031::ha target host cannot be null");
            }

            SecurityClusterInfo haClusterInfo = new SecurityClusterInfo();
            haClusterInfo.setClusterId(cluster.getId());
            haClusterInfo.setHostname(ccp.getHaInfo().getCreateInfo().getHostName());
            haClusterInfo.setHa(true);

            securityClusterInfoDao.save(haClusterInfo);

            checkNetwork(ccp.getHaInfo().getNet(), SecurityNetworkType.HA, subnetMap, securityGroupMap);
            saveNetwork(cluster.getId(), ccp.getHaInfo().getNet(), SecurityNetworkType.HA, loginUser.getId(), subnetMap, securityGroupMap);

            networkTypeSubnetMap.put(SecurityNetworkType.HA, ccp.getHaInfo().getNet().getSubnetId());
            networkTypeSecurityGroupMap.put(SecurityNetworkType.HA, ccp.getHaInfo().getNet().getSecurityGroupId());
        }


        ComponentResource resource = new ComponentResource();
        ComponentResource haResource = new ComponentResource();

        List<ComputeClusterCreateObjectInfo> components = new ArrayList<>();
        List<ComputeClusterCreateObjectInfo> haComponents = new ArrayList<>();
        if(ccp.getFirewall() != null){
            handleComponent(ccp.getCreateInfo(), isHa ? ccp.getHaInfo().getCreateInfo() : null, ccp.getFirewall(), isHa, cluster, loginUser, resource, haResource, SecurityComponent.FIREWALL, components, haComponents);
        }


        if(ccp.getWaf() != null){
            handleComponent(ccp.getCreateInfo(), isHa ? ccp.getHaInfo().getCreateInfo() : null, ccp.getWaf(), isHa, cluster, loginUser, resource, haResource, SecurityComponent.WAF, components, haComponents);
        }

        if(ccp.getIsms() != null){
            handleComponent(ccp.getCreateInfo(), isHa ? ccp.getHaInfo().getCreateInfo() : null, ccp.getIsms(), isHa, cluster, loginUser, resource, haResource, SecurityComponent.ISMS, components, haComponents);
        }


        if(ccp.getFortress() != null){
            handleComponent(ccp.getCreateInfo(), isHa ? ccp.getHaInfo().getCreateInfo() : null, ccp.getFortress(), isHa, cluster, loginUser, resource, haResource, SecurityComponent.FORTRESS, components, haComponents);
        }

        List<ClusterCreateOvsBridgeInfo> createOvsBridgeInfos = new ArrayList<>();
        List<SecurityClusterNetworkConfig> networkConfigs = securityClusterNetworkConfigDao.findAll(SecurityClusterNetworkConfig.ORDERS, OrderBy.OrderType.ASC.value());
        ClusterCreateNetInfo comNetworkConfig = getNetConfig(networkConfigs, false, cluster.getId(), ccp.getCreateInfo().getHostName(), networkTypeSubnetMap, networkTypeSecurityGroupMap);
        if(comNetworkConfig != null && comNetworkConfig.getCreateOvsBridgeInfos() != null){
            createOvsBridgeInfos.addAll(comNetworkConfig.getCreateOvsBridgeInfos());
        }

        ClusterCreateNetInfo haComNetworkConfig = null;
        if(isHa){
            haComNetworkConfig = getNetConfig(networkConfigs, true, cluster.getId(), ccp.getHaInfo().getCreateInfo().getHostName(), networkTypeSubnetMap, networkTypeSecurityGroupMap);
            if(haComNetworkConfig != null && haComNetworkConfig.getCreateOvsBridgeInfos() != null){
                createOvsBridgeInfos.addAll(haComNetworkConfig.getCreateOvsBridgeInfos());
            }
        }



        List<CreateClusterComponentObjectInfo> createObjectInfos = new ArrayList<>();
        //comNetworkConfig
        //components
        for(ComputeClusterCreateObjectInfo objInfo : components){
            createObjectInfos.add(toClusterComponentObjectParams(false, isHa, ccp.getCreateInfo(), objInfo, comNetworkConfig, haComNetworkConfig));
        }

        for(ComputeClusterCreateObjectInfo objInfo : haComponents){
            createObjectInfos.add(toClusterComponentObjectParams(true, isHa, ccp.getHaInfo().getCreateInfo(), objInfo, comNetworkConfig, haComNetworkConfig));
        }


        //如果是同一个节点，要一起判断
        if (isHa && ccp.getCreateInfo().getHostName().equals(ccp.getHaInfo().getCreateInfo().getHostName())) {
            int checkTotalCore = resource.getCore() + haResource.getCore();
            int checkTotalMemory = resource.getMemory() + haResource.getMemory();
            if(!Dispatcher.dispatcher().checkResource(ccp.getCreateInfo().getHostName(), checkTotalCore, checkTotalMemory)){
                throw new GCloudException("::has no enough resource");
            }

        }else{

            //和上面分开，先判断主节点，再判断ha节点。体验好一点
            if(!Dispatcher.dispatcher().checkResource(ccp.getCreateInfo().getHostName(), resource.getCore(), resource.getMemory())){
                throw new GCloudException("::has no enough resource");
            }

            if(isHa){
                if(!Dispatcher.dispatcher().checkResource(ccp.getHaInfo().getCreateInfo().getHostName(), haResource.getCore(), haResource.getMemory())){
                    throw new GCloudException("security_controller_cluster_010033::HA节点资源不足");
                }
            }
        }


        CreateClusterResponse response = new CreateClusterResponse();
        response.setId(cluster.getId());
        response.setBridgeInfos(createOvsBridgeInfos);
        response.setComponents(createObjectInfos);
        response.setHa(isHa);

        return response;
    }

    @Override
    public void delete(String clusterId){

        SecurityCluster cluster = securityClusterDao.getById(clusterId);
        if(cluster == null){
            throw new GCloudException("::集群不存在");
        }

        SecurityClusterState state = SecurityClusterState.getByValue(cluster.getState());
        //正在执行操作，不能删除
        if(SecurityBaseState.RUNNING == state.getSecurityBaseState()){
            throw new GCloudException("security_controller_cluster_070004::cluster is executing others operation");
        }

        securityClusterDao.changeState(cluster.getId(), SecurityClusterState.DELETING.value());

    }

    @Override
    public EnableClusterHaResponse enableHa(EnableClusterHaParams params, CurrentUser loginUser){

        SecurityCluster cluster = securityClusterDao.getById(params.getId());
        if(cluster == null){
            throw new GCloudException("::集群不存在");
        }

        SecurityClusterState state = SecurityClusterState.getByValue(cluster.getState());
        //正在执行操作，不能删除
        if(SecurityBaseState.RUNNING == state.getSecurityBaseState()){
            throw new GCloudException("security_controller_cluster_080004::cluster is executing others operation");
        }

        if(SecurityBaseState.SUCCESS != state.getSecurityBaseState()){
            throw new GCloudException("security_controller_cluster_080005::cluster state is not correct");
        }

        //已经是HA
        if(cluster.getHa() != null && cluster.getHa()){
            throw new GCloudException("security_controller_cluster_080006::cluster state is already in ha");
        }

        Map<String, Object> componentFilter = new HashMap<>();
        componentFilter.put(SecurityClusterComponent.CLUSTER_ID, params.getId());
        componentFilter.put(SecurityClusterComponent.HA, false);
        List<SecurityClusterComponent> components = securityClusterComponentDao.findByProperties(componentFilter);
        if(components == null || components.size() == 0){
            throw new GCloudException("security_controller_cluster_080007::components is not found");
        }

        if(params.getNet() == null){
            throw new GCloudException("security_controller_cluster_080009::ha net cannot be null");
        }

        List<SecurityClusterSubnet> subnets = securityClusterSubnetDao.findByProperty(SecurityClusterSubnet.CLUSTER_ID, cluster.getId());
        if(subnets != null && subnets.size() > 0){
            for(SecurityClusterSubnet subnet : subnets){
                if(subnet.getSubnetId().equals(params.getNet().getSubnetId())){
                    throw new GCloudException("security_controller_cluster_080010::ha net cannot be the same");
                }
            }
        }

        if(params.getCreateInfo() == null || StringUtils.isBlank(params.getCreateInfo().getHostName())){
            throw new GCloudException("security_controller_cluster_080016::target host cannot be null");
        }


        SecurityClusterInfo clusterInfo = new SecurityClusterInfo();
        clusterInfo.setClusterId(cluster.getId());
        clusterInfo.setHostname(params.getCreateInfo().getHostName());
        clusterInfo.setHa(true);

        securityClusterInfoDao.save(clusterInfo);

        securityClusterDao.changeState(cluster.getId(), SecurityClusterState.ENABLING_HA.value());


        List<SecurityClusterSecurityGroup> securityGroups = securityClusterSecurityGroupDao.findByProperty(SecurityClusterSecurityGroup.CLUSTER_ID, cluster.getId());

        Map<SecurityNetworkType, String> networkTypeSubnetMap = new HashMap<>();
        Map<SecurityNetworkType, String> networkTypeSecurityGroupMap = new HashMap<>();

        if(subnets == null || subnets.size() == 0 ){
            throw new GCloudException("security_controller_cluster_080008::subnet is not found");
        }

        for(SecurityClusterSubnet subnet : subnets){
            SecurityNetworkType networkType = SecurityNetworkType.getByValue(subnet.getNetworkType());
            if(networkType != null){
                networkTypeSubnetMap.put(networkType, subnet.getSubnetId());
            }
        }

        if(securityGroups != null && securityGroups.size() > 0){
            for(SecurityClusterSecurityGroup securityGroup : securityGroups){
                SecurityNetworkType networkType = SecurityNetworkType.getByValue(securityGroup.getNetworkType());
                if(networkType != null){
                    networkTypeSecurityGroupMap.put(networkType, securityGroup.getSecurityGroupId());
                }
            }
        }

        Map<String, Subnet> subnetMap = new HashMap<>();
        Map<String, SecurityGroup> securityGroupMap = new HashMap<>();

        checkNetwork(params.getNet(), SecurityNetworkType.HA, subnetMap, securityGroupMap);
        saveNetwork(cluster.getId(), params.getNet(), SecurityNetworkType.HA, loginUser.getId(), subnetMap, securityGroupMap);

        networkTypeSubnetMap.put(SecurityNetworkType.HA, params.getNet().getSubnetId());
        networkTypeSecurityGroupMap.put(SecurityNetworkType.HA, params.getNet().getSecurityGroupId());

        List<SecurityClusterNetworkConfig> networkConfigs = securityClusterNetworkConfigDao.findAll(SecurityClusterNetworkConfig.ORDERS, OrderBy.OrderType.ASC.value());

        List<CreateClusterComponentObjectInfo> createObjectInfos = new ArrayList<>();

        ClusterCreateNetInfo haComNetworkConfig = getNetConfig(networkConfigs, true, cluster.getId(), params.getCreateInfo().getHostName(), networkTypeSubnetMap, networkTypeSecurityGroupMap);

        Map<SecurityClusterComponentObjectType, Map<String, CreateClusterObjectParams>> objectTypeMap = clusterComponentService.getComponentConfig(components, params.getCreateInfo());

        for(SecurityClusterComponent component : components){

            SecurityComponent type = SecurityComponent.getByValue(component.getType());
            if(!type.getHasHa()){
                continue;
            }
            SecurityClusterComponentObjectType objectType = SecurityClusterComponentObjectType.getByValue(component.getObjectType());
            if(objectType == null){
                throw new GCloudException("::对象不存在");
            }

            Map<String, CreateClusterObjectParams> objInfos = objectTypeMap.get(objectType);
            if(objInfos == null || objInfos.get(component.getObjectId()) == null){
                throw new GCloudException("::对象不存在");
            }

            if(SecurityClusterComponentObjectType.VM.equals(objectType)){

                CreateClusterObjectParams objParam = objInfos.get(component.getObjectId());
                if(objParam == null){
                    throw new GCloudException("security_controller_cluster_080015::fail to get vm info");
                }

                ComputeClusterCreateObjectInfo vmInfo = clusterComponentService.createClusterComponentVm(cluster.getId(), cluster.getName(), loginUser, params.getCreateInfo(), objParam.getVm(), type, true);
                createObjectInfos.add(toClusterComponentObjectParams(true, true, params.getCreateInfo(), vmInfo, null, haComNetworkConfig));
            }


        }

        //获取防火墙
        Map<String, Object> firewallFilter = new HashMap<>();
        firewallFilter.put(SecurityClusterComponent.CLUSTER_ID, params.getId());
        firewallFilter.put(SecurityClusterComponent.HA, false);
        firewallFilter.put(SecurityClusterComponent.TYPE, SecurityComponent.FIREWALL.value());
        SecurityClusterComponent firewallComponent = securityClusterComponentDao.findUniqueByProperties(firewallFilter);

        //获取防火墙ha网卡
        List<ClusterCreateNetcardInfo> firewallNetInfos = haComNetworkConfig.getCreateNetcardInfo().get(SecurityComponent.FIREWALL);
        ClusterCreateNetcardInfo firewallNetInfo = null;
        if(firewallNetInfos != null && firewallNetInfos.size() > 0){
            for(ClusterCreateNetcardInfo netInfo : firewallNetInfos){
                if(SecurityNetworkType.HA == netInfo.getSecurityNetworkType()){
                    firewallNetInfo = netInfo;
                }
            }
        }

        List<SecurityClusterComponentHaNetcardInfo> netcardInfos = new ArrayList<>();

        SecurityClusterComponentHaNetcardInfo firewallHaInfo = new SecurityClusterComponentHaNetcardInfo();
        firewallHaInfo.setComponentId(firewallComponent.getId());
        firewallHaInfo.setObjectId(firewallComponent.getObjectId());
        firewallHaInfo.setObjectType(firewallComponent.getObjectType());
        firewallHaInfo.setNetcardInfo(firewallNetInfo);
        netcardInfos.add(firewallHaInfo);

        EnableClusterHaResponse response = new EnableClusterHaResponse();
        response.setId(params.getId());
        response.setBridgeInfos(haComNetworkConfig.getCreateOvsBridgeInfos());
        response.setComponents(createObjectInfos);
        response.setHaNetcardInfos(netcardInfos);

        return response;
    }

    @Override
    public void disable(String clusterId, CurrentUser loginUser){

        SecurityCluster cluster = securityClusterDao.getById(clusterId);
        if(cluster == null){
            throw new GCloudException("::集群不存在");
        }

        SecurityClusterState state = SecurityClusterState.getByValue(cluster.getState());
        //正在执行操作，不能删除
        if(SecurityBaseState.RUNNING == state.getSecurityBaseState()){
            throw new GCloudException("security_controller_cluster_080004::cluster is executing others operation");
        }

        if(SecurityBaseState.SUCCESS != state.getSecurityBaseState()){
            throw new GCloudException("security_controller_cluster_080005::cluster state is not correct");
        }

        //已经不是是HA
        if(cluster.getHa() != null && !cluster.getHa()){
            throw new GCloudException("security_controller_cluster_080006::cluster state is already in ha");
        }


        securityClusterDao.changeState(cluster.getId(), SecurityClusterState.DISABLING_HA.value());

    }

    @Override
    public void cleanClusterData(String clusterId) {
        securityClusterDao.deleteById(clusterId);
        securityClusterSubnetDao.deleteByClusterId(clusterId);
        securityClusterSecurityGroupDao.deleteByClusterId(clusterId);
        securityClusterComponentDao.deleteByClusterId(clusterId);
        securityClusterOvsBridgeDao.deleteByClusterId(clusterId);
        securityClusterInfoDao.deleteByClusterId(clusterId);
    }

    @Override
    public void cleanClusterHaData(String clusterId) {

        securityClusterSubnetDao.deleteHaByClusterId(clusterId);
        securityClusterSecurityGroupDao.deleteHaByClusterId(clusterId);
        securityClusterComponentDao.deleteHaByClusterId(clusterId);
        securityClusterOvsBridgeDao.deleteHaByClusterId(clusterId);
        securityClusterInfoDao.deleteHaByClusterId(clusterId);
    }

    public void checkNetwork(CreateNetworkParams cnp, SecurityNetworkType securityNetworkType, Map<String, Subnet> subnetMap, Map<String, SecurityGroup> securityGroupMap) throws GCloudException{

        if(StringUtils.isBlank(cnp.getSubnetId())){
            throw new GCloudException("security_controller_cluster_010007::subnet can not be null");
        }

        Subnet subnet = subnetMap.get(cnp.getSubnetId());
        if(subnet == null){
            subnet = subnetDao.getById(cnp.getSubnetId());
        }

        if(subnet == null){
            throw new GCloudException("security_controller_cluster_010009::subnet does not exist");
        }else{
            subnetMap.put(cnp.getSubnetId(), subnet);
        }

        if(StringUtils.isBlank(cnp.getNetworkId()) || !cnp.getNetworkId().equals(subnet.getNetworkId())){
            throw new GCloudException("security_controller_cluster_010010::network info is not correct");
        }

        if(securityNetworkType.getNeedSecurityGroup()){
            SecurityGroup securityGroup = securityGroupMap.get(cnp.getSecurityGroupId());
            if(securityGroup == null){
                securityGroup = securityGroupDao.getById(cnp.getSecurityGroupId());
            }

            if(securityGroup == null){
                throw new GCloudException("security_controller_cluster_010012::security group does not exist");
            }else{
                securityGroupMap.put(cnp.getSecurityGroupId(), securityGroup);
            }
        }

    }


    public void saveNetwork(String clusterId, CreateNetworkParams cnp, SecurityNetworkType securityNetworkType, String userId, Map<String, Subnet> subnetMap, Map<String, SecurityGroup> securityGroupMap){

        if(cnp != null){
            Subnet subnet = subnetMap.get(cnp.getSubnetId());
            SecurityClusterSubnet clusterSubnet = new SecurityClusterSubnet();
            clusterSubnet.setClusterId(clusterId);
            clusterSubnet.setSubnetId(subnet.getId());
            clusterSubnet.setNetworkId(subnet.getNetworkId());
            clusterSubnet.setCreateUser(userId);
            clusterSubnet.setCreateTime(new Date());
            clusterSubnet.setUpdateTime(new Date());
            clusterSubnet.setNetworkType(securityNetworkType.value());
            clusterSubnet.setCidr(subnet.getCidr());
            securityClusterSubnetDao.save(clusterSubnet);

            if(securityNetworkType.getNeedSecurityGroup()){
                SecurityGroup sg = securityGroupMap.get(cnp.getSecurityGroupId());
                SecurityClusterSecurityGroup clusterSg = new SecurityClusterSecurityGroup();
                clusterSg.setClusterId(clusterId);
                clusterSg.setSecurityGroupId(sg.getId());
                clusterSg.setCreateUser(userId);
                clusterSg.setCreateTime(new Date());
                clusterSg.setUpdateTime(new Date());
                clusterSg.setNetworkType(securityNetworkType.value());
                securityClusterSecurityGroupDao.save(clusterSg);
            }

        }

    }

    private void handleComponent(CreateClusterInfoParams clusterParams, CreateClusterInfoParams haClusterParams, CreateClusterObjectParams objectParams, boolean isHa, SecurityCluster cluster, CurrentUser loginUser, ComponentResource resource, ComponentResource haResource, SecurityComponent component, List<ComputeClusterCreateObjectInfo> components, List<ComputeClusterCreateObjectInfo> haComponents){

        if(SecurityClusterComponentObjectType.VM.value().equals(objectParams.getObjectType())){
            ComputeClusterCreateObjectInfo info = clusterComponentService.createClusterComponentVm(cluster.getId(), cluster.getName(), loginUser, clusterParams, objectParams.getVm(), component, false);
            components.add(info);
            resource.increaseCore(info.getCreateVm().getInstanceType().getVcpus());
            resource.increaseMemory(info.getCreateVm().getInstanceType().getMemoryMb());
            if(isHa && component.getHasHa()){
                ComputeClusterCreateObjectInfo haInfo = clusterComponentService.createClusterComponentVm(cluster.getId(), cluster.getName(), loginUser, haClusterParams, objectParams.getVm(), component, true);
                haComponents.add(haInfo);
                haResource.increaseCore(haInfo.getCreateVm().getInstanceType().getVcpus());
                haResource.increaseMemory(haInfo.getCreateVm().getInstanceType().getMemoryMb());
            }
        }else if(SecurityClusterComponentObjectType.DOCKER_CONTAINER.value().equals(objectParams.getObjectType())){
            components.add(clusterComponentService.createClusterComponentDc(cluster.getId(), cluster.getName(), loginUser, clusterParams, objectParams.getDc(), SecurityComponent.FIREWALL, false));
            if(isHa && component.getHasHa()){
                haComponents.add(clusterComponentService.createClusterComponentDc(cluster.getId(), cluster.getName(), loginUser, haClusterParams, objectParams.getDc(), SecurityComponent.FIREWALL, true));
            }
        }

    }


    public ClusterCreateNetInfo getNetConfig(List<SecurityClusterNetworkConfig> networkConfigs, boolean isHa, String clusterId, String hostname, Map<SecurityNetworkType, String> networkTypeSubnetMap, Map<SecurityNetworkType, String> networkTypeSecurityGroupMap) throws GCloudException{

        ClusterCreateNetInfo createNetInfo = new ClusterCreateNetInfo();

        Map<SecurityComponent, List<ClusterCreateNetcardInfo>> comNetworkConfigMap = new HashMap<>();
        Map<String, SecurityClusterOvsBridge> ovsBrMap = new HashMap<>();
        List<ClusterCreateOvsBridgeInfo> createOvsBridgeInfos = new ArrayList<>();

        createNetInfo.setCreateNetcardInfo(comNetworkConfigMap);
        createNetInfo.setCreateOvsBridgeInfos(createOvsBridgeInfos);


        if(networkConfigs != null && networkConfigs.size() > 0){
            for(SecurityClusterNetworkConfig networkConfig : networkConfigs){

                ClusterCreateNetcardInfo netInfo = new ClusterCreateNetcardInfo();

                SecurityNetworkType securityNetworkType = SecurityNetworkType.getByValue(networkConfig.getNetworkType());
                SecurityNetcardConfigType securityNetcardConfigType = SecurityNetcardConfigType.getByValue(networkConfig.getConfigType());
                PortType portType = PortType.getByValue(networkConfig.getPortType());
                SecurityComponent component = SecurityComponent.getByValue(networkConfig.getComponentType());

                if(securityNetworkType == null){
                    throw new GCloudException("security_controller_cluster_010018::network type is not found");
                }
                if(securityNetcardConfigType == null){
                    throw new GCloudException("security_controller_cluster_010019::netcard config type is not found");
                }
                if(portType == null){
                    throw new GCloudException("security_controller_cluster_010020::port type is not found");
                }

                if(component == null){
                    throw new GCloudException("security_controller_cluster_010021::component type is not found");
                }

                if(!isHa && SecurityNetworkType.HA == securityNetworkType){
                    continue;
                }

                netInfo.setConfigType(securityNetcardConfigType);
                netInfo.setPortType(portType);
                netInfo.setSecurityNetworkType(securityNetworkType);

                netInfo.setSubnetId(networkTypeSubnetMap.get(securityNetworkType));

                if(securityNetworkType.getNeedSecurityGroup()){
                    netInfo.setSecurityGroupId(networkTypeSecurityGroupMap.get(securityNetworkType));
                }

                if(SecurityNetcardConfigType.OVS_BRIDGE == securityNetcardConfigType){

                    SecurityClusterOvsBridge clusterOvsBridge = ovsBrMap.get(networkConfig.getId());
                    if(clusterOvsBridge == null){
                        clusterOvsBridge = ovsBrMap.get(networkConfig.getRefConfig());
                    }

                    if(clusterOvsBridge == null){

                        clusterOvsBridge = new SecurityClusterOvsBridge();
                        clusterOvsBridge.setId(UUID.randomUUID().toString());
                        clusterOvsBridge.setClusterId(clusterId);
                        clusterOvsBridge.setHa(isHa);

                        securityClusterOvsBridgeDao.save(clusterOvsBridge);

                        ovsBrMap.put(networkConfig.getId(), clusterOvsBridge);
                        ovsBrMap.put(networkConfig.getRefConfig(), clusterOvsBridge);

                        ClusterCreateOvsBridgeInfo clusterCreateOvsBridgeInfo = new ClusterCreateOvsBridgeInfo();
                        clusterCreateOvsBridgeInfo.setHostname(hostname);
                        clusterCreateOvsBridgeInfo.setId(clusterOvsBridge.getId());
                        createOvsBridgeInfos.add(clusterCreateOvsBridgeInfo);
                    }

                    netInfo.setClusterOvsId(clusterOvsBridge.getId());

                }

                List<ClusterCreateNetcardInfo> configs = comNetworkConfigMap.get(component);
                if(configs == null){
                    configs = new ArrayList<>();
                    comNetworkConfigMap.put(component, configs);
                }
                configs.add(netInfo);

            }
        }
        return createNetInfo;
    }

    private CreateClusterComponentObjectInfo toClusterComponentObjectParams(boolean haComponent, boolean isHa, CreateClusterInfoParams createInfo, ComputeClusterCreateObjectInfo objInfo, ClusterCreateNetInfo comNetworkConfig, ClusterCreateNetInfo haComNetworkConfig){

        CreateClusterComponentObjectInfo params = new CreateClusterComponentObjectInfo();
        params.setComponent(objInfo.getComponent());
        params.setComponentId(objInfo.getComponentId());
        params.setCreateDc(objInfo.getCreateDc());
        params.setCreateVm(objInfo.getCreateVm());
        params.setCreateInfo(createInfo);
        params.setObjectType(objInfo.getObjectType());

        //netcard
        List<ClusterCreateNetcardInfo> netcards = new ArrayList<>();

        if(haComponent){
            if(haComNetworkConfig != null && haComNetworkConfig.getCreateNetcardInfo() != null){
                List<ClusterCreateNetcardInfo> haNetcardInfos = haComNetworkConfig.getCreateNetcardInfo().get(objInfo.getComponent());
                if(haNetcardInfos != null){
                    netcards.addAll(haNetcardInfos);
                }
            }
        }else{
            if(comNetworkConfig != null && comNetworkConfig.getCreateNetcardInfo() != null){
                List<ClusterCreateNetcardInfo> netcardInfos = comNetworkConfig.getCreateNetcardInfo().get(objInfo.getComponent());
                if(netcardInfos != null){
                    netcards.addAll(netcardInfos);
                }
            }

            if(isHa && SecurityComponent.FIREWALL == objInfo.getComponent() && haComNetworkConfig != null && haComNetworkConfig.getCreateNetcardInfo() != null && haComNetworkConfig.getCreateNetcardInfo().get(SecurityComponent.FIREWALL) != null){
                for(ClusterCreateNetcardInfo netcardInfo : haComNetworkConfig.getCreateNetcardInfo().get(SecurityComponent.FIREWALL)){
                    if(netcardInfo.getSecurityNetworkType() == SecurityNetworkType.HA){
                        netcards.add(netcardInfo);
                    }
                }



            }
        }

        if(SecurityClusterComponentObjectType.VM.equals(params.getObjectType())){
            params.getCreateVm().setNetcardInfos(netcards);
        }else if(SecurityClusterComponentObjectType.DOCKER_CONTAINER.equals(params.getObjectType())){
//            objInfo.getCreateDc().setNetcardInfos(netcards);
        }

        return params;
    }

	@Override
	public PageResult<SecurityClusterType> describeSecurityCluster(DescribeSecurityClusterParams params,
			CurrentUser currentUser) {
		PageResult<SecurityClusterType> response = securityClusterDao.describeSecurityCluster(params, SecurityClusterType.class, currentUser);
		for (SecurityClusterType sc : response.getList()) {
			//TODO 差userName
    		SecurityClusterState state = SecurityClusterState.getByValue(sc.getState());
    		sc.setStateCnName(state == null ? null : state.getCnName());
		}
		
		return response;
	}

	@Override
	public PageResult<SecurityClusterComponentType> describeSecurityClusterComponent(DescribeSecurityClusterComponentParams params,
			CurrentUser currentUser) {
//		if(StringUtils.isBlank(id)) {
//			log.error("0160201::安全集群ID不能为空");
//			throw new GCloudException("0160201::安全集群ID不能为空");
//		}
		
		PageResult<SecurityClusterComponentType> response = securityClusterComponentDao.pageCompontents(params, SecurityClusterComponentType.class);
		response.setList(this.initComponentObjectInfo(response.getList()));
		for (SecurityClusterComponentType scc : response.getList()) {
    		SecurityClusterComponentState sccState = SecurityClusterComponentState.getByValue(scc.getState());
    		scc.setStateCnName(sccState == null ? null : sccState.getCnName());
		}
		return response;
	}

	@Override
	public PageResult<SecurityClusterInstanceType> describeSecurityClusterAddableInstance(
			SecurityClusterAddableInstanceParams params, CurrentUser currentUser) {
//		String id = params.getId();
//		if(StringUtils.isBlank(id)) {
//			log.error("0160301::安全集群ID不能为空");
//			throw new GCloudException("0160301::安全集群ID不能为空");
//		}
		
		PageResult<SecurityClusterInstanceType> response = securityClusterVmDao.findAddableInstance(params, SecurityClusterInstanceType.class);
		return response;
	}

	@Override
	public void modifySecurityCluster(ModifySecurityClusterParams params,
			CurrentUser currentUser) {
		String id = params.getId();
    	String name = params.getName();
    	String description = params.getDescription();
//    	if(StringUtils.isBlank(id)) {
//    		log.error("0160401::安全集群ID不能为空");
//    		throw new GCloudException("0160401::安全集群ID不能为空");
//    	} 
//    	
//    	if(StringUtils.isBlank(name)) {
//    		log.error("0160402::安全集群名字不能为空");
//    		throw new GCloudException("0160402::安全集群名字不能为空");
//    	}
    	
		SecurityCluster sc = securityClusterDao.getById(id);
    	if(sc == null) {
    		log.error("0160403::找不到该安全集群");
    		throw new GCloudException("0160403::找不到该安全集群");
    	}
    	
    	//TODO 真正底层实现
    	
    	List<String> filed = new ArrayList<>();
    	filed.add(sc.updateName(name));
    	filed.add(sc.updateDescription(description));
    	//TODO 时间修改问题
    	filed.add(sc.updateUpdateTime(new Date()));
    	
    	try{
    		securityClusterDao.update(sc, filed);
    	} catch (Exception e) {
    		log.error("0160404::修改安全集群失败");
    		throw new GCloudException("0160404::修改安全集群失败");
    	}
	}

	@Override
	public void securityClusterAddInstance(SecurityClusterAddInstanceParams params,
			CurrentUser currentUser) {
		String id = params.getId();
		List<String> instanceIds = params.getInstanceIds();
//		if (id == null) {
//			log.error("0160501::安全集群ID不能为空");
//			throw new GCloudException("0160501::安全集群ID不能为空");
//		}

		//入参校验List类型抛异常，在这里校验
		if (instanceIds == null || instanceIds.isEmpty()) {
			log.error("0160502::实例ID不能为空");
			throw new GCloudException("0160502::实例ID不能为空");
		}
		
		SecurityClusterAddableInstanceParams findParams = new SecurityClusterAddableInstanceParams();
		findParams.setId(params.getId());
		findParams.setPageNumber(1);
		findParams.setPageSize(-1);
		PageResult<SecurityClusterInstanceType> addablePage = securityClusterVmDao.findAddableInstance(findParams, SecurityClusterInstanceType.class);
		
		List<SecurityClusterInstanceType> vms = addablePage.getList();
		List<String> addAbleIds = new ArrayList<>();
		for (SecurityClusterInstanceType vm : vms) {
			addAbleIds.add(vm.getId());
		}
		
		
		List<String> noNeedAdd = new ArrayList<String>();
		for (String instanceId : instanceIds) {
			noNeedAdd.add(instanceId);
		}
		noNeedAdd.removeAll(addAbleIds);
		
		if(noNeedAdd != null && !noNeedAdd.isEmpty()) {
			log.debug("实例ID: " + noNeedAdd + "不可添加到该安全集群或已添加");
		}
		
		instanceIds.removeAll(noNeedAdd);
		
		if(instanceIds == null || instanceIds.isEmpty()) {
			log.error("0160503::所选的实例无法添加或已添加");
			throw new GCloudException("0160503::所选的实例无法添加或已添加");
		}
		
		//TODO 真正底层的实现
		
		securityClusterVmDao.addInstance(id, instanceIds);
	}

	@Override
	public void securityClusterRemoveInstance(
			SecurityClusterRemoveInstanceParams params, CurrentUser currentUser) {
		String id = params.getId();
		String instanceId = params.getInstanceId();
		
//		if(StringUtils.isBlank(id)) {
//			log.error("0160601::安全集群ID不能为空");
//			throw new GCloudException("0160601::安全集群ID不能为空");
//		}
//		
//		if(StringUtils.isBlank(instanceId)) {
//			log.error("0160602::实例ID不能为空");
//			throw new GCloudException("0160602::实例ID不能为空");
//		}
		
		Map<String, Object> findMap = new HashMap<String, Object>();
		findMap.put(SecurityClusterVm.CLUSTER_ID, id);
		findMap.put(SecurityClusterVm.INSTANCE_ID, instanceId);
		List<SecurityClusterVm> findResponse = securityClusterVmDao.findByProperties(findMap, SecurityClusterVm.class);
			
		if(findResponse == null || findResponse.isEmpty()) {
			log.error("0160603::实例未添加到改安全集群");
			throw new GCloudException("0160603::实例未添加到改安全集群");
		}

		//TODO 真正底层的实现
		
		try {
			for (SecurityClusterVm item : findResponse) {
				securityClusterVmDao.delete(item);
			}
		} catch(Exception e) {
			log.error("0160604::移除实例失败");
			throw new GCloudException("0160604::移除实例失败");
		}
	}

	@Override
	public SecurityClusterDetailResponse securityClusterDetail(SecurityClusterDetailParams params,
			CurrentUser currentUser) {
		String id = params.getId();
    	
//    	if(StringUtils.isBlank(id)) {
//    		log.error("0160701::安全集群ID不能为空");
//    		throw new GCloudException("0160701::安全集群ID不能为空");
//    	}
    	
		SecurityClusterDetailResponse response = securityClusterDao.getById(id, SecurityClusterDetailResponse.class);
    	
    	List<SecurityClusterInfoType> infoList = securityClusterInfoDao.findByProperty(SecurityClusterInfo.CLUSTER_ID, id, SecurityClusterInfoType.class);
    	SecurityClusterInfoType clusterInfo = new SecurityClusterInfoType();
    	if(infoList != null && !infoList.isEmpty()) {
    		clusterInfo = infoList.get(0);
    	}
    	
    	response.setClusterInfo(clusterInfo);
    	SecurityClusterState securityClusterState = SecurityClusterState.getByValue(response.getState());
    	response.setStateCnName(securityClusterState == null ? null : securityClusterState.getCnName());
		return response;
	}

	@Override
	public SecurityClusterTopologyResponse securityClusterTopology(SecurityClusterTopologyParams params,
			CurrentUser currentUser) {
		String clusterId = params.getId();
//		if(StringUtils.isBlank(clusterId)) {
//			log.error("0160801::安全集群ID不能为空");
//			throw new GCloudException("0160801::安全集群ID不能为空");
//		}
		
		SecurityCluster securityCluster = securityClusterDao.getById(clusterId);
		Boolean bHa = securityCluster.getHa();
		boolean ha = (bHa == null ? false : bHa.booleanValue()); 
		
		//处理拓扑的components的数据
		List<SecurityClusterComponentType> componentList = securityClusterComponentDao.findByProperty(SecurityClusterComponent.CLUSTER_ID, clusterId, SecurityClusterComponentType.class);
		if(componentList != null) {
			for (SecurityClusterComponentType e : componentList) {
				SecurityClusterComponentState state = SecurityClusterComponentState.getByValue(e.getState());
				e.setStateCnName(state == null ? null : state.getCnName());
			}
		}
		
		//处理拓扑的instances的数据
		List<SecurityClusterInstanceType> vmInstanceList = securityClusterVmDao.findInstancesByClusterId(clusterId, SecurityClusterInstanceType.class);
		
		//处理拓扑的subnet的数据
		Map<String, Object> subnetParams = new HashMap<>();
		subnetParams.put(SecurityClusterSubnet.CLUSTER_ID, clusterId);
		subnetParams.put(SecurityClusterSubnet.NETWORK_TYPE, SecurityNetworkType.PROTECTION.value());
//		List<SecurityClusterSubnetType> subnetList = securityClusterSubnetDao.findByProperty(SecurityClusterSubnet.CLUSTER_ID, clusterId, SecurityClusterSubnetType.class);
		List<SecurityClusterSubnetType> subnetList = securityClusterSubnetDao.findByProperties(subnetParams, SecurityClusterSubnetType.class);
		SecurityClusterSubnetType subnet = new SecurityClusterSubnetType();
		if(subnetList != null && !subnetList.isEmpty()) {
			subnet = subnetList.get(0);
		}
		
		//包装结果
		SecurityClusterTopologyResponse response = new SecurityClusterTopologyResponse();
		response.setClusterId(clusterId);
		response.setComponents(componentList);
		response.setInstances(vmInstanceList);
		response.setSubnet(subnet);
		response.setHa(ha);
		
		return response;
	}

	@Override
	public PageResult<SecurityClusterListType> apiListSecurityCluseter(ApiListSecurityCluseterParams params, CurrentUser currentUser) {
		PageResult<SecurityClusterListType> response = securityClusterDao.apiPageSecurityCluster(params, SecurityClusterListType.class);
		
		//根据集群ID获取组件信息前的准备
		List<String> clusterIds = new ArrayList<>();
		for (SecurityClusterListType item : response.getList()) {
			clusterIds.add(item.getId());
		}
		
		//获取组件列表
		//List<SecurityClusterComponentType> compontents = securityClusterComponentDao.findComponentsByClusterIds(clusterIds, SecurityClusterComponentType.class);
		List<SecurityClusterComponentType> compontents = securityClusterComponentDao.findAll(SecurityClusterComponentType.class);
		compontents = initComponentObjectInfo(compontents);
		
		//信息填充到结果集里
		for (SecurityClusterListType item : response.getList()) {
			List<SecurityClusterComponentType> compontentsList = item.getSecurityClusterComponents();
			for (SecurityClusterComponentType sc : compontents) {
				//组装安全集群的组件列表信息
				if(sc.getClusterId().equals(item.getId())) {
					if(compontentsList == null) {
						compontentsList = new ArrayList<>();
					}
					compontentsList.add(sc);
				}
			}
			item.setSecurityClusterComponents(compontentsList);
		}
		
		return response;
	}
	
	
	/**
	 * 安全集群组件列表的object的数据初始化
	 * @param componentList 安全集群组件列表
	 * @return 安全集群组件列表
	 */
	private List<SecurityClusterComponentType> initComponentObjectInfo(List<SecurityClusterComponentType> list) {
		Map<String, SecurityClusterComponentType> instanceCompontentMap = new HashMap<>();
		Map<String, SecurityClusterComponentType> containerCompontentMap = new HashMap<>();
		
		for (SecurityClusterComponentType component : list) {
			if(StringUtils.isNotBlank(component.getObjectId())) {
				String objectId = component.getObjectId();
				SecurityClusterComponentObjectType scObjectType = SecurityClusterComponentObjectType.getByValue(component.getObjectType());
				if(scObjectType.equals(SecurityClusterComponentObjectType.VM)) {
					instanceCompontentMap.put(objectId, component);
				} else if(scObjectType.equals(SecurityClusterComponentObjectType.DOCKER_CONTAINER)) {
					containerCompontentMap.put(objectId, component);
				}
			}
		}
		
		//objectType是vm类型的情况数据初始化
		if(instanceCompontentMap != null && !instanceCompontentMap.isEmpty()) {
			List<SecurityClusterInstanceType> instanceList = securityClusterComponentDao.getComponentObjectList(SecurityClusterComponentObjectType.VM, SecurityClusterInstanceType.class);
			for (SecurityClusterInstanceType instance : instanceList) {
				SecurityClusterComponentType scComponent = instanceCompontentMap.get(instance.getId());
				scComponent.setVmInfo(instance);
			}
		}
		
		//TODO objectType是dc类型的情况数据初始化
		if(containerCompontentMap != null && !containerCompontentMap.isEmpty()) {
//			List<SecurityClusterDcType> list = securityClusterComponentDao.getComponentObjectList(SecurityClusterComponentObjectType.DOCKER_CONTAINER, SecurityClusterDcType.class);
//			for (SecurityClusterDcType dc : list) {
//				SecurityClusterComponentType scComponent = instanceCompontentMap.get(dc.getId());
//				scComponent.setDcInfo(dc);
//			}
		}
		return list;
		
	}
	
}
