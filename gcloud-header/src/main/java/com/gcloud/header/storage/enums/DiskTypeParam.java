package com.gcloud.header.storage.enums;

/**
 * Created by yaowj on 2018/10/8.
 */
public enum DiskTypeParam {

    ALL("all", "所有"), SYSTEM(DiskType.SYSTEM.getValue(), "系统盘"), DATA(DiskType.DATA.getValue(), "数据盘");

    private String value;
    private String cnName;


    DiskTypeParam(String value, String cnName) {
        this.value = value;
        this.cnName = cnName;
    }

    public String getValue() {
        return value;
    }

    public String getCnName() {
        return cnName;
    }

    public static DiskTypeParam getByValue(String value){

        DiskTypeParam result = null;
        if(value != null){
            for(DiskTypeParam param : DiskTypeParam.values()){
                if(value.equals(param.getValue())){
                    result = param;
                    break;
                }
            }
        }
        return result;
    }

}
