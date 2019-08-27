package com.gcloud.controller.network.service.impl;

import com.gcloud.controller.ResourceProviders;
import com.gcloud.controller.compute.model.node.Node;
import com.gcloud.controller.compute.utils.RedisNodesUtil;
import com.gcloud.controller.network.dao.OvsBridgeDao;
import com.gcloud.controller.network.dao.OvsBridgeUsageDao;
import com.gcloud.controller.network.dao.PortDao;
import com.gcloud.controller.network.entity.OvsBridge;
import com.gcloud.controller.network.entity.OvsBridgeUsage;
import com.gcloud.controller.network.entity.Port;
import com.gcloud.controller.network.enums.OvsBridgeRefType;
import com.gcloud.controller.network.enums.OvsBridgeState;
import com.gcloud.controller.network.provider.IOvsBridgeProvider;
import com.gcloud.controller.network.service.IOvsBridgeService;
import com.gcloud.core.exception.GCloudException;
import com.gcloud.header.enums.ProviderType;
import com.gcloud.header.enums.ResourceType;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Slf4j
@Transactional
@Component
public class OvsBridgeServiceImpl implements IOvsBridgeService {

    @Autowired
    private OvsBridgeDao ovsBridgeDao;

    @Autowired
    private OvsBridgeUsageDao ovsBridgeUsageDao;

    @Autowired
    private PortDao portDao;

    @Override
    public String createOvsBridge(String bridge, String hostname, String name, String flowTaskId) {
        Node node = RedisNodesUtil.getComputeNodeByHostName(hostname);
        if (node == null) {
            throw new GCloudException("0010605::节点不存在");
        }
        return this.getProviderOrDefault().createOvsBridge(bridge, hostname, name, flowTaskId);
    }

    @Override
    public String deleteOvsBridge(String id, String flowTaskId) {

        OvsBridge ovsBridge = ovsBridgeDao.getById(id);

        if (ovsBridge == null) {
            throw new GCloudException("::不存在");
        }

        List<OvsBridgeUsage> usages = ovsBridgeUsageDao.getForUpdate(id);
        if (usages != null && usages.size() > 0) {
            throw new GCloudException("::有其他人在使用");
        }

        List<String> updateField = new ArrayList<>();
        updateField.add(ovsBridge.updateState(OvsBridgeState.DELETING.value()));

        ovsBridgeDao.update(ovsBridge, updateField);

        this.checkAndGetProvider(ovsBridge.getProvider()).deleteOvsBridge(id, flowTaskId, ovsBridge.getHostname(), ovsBridge.getBridge());
        return null;
    }

    @Override
    public String allocate(String id, OvsBridgeRefType refType, String refId) {

        OvsBridge usage = ovsBridgeDao.getById(id);
        if (usage == null) {
            throw new GCloudException("::ovs 桥不存在");
        }
        checkRefObject(refType, refId);

        OvsBridgeUsage ovsUsage = new OvsBridgeUsage();
        ovsUsage.setBridgeId(id);
        ovsUsage.setRefId(refId);
        ovsUsage.setRefType(refType.value());
        ovsUsage.setCreateTime(new Date());

        ovsBridgeUsageDao.allocate(ovsUsage);

        return null;
    }

    @Override
    public int release(String id, OvsBridgeRefType refType, String refId) {
        return ovsBridgeUsageDao.delete(id, refType, refId);
    }

    @Override
    public int release(OvsBridgeRefType refType, String refId) {
        return ovsBridgeUsageDao.delete(refType, refId);
    }

    private void checkRefObject(OvsBridgeRefType type, String refId) {

        switch (type) {
            case PORT:
                Port port = portDao.getById(refId);
                if (port == null) {
                    throw new GCloudException("::端口不存在");
                }
                break;
            default:
                break;
        }

    }

    private IOvsBridgeProvider getProviderOrDefault() {
        IOvsBridgeProvider provider = ResourceProviders.checkAndGet(ResourceType.OVS_BRIDGE, ProviderType.GCLOUD.getValue());
        return provider;
    }

    private IOvsBridgeProvider checkAndGetProvider(Integer providerType) {
        IOvsBridgeProvider provider = ResourceProviders.checkAndGet(ResourceType.OVS_BRIDGE, providerType);
        return provider;
    }

}
