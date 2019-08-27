package com.gcloud.controller.compute.handler.node.vm.base;

import com.gcloud.common.util.StringUtils;
import com.gcloud.controller.compute.dao.InstanceDao;
import com.gcloud.controller.compute.dispatcher.Dispatcher;
import com.gcloud.controller.compute.entity.VmInstance;
import com.gcloud.controller.compute.utils.VmControllerUtil;
import com.gcloud.core.cache.redis.template.GCloudRedisTemplate;
import com.gcloud.core.handle.AsyncMessageHandler;
import com.gcloud.core.handle.Handler;
import com.gcloud.header.compute.enums.VmState;
import com.gcloud.header.compute.msg.node.vm.base.SyncStateMsg;
import com.gcloud.header.compute.msg.node.vm.model.VmStateInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by yaowj on 2019/1/10.
 */
@Slf4j
@Handler
public class SyncStateHandler extends AsyncMessageHandler<SyncStateMsg> {

    @Autowired
    private InstanceDao instanceDao;

    @Autowired
    private GCloudRedisTemplate redisTemplate;

    private static int MAX_DIFF_TIME = 2;

    @Override
    public void handle(SyncStateMsg msg) {

        String hostname = msg.getHostname();
        Map<String, VmStateInfo> stateInfoMap = msg.getStateInfos();

        if(stateInfoMap == null || stateInfoMap.size() == 0){
            return;
        }

        List<VmInstance> instances = instanceDao.findByProperty(VmInstance.HOSTNAME, hostname);
        if(instances == null || instances.size() == 0){
            return;
        }

        Map<Object, Object> syncInfoMap = redisTemplate.opsForHash().entries(VmControllerUtil.getVmStateSyncKey(hostname));

        for(VmInstance instance : instances){
            VmStateInfo info = stateInfoMap.get(instance.getId());
            if(info == null){
                continue;
            }

            //有任务状态不处理
            if(StringUtils.isNotBlank(instance.getTaskState()) || StringUtils.isNotBlank(instance.getStepState())){
                continue;
            }

            int times = 0;

            if(syncInfoMap != null && syncInfoMap.get(instance.getId()) != null){
                try{
                    times = Integer.valueOf(String.valueOf(syncInfoMap.get(instance.getId())));
                }catch (Exception ex){
                    log.error("转int失败", ex);
                }
            }

            try{
                syncState(instance, info, times);
            }catch (Exception ex){
                log.error("同步状态失败", ex);
            }

        }

    }

    private void syncState(VmInstance instance, VmStateInfo info, int times){

        if(instance.getState().equals(info.getGcState())){
            return;
        }

        //多次不一样再修改
        if(times < MAX_DIFF_TIME - 1){
            times++;
            redisTemplate.opsForHash().put(VmControllerUtil.getVmStateSyncKey(instance.getHostname()), instance.getId(), String.valueOf(times));
            return;
        }

        // 关机，释放资源
        if (VmState.RUNNING.value().equals(instance.getState()) && VmState.STOPPED.value().equals(info.getGcState())) {
            Dispatcher.dispatcher().release(instance.getHostname(), instance.getCore(), instance.getMemory());
        }
        // 开机，分配资源
        if ((VmState.STOPPED.value().equals(instance.getState()) || VmState.DISABLED.value().equals(instance.getState())) && VmState.RUNNING.value().equals(info.getGcState())) {
            Dispatcher.dispatcher().assignNode(instance.getHostname(), instance.getCore(), instance.getMemory());
        }

        List<String> updateFiled = new ArrayList<>();
        updateFiled.add(instance.updateLastState(instance.getState()));
        updateFiled.add(instance.updateState(info.getGcState()));

        instanceDao.update(instance, updateFiled);

        redisTemplate.opsForHash().delete(VmControllerUtil.getVmStateSyncKey(instance.getHostname()), instance.getId());
    }
}
