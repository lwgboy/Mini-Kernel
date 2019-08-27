package com.gcloud.controller.storage.handler.api.volume;

import com.gcloud.controller.storage.service.IVolumeService;
import com.gcloud.core.exception.GCloudException;
import com.gcloud.core.handle.ApiHandler;
import com.gcloud.core.handle.MessageHandler;
import com.gcloud.header.Module;
import com.gcloud.header.SubModule;
import com.gcloud.header.storage.model.VolumeTypeListResponse;
import com.gcloud.header.storage.msg.api.volume.ApiVolumeTypeListMsg;
import com.gcloud.header.storage.msg.api.volume.ApiVolumeTypeListReplyMsg;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by yaowj on 2018/11/8.
 */
@ApiHandler(module= Module.ECS,subModule=SubModule.DISK, action="VolumeTypeList")
public class ApiVolumeTypeListHandler extends MessageHandler<ApiVolumeTypeListMsg, ApiVolumeTypeListReplyMsg> {

    @Autowired
    private IVolumeService volumeService;

    @Override
    public ApiVolumeTypeListReplyMsg handle(ApiVolumeTypeListMsg msg) throws GCloudException {

        List<VolumeTypeListResponse> list = new ArrayList<>();
        ApiVolumeTypeListReplyMsg reply = new ApiVolumeTypeListReplyMsg();
        reply.setVolumeTypes(list);
        return reply;
    }
}
