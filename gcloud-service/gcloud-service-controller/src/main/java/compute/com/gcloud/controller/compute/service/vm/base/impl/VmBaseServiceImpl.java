package com.gcloud.controller.compute.service.vm.base.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.gcloud.common.util.StringUtils;
import com.gcloud.controller.compute.dao.InstanceDao;
import com.gcloud.controller.compute.dao.InstanceTypeDao;
import com.gcloud.controller.compute.dao.ZoneDao;
import com.gcloud.controller.compute.dao.ZoneInstanceTypeDao;
import com.gcloud.controller.compute.dispatcher.Dispatcher;
import com.gcloud.controller.compute.entity.AvailableZoneEntity;
import com.gcloud.controller.compute.entity.InstanceType;
import com.gcloud.controller.compute.entity.VmInstance;
import com.gcloud.controller.compute.entity.ZoneInstanceTypeEntity;
import com.gcloud.controller.compute.handler.api.model.DescribeInstanceTypesParams;
import com.gcloud.controller.compute.handler.api.model.DescribeInstancesParams;
import com.gcloud.controller.compute.model.vm.AssociateInstanceTypeParams;
import com.gcloud.controller.compute.model.vm.CreateInstanceTypeParams;
import com.gcloud.controller.compute.model.vm.DeleteInstanceTypeParams;
import com.gcloud.controller.compute.model.vm.DetailInstanceTypeParams;
import com.gcloud.controller.compute.model.vm.ModifyInstanceTypeParams;
import com.gcloud.controller.compute.service.vm.base.IVmBaseService;
import com.gcloud.controller.compute.utils.NoVncUtil;
import com.gcloud.controller.network.dao.FloatingIpDao;
import com.gcloud.controller.network.dao.PortDao;
import com.gcloud.controller.network.entity.FloatingIp;
import com.gcloud.core.cache.container.CacheContainer;
import com.gcloud.core.cache.enums.CacheType;
import com.gcloud.core.exception.GCloudException;
import com.gcloud.core.messagebus.MessageBus;
import com.gcloud.core.util.MessageUtil;
import com.gcloud.framework.db.PageResult;
import com.gcloud.header.api.model.CurrentUser;
import com.gcloud.header.compute.enums.VmState;
import com.gcloud.header.compute.enums.VmStepState;
import com.gcloud.header.compute.msg.api.model.DetailInstanceType;
import com.gcloud.header.compute.msg.api.model.InstanceAttributesType;
import com.gcloud.header.compute.msg.api.model.InstanceTypeItemType;
import com.gcloud.header.compute.msg.api.model.ZoneInfo;
import com.gcloud.header.compute.msg.node.vm.base.ModifyPasswordMsg;
import com.gcloud.header.compute.msg.node.vm.base.QueryInstanceVNCMsg;
import com.gcloud.header.compute.msg.node.vm.base.QueryInstanceVNCMsgReply;
import com.gcloud.header.compute.msg.node.vm.base.RebootInstanceMsg;
import com.gcloud.header.compute.msg.node.vm.base.StartInstanceMsg;
import com.gcloud.header.compute.msg.node.vm.base.StopInstanceMsg;
import com.gcloud.header.network.model.IpAddressSetType;
import com.gcloud.header.network.model.NetworkInterfaceType;
import com.gcloud.header.network.model.NetworkInterfaces;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@Transactional
public class VmBaseServiceImpl implements IVmBaseService {
    @Autowired
    private InstanceDao instanceDao;

    @Autowired
    private InstanceTypeDao instanceTypeDao;
    
    @Autowired
    private ZoneDao zoneDao;
    
    @Autowired
    private ZoneInstanceTypeDao zoneInstanceTypeDao;

    @Autowired
    private MessageBus bus;
    
    @Autowired
    private PortDao portDao;
    
    @Autowired
    private FloatingIpDao floatingIpDao;


    @Override
    public void startInstance(String instanceId) {
        startInstance(instanceId, true, true);
    }

    @Override
    public void startInstance(String instanceId, boolean inTask, boolean handleResource) {

        VmInstance vm = instanceDao.getById(instanceId);
        if (vm == null) {
            throw new GCloudException("0010202::找不到云服务器");
        }

        if (!instanceDao.updateInstanceStepState(instanceId, VmStepState.STARTING, inTask, VmState.STOPPED)) {
            throw new GCloudException("0010204::云服务器当前状态不能开机");
        }

        if(handleResource){
            Dispatcher.dispatcher().assignNode(vm.getHostname(), vm.getCore(), vm.getMemory());
        }

        StartInstanceMsg startInstanceMsg = new StartInstanceMsg();
        startInstanceMsg.setInstanceId(instanceId);
        startInstanceMsg.setServiceId(MessageUtil.computeServiceId(vm.getHostname()));
        startInstanceMsg.setHandleResource(handleResource);
        bus.send(startInstanceMsg);

    }

    @Override
    public void stopInstance(String instanceId) throws GCloudException {
        stopInstance(instanceId, true, true);
    }

    @Override
    public void stopInstance(String instanceId, boolean inTask, boolean handleResource) throws GCloudException {
        VmInstance vm = instanceDao.getById(instanceId);
        if (vm == null) {
            throw new GCloudException("0010302::找不到云服务器");
        }

        if (!instanceDao.updateInstanceStepState(instanceId, VmStepState.STOPPING, inTask, VmState.RUNNING)){
            throw new GCloudException("0010204::云服务器当前状态不能关机");
        }

        StopInstanceMsg stopInstanceMsg = new StopInstanceMsg();
        stopInstanceMsg.setInstanceId(vm.getId());
        stopInstanceMsg.setServiceId(MessageUtil.computeServiceId(vm.getHostname()));
        stopInstanceMsg.setHandleResource(handleResource);

        bus.send(stopInstanceMsg);

    }

    @Override
    public void rebootInstance(String instanceId, Boolean forceStop) throws GCloudException {
        rebootInstance(instanceId, true);
    }

    @Override
    public void rebootInstance(String instanceId, Boolean forceStop, boolean inTask) throws GCloudException {
        VmInstance vm = instanceDao.getById(instanceId);
        if (vm == null) {
            throw new GCloudException("0010402::找不到云服务器");
        }
        if (!instanceDao.updateInstanceStepState(instanceId, VmStepState.REBOOTING, inTask, VmState.RUNNING)){
            throw new GCloudException("0010404::云服务器当前状态不能重启");
        }

        RebootInstanceMsg rebootInstanceMsg = new RebootInstanceMsg();
        rebootInstanceMsg.setInstanceId(vm.getId());
        rebootInstanceMsg.setServiceId(MessageUtil.computeServiceId(vm.getHostname()));
        rebootInstanceMsg.setForceStop(forceStop);
        bus.send(rebootInstanceMsg);

    }

    @Override
    public List<InstanceTypeItemType> describeInstanceTypes(DescribeInstanceTypesParams params) {

        if (params == null) {
            params = new DescribeInstanceTypesParams();
        }

        return instanceTypeDao.describeInstanceTypes(params, InstanceTypeItemType.class);

    }

	@Override
	public void modifyInstanceAttribute(String instanceId, String instanceName, String password, String taskId) {
		VmInstance vm = instanceDao.getById(instanceId);
        if (vm == null) {
            throw new GCloudException("0010503::找不到云服务器");
        }
        if(StringUtils.isNotBlank(instanceName)) {
        	List<String> updateFields = new ArrayList<String>();
        	updateFields.add(vm.updateAlias(instanceName));
        	instanceDao.update(vm, updateFields);
        	CacheContainer.getInstance().put(CacheType.INSTANCE_ALIAS, instanceId, instanceName);//修改缓存
        }
        
        if(StringUtils.isNotBlank(password)) {
        	ModifyPasswordMsg modifyMsg = new ModifyPasswordMsg();
        	modifyMsg.setInstanceId(vm.getId());
        	modifyMsg.setServiceId(MessageUtil.computeServiceId(vm.getHostname()));
        	modifyMsg.setPassword(password);
        	if(StringUtils.isNotBlank(taskId)) {
        		modifyMsg.setTaskId(taskId);
        	}
            bus.send(modifyMsg);
        }
        
	}

	@Override
	public PageResult<InstanceAttributesType> describeInstances(DescribeInstancesParams params, CurrentUser currentUser) {
		PageResult<InstanceAttributesType> pages = instanceDao.describeInstances(params, currentUser, InstanceAttributesType.class);
		//加上浮动IP列表、网卡列表
		List<InstanceAttributesType> lists = pages.getList();
		for(InstanceAttributesType item:lists) {
			List<FloatingIp> ips = floatingIpDao.findByProperty("instanceId", item.getInstanceId());
            IpAddressSetType ipAddressSetType = new IpAddressSetType();
			List<String> ipAddrs = new ArrayList<>();
			for(FloatingIp ip : ips) {
                ipAddrs.add(ip.getFloatingIpAddress());
			}
            ipAddressSetType.setIpAddress(ipAddrs);
			item.setEipAddress(ipAddressSetType);

            NetworkInterfaces networkInterfaces = new NetworkInterfaces();
            networkInterfaces.setNetworkInterface(portDao.getInstanceNetworkInterfaces(item.getInstanceId(), NetworkInterfaceType.class));
			item.setNetworkInterfaces(networkInterfaces);
            if (StringUtils.isNotBlank(item.getTaskState())) {
                item.setStatus(item.getTaskState());
            }else if(StringUtils.isNotBlank(item.getStepState())){
                item.setStatus(item.getStepState());
            }
            
            //填充虚拟机列表的私有网络IP
            IpAddressSetType innerIpAddress = new IpAddressSetType();
            List<String> ipList = new ArrayList<>();
            for (NetworkInterfaceType networkInterface : networkInterfaces.getNetworkInterface()) {
            	ipList.add(networkInterface.getPrimaryIpAddress());
			}
            innerIpAddress.setIpAddress(ipList);
            item.setInnerIpAddress(innerIpAddress);
		}
		return pages;
	}

    @Override
    public void cleanState(String instanceId, Boolean inTask) {
        if(inTask != null && inTask){
            instanceDao.cleanStepState(instanceId);
        }else{
            instanceDao.cleanState(instanceId);
        }
    }

    @Override
    public String queryInstanceVNC(String instanceId) {
        VmInstance vm = instanceDao.getById(instanceId);
        if (vm == null) {
            throw new GCloudException("0010202::找不到云服务器");
        }

        if (! vm.getState().equals(VmState.RUNNING.value())) {
            throw new GCloudException("0010801::非运行状态无法连接VNC");
        }

        QueryInstanceVNCMsg msg = new QueryInstanceVNCMsg();
        msg.setInstanceId(instanceId);
        msg.setServiceId(MessageUtil.computeServiceId(vm.getHostname()));
        QueryInstanceVNCMsgReply reply = bus.call(msg, QueryInstanceVNCMsgReply.class);

        NoVncUtil.addToken(msg.getInstanceId(), reply.getHostIp(), Integer.valueOf(reply.getPort()));
        return NoVncUtil.generateVncUrl(msg.getInstanceId());
    }

	@Override
	public void createInstanceType(CreateInstanceTypeParams params, CurrentUser currentUser) {
		List<InstanceType> instanceType = instanceTypeDao.findByProperty(InstanceType.NAME, params.getName());
		if(instanceType != null && !instanceType.isEmpty()) {
			log.error("0011402::已有该实例类型");
			throw new GCloudException("0011402::已有该实例类型");
		}
		
		InstanceType item = new InstanceType();
		String uuid = UUID.randomUUID().toString();
		if(StringUtils.isBlank(uuid)) {
		}
		
		//数据赋值
		item.setId(uuid);
		item.setName(params.getName());
		item.setVcpus(params.getCpu());
		item.setMemoryMb(params.getMemory());
		item.setDeleted(false);
		item.setCreatedTime(new Date());
		
		try {
			instanceTypeDao.save(item);
		} catch(Exception e) {
			log.error("创建实例类型失败，原因：【"+ e.getCause() + "::" + e.getMessage() +"】");
			throw new GCloudException("0011403::创建实例类型失败");
		}
		
	}

	@Override
	public void deleteInstanceType(DeleteInstanceTypeParams params, CurrentUser currentUser) {
		InstanceType instanceType = instanceTypeDao.getById(params.getId());
		if(instanceType == null) {
			log.error("0011502::不存在该实例类型");
			throw new GCloudException("0011502::不存在该实例类型");
		}
		//TODO 如果有虚拟机在使用
		
		try {
			instanceTypeDao.deleteById(params.getId());
		} catch(Exception e) {
			log.error("删除实例类型失败，原因：【"+ e.getCause() + "::" + e.getMessage() +"】");
			throw new GCloudException("0011503::删除实例类型失败");
		}
	}

	@Override
	public void modifyInstanceType(ModifyInstanceTypeParams params, CurrentUser currentUser) {
		InstanceType instanceType = instanceTypeDao.getById(params.getId());
		if(instanceType == null) {
			log.error("0011604::不存在该实例类型");
			throw new GCloudException("0011604::不存在该实例类型");
		}
		
		//TODO 如果有虚拟机在用
		
		instanceType.setMemoryMb(params.getMemory());
		instanceType.setVcpus(params.getCpu());
		
		List<String> fields = new ArrayList<>();
		fields.add(InstanceType.MEMORY_MB);
		fields.add(InstanceType.VCPUS);
		
		try{
			instanceTypeDao.update(instanceType, fields);
		} catch(Exception e) {
			log.error("修改实例类型失败，原因：【"+ e.getCause() + "::" + e.getMessage() +"】");
			throw new GCloudException("0011605::修改实例类型失败");
		}
	}

	@Override
	public DetailInstanceType detailInstanceType(DetailInstanceTypeParams params, CurrentUser currentUser) {
		DetailInstanceType response = instanceTypeDao.getById(params.getId(), DetailInstanceType.class);
		if(response == null) {
			log.error("0011702::不存在该实例类型");
			throw new GCloudException("0011702::不存在该实例类型");
//			response = new DetailInstanceType();
//			response.setAvailableZones(new ArrayList<ZoneInfo>());
//			return response;
		}
		
		
		List<ZoneInfo> zones = instanceTypeDao.getZonesByInstanceType(params.getId(), ZoneInfo.class);
		response.setAvailableZones(zones);
		
		response.setMemoryMb(Math.ceil(response.getMemoryMb() == null ? 0 : response.getMemoryMb()/ 1024));
		response.setVcpus(response.getVcpus() == null ? 0 : response.getVcpus());
		
		return response;
	}

	@Override
	public void associateInstanceType(AssociateInstanceTypeParams params, CurrentUser currentUser) {
		InstanceType instanceType = instanceTypeDao.getById(params.getInstanceTypeId());
		if(instanceType == null) {
			log.error("0011803::不存在该实例类型");
			throw new GCloudException("0011803::不存在该实例类型");
		}
		
		AvailableZoneEntity zone = zoneDao.getById(params.getZoneId());
		if(zone == null) {
			log.error("0011804::不存在该可用区");
			throw new GCloudException("0011804::不存在该可用区");
		}
		
		Map<String, Object> findParams = new HashMap<>();
		findParams.put(ZoneInstanceTypeEntity.ZONE_ID, zone.getId());
		findParams.put(ZoneInstanceTypeEntity.INSTANCE_TYPE_ID, instanceType.getId());
		
		List<ZoneInstanceTypeEntity> list = zoneInstanceTypeDao.findByProperties(findParams, ZoneInstanceTypeEntity.class);
		if(list != null && !list.isEmpty()) {
			log.error("0011805::实例类型和可用区已关联");
			throw new GCloudException("0011805::实例类型和可用区已关联");
		}
		
		ZoneInstanceTypeEntity entity = new ZoneInstanceTypeEntity();
		entity.setZoneId(zone.getId());
		entity.setInstanceTypeId(instanceType.getId());
		
		try{
			zoneInstanceTypeDao.saveOrUpdate(entity);
		} catch(Exception e) {
			log.error("实例类型和可用区关联失败，原因：【"+ e.getCause() + "::" + e.getMessage() +"】");
			throw new GCloudException("0011806::实例类型和可用区关联失败");
		} 
		
	}
}
