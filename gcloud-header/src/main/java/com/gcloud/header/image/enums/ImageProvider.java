
package com.gcloud.header.image.enums;

import java.util.Arrays;

public enum ImageProvider {

    GCLOUD(0, "gcloud"),
    GLANCE(1, "glance");

    private int value;
    private String enValue;

    ImageProvider(int value, String enValue) {
        this.value = value;
        this.enValue = enValue;
    }

    public int getValue() {
        return value;
    }

    public String getEnValue() {
        return enValue;
    }

    public static ImageProvider value(String enValue){
        return Arrays.stream(ImageProvider.values()).filter(s -> s.getEnValue().equals(enValue)).findFirst().orElse(null);
    }

}
