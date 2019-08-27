package com.gcloud.header.security.msg.api.cluster;

import com.gcloud.header.ApiMessage;
import com.gcloud.header.security.msg.model.CreateClusterInfoParams;
import com.gcloud.header.security.msg.model.CreateClusterObjectParams;
import com.gcloud.header.security.msg.model.CreateHaInfoParams;
import com.gcloud.header.security.msg.model.CreateNetworkParams;

public class ApiCreateSecurityClusterMsg extends ApiMessage {

    @Override
    public Class replyClazz() {
        return ApiCreateSecurityClusterReplyMsg.class;
    }

    private static final long serialVersionUID = 1L;

    private String name;
    private String description;

    private CreateClusterInfoParams createInfo;

    private CreateNetworkParams outerNet;
    private CreateNetworkParams managementNet;
    private CreateNetworkParams protectionNet;

    private CreateClusterObjectParams firewall;
    private CreateClusterObjectParams fortress;
    private CreateClusterObjectParams waf;
    private CreateClusterObjectParams isms;

    private Boolean ha;
    private CreateHaInfoParams haInfo;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public CreateClusterInfoParams getCreateInfo() {
        return createInfo;
    }

    public void setCreateInfo(CreateClusterInfoParams createInfo) {
        this.createInfo = createInfo;
    }

    public CreateNetworkParams getOuterNet() {
        return outerNet;
    }

    public void setOuterNet(CreateNetworkParams outerNet) {
        this.outerNet = outerNet;
    }

    public CreateNetworkParams getManagementNet() {
        return managementNet;
    }

    public void setManagementNet(CreateNetworkParams managementNet) {
        this.managementNet = managementNet;
    }

    public CreateNetworkParams getProtectionNet() {
        return protectionNet;
    }

    public void setProtectionNet(CreateNetworkParams protectionNet) {
        this.protectionNet = protectionNet;
    }

    public CreateClusterObjectParams getFirewall() {
        return firewall;
    }

    public void setFirewall(CreateClusterObjectParams firewall) {
        this.firewall = firewall;
    }

    public CreateClusterObjectParams getFortress() {
        return fortress;
    }

    public void setFortress(CreateClusterObjectParams fortress) {
        this.fortress = fortress;
    }

    public CreateClusterObjectParams getWaf() {
        return waf;
    }

    public void setWaf(CreateClusterObjectParams waf) {
        this.waf = waf;
    }

    public CreateClusterObjectParams getIsms() {
        return isms;
    }

    public void setIsms(CreateClusterObjectParams isms) {
        this.isms = isms;
    }

    public Boolean getHa() {
        return ha;
    }

    public void setHa(Boolean ha) {
        this.ha = ha;
    }

    public CreateHaInfoParams getHaInfo() {
        return haInfo;
    }

    public void setHaInfo(CreateHaInfoParams haInfo) {
        this.haInfo = haInfo;
    }
}
