package com.gcloud.header.network.msg.api;

import com.gcloud.header.ApiMessage;
import com.gcloud.header.ApiReplyMessage;
import com.gcloud.header.api.ApiModel;

import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotBlank;

import java.util.List;

public class ModifyNetworkInterfaceAttributeMsg extends ApiMessage {

    private static final long serialVersionUID = 1L;

    @ApiModel(description = "网卡ID不能为空", require = true)
    @NotBlank(message = "0080201::端口ID不能为空")
    private String networkInterfaceId;
    @ApiModel(description = "安全组ID列表")
    private List<String> securityGroupIds;
    @ApiModel(description = "网卡名称")
    @Length(max = 255, message = "0080203::名称长度不能大于255")
    private String networkInterfaceName;

    public String getNetworkInterfaceId() {
        return networkInterfaceId;
    }

    public void setNetworkInterfaceId(String networkInterfaceId) {
        this.networkInterfaceId = networkInterfaceId;
    }

    public String getNetworkInterfaceName() {
        return networkInterfaceName;
    }

    public void setNetworkInterfaceName(String networkInterfaceName) {
        this.networkInterfaceName = networkInterfaceName;
    }


    public List<String> getSecurityGroupIds() {
        return securityGroupIds;
    }

    public void setSecurityGroupIds(List<String> securityGroupIds) {
        this.securityGroupIds = securityGroupIds;
    }

    @Override
    public Class replyClazz() {
        // TODO Auto-generated method stub
        return ApiReplyMessage.class;
    }

}
