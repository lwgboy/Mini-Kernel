package com.gcloud.controller.compute.utils;

import com.gcloud.common.util.ObjectUtil;
import com.gcloud.controller.compute.cache.NodeHaCache;
import com.gcloud.controller.compute.model.node.Node;
import com.gcloud.controller.compute.model.node.NodeCommentInfo;
import com.gcloud.controller.compute.model.node.RefreshNodeParams;
import com.gcloud.controller.compute.model.node.ResourceUnit;
import com.gcloud.core.cache.redis.lock.util.LockUtil;
import com.gcloud.core.cache.redis.template.GCloudRedisTemplate;
import com.gcloud.core.messagebus.MessageBus;
import com.gcloud.core.service.SpringUtil;
import com.gcloud.core.util.MessageUtil;
import com.gcloud.header.compute.enums.BooleanType;
import com.gcloud.header.compute.msg.node.vm.senior.CleanAdoptCacheMsg;
import com.gcloud.service.common.Consts;
import lombok.extern.slf4j.Slf4j;

import java.text.MessageFormat;
import java.time.Duration;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Slf4j
public class RedisNodesUtil {

    public static void registerComputeNode(String nodeName, Node node) {

        // 修改的也要上锁

        String lockName = VmControllerUtil.getNodesHostNameLock(nodeName);
        String nodeKey = VmControllerUtil.getNodesHostNameKey(nodeName);
        String lockid = "";
        try {
            lockid = LockUtil.spinLock(lockName, Consts.Time.NODES_REDIS_LOCK_TIMEOUT, Consts.Time.NODES_REDIS_LOCK_GET_LOCK_TIMEOUT);
        } catch (Exception ex) {
            log.error("节点注册失败:获取锁失败,hostName=" + nodeName, ex);
            return;
        }

        try {
            GCloudRedisTemplate redisTemplate = SpringUtil.getBean(GCloudRedisTemplate.class);
            // 分配前要重新获取nodes
            redisTemplate.setObject(nodeKey, node);
            log.debug(String.format("【节点-情况】连接成功，node：%s", nodeName));

            try {
                // TODO 后面天增加cleanAdopt的逻辑
                MessageBus bus = SpringUtil.getBean(MessageBus.class);
                CleanAdoptCacheMsg msg = new CleanAdoptCacheMsg();
                msg.setHostname(nodeName);
                msg.setServiceId(MessageUtil.computeServiceId(nodeName));
                bus.send(msg);
            } catch (Exception e) {
                log.error("【节点重连-清空节点收养缓存】 调用错误， hostName:" + nodeName + "eroor:" + e.getMessage());
            }

            try {
                log.debug("【节点重连-清空故障恢复标记】hostName" + nodeName);
                NodeHaCache.removeNodeHa(nodeName);
            } catch (Exception e) {
                log.error("【节点重连-清空故障恢复标记】 调用错误， hostName:" + nodeName + "eroor:" + e.getMessage());
            }

        } catch (Exception ex) {
            log.error("::registerComputeNode error", ex);
        } finally {
            LockUtil.releaseSpinLock(lockName, lockid);
        }

    }
    
    public static void updateComputeNodeZone(String nodeName, String zoneId) {
        String lockName = VmControllerUtil.getNodesHostNameLock(nodeName);
        String nodeKey = VmControllerUtil.getNodesHostNameKey(nodeName);
        String lockid = "";
        try {
            lockid = LockUtil.spinLock(lockName, Consts.Time.NODES_REDIS_LOCK_TIMEOUT, Consts.Time.NODES_REDIS_LOCK_GET_LOCK_TIMEOUT);
        }
        catch (Exception ex) {
            log.error("更新节点可用区失败:获取锁失败,hostName=" + nodeName, ex);
            return;
        }
        try {
            GCloudRedisTemplate redisTemplate = SpringUtil.getBean(GCloudRedisTemplate.class);
            Node n = (Node)redisTemplate.getObject(nodeKey);
            if (n != null) {
                n.setZoneId(zoneId);
                redisTemplate.setObject(nodeKey, n);
            }
        }
        catch (Exception ex) {
            log.error("::更新节点可用区失败 error", ex);
        }
        finally {
            LockUtil.releaseSpinLock(lockName, lockid);
        }
    }

    /**
     * 判断计算资源
     *
     * @param hostName 计算节点全名,如compute_testNode,全名找不到节点,会通过短名
     * @param memory   所需内存
     * @return 计算资源单元.若其中有一个属性少于0, 表示资源不足.返回null.表示节点不可用.
     */
    public static ResourceUnit checkComputeNode(String hostName, int core, int memory) {

        String nodeKey = VmControllerUtil.getNodesHostNameKey(hostName);

        try {

            GCloudRedisTemplate redisTemplate = SpringUtil.getBean(GCloudRedisTemplate.class);
            Node n = (Node)redisTemplate.getObject(nodeKey);
            if (n == null)
                return null;
            if (n.getAvailableVmResource() != null) {
                ResourceUnit ru = new ResourceUnit();
                ru.setCore(n.getAvailableVmResource().getAvailableCore() - core);
                ru.setMemory(n.getAvailableVmResource().getAvailableMemory() - memory);
                return ru;
            }

        } catch (Exception ex) {
            log.error("checkComputeNode error", ex);
        }

        return null;
    }

    /**
     * 分配计算资源
     *
     * @param hostName 节点全名,全名找不到节点,会通过短名
     * @param cores
     * @param memory   使用共享存储时值0
     * @return 计算资源单元.若其中有一个属性少于0, 表示资源不足.返回null.表示节点不可用.
     */
    public static ResourceUnit allocateCompute(String hostName, int cores, int memory) {
        log.debug(String.format("【资源管理-%s-调用占用资源-开始-%s】cpu:%s,memory:%s", ObjectUtil.getCalledMethod(), hostName, cores, memory));

        String lockName = VmControllerUtil.getNodesHostNameLock(hostName);
        String nodeKey = VmControllerUtil.getNodesHostNameKey(hostName);
        String lockid = "";
        try {
            lockid = LockUtil.spinLock(lockName, Consts.Time.NODES_REDIS_LOCK_TIMEOUT, Consts.Time.NODES_REDIS_LOCK_GET_LOCK_TIMEOUT);
        } catch (Exception ex) {
            log.error("分配资源失败:获取锁失败,hostName=" + hostName, ex);
            return null;
        }

        try {
            GCloudRedisTemplate redisTemplate = SpringUtil.getBean(GCloudRedisTemplate.class);
            // 分配前要重新获取nodes
            ResourceUnit ru = new ResourceUnit();
            Node n = (Node)redisTemplate.getObject(nodeKey);
            if (n != null)
                if (n.getAvailableVmResource() != null) {
                    int freshCore = n.getAvailableVmResource().getAvailableCore() - cores;
                    int freshMemory = n.getAvailableVmResource().getAvailableMemory() - memory;
                    ru.setCore(freshCore);
                    ru.setMemory(freshMemory);
                    if (freshCore < 0 || freshMemory < 0) {
                        return ru;
                    }
                    n.getAvailableVmResource().setAvailableCore(freshCore);
                    n.getAvailableVmResource().setAvailableMemory(freshMemory);
                    redisTemplate.setObject(nodeKey, n);
                    log.debug(String.format("【资源管理-%s-占用资源之后当前可用资源-%s】cpu:%s,memory:%s", hostName, ObjectUtil.getCalledMethod(), n.getAvailableVmResource().getAvailableCore(), n.getAvailableVmResource().getAvailableMemory()));
                    return ru;
                }
        } catch (Exception ex) {
            log.error("::allocateCompute error", ex);
        } finally {
            LockUtil.releaseSpinLock(lockName, lockid);
        }

        return null;
    }

    /**
     * 释放计算资源
     *
     * @param hostName 节点全名,全名找不到节点,会通过短名
     * @param cores
     * @param memory
     */
    public static void releaseCompute(String hostName, int cores, int memory) {
        log.debug(String.format("【资源管理-%s-调用释放资源-开始-%s】cpu:%s,memory:%s", ObjectUtil.getCalledMethod(), hostName, cores, memory));

        String lockName = VmControllerUtil.getNodesHostNameLock(hostName);
        String nodeKey = VmControllerUtil.getNodesHostNameKey(hostName);

        String lockid = "";
        try {
            lockid = LockUtil.spinLock(lockName, Consts.Time.NODES_REDIS_LOCK_TIMEOUT, Consts.Time.NODES_REDIS_LOCK_GET_LOCK_TIMEOUT);
        } catch (Exception ex) {
            log.error("施放资源:获取锁失败,hostName=" + hostName, ex);
            return;
        }

        try {
            GCloudRedisTemplate redisTemplate = SpringUtil.getBean(GCloudRedisTemplate.class);
            // 分配前要重新获取nodes
            Node n = (Node)redisTemplate.getObject(nodeKey);
            if (n != null)
                if (n.getAvailableVmResource() != null) {
                    n.getAvailableVmResource().setAvailableCore(n.getAvailableVmResource().getAvailableCore() + cores);
                    n.getAvailableVmResource().setAvailableMemory(n.getAvailableVmResource().getAvailableMemory() + memory);
                }

            redisTemplate.setObject(nodeKey, n);
            log.debug(String.format("【资源管理-%s-释放资源之后当前可用资源-%s】cpu:%s,memory:%s", ObjectUtil.getCalledMethod(), hostName, n.getAvailableVmResource().getAvailableCore(), n.getAvailableVmResource().getAvailableMemory()));
        } catch (Exception ex) {
            log.error("::releaseCompute error", ex);
        } finally {
            LockUtil.releaseSpinLock(lockName, lockid);
        }

    }

    /**
     * 按全名获取计算节点对象
     *
     * @param hostName 节点全名,如compute_ip,带节点类型字串
     * @return 节点Node对象 不存在返回null
     */
    public static Node getComputeNodeByHostName(String hostName) {

        Node node = null;
        String nodeKey = VmControllerUtil.getNodesHostNameKey(hostName);

        try {
            GCloudRedisTemplate redisTemplate = SpringUtil.getBean(GCloudRedisTemplate.class);
            node = (Node)redisTemplate.getObject(nodeKey);
        } catch (Exception ex) {
            log.error("getComputeNodeByHostName error", ex);
        }

        return node;
    }

    /**
     * 按全名获取节点Ip
     *
     * @param hostName 节点全名,如compute_ip,带节点类型字串
     */
    public static String getNodeIpByHostName(String hostName) {
        return null;
    }

    /**
     * 移除解注册的计算节点
     */
    public static void cleanComputeNodes(List<String> hostNames) {

        GCloudRedisTemplate redisTemplate = SpringUtil.getBean(GCloudRedisTemplate.class);

        Set<String> hostSet = getAllHostName();
        if (hostSet == null) {
            return;
        }

        //剩下的就是失去连接，需要移除的
        hostSet.removeAll(hostNames);

        for (String rm : hostSet) {

            String lockName = VmControllerUtil.getNodesHostNameLock(rm);
            String nodeKey = VmControllerUtil.getNodesHostNameKey(rm);
            String lockid = "";

            log.debug(String.format("获取 Lock 开始:%s", lockName));
            try {
                lockid = LockUtil.spinLock(lockName, Consts.Time.NODES_REDIS_LOCK_TIMEOUT, Consts.Time.NODES_REDIS_LOCK_GET_LOCK_TIMEOUT);
            } catch (Exception ex) {
                log.error("清除节点失败:获取锁失败,hostName=" + rm, ex);
                continue;
            }
            log.debug(String.format("获取 Lock结束:%s", lockName));
            try {

                redisTemplate.delete(nodeKey);
                log.error(String.format("【节点-情况】失去连接，node：%s", rm));
            } catch (Exception ex) {
                log.error("getComputeNodeByHostName error", ex);
            } finally {

                LockUtil.releaseSpinLock(lockName, lockid);

            }
        }

        // TODO 以后用失去连接的观察者
        try {
            // OperatorCache.cleanHostOperCache(removeList);
            for (String hostname : hostSet) {
                redisTemplate.delete(VmControllerUtil.getVmStateSyncKey(hostname));
            }
        } catch (Exception ex) {
            log.error("清除操作缓存失败", ex);
        }

    }

    public static Map<String, Node> getComputeFaultTolreantNodes(String hyperType) {
        Map<String, Node> ftNodes = new HashMap<String, Node>();
        Map<String, Node> computeNodes = getComputeNodes();
        Iterator<String> it = computeNodes.keySet().iterator();
        while (it.hasNext()) {
            String key = it.next().toString();
            Node node = computeNodes.get(key);
            if (node != null && BooleanType.TRUE.getValue() == Integer.parseInt(node.getCommentInfo(NodeCommentInfo.IS_FT)) && hyperType.equals(node.getCommentInfo(NodeCommentInfo.HYPERVISOR))) {
                ftNodes.put(key, node);
            }
        }
        return ftNodes;
    }

    public static void updateNodeAlive(String hostName, int timeout) {

        try {

            GCloudRedisTemplate redisTemplate = SpringUtil.getBean(GCloudRedisTemplate.class);
            String aliveKey = VmControllerUtil.getNodesAliveKey(hostName);
            redisTemplate.opsForValue().set(aliveKey, hostName, Duration.ofSeconds(timeout));

        } catch (Exception ex) {
            log.error("updateNodeAlive error", ex);
        }
    }

    public static Map<String, Node> getComputeNodes() {

        Map<String, Node> nodeMap = new HashMap<String, Node>();
        GCloudRedisTemplate redisTemplate = SpringUtil.getBean(GCloudRedisTemplate.class);

        Set<String> hostSet = getAllHostName();
        if (hostSet != null && hostSet.size() > 0) {

            for (String hostname : hostSet) {
                try {
                    String nodeKey = VmControllerUtil.getNodesHostNameKey(hostname);
                    Node n = (Node)redisTemplate.getObject(nodeKey);
                    if (n != null) {
                        nodeMap.put(hostname, n);
                    }
                } catch (Exception ex) {
                    log.error("get node error", ex);
                }

            }

        }

        return nodeMap;
    }

    public static Set<String> getAllHostName() {

        Set<String> hostKeySet = null;
        Set<String> hostNameSet = null;

        try {
            String key = MessageFormat.format(Consts.RedisKey.GCLOUD_CONTROLLER_COMPUTE_NODES_COMPUTE_NODE_HOSTNAME, "*");
            GCloudRedisTemplate redisTemplate = SpringUtil.getBean(GCloudRedisTemplate.class);
            hostKeySet = redisTemplate.keys(key);

        } catch (Exception ex) {
            log.error("getAllNodes error", ex);
        }

        if (hostKeySet != null && hostKeySet.size() > 0) {
            hostNameSet = new HashSet<String>();
            for (String hostKey : hostKeySet) {
                String hostname = VmControllerUtil.getHostNameByHostKey(hostKey);
                hostNameSet.add(hostname);
            }
        }

        return hostNameSet;

    }

    /**
     * 检测计算节点是否为容错节点
     *
     * @param computeNode
     * @return
     */
    public static boolean checkIsFTNode(Node computeNode) {
        if (computeNode != null && "1".equals(computeNode.getCommentInfo(NodeCommentInfo.IS_FT))) {
            return true;
        } else {
            return false;
        }

    }

    public static Integer getAllAliveHostSize() {

        Set<String> hostKeySet = null;

        try {
            String key = MessageFormat.format(Consts.RedisKey.GCLOUD_CONTROLLER_COMPUTE_NODES_COMPUTE_NODE_ALIVE, "*");
            GCloudRedisTemplate redisTemplate = SpringUtil.getBean(GCloudRedisTemplate.class);
            hostKeySet = redisTemplate.keys(key);

        } catch (Exception ex) {
            log.error("getAllHostSize error", ex);
        }

        if (hostKeySet != null) {
            return hostKeySet.size();
        }
        return 0;

    }

    /**
     * 分配计算资源
     *
     * @param hostName 节点全名,全名找不到节点,会通过短名
     *                 使用共享存储时值0
     * @return 计算资源单元.若其中有一个属性少于0, 表示资源不足.返回null.表示节点不可用.
     */
    public static void refreshNode(String hostName, RefreshNodeParams p) {


        String lockName = VmControllerUtil.getNodesHostNameLock(hostName);
        String nodeKey = VmControllerUtil.getNodesHostNameKey(hostName);

        String lockid = "";
        try {
            lockid = LockUtil.spinLock(lockName, Consts.Time.NODES_REDIS_LOCK_TIMEOUT, Consts.Time.NODES_REDIS_LOCK_GET_LOCK_TIMEOUT);
        } catch (Exception ex) {
            log.error("refreshNode:获取锁失败,hostName=" + hostName, ex);
        }

        try {
            GCloudRedisTemplate redisTemplate = SpringUtil.getBean(GCloudRedisTemplate.class);
            Node n = (Node)redisTemplate.getObject(nodeKey);
            if (n != null)
                if (n.getAvailableVmResource() != null) {
                    n.getAvailableVmResource().setCpuUtil(p.getCpuUtil());
                    n.getAvailableVmResource().setMemoryUtil(p.getMemoryUtil());
                    redisTemplate.setObject(nodeKey, n);
                }
        } catch (Exception ex) {
            log.error("::refreshNode error", ex);
        } finally {
            LockUtil.releaseSpinLock(lockName, lockid);
        }
    }
}
