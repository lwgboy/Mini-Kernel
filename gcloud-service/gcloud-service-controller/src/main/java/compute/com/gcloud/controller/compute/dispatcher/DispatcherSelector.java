package com.gcloud.controller.compute.dispatcher;

import java.util.Arrays;

/**
 * Created by yaowj on 2019/1/8.
 */
public enum DispatcherSelector {
    SIMPLE("simple", SimpleDispatcher.class),
    POLLING("polling", PollingDispatcher.class);

    DispatcherSelector(String value, Class<?> impl) {
        this.value = value;
        this.impl = impl;
    }

    private String value;
    private Class<?> impl;

    public static DispatcherSelector value(String value){
        return Arrays.stream(DispatcherSelector.values()).filter(s -> s.getValue().equals(value)).findFirst().orElse(null);
    }

    public String getValue() {
        return value;
    }

    public Class<?> getImpl() {
        return impl;
    }
}
