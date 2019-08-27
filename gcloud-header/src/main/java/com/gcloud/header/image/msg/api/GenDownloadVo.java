
package com.gcloud.header.image.msg.api;

import java.io.Serializable;

public class GenDownloadVo implements Serializable {

    private static final long serialVersionUID = 1L;

    private String imageId;
    private String imageRefId;
    private String tokenId;
    private String serviceUrl;

    public String getImageId() {
        return imageId;
    }

    public void setImageId(String imageId) {
        this.imageId = imageId;
    }

    public String getImageRefId() {
        return imageRefId;
    }

    public void setImageRefId(String imageRefId) {
        this.imageRefId = imageRefId;
    }

    public String getTokenId() {
        return tokenId;
    }

    public void setTokenId(String tokenId) {
        this.tokenId = tokenId;
    }

    public String getServiceUrl() {
        return serviceUrl;
    }

    public void setServiceUrl(String serviceUrl) {
        this.serviceUrl = serviceUrl;
    }

}
