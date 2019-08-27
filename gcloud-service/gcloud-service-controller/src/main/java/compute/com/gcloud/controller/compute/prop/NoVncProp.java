package com.gcloud.controller.compute.prop;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "gcloud.controller.novnc")
public class NoVncProp {
    /*
    * Environment env = (Environment) SpringUtil.getBean("environment");
        tokenDir = env.getProperty("vnc.novnc.tokendir", "/var/log/gcloud/") + "/";
        websockifyHost = env.getProperty("vnc.novnc.websockify.ip", "localhost");
        websockifyPort = env.getProperty("vnc.novnc.websockify.port", "6080");
    * */

    @Value("${gcloud.controller.novnc.tokenDir}")
    private String tokenDir;

    @Value("${gcloud.controller.novnc.websockifyHost}")
    private String websockifyHost;

    @Value("${gcloud.controller.novnc.websockifyPort}")
    private String websockifyPort;

    @Value("${gcloud.controller.novnc.noVncHost}")
    private String noVncHost;

    @Value("${gcloud.controller.novnc.noVncPort}")
    private String noVncPort;

    public String getNoVncHost() {
        return noVncHost;
    }

    public void setNoVncHost(String noVncHost) {
        this.noVncHost = noVncHost;
    }

    public String getNoVncPort() {
        return noVncPort;
    }

    public void setNoVncPort(String noVncPort) {
        this.noVncPort = noVncPort;
    }

    public String getTokenDir() {
        return tokenDir;
    }

    public void setTokenDir(String tokenDir) {
        this.tokenDir = tokenDir;
    }

    public String getWebsockifyHost() {
        return websockifyHost;
    }

    public void setWebsockifyHost(String websockifyHost) {
        this.websockifyHost = websockifyHost;
    }

    public String getWebsockifyPort() {
        return websockifyPort;
    }

    public void setWebsockifyPort(String websockifyPort) {
        this.websockifyPort = websockifyPort;
    }
}
