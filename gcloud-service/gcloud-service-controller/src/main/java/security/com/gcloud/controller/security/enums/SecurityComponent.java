package com.gcloud.controller.security.enums;


import com.google.common.base.CaseFormat;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by yaowj on 2018/7/11.
 */
public enum SecurityComponent {
    FIREWALL("防火墙", true, true),
    WAF("waf", true, true),
    ISMS("isms", true, true),
    FORTRESS("堡垒机", false, false);

    private String cnName;
    private boolean hasHa;
    private boolean zxAuth;

    SecurityComponent(String cnName, boolean hasHa, boolean zxAuth) {
        this.cnName = cnName;
        this.hasHa = hasHa;
        this.zxAuth = zxAuth;
    }

    public static SecurityComponent getByValue(String value){
        return Arrays.stream(SecurityComponent.values()).filter(c -> c.value().equals(value)).findFirst().orElse(null);
    }

    public static Map<String, String> valueCnMap(){
        Map<String, String> result = new HashMap<>();
        Arrays.stream(SecurityComponent.values()).forEach(c -> result.put(c.value(), c.getCnName()));
        return result;
    }

    public String value() {
        return CaseFormat.UPPER_UNDERSCORE.to(CaseFormat.LOWER_HYPHEN, name());
    }

    public String getCnName() {
        return cnName;
    }

    public boolean getHasHa() {
        return hasHa;
    }

    public boolean getZxAuth() {
        return zxAuth;
    }
}
