package com.gcloud.controller.network.timer;

import com.gcloud.controller.network.dao.FloatingIpDao;
import com.gcloud.controller.network.dao.NetworkDao;
import com.gcloud.controller.network.dao.PortDao;
import com.gcloud.controller.network.dao.RouterDao;
import com.gcloud.controller.network.dao.SecurityGroupDao;
import com.gcloud.controller.network.dao.SubnetDao;
import com.gcloud.controller.network.entity.FloatingIp;
import com.gcloud.controller.network.entity.Network;
import com.gcloud.controller.network.entity.Port;
import com.gcloud.controller.network.entity.Router;
import com.gcloud.controller.network.entity.SecurityGroup;
import com.gcloud.controller.network.entity.Subnet;
import com.gcloud.controller.network.provider.IFloatingIpProvider;
import com.gcloud.controller.network.provider.INetworkProvider;
import com.gcloud.controller.network.provider.IPortProvider;
import com.gcloud.controller.network.provider.IRouterProvider;
import com.gcloud.controller.network.provider.ISecurityGroupProvider;
import com.gcloud.controller.network.provider.ISubnetProvider;
import com.gcloud.core.cache.container.CacheContainer;
import com.gcloud.core.cache.enums.CacheType;
import com.gcloud.core.quartz.GcloudJobBean;
import com.gcloud.core.quartz.annotation.QuartzTimer;
import com.gcloud.core.service.SpringUtil;
import com.gcloud.header.enums.ProviderType;
import lombok.extern.slf4j.Slf4j;
import org.quartz.DisallowConcurrentExecution;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.PersistJobDataAfterExecution;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
@PersistJobDataAfterExecution
@DisallowConcurrentExecution
@QuartzTimer(fixedDelay = 1800 * 1000L)  // half hour
@Slf4j
public class RemoteNetworkDataSyncTimer extends GcloudJobBean {

    @Autowired
    private NetworkDao networkDao;
    @Autowired
    private SubnetDao subnetDao;
    @Autowired
    private SecurityGroupDao sgDao;
    @Autowired
    private FloatingIpDao fiDao;
    @Autowired
    private PortDao pDao;
    @Autowired
    private RouterDao rDao;

    // 记录上次更新时间，以便判断本次的数据是否在上次更新后又更新过，减少访问DB次数
    private long lastUpdateTime = 0L;
    private static final String updateTimeKey = "NetworkLastUpdateTime";

    @Override
    protected void executeInternal(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        Object time = CacheContainer.getInstance().get(CacheType.TIMER, updateTimeKey);
        if (time == null) {
            log.warn("get network last update time from redis got null");
        } else {
            lastUpdateTime = (long)time;
        }
        this.networkUpdate();
//        this.subnetUpdate();       do not have status, so no need to sync.
        this.floatingIpUpdate();
        this.portUpdate();
//        this.securityGroupUpdate();   security group does not have status , no need to sync.
        this.routerUpdate();

        lastUpdateTime = (new Date()).getTime();
        CacheContainer.getInstance().put(CacheType.TIMER, updateTimeKey, lastUpdateTime);
    }

    private void floatingIpUpdate() {
        log.debug("start floating ip syncing...");
        List<String> fields = Arrays.asList(FloatingIp.STATUS, FloatingIp.UPDATED_AT);
        for (IFloatingIpProvider f : SpringUtil.getBeans(IFloatingIpProvider.class)) {
            log.debug("syncing with provider: " + f.getClass().getName());
            if (f.providerType() == ProviderType.GCLOUD) continue;
            List<FloatingIp> ips = f.list(null);
            for (FloatingIp fi : ips) {
                if (fi.getUpdatedAt().getTime() <= lastUpdateTime) continue;

                try {
                    Map<String, Object> items = new HashMap<>();
                    items.put(FloatingIp.PROVIDER, fi.getProvider());
                    items.put(FloatingIp.PROVIDER_REF_ID, fi.getProviderRefId());
                    List<FloatingIp> entities = this.fiDao.findByProperties(items);

                    if (entities == null || entities.size() == 0) {
                        log.warn("can not found floating_ip with provider [" + fi.getProvider() + "] and provider_ref_id ["
                                + fi.getProviderRefId() + "], ignore update status.");
                        continue;
                    }
                    if (entities.size() > 1) {
                        log.warn("found more than one matched entity, ignore.");
                        continue;
                    }

                    FloatingIp target = entities.get(0);
                    target.setUpdatedAt(fi.getUpdatedAt());
                    target.setStatus(fi.getStatus());
                    this.fiDao.update(target, fields);
                }
                catch (Exception e) {
                    log.error("got exception when syncing volume status: " + e.getMessage());
                }
            }
        }
        log.debug("floating ip syncing done.");
    }

    private void networkUpdate() {
        log.debug("start network syncing...");
        List<String> fields = Arrays.asList(Network.STATUS, Network.UPDATED_AT);
        for (INetworkProvider n : SpringUtil.getBeans(INetworkProvider.class)) {
            log.debug("syncing with provider: " + n.getClass().getName());
            if (n.providerType() == ProviderType.GCLOUD) continue;

            Network network = networkDao.findOneByProperty(Network.PROVIDER, n.providerType().getValue());
            if(network == null){
                log.debug(String.format("%s network 没有数据不进行同步", n.providerType().name()));
                return;
            }

            List<Network> networkList = n.list(null);
            for (Network nw : networkList) {
                if (nw.getUpdatedAt().getTime() <= lastUpdateTime) continue;

                try {
                    Map<String, Object> items = new HashMap<>();
                    items.put(Network.PROVIDER, nw.getProvider());
                    items.put(Network.PROVIDER_REF_ID, nw.getProviderRefId());
                    List<Network> entities = this.networkDao.findByProperties(items);

                    // volume must be unique.
                    if (entities == null || entities.size() == 0) {
                        log.warn("can not found network with provider [" + nw.getProvider() + "] and provider_ref_id ["
                                + nw.getProviderRefId() + "], ignore update status.");
                        continue;
                    }
                    if (entities.size() > 1) {
                        log.warn("found more than one matched entity, ignore.");
                        continue;
                    }

                    Network target = entities.get(0);
                    target.setUpdatedAt(nw.getUpdatedAt());
                    target.setStatus(nw.getStatus());
                    this.networkDao.update(target, fields);
                }
                catch (Exception e) {
                    log.error("got exception when syncing network status: " + e.getMessage());
                }
            }
        }
        log.debug("network syncing done.");
    }

    private void portUpdate() {
        log.debug("start port syncing...");
        List<String> fields = Arrays.asList(Port.STATUS, Port.UPDATED_AT);
        for (IPortProvider ip : SpringUtil.getBeans(IPortProvider.class)) {
            log.debug("syncing with provider: " + ip.getClass().getName());
            if (ip.providerType() == ProviderType.GCLOUD) continue;

            Port port = pDao.findOneByProperty(Port.PROVIDER, ip.providerType().getValue());
            if(port == null){
                log.debug(String.format("%s port 没有数据不进行同步", ip.providerType().name()));
                return;
            }

            List<Port> ports = ip.list(null);
            for (Port p : ports) {
                if (p.getUpdatedAt().getTime() <= lastUpdateTime) continue;
                try {
                    Map<String, Object> items = new HashMap<>();
                    items.put(Port.PROVIDER, p.getProvider());
                    items.put(Port.PROVIDER_REF_ID, p.getProviderRefId());
                    List<Port> entities = this.pDao.findByProperties(items);

                    if (entities == null || entities.size() == 0) {
                        log.warn("can not found port with provider [" + p.getProvider() + "] and provider_ref_id ["
                                + p.getProviderRefId() + "], ignore update status.");
                        continue;
                    }
                    if (entities.size() > 1) {
                        log.warn("found more than one matched entity, ignore.");
                        continue;
                    }

                    Port target = entities.get(0);
                    target.setUpdatedAt(p.getUpdatedAt());
                    target.setStatus(p.getStatus());
                    this.pDao.update(target, fields);
                }
                catch (Exception e) {
                    log.error("got exception when syncing port status: " + e.getMessage());
                }
            }
        }
        log.debug("port syncing done.");
    }

    private void subnetUpdate() {
        log.debug("start subnet syncing...");
        List<String> items = Arrays.asList("name", "updated_at");
        for (ISubnetProvider sp : SpringUtil.getBeans(ISubnetProvider.class)) {
            log.debug("syncing with provider: " + sp.getClass().getName());
            if (sp.providerType() == ProviderType.GCLOUD) continue;
            List<Subnet> subnets = sp.list(null);
            for (Subnet s : subnets) {
                if (s.getUpdatedAt().getTime() > lastUpdateTime) {
                    subnetDao.update(s, items);
                }
            }
        }
        log.debug("subnet sync done.");
    }

    private void securityGroupUpdate() {
        log.debug("security group syncing start...");
        List<String> items = Arrays.asList("updated_at", "name");
        for (ISecurityGroupProvider s : SpringUtil.getBeans(ISecurityGroupProvider.class)) {
            log.debug("syncing with provider: " + s.getClass().getName());
            if (s.providerType() == ProviderType.GCLOUD) continue;
            List<SecurityGroup> group = s.list(null);
            for (SecurityGroup sg : group) {
                if (sg.getUpdatedAt().getTime() > lastUpdateTime) {
                    sgDao.update(sg, items);
                }
            }
        }
        log.debug("security group sync done.");
    }

    private void routerUpdate() {
        log.debug("router syncing start...");
        List<String> fields = Arrays.asList(Router.UPDATED_AT, Router.STATUS);
        for (IRouterProvider router : SpringUtil.getBeans(IRouterProvider.class)) {
            log.debug("syncing with provider: " + router.getClass().getName());
            if (router.providerType() == ProviderType.GCLOUD) continue;

            Router routerOne = rDao.findOneByProperty(Router.PROVIDER, router.providerType().getValue());
            if(routerOne == null){
                log.debug(String.format("%s router 没有数据不进行同步", router.providerType().name()));
                return;
            }

            List<Router> routers = router.list(null);
            for (Router r : routers) {
                if (r.getUpdatedAt().getTime() <= lastUpdateTime) continue;
                try {
                    Map<String, Object> items = new HashMap<>();
                    items.put(Router.PROVIDER, r.getProvider());
                    items.put(Router.PROVIDER_REF_ID, r.getProviderRefId());
                    List<Router> entities = this.rDao.findByProperties(items);

                    if (entities == null || entities.size() == 0) {
                        log.warn("can not found router with provider [" + r.getProvider() + "] and provider_ref_id ["
                                + r.getProviderRefId() + "], ignore update status.");
                        continue;
                    }
                    if (entities.size() > 1) {
                        log.warn("found more than one matched entity, ignore.");
                        continue;
                    }

                    Router target = entities.get(0);
                    target.setUpdatedAt(r.getUpdatedAt());
                    target.setStatus(r.getStatus());
                    this.rDao.update(target, fields);
                }
                catch (Exception e) {
                    log.error("got exception when syncing router status: " + e.getMessage());
                }
            }
        }
        log.debug("router sync done.");
    }
}