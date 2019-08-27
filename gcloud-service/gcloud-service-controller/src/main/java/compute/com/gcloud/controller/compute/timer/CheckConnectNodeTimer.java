package com.gcloud.controller.compute.timer;

import com.gcloud.controller.compute.dao.ComputeNodeConnectLogDao;
import com.gcloud.controller.compute.dao.ComputeNodeDao;
import com.gcloud.controller.compute.entity.ComputeNode;
import com.gcloud.controller.compute.entity.ComputeNodeConnectLog;
import com.gcloud.controller.compute.model.node.Node;
import com.gcloud.controller.compute.model.node.NodeCommentInfo;
import com.gcloud.controller.compute.utils.RedisNodesUtil;
import com.gcloud.controller.compute.utils.VmControllerUtil;
import com.gcloud.core.cache.redis.template.GCloudRedisTemplate;
import com.gcloud.core.quartz.GcloudJobBean;
import com.gcloud.core.quartz.annotation.QuartzTimer;
import com.gcloud.core.service.SpringUtil;
import com.gcloud.header.compute.enums.ComputeNodeConnectType;
import com.gcloud.header.compute.enums.ComputeNodeState;
import com.gcloud.service.common.Consts;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.quartz.DisallowConcurrentExecution;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.PersistJobDataAfterExecution;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by yaowj on 2018/10/19.
 */
@Component
@PersistJobDataAfterExecution
@DisallowConcurrentExecution
@QuartzTimer(fixedDelay = 10 * 1000L)
@Slf4j
public class CheckConnectNodeTimer extends GcloudJobBean {

    @Autowired
    private GCloudRedisTemplate redisTemplate;

    @Override
    protected void executeInternal(JobExecutionContext jobExecutionContext) throws JobExecutionException {

        log.debug(String.format("【控制器节点检测】开始"));
        long startTime = System.currentTimeMillis();
        try {
            ComputeNodeDao computeNodeDao = SpringUtil.getBean(ComputeNodeDao.class);

            String searchKey = MessageFormat.format(Consts.RedisKey.GCLOUD_CONTROLLER_COMPUTE_NODES_COMPUTE_NODE_ALIVE, "*");
            Set<String> aliveKeySet = redisTemplate.keys(searchKey);

            // 从key获取hostName，不用再查redis
            if (aliveKeySet == null) {
                aliveKeySet = new HashSet<String>();
            }
            List<String> onLineNodes = new ArrayList<String>();
            for (String aliveKey : aliveKeySet) {
                String aliveNode = VmControllerUtil.getHostNameByAliveKey(aliveKey);
                onLineNodes.add(aliveNode);
            }


            RedisNodesUtil.cleanComputeNodes(onLineNodes);


            Map<String, Node> nodesInCache = RedisNodesUtil.getComputeNodes();
            List<ComputeNode> nodesInDB = computeNodeDao.findAll();

            syncComputeNode(nodesInCache, nodesInDB);

        } catch (Exception e) {
            log.error("监听计算节点汇报出错:", e);
        }

        log.debug(String.format("【控制器节点检测】结束: 用时(ms)【%s】", (System.currentTimeMillis() - startTime)));
    }


    /**
     *
     * @Description: 同步数据库节点和内存节点
     * @date 2015-4-17 下午4:39:29
     *
     * @param nodesInCache
     *            内存节点列表
     * @param nodesInDB
     *            数据库节点列表
     */
    private void syncComputeNode(Map<String, Node> nodesInCache, List<ComputeNode> nodesInDB) {
        Hashtable<String, ComputeNode> nodeMapsInDB = new Hashtable<String, ComputeNode>();
        if (nodesInDB != null && nodesInDB.size() > 0) {
            for (ComputeNode nodeDB : nodesInDB) {
                nodeMapsInDB.put(nodeDB.getHostname(), nodeDB);
            }
        }

//        IConnectDao iConnectDao = (IConnectDao) SpringUtil.getBean("IConnectDao");
//        CheckConnectServiceImpl checkConnectImpl = (CheckConnectServiceImpl) SpringUtil.getBean("IConnectService");

        ComputeNodeDao computeNodeDao = SpringUtil.getBean(ComputeNodeDao.class);
        ComputeNodeConnectLogDao connectLogDao = SpringUtil.getBean(ComputeNodeConnectLogDao.class);

        // 内存列表有，数据库没有的，需要新增和修改为连接状态
        for (Map.Entry<String, Node> map : nodesInCache.entrySet()) {
            String hostName = map.getKey();
            Node node = map.getValue();
            if (node != null) {
                ComputeNode nodeDB = nodeMapsInDB.get(hostName);
                if (nodeDB == null) {
                    ComputeNode newComputeNode = new ComputeNode();
                    newComputeNode.setNodeIp(node.getNodeIp());
                    newComputeNode.setCloudPlatform(node.getCommentInfo(NodeCommentInfo.CLOUD_PLATFORM));
                    newComputeNode.setFtNode(Integer.parseInt(node.getCommentInfo(NodeCommentInfo.IS_FT)));
                    newComputeNode.setHypervisorType(node.getCommentInfo(NodeCommentInfo.HYPERVISOR));
                    newComputeNode.setHostname(hostName);
                    newComputeNode.setCpuTotal(node.getAvailableVmResource().getMaxCore());
                    newComputeNode.setMemoryTotal((int) node.getAvailableVmResource().getMaxMemory());
                    newComputeNode.setDiskTotal(node.getAvailableVmResource().getMaxDisk());
                    newComputeNode.setState(ComputeNodeState.CONNECTED.getValue());
                    newComputeNode.setPhysicalCpu((int) node.getAvailableVmResource().getPhysicalCpu());
                    newComputeNode.setCreateTime(new Date());
                    computeNodeDao.save(newComputeNode);

                    // 这里加上节点连接的代码，记录首次连接或者重连的时间等信息。

                    ComputeNodeConnectLog connect = new ComputeNodeConnectLog();
                    connect.setHostname(hostName);
                    connect.setLogTime(new Date());
                    connect.setNodeIp(node.getNodeIp());
                    connect.setConnectType(ComputeNodeConnectType.REGISTER.getValue());

                    connectLogDao.save(connect);

                } else {
                    updateDbByCache(node, nodeDB);
                    // 标识内存有，数据库也有但是数据库的状态为0

                }
            }
        }

        // 数据库有，内存没有的，需要修改为失去连接状态
        for (ComputeNode nodeInDB : nodesInDB) {
            if (nodesInCache.get(nodeInDB.getHostname()) == null) {
                if (ComputeNodeState.CONNECTED.getValue() == nodeInDB.getState()) {
                    List<String> ids = new ArrayList<String>();
                    List<String> ips = new ArrayList<String>();
                    ids.add(nodeInDB.getHostname());
                    ips.add(nodeInDB.getNodeIp());
                    computeNodeDao.updateBatch("hostName", ids, "state", ComputeNodeState.DISCONNECTED.getValue());

                    ComputeNodeConnectLog connect = new ComputeNodeConnectLog();
                    connect.setHostname(nodeInDB.getHostname());
                    connect.setLogTime(new Date());
                    connect.setNodeIp(nodeInDB.getNodeIp());
                    connect.setConnectType(ComputeNodeConnectType.DISCONNECT.getValue());

                    connectLogDao.save(connect);

                    //TODO 等证书完成再开发
                    //解注册--向总线释放物理cpu
//                    if(ControllerSystemContext.getProp().isCpuScaleLimit()) {
//                        try {
//                            ControllerRestApi.nodeUnRegist(nodeInDB.getHostName(), nodeInDB.getPhysicalCpu());
//                        } catch(Exception ex) {
//                            LOG.log(LogLevel.GCLOUD_DEBUG, String.format("【控制器节点检测】向总线解注册失去连接节点cpu失败"));
//                        }
//                    }

                    // 检查原因插入数据库
//                    checkConnectImpl.checkReason(ids);
                    // checkConnectImpl.checkByRequest(ips, new Date());

                }
                // 这里写断开连接时候的代码

                // 全部的逻辑用一个接口来写
            }
        }
    }

    private void updateDbByCache(Node node, ComputeNode nodeDB) {
        ComputeNodeDao iComputeNodeDao = SpringUtil.getBean(ComputeNodeDao.class);
        ComputeNodeConnectLogDao connectLogDao = SpringUtil.getBean(ComputeNodeConnectLogDao.class);
//        IConnectDao iConnectDao = (IConnectDao) SpringUtil.getBean("IConnectDao");

        List<String> fields = new ArrayList<String>();

        if (ComputeNodeState.DISCONNECTED.getValue() == nodeDB.getState()) {
            fields.add("state");
            nodeDB.setState(ComputeNodeState.CONNECTED.getValue());
            // 把重新连接写上数据库

            ComputeNodeConnectLog connect = new ComputeNodeConnectLog();
            connect.setHostname(node.getHostName());
            connect.setLogTime(new Date());
            connect.setNodeIp(node.getNodeIp());
            connect.setConnectType(ComputeNodeConnectType.CONNECT.getValue());

            connectLogDao.save(connect);
        }

        String isFt = node.getCommentInfo(NodeCommentInfo.IS_FT);
        if (StringUtils.isNotBlank(isFt) && (!isFt.equals(String.valueOf(nodeDB.getFtNode())))) {
            fields.add("ftNode");
            nodeDB.setFtNode(Integer.parseInt(isFt));
        }

        String nodeIp = node.getNodeIp();
        if (StringUtils.isNotBlank(nodeIp) && !nodeIp.equals(nodeDB.getNodeIp())) {
            fields.add("nodeIp");
            nodeDB.setNodeIp(nodeIp);
        }

        int memoryTotal = node.getAvailableVmResource().getMaxMemory();
        if (memoryTotal != nodeDB.getMemoryTotal()) {
            fields.add("memoryTotal");
            nodeDB.setMemoryTotal(memoryTotal);
        }

        int cpuTotal = node.getAvailableVmResource().getMaxCore();
        if (cpuTotal != nodeDB.getCpuTotal()) {
            fields.add("cpuTotal");
            nodeDB.setCpuTotal(cpuTotal);
        }

        int physicalCpu = node.getAvailableVmResource().getPhysicalCpu();
        if (null == nodeDB.getPhysicalCpu() || physicalCpu != nodeDB.getPhysicalCpu()) {
            fields.add("physicalCpu");
            nodeDB.setPhysicalCpu(physicalCpu);
        }

        double diskTotal = node.getAvailableVmResource().getMaxDisk();
        if (null != nodeDB.getDiskTotal() && diskTotal != nodeDB.getDiskTotal()) {
            fields.add("diskTotal");
            nodeDB.setDiskTotal(diskTotal);
        }

        String hypervisorType = node.getCommentInfo(NodeCommentInfo.HYPERVISOR);
        if (StringUtils.isNotBlank(hypervisorType) && !hypervisorType.equals(nodeDB.getHypervisorType())) {
            fields.add("hypervisorType");
            nodeDB.setHypervisorType(hypervisorType);
        }

        String cloudPlatform = node.getCommentInfo(NodeCommentInfo.CLOUD_PLATFORM);
        if (StringUtils.isNotBlank(cloudPlatform) && !cloudPlatform.equals(nodeDB.getCloudPlatform())) {
            fields.add("cloudPlatform");
            nodeDB.setCloudPlatform(cloudPlatform);
        }

        if (fields.size() > 0) {
            for (String field : fields) {
                log.debug(String.format("【修改节点属性】节点：%s, 修改项：%s", node.getHostName(), field));
            }

            iComputeNodeDao.update(nodeDB, fields);
        }
    }
}
