package com.gcloud.controller.security.enums;

import com.google.common.base.CaseFormat;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by yaowj on 2018/7/16.
 */
public enum SecurityClusterState {

    CREATING("创建中", SecurityBaseState.RUNNING),
    CREATED("已创建", SecurityBaseState.SUCCESS),
    DELETING("删除中", SecurityBaseState.RUNNING),
    DELETE_FAILED("删除失败", SecurityBaseState.FAILED),
    FAILED("创建失败", SecurityBaseState.FAILED),

    ENABLING_HA("启用HA中", SecurityBaseState.RUNNING),
    ENABLE_FAILED("启用HA失败", SecurityBaseState.FAILED),
    DISABLING_HA("取消HA中", SecurityBaseState.RUNNING),
    DISABLE_FAILED("取消HA失败", SecurityBaseState.FAILED)
    ;

    private String cnName;
    private SecurityBaseState securityBaseState;

    SecurityClusterState(String cnName, SecurityBaseState securityBaseState) {
        this.cnName = cnName;
        this.securityBaseState = securityBaseState;
    }

    public String value() {
        return CaseFormat.UPPER_UNDERSCORE.to(CaseFormat.LOWER_HYPHEN, name());
    }

    public static SecurityClusterState getByValue(String value){
        return Arrays.stream(SecurityClusterState.values()).filter(s -> s.value().equals(value)).findFirst().orElse(null);
    }

    public static Map<String, String> valueCnMap(){
        Map<String, String> result = new HashMap<>();
        Arrays.stream(SecurityClusterState.values()).forEach(s -> result.put(s.value(), s.getCnName()));
        return result;
    }

    public String getCnName() {
        return cnName;
    }

    public SecurityBaseState getSecurityBaseState() {
        return securityBaseState;
    }
}
