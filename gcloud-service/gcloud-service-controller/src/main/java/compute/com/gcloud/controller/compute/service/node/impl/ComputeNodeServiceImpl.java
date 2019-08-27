package com.gcloud.controller.compute.service.node.impl;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.apache.commons.lang.ObjectUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gcloud.controller.compute.ControllerComputeProp;
import com.gcloud.controller.compute.dao.ComputeNodeDao;
import com.gcloud.controller.compute.dao.ZoneDao;
import com.gcloud.controller.compute.dao.ZoneNodeDao;
import com.gcloud.controller.compute.entity.AvailableZoneEntity;
import com.gcloud.controller.compute.entity.ComputeNode;
import com.gcloud.controller.compute.entity.ZoneNodeEntity;
import com.gcloud.controller.compute.model.node.AttachNodeParams;
import com.gcloud.controller.compute.model.node.DescribeNodesParams;
import com.gcloud.controller.compute.model.node.Node;
import com.gcloud.controller.compute.model.node.NodeCommentInfo;
import com.gcloud.controller.compute.service.node.IComputeNodeService;
import com.gcloud.controller.compute.utils.RedisNodesUtil;
import com.gcloud.controller.compute.utils.VmControllerUtil;
import com.gcloud.core.cache.redis.lock.util.LockUtil;
import com.gcloud.core.cache.redis.template.GCloudRedisTemplate;
import com.gcloud.core.exception.GCloudException;
import com.gcloud.framework.db.PageResult;
import com.gcloud.header.api.model.CurrentUser;
import com.gcloud.header.compute.msg.node.node.model.ComputeNodeInfo;
import com.gcloud.header.compute.msg.node.node.model.NodeBaseInfo;
import com.gcloud.service.common.Consts;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class ComputeNodeServiceImpl implements IComputeNodeService {

    @Autowired
    private GCloudRedisTemplate redisTemplate;

    @Autowired
    private ControllerComputeProp prop;

    @Autowired
    private ComputeNodeDao nodeDao;
    
    @Autowired
    private ZoneDao zoneDao;
    
    @Autowired
    private ZoneNodeDao zoneNodeDao;

    @Override
    public void computeNodeConnect(ComputeNodeInfo computeNodeInfo, int nodeTimeout) {

        String hostname = computeNodeInfo.getHostname();

        String aliveKey = VmControllerUtil.getNodesAliveKey(hostname);
        String aliveNode = ObjectUtils.toString(redisTemplate.opsForValue().get(aliveKey));
        if (StringUtils.isBlank(aliveNode)) {
            log.debug("node has gone away, node=" + hostname);
            //判断是否能注册, 证书控制，先注释，后面等待控制开发完成
            //TODO
//            if(ControllerSystemContext.getProp().isCpuScaleLimit() && !ControllerRestApi.canNodeRegist(hostname, computeNodeInfo.getPhysicalCpu())) {
//                return;
//            }

            Node node = new Node();
            node.getAvailableVmResource().setAvailableCore(computeNodeInfo.getMaxCore());
            node.getAvailableVmResource().setAvailableMemory(computeNodeInfo.getMaxMemory());
            node.getAvailableVmResource().setAvailableDisk(computeNodeInfo.getMaxDisk());

            node.getAvailableVmResource().setMaxCore(computeNodeInfo.getMaxCore());
            node.getAvailableVmResource().setMaxMemory(computeNodeInfo.getMaxMemory());
            node.getAvailableVmResource().setPhysicalCpu(computeNodeInfo.getPhysicalCpu());

            BigDecimal b = new BigDecimal(computeNodeInfo.getMaxDisk());
            double maxDisk = b.setScale(5, BigDecimal.ROUND_HALF_UP).doubleValue();
            node.getAvailableVmResource().setMaxDisk(maxDisk);

            node.setNodeIp(computeNodeInfo.getNodeIp());
            node.setHostName(hostname);
            node.setCommentInfo(NodeCommentInfo.HYPERVISOR, computeNodeInfo.getHypervisor());
            node.setCommentInfo(NodeCommentInfo.KERNEL_VERSION, computeNodeInfo.getKernelVersion());
            node.setCommentInfo(NodeCommentInfo.CPU_TYPE, computeNodeInfo.getCpuType());
            node.setCommentInfo(NodeCommentInfo.CLOUD_PLATFORM, computeNodeInfo.getCloudPlatform());
            node.setCommentInfo(NodeCommentInfo.IS_FT, String.valueOf(computeNodeInfo.getIsFt()));
            node.setUpdateTime(new Date());

            ComputeNode nodeEntity = this.nodeDao.findUniqueByProperty("hostname", hostname);
            if (nodeEntity != null) {
                node.setZoneId(nodeEntity.getZoneId());
            }
            
            RedisNodesUtil.registerComputeNode(hostname, node);
            RedisNodesUtil.updateNodeAlive(hostname, nodeTimeout);
        }
        else {
            // 判断node是否存在redis里面
            // 更新节点信息
            updateNodeInfo(hostname, nodeTimeout);
        }

    }

    private void updateNodeInfo(String hostname, int nodeTimeout) {

        String nodeKey = VmControllerUtil.getNodesHostNameKey(hostname);
        String aliveKey = VmControllerUtil.getNodesAliveKey(hostname);

        String lockName = VmControllerUtil.getNodesHostNameLock(hostname);
        String lockid = "";
        Node node = null;
        try {
            lockid = LockUtil.spinLock(lockName, Consts.Time.NODES_REDIS_LOCK_TIMEOUT, Consts.Time.NODES_REDIS_LOCK_GET_LOCK_TIMEOUT);
            node = (Node)redisTemplate.getObject(nodeKey);
            if (node == null) {
                // 如果不存在，直接删除aliveNode，等待下次汇报
                redisTemplate.delete(aliveKey);
            }
            else {
                log.debug(String.format("Update the compute node:%s", hostname));
                redisTemplate.setObject(nodeKey, node);
            }

        }
        catch (Exception ex) {
            log.error("节点更新信息失败:获取锁失败,hostName=" + hostname, ex);
        }
        finally {
            LockUtil.releaseSpinLock(lockName, lockid);
        }

        if (node != null) {
            // 更新节点updateTime
            RedisNodesUtil.updateNodeAlive(hostname, nodeTimeout);
        }

    }

	@Override
	public PageResult<NodeBaseInfo> describeNodes(DescribeNodesParams params, CurrentUser currentUser) {
		PageResult<NodeBaseInfo> page;
		if("0".equals(params.getGroupId())) {
			page = nodeDao.pageNodeGroupOrNot(params, false, NodeBaseInfo.class);
		} else {
			page = nodeDao.pageNodeGroupOrNot(params, true, NodeBaseInfo.class);
		}
		return page;
	}

	@Override
	public void attachNode(AttachNodeParams params, CurrentUser currentUser) {
		AvailableZoneEntity zone = zoneDao.getById(params.getZoneId());
		if(zone == null) {
			log.error("1020203::未找到该可用区");
			throw new GCloudException("1020203::未找到该可用区");
		}
		
		Map<String, Object> nodeParams = new HashMap<>();
		nodeParams.put(ComputeNode.HOSTNAME, params.getHostname());
		List<ComputeNode> nodeList = nodeDao.findByProperties(nodeParams);
		if(nodeList == null || nodeList.isEmpty()) {
			log.error("1020204::未找到该节点");
			throw new GCloudException("1020204::未找到该节点");
		}
		
		Map<String, Object> props = new HashMap<>();
		props.put(ZoneNodeEntity.HOSTNAME, params.getHostname());
		props.put(ZoneNodeEntity.ZONE_ID, params.getZoneId());
		
		List<ZoneNodeEntity> zoneNodeList = zoneNodeDao.findByProperties(props, ZoneNodeEntity.class);
		if(zoneNodeList != null && !zoneNodeList.isEmpty()) {
			log.error("1020205::该可用区和节点已经关联了");
			throw new GCloudException("1020205::该可用区和节点已经关联了");
		}
		
		
		ZoneNodeEntity entity = new ZoneNodeEntity();
		entity.setId(UUID.randomUUID().toString());
		entity.setHostname(params.getHostname());
		entity.setZoneId(params.getZoneId());
		
		try{
			zoneNodeDao.saveOrUpdate(entity);
		} catch(Exception e) {
			log.error("关联节点和可用区失败，原因：【"+ e.getCause() + "::" + e.getMessage() +"】");
			throw new GCloudException("1020206::关联节点和可用区失败");
		}
		
	}
}
