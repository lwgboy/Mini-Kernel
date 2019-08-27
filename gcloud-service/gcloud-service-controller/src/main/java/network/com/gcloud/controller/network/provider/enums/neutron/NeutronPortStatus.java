package com.gcloud.controller.network.provider.enums.neutron;

import com.gcloud.common.util.StringUtils;
import com.gcloud.controller.IResourceState;
import com.gcloud.header.enums.ProviderType;
import com.gcloud.header.enums.ResourceType;
import com.gcloud.header.network.enums.PortStatus;

import java.util.Arrays;

import org.springframework.stereotype.Component;
@Component
public class NeutronPortStatus implements IResourceState {

    public enum State{

        ACTIVE(org.openstack4j.model.network.State.ACTIVE, PortStatus.ACTIVE),
        DOWN(org.openstack4j.model.network.State.DOWN, PortStatus.DOWN),
        BUILD(org.openstack4j.model.network.State.BUILD, PortStatus.BUILD),
        ERROR(org.openstack4j.model.network.State.ERROR, PortStatus.ERROR),
        PENDING_CREATE(org.openstack4j.model.network.State.PENDING_CREATE, PortStatus.PENDING_CREATE),
        PENDING_UPDATE(org.openstack4j.model.network.State.PENDING_UPDATE, PortStatus.PENDING_UPDATE),
        PENDING_DELETE(org.openstack4j.model.network.State.PENDING_DELETE, PortStatus.PENDING_DELETE),
        UNRECOGNIZED(org.openstack4j.model.network.State.UNRECOGNIZED, PortStatus.UNRECOGNIZED);

        private org.openstack4j.model.network.State neutronStatus;
        private PortStatus status;

        State(org.openstack4j.model.network.State neutronStatus, PortStatus status) {
            this.neutronStatus = neutronStatus;
            this.status = status;
        }

        public static String value(String neutronStatus){
            NeutronPortStatus.State state = getByNeutronStatus(neutronStatus);
            return state == null ? null : state.getStatus().value();
        }

        public static String cnName(String neutronStatus){
            NeutronPortStatus.State state = getByNeutronStatus(neutronStatus);
            return state == null ? null : state.getStatus().getCnName();
        }

        public static NeutronPortStatus.State getByNeutronStatus(String neutronStatus){
            if(StringUtils.isBlank(neutronStatus)){
                return null;
            }
            return Arrays.stream(NeutronPortStatus.State.values()).filter(s -> s.getNeutronStatus().name().equalsIgnoreCase(neutronStatus)).findFirst().orElse(null);
        }

        public org.openstack4j.model.network.State getNeutronStatus() {
            return neutronStatus;
        }

        public PortStatus getStatus() {
            return status;
        }
    }

    @Override
    public ResourceType resourceType() {
        return ResourceType.PORT;
    }

    @Override
    public ProviderType providerType() {
        return ProviderType.NEUTRON;
    }

    @Override
    public String value(String status) {
        return NeutronPortStatus.State.value(status);
    }

    @Override
    public String cnName(String status) {
        return NeutronPortStatus.State.cnName(status);
    }
}
