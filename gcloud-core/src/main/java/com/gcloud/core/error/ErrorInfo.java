package com.gcloud.core.error;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.gcloud.core.exception.GCloudException;
import com.gcloud.core.service.SpringUtil;

import java.io.Serializable;

import org.apache.commons.lang.StringUtils;

public class ErrorInfo implements Serializable{

    private final String DEFAULT_CODE = "-1";
    private final String DEFAULT_MESSAGE = "系统异常，请联系管理员";

    @JsonProperty("RequestId")
    private String requestId;
    @JsonProperty("HostId")
    private String hostId;
    @JsonProperty("Code")
    private String code;
    @JsonProperty("Message")
    private String message;

    public ErrorInfo() {

    }

    public ErrorInfo(GCloudException ex) {
        String errMsg = ex.getMessage();
        String[] params = ex.getParams();
        this.initMessage(errMsg, params);

    }

    public ErrorInfo(String errMsg) {
        this.initMessage(errMsg, null);
    }

    private void initMessage(String errMsg, String[] params) {

        if (errMsg != null && !"".equals(errMsg)) {
            //只取第一个::
            int pos = errMsg.indexOf("::");
            if (pos != -1) {
                this.setCode(errMsg.substring(0, pos));
                this.setMessage(errMsg.substring(pos + 2));
            } else {
                this.setCode(errMsg);
            }
        }

        if (code == null || "".equals(code)) {
            this.setCode(DEFAULT_CODE);
            if (message == null || "".equals(message)) {
                this.setMessage(DEFAULT_MESSAGE);
            }
        } else {
            transCode(params);
        }
    }

    private void transCode(Object[] params) {
        ErrorCodes errorCodes = SpringUtil.getApplicationContext().getBean(ErrorCodes.class);
        String msg = errorCodes.get(code, message, params);
        if (StringUtils.isNotBlank(msg)) {
            this.setMessage(msg);
        }
    }

    public String getRequestId() {
        return requestId;
    }

    public void setRequestId(String requestId) {
        this.requestId = requestId;
    }

    public String getHostId() {
        return hostId;
    }

    public void setHostId(String hostId) {
        this.hostId = hostId;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
