package com.gcloud.controller.image.service.impl;

import com.gcloud.common.util.StringUtils;
import com.gcloud.controller.ResourceProviders;
import com.gcloud.controller.compute.dao.InstanceDao;
import com.gcloud.controller.image.dao.ImageDao;
import com.gcloud.controller.image.dao.ImagePropertyDao;
import com.gcloud.controller.image.entity.Image;
import com.gcloud.controller.image.entity.ImageProperty;
import com.gcloud.controller.image.entity.enums.ImagePropertyItem;
import com.gcloud.controller.image.model.CreateImageParams;
import com.gcloud.controller.image.model.DescribeImageParams;
import com.gcloud.controller.image.provider.IImageProvider;
import com.gcloud.controller.image.service.IImageService;
import com.gcloud.core.cache.container.CacheContainer;
import com.gcloud.core.cache.enums.CacheType;
import com.gcloud.core.exception.GCloudException;
import com.gcloud.framework.db.PageResult;
import com.gcloud.header.api.model.CurrentUser;
import com.gcloud.header.enums.ResourceType;
import com.gcloud.header.image.enums.ImageArchitecture;
import com.gcloud.header.image.enums.OsType;
import com.gcloud.header.image.model.ImageStatisticsItem;
import com.gcloud.header.image.model.ImageType;
import com.gcloud.header.image.msg.api.GenDownloadVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by yaowj on 2018/11/30.
 */
@Service
@Transactional(propagation = Propagation.REQUIRES_NEW)
public class ImageServiceImpl implements IImageService {

    @Autowired
    private ImagePropertyDao imagePropertyDao;

    @Autowired
    private ImageDao imageDao;

    @Autowired
    private InstanceDao instanceDao;
    
    @Override
    public String createImage(CreateImageParams params, CurrentUser currentUser) throws GCloudException {

        if(StringUtils.isNotBlank(params.getImageId())){
            Image image = imageDao.getById(params.getImageId());
            if(image == null){
                throw new GCloudException("0090108::镜像不存在");
            }

            Map<String, String> imageProps = getImageProperties(params.getImageId());
            if(imageProps != null){
                params.setOsType(imageProps.get(ImagePropertyItem.OS_TYPE.value()));
                params.setArchitecture(imageProps.get(ImagePropertyItem.ARCHITECTURE.value()));
            }
        }

        OsType osType = OsType.value(params.getOsType());
        if (osType == null) {
            throw new GCloudException("0090104::不支持此操作系统类型");
        }
        ImageArchitecture architecture = ImageArchitecture.value(params.getArchitecture());
        if (architecture == null) {
            throw new GCloudException("0090105::不支持此架构");
        }
        return this.getProviderOrDefault().createImage(params, currentUser);
    }

    @Override
    public void updateImage(String imageId, String imageName) throws GCloudException {
        Image image = imageDao.getById(imageId);
        if (image == null) {
            throw new GCloudException("0090202::找不到对应的镜像");
        }
        this.checkAndGetProvider(image.getProvider()).updateImage(imageId, image.getProviderRefId(), imageName);
        CacheContainer.getInstance().put(CacheType.IMAGE_NAME, imageId, imageName);
    }

    @Override
    public void deleteImage(String imageId) throws GCloudException {
        Image image = imageDao.getById(imageId);
        if (image == null) {
            throw new GCloudException("0090302::找不到对应的镜像");
        }
        if (instanceDao.hasImageInstance(imageId)) {
            throw new GCloudException("0090303::有云服务器正在使用此镜像，不能删除");
        }
        this.checkAndGetProvider(image.getProvider()).deleteImage(imageId, image.getProviderRefId());
    }

    @Override
    public Map<String, String> getImageProperties(String imageId) {
        Map<String, String> props = null;
        List<ImageProperty> imageProperties = imagePropertyDao.findByProperty(ImageProperty.IMAGE_ID, imageId);
        if (imageProperties != null && imageProperties.size() > 0) {
            props = new HashMap<>();
            for (ImageProperty imageProperty : imageProperties) {
                props.put(imageProperty.getName(), imageProperty.getValue());
            }
        }
        return props;
    }

    @Override
    public PageResult<ImageType> describeImage(DescribeImageParams params, CurrentUser currentUser) {
        return imageDao.describeDisks(params, ImageType.class, currentUser);
    }
    
    @Override
    public GenDownloadVo genDownload(String imageId) {
        Image image = imageDao.getById(imageId);
        if (image == null) {
            throw new GCloudException("0090302::找不到对应的镜像");
        }
        return this.checkAndGetProvider(image.getProvider()).genDownload(imageId, image.getProviderRefId());
    }

    private IImageProvider getProviderOrDefault() {
        IImageProvider provider = ResourceProviders.getDefault(ResourceType.IMAGE);
        return provider;
    }

    private IImageProvider checkAndGetProvider(Integer providerType) {
        IImageProvider provider = ResourceProviders.checkAndGet(ResourceType.IMAGE, providerType);
        return provider;
    }

	@Override
	public void disableImage(String imageId, Boolean disable) {
		Image image = imageDao.getById(imageId);
        if (image == null) {
            throw new GCloudException("0090403::找不到对应的镜像");
        }
        List<String> updateFields = new ArrayList<String>();
        updateFields.add(image.updateDisable(disable));
        
        imageDao.update(image, updateFields);
	}

	@Override
	public List<ImageStatisticsItem> imageStatistics() {
		return imageDao.imageStatistics(ImageStatisticsItem.class);
	}

}
