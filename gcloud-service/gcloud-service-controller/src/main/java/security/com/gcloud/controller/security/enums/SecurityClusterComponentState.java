package com.gcloud.controller.security.enums;

import com.google.common.base.CaseFormat;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by yaowj on 2018/7/16.
 */
public enum SecurityClusterComponentState {

    CREATING("创建中", SecurityBaseState.RUNNING),
    CREATED("已创建", SecurityBaseState.SUCCESS),
    DELETING("删除中", SecurityBaseState.RUNNING),
    DELETED("已删除", SecurityBaseState.SUCCESS),
    DELETE_FAILED("删除失败", SecurityBaseState.FAILED),
    FAIL("失败", SecurityBaseState.FAILED);

    private String cnName;
    private SecurityBaseState securityBaseState;

    SecurityClusterComponentState(String cnName, SecurityBaseState securityBaseState) {
        this.cnName = cnName;
        this.securityBaseState = securityBaseState;
    }

    public static Map<String, String> valueCnMap(){
        Map<String, String> result = new HashMap<>();
        Arrays.stream(SecurityClusterComponentState.values()).forEach(s -> result.put(s.value(), s.getCnName()));
        return result;
    }

    public static SecurityClusterComponentState getByValue(String value){
        return Arrays.stream(SecurityClusterComponentState.values()).filter(s -> s.value().equals(value)).findFirst().orElse(null);
    }

    public String value() {
        return CaseFormat.UPPER_UNDERSCORE.to(CaseFormat.LOWER_HYPHEN, name());
    }

    public String getCnName() {
        return cnName;
    }

    public SecurityBaseState getSecurityBaseState() {
        return securityBaseState;
    }
}
