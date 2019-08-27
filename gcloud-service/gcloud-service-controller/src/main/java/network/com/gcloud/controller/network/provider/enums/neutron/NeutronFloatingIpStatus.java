package com.gcloud.controller.network.provider.enums.neutron;

import com.gcloud.common.util.StringUtils;
import com.gcloud.controller.IResourceState;
import com.gcloud.header.enums.ProviderType;
import com.gcloud.header.enums.ResourceType;
import com.gcloud.header.network.enums.FloatingIpStatus;

import java.util.Arrays;

import org.springframework.stereotype.Component;
@Component
public class NeutronFloatingIpStatus implements IResourceState {

    public enum State{
        ACTIVE("ACTIVE", FloatingIpStatus.ACTIVE),
        DOWN("DOWN", FloatingIpStatus.DOWN),
        ERROR("ERROR", FloatingIpStatus.ERROR);

        private String neutronStatus;
        private FloatingIpStatus status;

        State(String neutronStatus, FloatingIpStatus status) {
            this.neutronStatus = neutronStatus;
            this.status = status;
        }

        public static String value(String neutronStatus){
            NeutronFloatingIpStatus.State state = getByNeutronStatus(neutronStatus);
            return state == null ? null : state.getStatus().value();
        }

        public static String cnName(String neutronStatus){
            NeutronFloatingIpStatus.State state = getByNeutronStatus(neutronStatus);
            return state == null ? null : state.getStatus().getCnName();
        }

        public static NeutronFloatingIpStatus.State getByNeutronStatus(String neutronStatus){
            if(StringUtils.isBlank(neutronStatus)){
                return null;
            }
            return Arrays.stream(NeutronFloatingIpStatus.State.values()).filter(s -> s.getNeutronStatus().equals(neutronStatus)).findFirst().orElse(null);
        }

        public FloatingIpStatus getStatus() {
            return status;
        }

        public String getNeutronStatus() {
            return neutronStatus;
        }


    }


    @Override
    public ResourceType resourceType() {
        return ResourceType.FLOATING_IP;
    }

    @Override
    public ProviderType providerType() {
        return ProviderType.NEUTRON;
    }

    @Override
    public String value(String status) {
        return NeutronFloatingIpStatus.State.value(status);
    }

    @Override
    public String cnName(String status) {
        return NeutronFloatingIpStatus.State.cnName(status);
    }
}
