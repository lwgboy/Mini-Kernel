package com.gcloud.controller.image.provider.enums;

import com.gcloud.common.util.StringUtils;
import com.gcloud.controller.IResourceState;
import com.gcloud.header.enums.ProviderType;
import com.gcloud.header.enums.ResourceType;
import com.gcloud.header.image.enums.ImageStatus;
import org.openstack4j.model.image.v2.Image;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Component
public class GlanceImageStatus implements IResourceState {

    public enum State{

        UNRECOGNIZED(Image.ImageStatus.UNRECOGNIZED, ImageStatus.UNRECOGNIZED),
        QUEUED(Image.ImageStatus.QUEUED, ImageStatus.QUEUED),
        SAVING(Image.ImageStatus.SAVING, ImageStatus.SAVING),
        ACTIVE(Image.ImageStatus.ACTIVE, ImageStatus.ACTIVE),
        DEACTIVATED(Image.ImageStatus.DEACTIVATED, ImageStatus.DEACTIVATED),
        KILLED(Image.ImageStatus.KILLED, ImageStatus.KILLED),
        DELETED(Image.ImageStatus.DELETED, ImageStatus.DELETED),
        PENDING_DELETE(Image.ImageStatus.PENDING_DELETE, ImageStatus.PENDING_DELETE);

        private Image.ImageStatus glanceStatus;
        private ImageStatus status;

        State(Image.ImageStatus glanceStatus, ImageStatus status) {
            this.glanceStatus = glanceStatus;
            this.status = status;
        }

        public static String value(String glanceStatus){
            GlanceImageStatus.State state = getByGlanceStatus(glanceStatus);
            return state == null ? null : state.getStatus().value();
        }

        public static String cnName(String glanceStatus){
            GlanceImageStatus.State state = getByGlanceStatus(glanceStatus);
            return state == null ? null : state.getStatus().getCnName();
        }

        public static GlanceImageStatus.State getByGlanceStatus(String glanceStatus){
            if(StringUtils.isBlank(glanceStatus)){
                return null;
            }
            return Arrays.stream(GlanceImageStatus.State.values()).filter(s -> s.getGlanceStatus().value().equals(glanceStatus)).findFirst().orElse(null);
        }

        public Image.ImageStatus getGlanceStatus() {
            return glanceStatus;
        }

        public ImageStatus getStatus() {
            return status;
        }
    }

    @Override
    public ResourceType resourceType() {
        return ResourceType.IMAGE;
    }

    @Override
    public ProviderType providerType() {
        return ProviderType.GLANCE;
    }

    @Override
    public String value(String status) {
        return GlanceImageStatus.State.value(status);
    }

    @Override
    public String cnName(String status) {
        return GlanceImageStatus.State.cnName(status);
    }
}
