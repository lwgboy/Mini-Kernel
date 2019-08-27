package com.gcloud.controller.compute.handler.api.vm.base;

import com.gcloud.controller.ResourceIsolationCheck;
import com.gcloud.controller.compute.service.vm.base.IVmBaseService;
import com.gcloud.controller.enums.ResourceIsolationCheckType;
import com.gcloud.core.annotations.CustomAnnotations.GcLog;
import com.gcloud.core.annotations.CustomAnnotations.LongTask;
import com.gcloud.core.cache.container.CacheContainer;
import com.gcloud.core.cache.enums.CacheType;
import com.gcloud.core.exception.GCloudException;
import com.gcloud.core.handle.ApiHandler;
import com.gcloud.core.handle.MessageHandler;
import com.gcloud.header.Module;
import com.gcloud.header.SubModule;
import com.gcloud.header.compute.msg.api.vm.base.ApiStopInstanceMsg;
import com.gcloud.header.compute.msg.api.vm.base.ApiStopInstanceReplyMsg;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Created by yaowj on 2018/9/17.
 */
@ApiHandler(module = Module.ECS,subModule=SubModule.VM, action = "StopInstance")
@LongTask
@GcLog(taskExpect = "关闭云服务器")
@ResourceIsolationCheck(resourceIsolationCheckType = ResourceIsolationCheckType.INSTANCE, resourceIdField = "instanceId")
public class ApiStopInstanceHandler extends MessageHandler<ApiStopInstanceMsg, ApiStopInstanceReplyMsg> {

    @Autowired
    private IVmBaseService vmBaseService;

    @Override
    public ApiStopInstanceReplyMsg handle(ApiStopInstanceMsg msg) throws GCloudException {
        vmBaseService.stopInstance(msg.getInstanceId(), false, true);
        ApiStopInstanceReplyMsg reply = new ApiStopInstanceReplyMsg();
        msg.setInstanceId(msg.getInstanceId());
        msg.setObjectName(CacheContainer.getInstance().getString(CacheType.INSTANCE_ALIAS, msg.getInstanceId()));

        return reply;
    }

}
