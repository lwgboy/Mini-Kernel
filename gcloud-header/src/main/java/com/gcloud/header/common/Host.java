package com.gcloud.header.common;

import java.io.Serializable;

/**
 * Created by yaowj on 2018/12/28.
 */
public class Host implements Serializable {

    private static final long serialVersionUID = 1L;

    private String hostname;
    private String port;

    public Host() {
    }

    public Host(String hostname, String port) {
        this.hostname = hostname;
        this.port = port;
    }

    public String getHostname() {
        return hostname;
    }

    public void setHostname(String hostname) {
        this.hostname = hostname;
    }

    public String getPort() {
        return port;
    }

    public void setPort(String port) {
        this.port = port;
    }
}
