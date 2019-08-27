package com.gcloud.controller.storage.util;

import com.gcloud.controller.provider.CinderProviderProxy;
import com.gcloud.controller.storage.model.CheckStatusResult;
import com.gcloud.core.exception.GCloudException;
import com.gcloud.core.service.SpringUtil;
import org.openstack4j.model.storage.block.Volume;
import org.openstack4j.model.storage.block.VolumeAttachment;
import org.openstack4j.model.storage.block.VolumeSnapshot;

import java.util.List;

/**
 * Created by yaowj on 2018/9/28.
 */
public class VolumeUtil {


    public static CheckStatusResult<Volume> checkVolumeState(String id, List<Volume.Status> targetStatus, List<Volume.Status> errorStatus){

        CinderProviderProxy proxy = SpringUtil.getBean(CinderProviderProxy.class);
        Volume volume = proxy.getVolume(id);
        if(volume == null){
            throw new GCloudException("::volume is null");
        }

        CheckStatusResult<Volume> result = checkStatus(volume.getStatus(), targetStatus, errorStatus);
        result.setEntity(volume);
        return result;
    }

    public static CheckStatusResult<VolumeSnapshot> checkSnapshotState(String id, List<Volume.Status> targetStatus, List<Volume.Status> errorStatus){
        CinderProviderProxy proxy = SpringUtil.getBean(CinderProviderProxy.class);
        VolumeSnapshot snapshot = proxy.getVolumeSnapshot(id);
        if(snapshot == null){
            throw new GCloudException("::snapshot is null");
        }

        CheckStatusResult<VolumeSnapshot> result = checkStatus(snapshot.getStatus(), targetStatus, errorStatus);
        result.setEntity(snapshot);
        return result;
    }

    private static <T> boolean instatus(T status, List<T> targetStatus){

        boolean isInState = false;

        if(targetStatus != null && targetStatus.size() > 0){

            for(T stat : targetStatus){

                if(status == null){
                    if(stat == null){
                        isInState = true;
                        break;
                    }
                }else if(status.equals(stat)){
                    isInState = true;
                    break;
                }
            }

        }

        return isInState;
    }

    public static <T, S> CheckStatusResult<T> checkStatus(S status, List<S> targetStatus, List<S> errorStatus){

        CheckStatusResult<T> result = new CheckStatusResult<>();
        Boolean isSuccess = null;

        if(instatus(status, targetStatus)){
            isSuccess = true;
        }else if(instatus(status, errorStatus)){
            isSuccess = false;
        }

        result.setSuccess(isSuccess);
        return result;
    }

    public static String getVolumeAttachmentId(Volume volume, String instanceUuid, String mountPoint, String hostname){

        String attachmentId = null;
        if(volume == null || volume.getAttachments() == null){
            return attachmentId;
        }

        for(VolumeAttachment volumeAttachment : volume.getAttachments()){
            if(!((volumeAttachment.getDevice() == null && mountPoint == null) || volumeAttachment.getDevice().equals(mountPoint))){
                continue;
            }

            if(!((volumeAttachment.getServerId() == null && instanceUuid == null) || volumeAttachment.getServerId().equals(instanceUuid))){
                continue;
            }

            if(!((volumeAttachment.getHostname() == null && hostname == null) || volumeAttachment.getHostname().equals(hostname))){
                continue;
            }

            attachmentId = volumeAttachment.getAttachmentId();
            break;

        }

        return attachmentId;

    }

}
