
package com.gcloud.storage.service.impl;

import com.gcloud.common.util.SystemUtil;
import com.gcloud.core.exception.GCloudException;
import com.gcloud.core.messagebus.MessageBus;
import com.gcloud.core.util.MessageUtil;
import com.gcloud.header.compute.enums.StorageType;
import com.gcloud.header.enums.ProviderType;
import com.gcloud.header.storage.StorageErrorCodes;
import com.gcloud.header.storage.model.StoragePoolInfo;
import com.gcloud.header.storage.msg.node.pool.NodeReportStoragePoolReplyMsg;
import com.gcloud.service.common.compute.uitls.VmUtil;
import com.gcloud.storage.StorageNodeProp;
import com.gcloud.storage.service.IStoragePoolService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.File;

@Slf4j
@Component
public class NodeStoragePoolServiceImpl implements IStoragePoolService {

    @Autowired
    private StorageNodeProp props;

    @Autowired
    private MessageBus bus;

    @Autowired
    private StoragePools pools;

    @PostConstruct
    private void init() {
        log.info("[ReportStoragePool]: " + props.getReportPools());
        if (props.getReportPools() != null && props.getReportPools()) {
            if (props.getPools() != null) {
                for (ReportStoragePoolModel pool : props.getPools()) {
                    log.info("[ReportStoragePool]: " + pool);
                    try {
                        ProviderType provider = ProviderType.get(pool.getProvider());
                        if (provider == null) {
                            throw new GCloudException("provider type " + pool.getProvider() + " not found");
                        }
                        StorageType storageType = StorageType.value(pool.getStorageType());
                        if (storageType == null) {
                            throw new GCloudException(StorageErrorCodes.NO_SUCH_STORAGE_TYPE);
                        }
                        if (provider == ProviderType.GCLOUD && storageType == StorageType.LOCAL) {
                            File storageDir = new File(pool.getPoolName());
                            if (!storageDir.exists()) {
                                throw new GCloudException("path " + pool.getPoolName() + " not exist");
                            }
                            else if (!storageDir.isDirectory()) {
                                throw new GCloudException("path " + pool.getPoolName() + " is not a dir");
                            }
                            if (!pool.getPoolName().endsWith(File.separator)) {
                                pool.setPoolName(pool.getPoolName() + File.separator);
                            }
                        }
                        NodeReportStoragePoolReplyMsg msg = new NodeReportStoragePoolReplyMsg();
                        msg.setServiceId(MessageUtil.controllerServiceId());
                        msg.setProvider(pool.getProvider());
                        msg.setStorageType(pool.getStorageType());
                        msg.setPoolName(pool.getPoolName());
                        msg.setCategoryCode(pool.getCategoryCode());
                        msg.setHostname(VmUtil.getHostName());
                        this.bus.send(msg);

                        this.pools.add(pool.getProvider(), pool.getStorageType(), pool.getPoolName(), pool.getCategoryCode(), pool.getSnapshotType());

                    }
                    catch (Exception e) {
                        log.error("[ReportStoragePool] error: " + e.getMessage(), e);
                    }
                }
            }
        }
    }

	@Override
	public StoragePoolInfo getLocalPoolInfo(String poolName) {
		/*df /opt/gcloud/storage
		Filesystem              1K-blocks     Used Available Use% Mounted on
		/dev/mapper/centos-root  92115000 18264256  73850744  20% /opt/gcloud/storage   */
		String[] cmd1 = new String[] {"df", poolName};
		String[] cmd2 = new String[] {"grep", poolName};
		//String record = SystemUtil.run(cmd1, cmd2);
		String record = SystemUtil.run(cmd1);
		String[] records = record.split("\\n");
		String[] items = records[1].trim().split("\\s+");
		StoragePoolInfo info = new StoragePoolInfo();
		info.setUsedSize(Long.parseLong(items[2])/1024);
		info.setAvailSize(Long.parseLong(items[3])/1024);
		info.setTotalSize(info.getUsedSize() + info.getAvailSize());
		return info;
	}

}
