package com.gcloud.controller.compute.timer;

import com.gcloud.common.util.StringUtils;
import com.gcloud.controller.compute.dao.InstanceDao;
import com.gcloud.controller.compute.entity.VmInstance;
import com.gcloud.controller.compute.model.node.AvailableVmResource;
import com.gcloud.controller.compute.model.node.Node;
import com.gcloud.controller.compute.model.node.ResourceUnit;
import com.gcloud.controller.compute.utils.RedisNodesUtil;
import com.gcloud.controller.compute.utils.VmControllerUtil;
import com.gcloud.core.cache.redis.lock.util.LockUtil;
import com.gcloud.core.cache.redis.template.GCloudRedisTemplate;
import com.gcloud.core.quartz.GcloudJobBean;
import com.gcloud.core.quartz.annotation.QuartzTimer;
import com.gcloud.header.compute.enums.TrashState;
import com.gcloud.header.compute.enums.VmState;
import com.gcloud.header.compute.enums.VmStepState;
import com.gcloud.service.common.Consts;
import lombok.extern.slf4j.Slf4j;
import org.quartz.DisallowConcurrentExecution;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.PersistJobDataAfterExecution;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Set;

/**
 * Created by yaowj on 2019/1/8.
 */
@Component
@PersistJobDataAfterExecution
@DisallowConcurrentExecution
@QuartzTimer(fixedDelay = 10 * 1000L)
@Slf4j
public class RefreshResourceTimer extends GcloudJobBean {

    @Autowired
    private InstanceDao instanceDao;

    @Autowired
    private GCloudRedisTemplate redisTemplate;

    @Override
    protected void executeInternal(JobExecutionContext jobExecutionContext) throws JobExecutionException {

        Set<String> hosts = RedisNodesUtil.getAllHostName();
        if(hosts == null || hosts.size() == 0){
            return;
        }
        for(String hostname : hosts){
            try{
                refreshResource(hostname);
            }catch (Exception ex){
                log.error(String.format("refresh node resource error, hostname = %s", hostname), ex);
            }
        }

    }


    private void refreshResource(String hostname){

        ResourceUnit ru = getResource(hostname);

        String lockName = VmControllerUtil.getNodesHostNameLock(hostname);
        String lockId = LockUtil.spinLock(lockName, Consts.Time.NODES_REDIS_LOCK_TIMEOUT, Consts.Time.NODES_REDIS_LOCK_GET_LOCK_TIMEOUT);
        try{
            String nodeKey = VmControllerUtil.getNodesHostNameKey(hostname);

            Node node = RedisNodesUtil.getComputeNodeByHostName(hostname);
            AvailableVmResource avr = node.getAvailableVmResource();

            if (avr != null && ru != null) {
                if (avr.getAvailableCore() != (avr.getMaxCore() - ru.getCore()) || avr.getAvailableMemory() != avr.getMaxMemory() - ru.getMemory()) {
                    // 数据库和内存已用量不一致，需要同步
                    node.setRefreshTime(node.getRefreshTime() + 1);
                    if (node.getRefreshTime() >= 3 || node.isFirst()) {

                        avr.setAvailableCore(avr.getMaxCore() - ru.getCore());
                        avr.setAvailableMemory(avr.getMaxMemory() - ru.getMemory());

                        node.setFirst(false);
                        node.setRefreshTime(0);
                    }
                } else {
                    node.setRefreshTime(0);
                }
                redisTemplate.setObject(nodeKey, node);
            }
        }catch (Exception ex){
            log.error(String.format("refresh node resource error, hostname = %s", hostname), ex);
        }finally {
            LockUtil.releaseSpinLock(lockName, lockId);
        }

    }

    private ResourceUnit getResource(String hostname){
        List<VmInstance> instances = instanceDao.findByProperty(VmInstance.HOSTNAME, hostname);
        int totalCore = 0;
        int totalMemory = 0;
        if(instances != null && instances.size() > 0){
            for(VmInstance ins : instances){

                //已经删除不计算
                if(StringUtils.isNotBlank(ins.getTrashed()) && !TrashState.LOGICALDELETEING.getValue().equals(ins.getTaskState())){
                    continue;
                }

                //正在开机或已经是运行状态，则计算
                if(VmStepState.STARTING.value().equals(ins.getStepState())
                   || (!VmState.STOPPED.value().equals(ins.getState()) && !VmState.DISABLED.value().equals(ins.getState()))){
                    totalCore += ins.getCore();
                    totalMemory += ins.getMemory();
                }

            }
        }

        ResourceUnit ru = new ResourceUnit();
        ru.setCore(totalCore);
        ru.setMemory(totalMemory);

        return ru;
    }
}
