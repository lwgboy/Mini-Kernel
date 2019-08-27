package com.gcloud.controller.image.service;

import com.gcloud.controller.image.model.CreateImageParams;
import com.gcloud.controller.image.model.DescribeImageParams;
import com.gcloud.core.exception.GCloudException;
import com.gcloud.framework.db.PageResult;
import com.gcloud.header.api.model.CurrentUser;
import com.gcloud.header.image.model.ImageStatisticsItem;
import com.gcloud.header.image.model.ImageType;
import com.gcloud.header.image.msg.api.GenDownloadVo;

import java.util.List;
import java.util.Map;

/**
 * Created by yaowj on 2018/11/22.
 */
public interface IImageService {

    String createImage(CreateImageParams params, CurrentUser currentUser) throws GCloudException;

    void updateImage(String imageId, String imageName) throws GCloudException;

    void deleteImage(String imageId) throws GCloudException;

    PageResult<ImageType> describeImage(DescribeImageParams params, CurrentUser currentUser);

    Map<String, String> getImageProperties(String imageId);

    GenDownloadVo genDownload(String imageId);
    
    void disableImage(String imageId, Boolean disable);
    
    List<ImageStatisticsItem> imageStatistics();

}
