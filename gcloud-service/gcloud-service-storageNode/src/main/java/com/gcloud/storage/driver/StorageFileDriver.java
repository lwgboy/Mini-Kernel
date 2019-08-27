
package com.gcloud.storage.driver;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.gcloud.common.util.SystemUtil;
import com.gcloud.core.exception.GCloudException;
import com.gcloud.core.messagebus.MessageBus;
import com.gcloud.core.util.MessageUtil;
import com.gcloud.header.ResourceProviderVo;
import com.gcloud.header.compute.enums.FileFormat;
import com.gcloud.header.compute.enums.Unit;
import com.gcloud.header.image.msg.api.ApiGenDownloadMsg;
import com.gcloud.header.image.msg.api.ApiGenDownloadReplyMsg;
import com.gcloud.header.storage.StorageErrorCodes;
import com.gcloud.header.storage.enums.SnapshotType;
import com.gcloud.header.storage.enums.StoragePoolDriver;
import com.gcloud.service.common.compute.model.QemuInfo;
import com.gcloud.service.common.compute.uitls.DiskQemuImgUtil;
import com.gcloud.service.common.compute.uitls.LogUtil;
import com.gcloud.storage.NodeStoragePoolVo;
import com.gcloud.storage.StorageNodeProp;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class StorageFileDriver implements IStorageDriver {

    private static final String EMPTY_IMAGE_NAME = "empty.qcow2";

    @Autowired
    private StorageNodeProp props;

    @Autowired
    private MessageBus bus;

    @Override
    public StoragePoolDriver driver() {
        return StoragePoolDriver.FILE;
    }

    public void createDisk(NodeStoragePoolVo pool, String volumeId, Integer size, String imageId) throws GCloudException {
        String volumePath = this.genDiskPath(pool.getPoolName(), volumeId);
        String imagePath = imageId == null ? this.getEmptyImage(pool.getPoolName()) : this.getCacheImagePath(imageId);
        //String imagePath = this.props.getCachedImagesPath() + imageId;
        DiskQemuImgUtil.create(imagePath, volumePath, size, Unit.G);
    }

    private String getEmptyImage(String poolName) {
        String path = this.genDiskPath(poolName, EMPTY_IMAGE_NAME);
        File file = new File(path);
        if (!file.exists()) {
            synchronized (StorageFileDriver.class) {
                file = new File(path);
                if (!file.exists()) {
                    DiskQemuImgUtil.create(1, "G", path, FileFormat.QCOW2.getValue());
                }
            }
        }
        return path;
    }

    private String genDiskPath(String poolName, String volumeId) {
        return poolName + volumeId;
    }

    private String checkAndGet(String poolName, String volumeId) {
        String path = this.genDiskPath(poolName, volumeId);
        this.checkAndGetFile(path);
        return path;
    }

    private File checkAndGetFile(String path) {
        File file = new File(path);
        if (!file.exists()) {
            throw new GCloudException(StorageErrorCodes.FAILED_TO_FIND_VOLUME);
        }
        return file;
    }
    
    private String getCacheImagePath(String imageId) {
    	return this.props.getCachedImagesPath() + imageId;//镜像缓存路径配置需要跟imageNode中配置一致
    }

    /*synchronized private String checkAndDownloadImage(String imageId) {
        String imagePath = this.props.getCachedImagesPath();
        if (!imagePath.endsWith(File.separator)) {
            imagePath += File.separator;
        }
        File imageDir = new File(imagePath);
        if (!imageDir.exists()) {
            imageDir.mkdirs();
        }
        File imageFile = new File(imagePath + imageId);
        if (!imageFile.exists()) {
            String tmp = imagePath + imageId + ".tmp";
            log.info("image not exist, downloading {}", tmp);
            ApiGenDownloadMsg msg = new ApiGenDownloadMsg();
            msg.setServiceId(MessageUtil.controllerServiceId());
            msg.setImageId(imageId);
            ApiGenDownloadReplyMsg reply = this.bus.call(msg, ApiGenDownloadReplyMsg.class);
            if (!reply.getSuccess()) {
                throw new GCloudException(reply.getErrorMsg());
            }
            String[] cmd = new String[] {"glance", "--os-image-url", reply.getDownloadInfo().getServiceUrl(), "--os-auth-token", reply.getDownloadInfo().getTokenId(),
                    "image-download", reply.getDownloadInfo().getImageRefId(), "--file", tmp};
            int res = SystemUtil.runAndGetCode(cmd);
            LogUtil.handleLog(cmd, res, "::下载镜像失败");
            SystemUtil.runAndGetCode(new String[] {"mv", tmp, imagePath + imageId});
        }
        return imagePath + imageId;
    }*/

    @Override
    public void deleteDisk(NodeStoragePoolVo pool, String volumeId, List<ResourceProviderVo> snapshots) throws GCloudException {
        String diskPath = this.genDiskPath(pool.getPoolName(), volumeId);
        File diskFile = new File(diskPath);
        if (diskFile.exists()) {
            switch (pool.getSnapshotType()) {
                case EXTERNAL: {
                    for (ResourceProviderVo snapshot : snapshots) {
                        this.deleteFile(this.genDiskPath(pool.getPoolName(), snapshot.getRefId()));
                    }
                    break;
                }
                case INTERNAL: {
                    for (ResourceProviderVo snapshot : snapshots) {
                        this.deleteInternalSnapshot(diskPath, snapshot.getRefId());
                    }
                    break;
                }
            }
            if (!diskFile.delete()) {
                throw new GCloudException(StorageErrorCodes.FAILED_TO_DELETE_VOLUME);
            }
        }
    }

    @Override
    public void resizeDisk(NodeStoragePoolVo pool, String volumeId, Integer oldSize, Integer newSize) throws GCloudException {
        String diskPath = this.genDiskPath(pool.getPoolName(), volumeId);
        this.checkAndGetFile(diskPath);
        DiskQemuImgUtil.resize(diskPath, newSize - oldSize);
    }

    @Override
    public void createSnapshot(NodeStoragePoolVo pool, String volumeRefId, String snapshotId, String snapshotRefId) throws GCloudException {
        String diskPath = this.checkAndGet(pool.getPoolName(), volumeRefId);
        QemuInfo diskQemuInfo = DiskQemuImgUtil.info(diskPath);
        FileFormat diskFormat = FileFormat.value(diskQemuInfo.getFormat());
        this.checkSupported(pool.getSnapshotType(), diskFormat);
        switch (pool.getSnapshotType()) {
            case EXTERNAL: {
                String snapshotPath = this.genDiskPath(pool.getPoolName(), snapshotRefId);
                SystemUtil.runAndGetCode(new String[] {"mv", diskPath, snapshotPath});
                DiskQemuImgUtil.create(snapshotPath, diskPath);
                break;
            }
            case INTERNAL: {
                String[] cmd = new String[] {"qemu-img", "snapshot", "-c", snapshotRefId, diskPath};
                int res = SystemUtil.runAndGetCode(cmd);
                LogUtil.handleLog(cmd, res, StorageErrorCodes.FAILED_TO_CREATE_SNAP);
                break;
            }
        }
    }

    private void checkSupported(SnapshotType snapshotType, FileFormat diskFormat) throws GCloudException {
        if (snapshotType == SnapshotType.INTERNAL && diskFormat != FileFormat.QCOW2) {
            throw new GCloudException(StorageErrorCodes.SNAPSHOT_NOT_SUPPORTED);
        }
    }

    @Override
    public void deleteSnapshot(NodeStoragePoolVo pool, String volumeRefId, String snapshotId, String snapshotRefId) throws GCloudException {
        String diskPath = this.checkAndGet(pool.getPoolName(), volumeRefId);
        switch (pool.getSnapshotType()) {
            case EXTERNAL: {
                String snapshotPath = this.checkAndGet(pool.getPoolName(), snapshotRefId);
                File snapshotFile = new File(snapshotPath);
                if (!snapshotFile.exists()) {
                    return;
                }
                String[] backingChain = this.backingChain(diskPath);
                String snapshotParentPath = null;
                String snapshotChildPath = null;
                for (int i = 0; i < backingChain.length; i++) {
                    if (backingChain[i].equals(snapshotPath)) {
                        snapshotParentPath = backingChain[i + 1];
                        snapshotChildPath = backingChain[i - 1];
                        break;
                    }
                }
                DiskQemuImgUtil.rebase(snapshotChildPath, snapshotParentPath);
                snapshotFile.delete();
                break;
            }
            case INTERNAL: {
                this.deleteInternalSnapshot(diskPath, snapshotRefId);
                break;
            }
        }
    }

    private void deleteFile(String filePath) {
        File file = new File(filePath);
        if (file.exists()) {
            file.delete();
        }
    }

    private void deleteInternalSnapshot(String diskPath, String snapshotRefId) throws GCloudException {
        String[] cmd = new String[] {"qemu-img", "snapshot", "-d", snapshotRefId, diskPath};
        int res = SystemUtil.runAndGetCode(cmd);
        LogUtil.handleLog(cmd, res, StorageErrorCodes.FAILED_TO_DELETE_SNAP);
    }

    @Override
    public List<String> resetSnapshot(NodeStoragePoolVo pool, String volumeRefId, String snapshotId, String snapshotRefId, Integer size) throws GCloudException {
        String diskPath = this.checkAndGet(pool.getPoolName(), volumeRefId);
        List<String> snapshotsToDelete = new ArrayList<>();
        switch (pool.getSnapshotType()) {
            case EXTERNAL: {
                String snapshotPath = this.checkAndGet(pool.getPoolName(), snapshotRefId);
                String[] backingChain = this.backingChain(diskPath);
                DiskQemuImgUtil.create(snapshotPath, diskPath, size, Unit.G);
                for (String path : backingChain) {
                    if (path.equals(diskPath)) {
                        continue;
                    }
                    if (path.equals(snapshotPath)) {
                        break;
                    }
                    this.deleteFile(path);
                    snapshotsToDelete.add(path.substring(path.lastIndexOf(File.separator) + 1));
                }
                break;
            }
            case INTERNAL: {
                String[] cmd = new String[] {"qemu-img", "snapshot", "-a", snapshotRefId, diskPath};
                int res = SystemUtil.runAndGetCode(cmd);
                LogUtil.handleLog(cmd, res, StorageErrorCodes.FAILED_TO_RESET_SNAP);
                break;
            }
        }
        return snapshotsToDelete;
    }

    // system: volume - snap3 - snap2 - snap1 - image
    // data:   volume - snap3 - snap2 - snap1
    private String[] backingChain(String path) {
        List<String> chain = new ArrayList<>();
        String res = SystemUtil.run(new String[] {"qemu-img", "info", path, "--backing-chain"});
        for (String line : res.split("\n")) {
            if (line.startsWith("image: ")) {
                chain.add(line.substring("image: ".length()));
            }
        }
        return chain.toArray(new String[] {});
    }

}
