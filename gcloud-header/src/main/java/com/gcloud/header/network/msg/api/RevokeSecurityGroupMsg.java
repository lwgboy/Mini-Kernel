package com.gcloud.header.network.msg.api;

import org.hibernate.validator.constraints.NotBlank;

import com.gcloud.header.ApiMessage;
import com.gcloud.header.ApiReplyMessage;
import com.gcloud.header.api.ApiModel;

public class RevokeSecurityGroupMsg extends ApiMessage {

    @ApiModel(description = "安全组规则Id", require = true)
    @NotBlank(message = "0040601")
    private String securityGroupRuleId;

    public String getSecurityGroupRuleId() {
        return securityGroupRuleId;
    }

    public void setSecurityGroupRuleId(String securityGroupRuleId) {
        this.securityGroupRuleId = securityGroupRuleId;
    }

    @Override
    public Class replyClazz() {
        return ApiReplyMessage.class;
    }

}
