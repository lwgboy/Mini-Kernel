package com.gcloud.header.image.enums;

import java.util.Arrays;

/**
 * Created by yaowj on 2018/11/22.
 */
public enum ImageArchitecture {

    X86_64, I386, I686;

    public String value(){
        return name().toLowerCase();
    }

    public static ImageArchitecture value(String value){
        return Arrays.stream(ImageArchitecture.values()).filter(type -> type.value().equals(value)).findFirst().orElse(null);
    }

}
