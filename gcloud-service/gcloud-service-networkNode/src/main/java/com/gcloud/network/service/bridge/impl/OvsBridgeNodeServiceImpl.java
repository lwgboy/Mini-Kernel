package com.gcloud.network.service.bridge.impl;

import com.gcloud.core.exception.GCloudException;
import com.gcloud.network.service.bridge.IOvsBridgeNodeService;
import com.gcloud.service.common.util.NetworkNodeUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class OvsBridgeNodeServiceImpl implements IOvsBridgeNodeService {

    @Override
    public void createOvsBridge(String bridgeName) {

        try{
            if(!NetworkNodeUtil.isOvsBrExist(bridgeName)){
                NetworkNodeUtil.addOvsBr(bridgeName);
            }
        }catch (Exception ex){
            if(!NetworkNodeUtil.isOvsBrExist(bridgeName)){
                log.error("1010604::创建ovs网桥失败", ex);
                throw new GCloudException("1010604::创建ovs网桥失败");
            }
        }

    }

    @Override
    public void deleteOvsBridge(String bridgeName) {

        try{
            if(NetworkNodeUtil.isOvsBrExist(bridgeName)){
                NetworkNodeUtil.deleteOvsBr(bridgeName);
            }
        }catch (Exception ex){
            if(NetworkNodeUtil.isOvsBrExist(bridgeName)){
                log.error("::删除ovs网桥失败", ex);
                throw new GCloudException("::删除ovs网桥失败");
            }
        }

    }
}
