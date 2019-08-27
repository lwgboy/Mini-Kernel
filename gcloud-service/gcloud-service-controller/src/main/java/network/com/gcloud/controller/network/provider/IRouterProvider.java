package com.gcloud.controller.network.provider;

import com.gcloud.controller.IResourceProvider;
import com.gcloud.controller.network.entity.Router;
import com.gcloud.header.api.model.CurrentUser;
import com.gcloud.header.network.msg.api.CreateVRouterMsg;

import java.util.List;
import java.util.Map;

public interface IRouterProvider extends IResourceProvider {

    String createVRouter(CreateVRouterMsg msg);

    void deleteVRouter(String routerRefId);

    void modifyVRouterAttribute(String routerRefId, String vRouterName);

    void setVRouterGateway(String routerId, String routerRefId, String networkId, String networkRefId, CurrentUser currentUser);

    void cleanVRouterGateway(String routerRefId);

    String attachSubnetRouter(String routerRefId, String subnetId);

    void detachSubnetRouter(String routerRefId, String subnetId);
    
    void detachSubnetRouter(String routerRefId, String subnetId, String portId);

    List<Router> list(Map<String, String> filter);
    
    org.openstack4j.model.network.Router getVRouter(String routerRefId);
}
