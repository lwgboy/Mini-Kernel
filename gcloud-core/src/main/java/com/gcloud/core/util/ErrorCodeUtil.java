package com.gcloud.core.util;

import com.gcloud.core.exception.GCloudException;

/**
 * Created by yaowj on 2018/11/15.
 */
public class ErrorCodeUtil {

    public static String getErrorCode(Exception ex, String defaultErrorCode){
        String result = defaultErrorCode;
        if(ex instanceof GCloudException){
            result = ex.getMessage();
        }
        return result;
    }

}
