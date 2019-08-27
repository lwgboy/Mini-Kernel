package com.gcloud.controller.compute;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "gcloud.controller.compute")
public class ControllerComputeProp {
    @Value("${gcloud.controller.compute.defaultCephPoolId:images}")
    private String defaultCephPoolId;
    
    @Value("${gcloud.controller.compute.cephImagePathFormat:rbd:%s/%s}")
    private String cephImagePathFormat;
    
    @Value("${gcloud.controller.compute.imagePathFormat:rbd:/instances/%s/%s/gcloud_image_%s}")
    private String imagePathFormat;
    
    @Value("${gcloud.controller.compute.defaultFileImagePath:/var/lib/glance/images/}")
    private String defaultFileImagePath;
    
    @Value("${gcloud.controller.compute.fileImagePathFormat:%s%s}")
    private String fileImagePathFormat;
    
    @Value("${gcloud.controller.compute.defaultLvmImagePool:central-volumes01}")
    private String defaultLvmImagePool;
    
    @Value("${gcloud.controller.compute.lvmImagePathFormat:%s%s}")
    private String lvmImagePathFormat;
    
    @Value("${gcloud.controller.compute.usedImageCache:false}")
    private boolean usedImageCache;
    
    @Value("${gcloud.controller.compute.imageCachePath:/instances/cache/%s/%s}")
    private String imageCachePath;
    
    @Value("${spring.controller.compute.instanceDiskPath:/instances/disk}")
    private String instanceDiskPath;

	@Value("${gcloud.controller.compute.instanceConfigPath:/cephFileSystem/instances/config}")
	private String instanceConfigPath;

	public String getDefaultCephPoolId() {
		return defaultCephPoolId;
	}

	public void setDefaultCephPoolId(String defaultCephPoolId) {
		this.defaultCephPoolId = defaultCephPoolId;
	}

	public String getCephImagePathFormat() {
		return cephImagePathFormat;
	}

	public void setCephImagePathFormat(String cephImagePathFormat) {
		this.cephImagePathFormat = cephImagePathFormat;
	}

	public String getImagePathFormat() {
		return imagePathFormat;
	}

	public void setImagePathFormat(String imagePathFormat) {
		this.imagePathFormat = imagePathFormat;
	}

	public String getDefaultFileImagePath() {
		return defaultFileImagePath;
	}

	public void setDefaultFileImagePath(String defaultFileImagePath) {
		this.defaultFileImagePath = defaultFileImagePath;
	}

	public String getFileImagePathFormat() {
		return fileImagePathFormat;
	}

	public void setFileImagePathFormat(String fileImagePathFormat) {
		this.fileImagePathFormat = fileImagePathFormat;
	}

	public boolean isUsedImageCache() {
		return usedImageCache;
	}

	public void setUsedImageCache(boolean usedImageCache) {
		this.usedImageCache = usedImageCache;
	}

	public String getImageCachePath() {
		return imageCachePath;
	}

	public void setImageCachePath(String imageCachePath) {
		this.imageCachePath = imageCachePath;
	}

	public String getDefaultLvmImagePool() {
		return defaultLvmImagePool;
	}

	public void setDefaultLvmImagePool(String defaultLvmImagePool) {
		this.defaultLvmImagePool = defaultLvmImagePool;
	}

	public String getLvmImagePathFormat() {
		return lvmImagePathFormat;
	}

	public void setLvmImagePathFormat(String lvmImagePathFormat) {
		this.lvmImagePathFormat = lvmImagePathFormat;
	}

	public String getInstanceDiskPath() {
		return instanceDiskPath;
	}

	public void setInstanceDiskPath(String instanceDiskPath) {
		this.instanceDiskPath = instanceDiskPath;
	}

	public String getInstanceConfigPath() {
		return instanceConfigPath;
	}

	public void setInstanceConfigPath(String instanceConfigPath) {
		this.instanceConfigPath = instanceConfigPath;
	}
}
