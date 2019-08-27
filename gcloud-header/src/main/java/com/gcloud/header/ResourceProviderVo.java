
package com.gcloud.header;

import java.io.Serializable;

public class ResourceProviderVo implements Serializable {

    private static final long serialVersionUID = 1L;

    private String id;
    private String refId;

    public ResourceProviderVo() {

    }

    public ResourceProviderVo(String id, String refId) {
        this.id = id;
        this.refId = refId;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getRefId() {
        return refId;
    }

    public void setRefId(String refId) {
        this.refId = refId;
    }

}
