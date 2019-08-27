package com.gcloud.header.compute.msg.api.vm.base;

import com.gcloud.header.ApiMessage;


/*
 * @Date Nov 7, 2018
 * 
 * @Author zhangzj
 * 
 * @Description TODO
 */
public class ApiDescribeInstanceTypesMsg extends ApiMessage {

	private static final long serialVersionUID = 1L;
	
	private String zoneId;

	@Override
    public Class replyClazz() {
        return ApiDescribeInstanceTypesReplyMsg.class;
    }

    public String getZoneId() {
        return zoneId;
    }

    public void setZoneId(String zoneId) {
        this.zoneId = zoneId;
    }

}
