package com.gcloud.controller.network.provider.enums.neutron;

import com.gcloud.common.util.StringUtils;
import com.gcloud.controller.IResourceState;
import com.gcloud.header.enums.ProviderType;
import com.gcloud.header.enums.ResourceType;
import com.gcloud.header.network.enums.NetworkStatus;

import java.util.Arrays;

import org.springframework.stereotype.Component;
@Component
public class NeutronNetworkStatus implements IResourceState {


    public enum State{
        ACTIVE(org.openstack4j.model.network.State.ACTIVE, NetworkStatus.ACTIVE),
        DOWN(org.openstack4j.model.network.State.DOWN, NetworkStatus.DOWN),
        BUILD(org.openstack4j.model.network.State.BUILD, NetworkStatus.BUILD),
        ERROR(org.openstack4j.model.network.State.ERROR, NetworkStatus.ERROR),
        PENDING_CREATE(org.openstack4j.model.network.State.PENDING_CREATE, NetworkStatus.PENDING_CREATE),
        PENDING_UPDATE(org.openstack4j.model.network.State.PENDING_UPDATE, NetworkStatus.PENDING_UPDATE),
        PENDING_DELETE(org.openstack4j.model.network.State.PENDING_DELETE, NetworkStatus.PENDING_DELETE),
        UNRECOGNIZED(org.openstack4j.model.network.State.UNRECOGNIZED, NetworkStatus.UNRECOGNIZED);

        private org.openstack4j.model.network.State neutronStatus;
        private NetworkStatus status;

        State(org.openstack4j.model.network.State neutronStatus, NetworkStatus status) {
            this.neutronStatus = neutronStatus;
            this.status = status;
        }


        public static String value(String neutronStatus){
            NeutronNetworkStatus.State state = getByNeutronStatus(neutronStatus);
            return state == null ? null : state.getStatus().value();
        }

        public static String cnName(String neutronStatus){
            NeutronNetworkStatus.State state = getByNeutronStatus(neutronStatus);
            return state == null ? null : state.getStatus().getCnName();
        }

        public static NeutronNetworkStatus.State getByNeutronStatus(String neutronStatus){
            if(StringUtils.isBlank(neutronStatus)){
                return null;
            }
            return Arrays.stream(NeutronNetworkStatus.State.values()).filter(s -> s.getNeutronStatus().name().equalsIgnoreCase(neutronStatus)).findFirst().orElse(null);
        }

        public org.openstack4j.model.network.State getNeutronStatus() {
            return neutronStatus;
        }

        public NetworkStatus getStatus() {
            return status;
        }
    }

    @Override
    public ResourceType resourceType() {
        return ResourceType.NETWORK;
    }

    @Override
    public ProviderType providerType() {
        return ProviderType.NEUTRON;
    }

    @Override
    public String value(String status) {
        return NeutronNetworkStatus.State.value(status);
    }

    @Override
    public String cnName(String status) {
        return NeutronNetworkStatus.State.cnName(status);
    }
}
