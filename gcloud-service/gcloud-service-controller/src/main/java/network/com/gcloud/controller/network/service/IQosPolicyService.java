package com.gcloud.controller.network.service;

/**
 * Created by yaowj on 2018/10/30.
 */
public interface IQosPolicyService {

    String create(String name, String description, Boolean isDefault, Boolean shared);
    String createQosPolicy(String name, String description, Boolean isDefault, Boolean shared);
}
