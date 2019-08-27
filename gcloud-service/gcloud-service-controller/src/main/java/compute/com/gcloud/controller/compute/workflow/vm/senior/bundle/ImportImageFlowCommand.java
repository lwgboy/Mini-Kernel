package com.gcloud.controller.compute.workflow.vm.senior.bundle;

import com.gcloud.controller.compute.workflow.model.senior.ImportImageFlowCommandReq;
import com.gcloud.controller.compute.workflow.model.senior.ImportImageFlowCommandRes;
import com.gcloud.controller.image.model.CreateImageParams;
import com.gcloud.controller.image.service.IImageService;
import com.gcloud.core.workflow.core.BaseWorkFlowCommand;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 * Created by yaowj on 2018/11/30.
 */
@Component
@Scope("prototype")
@Slf4j
public class ImportImageFlowCommand extends BaseWorkFlowCommand {

    @Autowired
    private IImageService imageService;

    @Override
    protected Object process() throws Exception {

        ImportImageFlowCommandReq req = (ImportImageFlowCommandReq)this.getReqParams();

        CreateImageParams params = new CreateImageParams();
        params.setTaskId(this.getTaskId());
        params.setImageName(req.getImageName());
        params.setArchitecture(req.getArchitecture());
        params.setFilePath(req.getFilePath());
        params.setDescription(req.getDescription());
        params.setOsType(req.getOsType());
        params.setImageId(req.getImageId());

        String imageId = imageService.createImage(params, req.getCurrentUser());
        ImportImageFlowCommandRes res = new ImportImageFlowCommandRes();
        res.setImageId(imageId);

        return res;
    }

    @Override
    protected Object rollback() throws Exception {
        ImportImageFlowCommandRes res = (ImportImageFlowCommandRes)getResParams();
        imageService.deleteImage(res.getImageId());
        return null;
    }

    @Override
    protected Object timeout() throws Exception {
        return null;
    }

    @Override
    protected Class<?> getReqParamClass() {
        return ImportImageFlowCommandReq.class;
    }

    @Override
    protected Class<?> getResParamClass() {
        return ImportImageFlowCommandRes.class;
    }
}
