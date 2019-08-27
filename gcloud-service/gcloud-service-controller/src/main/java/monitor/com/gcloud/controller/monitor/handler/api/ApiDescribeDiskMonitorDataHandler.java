package com.gcloud.controller.monitor.handler.api;

import org.springframework.beans.factory.annotation.Autowired;

import com.gcloud.controller.monitor.service.IMonitorService;
import com.gcloud.core.handle.ApiHandler;
import com.gcloud.core.handle.MessageHandler;
import com.gcloud.header.Module;
import com.gcloud.header.SubModule;
import com.gcloud.header.monitor.msg.api.ApiDescribeDiskMonitorDataHandlerMsg;
import com.gcloud.header.monitor.msg.api.ApiDescribeDiskMonitorDataHandlerReplyMsg;

@ApiHandler(module = Module.ECS, subModule = SubModule.DISK ,action = "DescribeDiskMonitorData")
public class ApiDescribeDiskMonitorDataHandler extends MessageHandler<ApiDescribeDiskMonitorDataHandlerMsg, ApiDescribeDiskMonitorDataHandlerReplyMsg>{
	
	@Autowired
    private IMonitorService monitorService;

    @Override
    public ApiDescribeDiskMonitorDataHandlerReplyMsg handle(ApiDescribeDiskMonitorDataHandlerMsg msg) {
    	ApiDescribeDiskMonitorDataHandlerReplyMsg reply = new ApiDescribeDiskMonitorDataHandlerReplyMsg();
    	try {
			reply = monitorService.describeDiskMonitorData(msg);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        return reply;
    }
}
