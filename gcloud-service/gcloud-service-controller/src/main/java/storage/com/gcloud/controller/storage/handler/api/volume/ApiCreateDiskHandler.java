package com.gcloud.controller.storage.handler.api.volume;

import com.gcloud.controller.storage.model.CreateDiskParams;
import com.gcloud.controller.storage.service.IVolumeService;
import com.gcloud.core.annotations.CustomAnnotations.GcLog;
import com.gcloud.core.annotations.CustomAnnotations.LongTask;
import com.gcloud.core.exception.GCloudException;
import com.gcloud.core.handle.ApiHandler;
import com.gcloud.core.handle.MessageHandler;
import com.gcloud.core.util.BeanUtil;
import com.gcloud.header.Module;
import com.gcloud.header.SubModule;
import com.gcloud.header.storage.msg.api.volume.ApiCreateDiskMsg;
import com.gcloud.header.storage.msg.api.volume.ApiCreateDiskReplyMsg;
import org.springframework.beans.factory.annotation.Autowired;
/**
 * Created by yaowj on 2018/9/21.
 */
@LongTask
@GcLog(taskExpect = "创建磁盘")
@ApiHandler(module= Module.ECS,subModule=SubModule.DISK, action="CreateDisk")
public class ApiCreateDiskHandler extends MessageHandler<ApiCreateDiskMsg, ApiCreateDiskReplyMsg> {

    @Autowired
    private IVolumeService volumeService;

    @Override
    public ApiCreateDiskReplyMsg handle(ApiCreateDiskMsg msg) throws GCloudException {

        CreateDiskParams params = BeanUtil.copyProperties(msg, CreateDiskParams.class);
        params.setTaskId(msg.getTaskId());
        String volumeId = volumeService.create(params, msg.getCurrentUser());
        msg.setObjectId(volumeId);
        ApiCreateDiskReplyMsg reply = new ApiCreateDiskReplyMsg();
        reply.setDiskId(volumeId);

        return reply;
    }
}
