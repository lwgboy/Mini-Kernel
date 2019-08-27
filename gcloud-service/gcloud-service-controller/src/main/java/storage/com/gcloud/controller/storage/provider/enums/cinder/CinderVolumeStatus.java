package com.gcloud.controller.storage.provider.enums.cinder;

import com.gcloud.common.util.StringUtils;
import com.gcloud.controller.IResourceState;
import com.gcloud.header.enums.ProviderType;
import com.gcloud.header.enums.ResourceType;
import com.gcloud.header.storage.enums.VolumeStatus;
import org.openstack4j.model.storage.block.Volume;
import org.springframework.stereotype.Component;

import java.util.Arrays;
@Component
public class CinderVolumeStatus implements IResourceState {

    public enum State{

        AVAILABLE(Volume.Status.AVAILABLE, VolumeStatus.AVAILABLE),
        ATTACHING(Volume.Status.ATTACHING, VolumeStatus.ATTACHING),
        BACKING_UP(Volume.Status.BACKING_UP, VolumeStatus.CREATING_BACKUP),
        CREATING(Volume.Status.CREATING, VolumeStatus.CREATING),
        DELETING(Volume.Status.DELETING, VolumeStatus.DELETING),
        DOWNLOADING(Volume.Status.DOWNLOADING, VolumeStatus.DOWNLOADING),
        UPLOADING(Volume.Status.UPLOADING, VolumeStatus.UPLOADING),
        ERROR(Volume.Status.ERROR, VolumeStatus.ERROR),
        ERROR_DELETING(Volume.Status.ERROR_DELETING, VolumeStatus.ERROR_DELETING),
        ERROR_RESTORING(Volume.Status.ERROR_RESTORING, VolumeStatus.ERROR_RESTORING),
        IN_USE(Volume.Status.IN_USE, VolumeStatus.IN_USE),
        RESTORING_BACKUP(Volume.Status.REVERTING, VolumeStatus.RESTORING_BACKUP),
        DETACHING(Volume.Status.DETACHING, VolumeStatus.DETACHING),
        UNRECOGNIZED(Volume.Status.UNRECOGNIZED, VolumeStatus.UNRECOGNIZED);

        private Volume.Status cinderState;
        private VolumeStatus status;

        State(Volume.Status cinderState, VolumeStatus status) {
            this.cinderState = cinderState;
            this.status = status;
        }

        public static String value(String cinderStatus){
            State state = getByCinderStatus(cinderStatus);
            return state == null ? null : state.getStatus().value();
        }

        public static String cnName(String cinderStatus){
            State state = getByCinderStatus(cinderStatus);
            return state == null ? null : state.getStatus().getCnName();
        }

        public static State getByCinderStatus(String cinderStatus){
            if(StringUtils.isBlank(cinderStatus)){
                return null;
            }
            return Arrays.stream(State.values()).filter(s -> s.getCinderState().value().equals(cinderStatus)).findFirst().orElse(null);
        }

        public Volume.Status getCinderState() {
            return cinderState;
        }

        public VolumeStatus getStatus() {
            return status;
        }
    }

    @Override
    public ResourceType resourceType() {
        return ResourceType.VOLUME;
    }

    @Override
    public ProviderType providerType() {
        return ProviderType.CINDER;
    }

    @Override
    public String value(String status) {
        return CinderVolumeStatus.State.value(status);
    }

    @Override
    public String cnName(String status) {
        return CinderVolumeStatus.State.cnName(status);
    }
}
