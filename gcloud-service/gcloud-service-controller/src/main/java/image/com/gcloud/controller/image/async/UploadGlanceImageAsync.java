package com.gcloud.controller.image.async;

import com.gcloud.controller.ResourceStates;
import com.gcloud.controller.async.LogAsync;
import com.gcloud.controller.image.dao.ImageDao;
import com.gcloud.controller.image.dao.ImagePropertyDao;
import com.gcloud.controller.image.entity.Image;
import com.gcloud.controller.provider.GlanceProviderProxy;
import com.gcloud.core.async.AsyncResult;
import com.gcloud.core.async.AsyncStatus;
import com.gcloud.core.service.SpringUtil;
import com.gcloud.core.util.ErrorCodeUtil;
import com.gcloud.header.enums.ProviderType;
import com.gcloud.header.enums.ResourceType;
import org.openstack4j.model.common.Payloads;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by yaowj on 2018/11/22.
 */
public class UploadGlanceImageAsync extends LogAsync {

    private org.openstack4j.model.image.v2.Image image;
    private String imageId;
    private String filePath;

    @Override
    public long timeout() {
        return 0;
    }

    @Override
    public AsyncResult execute() {

        AsyncStatus status = AsyncStatus.SUCCEED;
        AsyncResult result = new AsyncResult();

        GlanceProviderProxy proxy = SpringUtil.getBean(GlanceProviderProxy.class);
        try{
            proxy.uploadImage(image, Payloads.create(new File(filePath)));
        }catch (Exception ex){
            status = AsyncStatus.FAILED;
            result.setErrorMsg(ErrorCodeUtil.getErrorCode(ex, "::上传镜像失败"));
        }

        result.setAsyncStatus(status);
        return result;
    }

    @Override
    public void successHandler() {

        GlanceProviderProxy proxy = SpringUtil.getBean(GlanceProviderProxy.class);
        org.openstack4j.model.image.v2.Image img = proxy.getImage(image.getId());

        ImageDao imageDao = SpringUtil.getBean(ImageDao.class);
        List<String> updateField = new ArrayList<>();

        Image image = new Image();
        image.setId(imageId);

        updateField.add(image.updateStatus(ResourceStates.status(ResourceType.IMAGE, ProviderType.GLANCE, img.getStatus().value())));
        updateField.add(image.updateSize(img.getSize()));
        updateField.add(image.updateMinDisk(img.getMinDisk()));

        imageDao.update(image, updateField);
    }

    @Override
    public void failHandler() {
        delete();
    }

    @Override
    public void timeoutHandler() {
        defaultHandler();
    }

    @Override
    public void exceptionHandler() {
        delete();
    }

    public void delete(){

        GlanceProviderProxy proxy = SpringUtil.getBean(GlanceProviderProxy.class);
        proxy.deleteImage(image.getId());

        ImageDao imageDao = SpringUtil.getBean(ImageDao.class);
        ImagePropertyDao imagePropertyDao = SpringUtil.getBean(ImagePropertyDao.class);

        imageDao.deleteById(image.getId());
        imagePropertyDao.deleteByImageId(image.getId());

    }

    @Override
    public void defaultHandler() {

        GlanceProviderProxy proxy = SpringUtil.getBean(GlanceProviderProxy.class);
        org.openstack4j.model.image.v2.Image img = proxy.getImage(image.getId());

        ImageDao imageDao = SpringUtil.getBean(ImageDao.class);
        List<String> updateField = new ArrayList<>();

        Image image = new Image();
        image.setId(imageId);
        updateField.add(image.updateStatus(ResourceStates.status(ResourceType.IMAGE, ProviderType.GLANCE, img.getStatus().value())));

        imageDao.update(image, updateField);

    }

    public org.openstack4j.model.image.v2.Image getImage() {
        return image;
    }

    public void setImage(org.openstack4j.model.image.v2.Image image) {
        this.image = image;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public String getImageId() {
        return imageId;
    }

    public void setImageId(String imageId) {
        this.imageId = imageId;
    }
}
