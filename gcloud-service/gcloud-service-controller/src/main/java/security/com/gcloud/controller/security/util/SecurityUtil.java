package com.gcloud.controller.security.util;

import com.gcloud.common.util.StringUtils;
import com.gcloud.controller.security.enums.SecurityComponent;
import com.gcloud.controller.security.enums.SecurityNetworkType;

/**
 * Created by yaowj on 2018/7/30.
 */
public class SecurityUtil {


    public static String getPortName(String clusterName, SecurityNetworkType securityNetworkType, SecurityComponent securityComponent){

        //255
        int maxSize = 255;
        String post = String.format("%s_%s卡", securityComponent.getCnName(), securityNetworkType.getName());

        return cutString(clusterName, post, "_", maxSize);
    }

    public static String getAlias(String clusterId, String clusterName, SecurityComponent type, Boolean isHa){

        //300
        int maxSize = 300;
        String name = StringUtils.isBlank(clusterName) ? clusterId : clusterName;

        //后半部分
        String post =  type.getCnName();
        if(isHa != null && isHa) post += "_ha";

        return cutString(name, post, "_", maxSize);
    }

    public static String cutString(String pre, String post, String join, int maxSize){

        String result = "";
        int strSize = pre.length() + post.length() + join.length();
        if(strSize <= maxSize){
            result = String.format("%s%s%s", pre, join, post);
        }else{

            int size = strSize - maxSize;
            if(pre.length() == size){
                result = post;
            }else if(pre.length() > size){
                result = String.format("%s%s%s", pre.substring(0, pre.length() - size), join, post);
            }else{
                result = String.format("%s%s%s", pre, join, post).substring(0, maxSize);
            }

        }

        return result;
    }

}
