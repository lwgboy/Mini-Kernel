package com.gcloud.controller.image.provider;

import com.gcloud.controller.IResourceProvider;
import com.gcloud.controller.image.entity.Image;
import com.gcloud.controller.image.model.CreateImageParams;
import com.gcloud.controller.image.model.DistributeImageParams;
import com.gcloud.core.exception.GCloudException;
import com.gcloud.header.api.model.CurrentUser;
import com.gcloud.header.image.msg.api.GenDownloadVo;

import java.util.List;
import java.util.Map;

public interface IImageProvider extends IResourceProvider {

    String createImage(CreateImageParams params, CurrentUser currentUser) throws GCloudException;

    void updateImage(String imageId, String imageProviderRefId, String imageName) throws GCloudException;

    void deleteImage(String imageId, String imageProviderRefId) throws GCloudException;

    List<Image> listImage(Map<String, String> filters) throws GCloudException;

    GenDownloadVo genDownload(String imageId, String providerRefId);
    
    void distributeImage(DistributeImageParams params);
}
