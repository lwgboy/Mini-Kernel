
package com.gcloud.controller;

public class ResourceProviderEntity {

    private Integer provider;
    private String providerRefId;

    public static final String PROVIDER = "provider";
    public static final String PROVIDER_REF_ID = "providerRefId";

    public Integer getProvider() {
        return provider;
    }

    public void setProvider(Integer provider) {
        this.provider = provider;
    }

    public String getProviderRefId() {
        return providerRefId;
    }

    public void setProviderRefId(String providerRefId) {
        this.providerRefId = providerRefId;
    }

    public String updateProvider(Integer provider) {
        this.setProvider(provider);
        return PROVIDER;
    }

    public String updateProviderRefId(String providerRefId) {
        this.setProviderRefId(providerRefId);
        return PROVIDER_REF_ID;
    }

}
