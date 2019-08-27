package com.gcloud.core.util;

import com.gcloud.core.service.SpringUtil;
import org.springframework.core.env.Environment;

/**
 * Created by yaowj on 2018/11/21.
 */
public class MessageUtil {

    private static String controllerServiceId;

    public static String computeServiceId(String hostname){
        return String.format("compute-%s", hostname);
    }

    public static String networkServiceId(String hostname){
        return String.format("network-%s", hostname);
    }

    public static String storageServiceId(String hostname){
        return String.format("storage-%s", hostname);
    }
    
    public static String imageServiceId(String hostname){
        return String.format("image-%s", hostname);
    }

    public static String controllerServiceId(){
        return getControllerServiceId();
    }

    private static String getControllerServiceId(){

        if(controllerServiceId == null){
            Environment env = SpringUtil.getBean(Environment.class);
            controllerServiceId = env.getProperty("gcloud.service.controller");
        }
        return controllerServiceId;
    }

}
