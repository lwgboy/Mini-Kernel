package com.gcloud.controller.monitor.handler.api;

import org.springframework.beans.factory.annotation.Autowired;

import com.gcloud.controller.monitor.service.IMonitorService;
import com.gcloud.core.handle.ApiHandler;
import com.gcloud.core.handle.MessageHandler;
import com.gcloud.header.Module;
import com.gcloud.header.SubModule;
import com.gcloud.header.monitor.msg.api.ApiDescribeInstanceMonitorDataHandlerMsg;
import com.gcloud.header.monitor.msg.api.ApiDescribeInstanceMonitorDataHandlerReplyMsg;

@ApiHandler(module = Module.ECS, subModule = SubModule.VM ,action = "DescribeInstanceMonitorData")
public class ApiDescribeInstanceMonitorDataHandler extends MessageHandler<ApiDescribeInstanceMonitorDataHandlerMsg, ApiDescribeInstanceMonitorDataHandlerReplyMsg>{
	
	@Autowired
    private IMonitorService monitorService;

    @Override
    public ApiDescribeInstanceMonitorDataHandlerReplyMsg handle(ApiDescribeInstanceMonitorDataHandlerMsg msg) {
    	ApiDescribeInstanceMonitorDataHandlerReplyMsg reply = new ApiDescribeInstanceMonitorDataHandlerReplyMsg();
    	try {
			reply = monitorService.describeInstanceMonitorData(msg);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        return reply;
    }
}
