package com.gcloud.controller.network.provider.impl;

import com.gcloud.common.util.StringUtils;
import com.gcloud.controller.compute.utils.VmControllerUtil;
import com.gcloud.controller.network.dao.OvsBridgeDao;
import com.gcloud.controller.network.entity.OvsBridge;
import com.gcloud.controller.network.enums.OvsBridgeState;
import com.gcloud.controller.network.provider.IOvsBridgeProvider;
import com.gcloud.core.exception.GCloudException;
import com.gcloud.core.messagebus.MessageBus;
import com.gcloud.core.util.MessageUtil;
import com.gcloud.header.enums.ProviderType;
import com.gcloud.header.enums.ResourceType;
import com.gcloud.header.network.msg.node.bridge.CreateOvsBridgeMsg;
import com.gcloud.header.network.msg.node.bridge.DeleteOvsBridgeMsg;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.UUID;

@Slf4j
@Component
public class GcloudOvsBridgeProvider implements IOvsBridgeProvider {

    @Autowired
    private OvsBridgeDao ovsBridgeDao;

    @Autowired
    private MessageBus bus;

    @Override
    public ResourceType resourceType() {
        return ResourceType.OVS_BRIDGE;
    }

    @Override
    public ProviderType providerType() {
        return ProviderType.GCLOUD;
    }

    @Override
    public String createOvsBridge(String bridge, String hostname, String name, String flowTaskId) {
        OvsBridge ovsBridge = new OvsBridge();
        ovsBridge.setId(UUID.randomUUID().toString());
        ovsBridge.setState(OvsBridgeState.CREATING.value());
        ovsBridge.setCreateTime(new Date());
        ovsBridge.setName(name);
        ovsBridge.setHostname(hostname);
        ovsBridge.setProvider(providerType().getValue());
        ovsBridge.setProviderRefId(ovsBridge.getId());

        if (StringUtils.isBlank(bridge)) {
            bridge = VmControllerUtil.generateOvsBridge();
        }
        ovsBridge.setBridge(bridge);
        try {
            ovsBridgeDao.save(ovsBridge);
        }
        catch (DuplicateKeyException dex) {
            log.error("，保存失败，违反唯一性约束", dex);
            throw new GCloudException("::host bridge is used");
        }

        CreateOvsBridgeMsg msg = new CreateOvsBridgeMsg();
        msg.setId(ovsBridge.getId());
        msg.setBridge(bridge);
        msg.setServiceId(MessageUtil.networkServiceId(hostname));
        msg.setTaskId(flowTaskId);
        bus.send(msg);

        return ovsBridge.getId();
    }

    @Override
    public void deleteOvsBridge(String id, String flowTaskId, String hostname, String bridge) {
        DeleteOvsBridgeMsg msg = new DeleteOvsBridgeMsg();
        msg.setId(id);
        msg.setBridge(bridge);
        msg.setServiceId(MessageUtil.computeServiceId(hostname));
        msg.setTaskId(flowTaskId);

        bus.send(msg);
    }

}
