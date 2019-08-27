package com.gcloud.controller.storage.provider.enums.cinder;

import com.gcloud.common.util.StringUtils;
import com.gcloud.controller.IResourceState;
import com.gcloud.header.enums.ProviderType;
import com.gcloud.header.enums.ResourceType;
import com.gcloud.header.storage.enums.SnapshotStatus;
import org.openstack4j.model.storage.block.Volume;
import org.springframework.stereotype.Component;

import java.util.Arrays;
@Component
public class CinderSnapshotStatus implements IResourceState {

    public enum State{

        AVAILABLE(Volume.Status.AVAILABLE, SnapshotStatus.AVAILABLE),
        ATTACHING(Volume.Status.ATTACHING, SnapshotStatus.ATTACHING),
        BACKING_UP(Volume.Status.BACKING_UP, SnapshotStatus.PROGRESSING),
        DELETING(Volume.Status.DELETING, SnapshotStatus.DELETING),
        DOWNLOADING(Volume.Status.DOWNLOADING, SnapshotStatus.DOWNLOADING),
        UPLOADING(Volume.Status.UPLOADING, SnapshotStatus.UPLOADING),
        ERROR(Volume.Status.ERROR, SnapshotStatus.ERROR),
        ERROR_DELETING(Volume.Status.ERROR_DELETING, SnapshotStatus.ERROR_DELETING),
        ERROR_RESTORING(Volume.Status.ERROR_RESTORING, SnapshotStatus.ERROR_RESTORING),
        IN_USE(Volume.Status.IN_USE, SnapshotStatus.IN_USE),
        RESTORING_BACKUP(Volume.Status.RESTORING, SnapshotStatus.RESTORING),
        DETACHING(Volume.Status.DETACHING, SnapshotStatus.DETACHING),
        UNRECOGNIZED(Volume.Status.UNRECOGNIZED, SnapshotStatus.UNRECOGNIZED);

        private Volume.Status cinderState;
        private SnapshotStatus status;

        State(Volume.Status cinderState, SnapshotStatus status) {
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

        public SnapshotStatus getStatus() {
            return status;
        }
    }

    @Override
    public ResourceType resourceType() {
        return ResourceType.SNAPSHOT;
    }

    @Override
    public ProviderType providerType() {
        return ProviderType.CINDER;
    }

    @Override
    public String value(String status) {
        return CinderSnapshotStatus.State.value(status);
    }

    @Override
    public String cnName(String status) {
        return CinderSnapshotStatus.State.cnName(status);
    }
}
