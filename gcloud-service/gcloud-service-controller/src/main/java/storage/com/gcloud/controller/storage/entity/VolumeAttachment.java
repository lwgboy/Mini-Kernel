package com.gcloud.controller.storage.entity;

import com.gcloud.controller.ResourceProviderEntity;
import com.gcloud.framework.db.jdbc.annotation.ID;
import com.gcloud.framework.db.jdbc.annotation.Table;

/**
 * Created by yaowj on 2018/9/29.
 */
@Table(name = "gc_volume_attachments", jdbc = "controllerJdbcTemplate")
public class VolumeAttachment extends ResourceProviderEntity {

    @ID
    private String id;
    private String volumeId;
    private String instanceUuid;
    private String mountpoint;
    private String attachedHost;

    public static final String ID = "id";
    public static final String VOLUME_ID = "volumeId";
    public static final String INSTANCE_UUID = "instanceUuid";
    public static final String MOUNTPOINT = "mountpoint";
    public static final String ATTACHED_HOST = "attachedHost";

    public String updateId(String id) {
        this.setId(id);
        return ID;
    }

    public String updateVolumeId(String volumeId) {
        this.setVolumeId(volumeId);
        return VOLUME_ID;
    }

    public String updateInstanceUuid(String instanceUuid) {
        this.setInstanceUuid(instanceUuid);
        return INSTANCE_UUID;
    }

    public String updateMountpoint(String mountpoint) {
        this.setMountpoint(mountpoint);
        return MOUNTPOINT;
    }

    public String updateAttachedHost(String attachedHost) {
        this.setAttachedHost(attachedHost);
        return ATTACHED_HOST;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getVolumeId() {
        return volumeId;
    }

    public void setVolumeId(String volumeId) {
        this.volumeId = volumeId;
    }

    public String getInstanceUuid() {
        return instanceUuid;
    }

    public void setInstanceUuid(String instanceUuid) {
        this.instanceUuid = instanceUuid;
    }

    public String getMountpoint() {
        return mountpoint;
    }

    public void setMountpoint(String mountpoint) {
        this.mountpoint = mountpoint;
    }

    public String getAttachedHost() {
        return attachedHost;
    }

    public void setAttachedHost(String attachedHost) {
        this.attachedHost = attachedHost;
    }
}
