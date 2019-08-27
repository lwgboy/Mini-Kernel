package com.gcloud.controller.compute.workflow.vm.senior.bundle;

import com.gcloud.controller.compute.ControllerComputeProp;
import com.gcloud.controller.compute.dao.InstanceDao;
import com.gcloud.controller.compute.entity.VmInstance;
import com.gcloud.controller.compute.model.node.Node;
import com.gcloud.controller.compute.model.vm.VmBundleResponse;
import com.gcloud.controller.compute.service.vm.base.IVmBaseService;
import com.gcloud.controller.compute.service.vm.senior.IVmSeniorService;
import com.gcloud.controller.compute.utils.RedisNodesUtil;
import com.gcloud.controller.compute.workflow.model.senior.BundleInstanceInitFlowCommandReq;
import com.gcloud.controller.compute.workflow.model.senior.BundleInstanceInitFlowCommandRes;
import com.gcloud.controller.image.dao.ImageDao;
import com.gcloud.controller.image.dao.ImagePropertyDao;
import com.gcloud.controller.image.service.IImageService;
import com.gcloud.controller.storage.service.IVolumeService;
import com.gcloud.core.workflow.core.BaseWorkFlowCommand;
import com.gcloud.header.compute.enums.Device;
import com.gcloud.header.storage.model.VmVolumeDetail;
import com.gcloud.service.common.compute.uitls.VmUtil;
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
public class BundleInstanceInitFlowCommand extends BaseWorkFlowCommand {

    @Autowired
    private InstanceDao instanceDao;

    @Autowired
    private IVmBaseService vmBaseService;

    @Autowired
    private IImageService imageService;

    @Autowired
    private ImageDao imageDao;

    @Autowired
    private ImagePropertyDao imagePropertyDao;

    @Autowired
    private ControllerComputeProp prop;

    @Autowired
    private IVolumeService volumeService;

    @Autowired
    private IVmSeniorService vmSeniorService;

    @Override
    protected Object process() throws Exception {

        BundleInstanceInitFlowCommandReq req = (BundleInstanceInitFlowCommandReq)getReqParams();

        VmBundleResponse bundleInfo = vmSeniorService.bundle(req.getInstanceId(), req.getImageName(), req.getInTask(), req.getCurrentUser());

        VmInstance ins = instanceDao.getById(req.getInstanceId());

        BundleInstanceInitFlowCommandRes res = new BundleInstanceInitFlowCommandRes();
        res.setBeginningState(ins.getState());

        VmVolumeDetail volumeDetail = volumeService.volumeDetail(req.getInstanceId(), Device.VDA);

        Node node = RedisNodesUtil.getComputeNodeByHostName(ins.getHostname());

        String imageFilePath = VmUtil.getBundleTargetPath(VmUtil.getInstanceConfigPath(prop.getInstanceConfigPath(), node.getNodeIp(), req.getInstanceId()));
        res.setImageFilePath(imageFilePath);
        res.setNodeIp(node.getNodeIp());
        res.setVolumeDetail(volumeDetail);
        res.setImageId(bundleInfo.getImageId());
        res.setImageName(bundleInfo.getImageName());

        return res;
    }

    @Override
    protected Object rollback() throws Exception {

        BundleInstanceInitFlowCommandReq req = (BundleInstanceInitFlowCommandReq)getReqParams();
        BundleInstanceInitFlowCommandRes res = (BundleInstanceInitFlowCommandRes)getResParams();

        vmBaseService.cleanState(req.getInstanceId(), req.getInTask());

        imageDao.deleteById(res.getImageId());
        imagePropertyDao.deleteByImageId(res.getImageId());

        return null;
    }

    @Override
    protected Object timeout() throws Exception {
        return null;
    }

    @Override
    protected Class<?> getReqParamClass() {
        return BundleInstanceInitFlowCommandReq.class;
    }

    @Override
    protected Class<?> getResParamClass() {
        return BundleInstanceInitFlowCommandRes.class;
    }
}
