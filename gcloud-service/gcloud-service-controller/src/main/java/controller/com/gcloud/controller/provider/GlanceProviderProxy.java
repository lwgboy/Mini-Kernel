package com.gcloud.controller.provider;

import com.gcloud.core.exception.GCloudException;
import lombok.extern.slf4j.Slf4j;
import org.openstack4j.api.Builders;
import org.openstack4j.model.common.ActionResponse;
import org.openstack4j.model.common.Payload;
import org.openstack4j.model.image.v2.Image;
import org.openstack4j.openstack.image.v2.domain.PatchOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Component
@Slf4j
public class GlanceProviderProxy extends OpenstackProviderProxy{

	@Autowired
	//GlanceProvider provider;
	public void setProvider(GlanceProvider provider){
		super.setProvider(provider);
	}

	public Image getImage(String id){
		return getClient().imagesV2().get(id);
	}


	public Image createImage(Image image){

		Image newImage = null;
		try{
			newImage = getClient().imagesV2().create(image);
		}catch (Exception ex){
			log.error("create image error" + ex, ex);
			throw new GCloudException("::创建镜像失败");
		}

		if(newImage == null){
			log.error("::create image error, response is null");
			throw new GCloudException("::创建镜像失败");
		}

		return newImage;
	}

	public void uploadImage(Image image, Payload<?> payload){


        ActionResponse response = null;
        try{
            response = getClient().imagesV2().upload(image.getId(), payload, image);
        }catch(Exception ex){
            log.error(String.format("上传镜像失败,imageId=%s", image.getId()) + ex, ex);
            throw new GCloudException("::上传镜像失败");
        }

        if(response == null){
            log.error(String.format("上传镜像失败,response为空,imageId=%s", image.getId()));
            throw new GCloudException("::上传镜像失败");
        }

        if (!response.isSuccess()) {
            log.error(String.format("上传镜像失败,message=%s,code=%s,imageId=%s", response.getFault(), response.getCode(), image.getId()));
            throw new GCloudException("::上传镜像失败");
        }
	}

	public void deleteImage(String imageId){

        ActionResponse response = null;
        try{
            response = getClient().imagesV2().delete(imageId);
        }catch(Exception ex){
            log.error(String.format("删除镜像失败,imageId=%s,", imageId) + ex, ex);
            throw new GCloudException("::删除镜像失败");
        }

        if(response == null){
            log.error(String.format("删除镜像失败,response为空,imageId=%s", imageId));
            throw new GCloudException("::删除镜像失败");
        }

        if (!response.isSuccess()) {
            log.error(String.format("删除镜像失败,message=%s,code=%s,imageId=%s", response.getFault(), response.getCode(), imageId));
            throw new GCloudException("::删除镜像失败");
        }

    }

    public Image updateImage(Image image){

        Image newImage = null;
        try{
            newImage = getClient().imagesV2().update(image);
        }catch (Exception ex){
            log.error("更新镜像失败" + ex, ex);
            throw new GCloudException("::更新镜像失败");
        }

        if(newImage == null){
            log.error("::更新镜像失败, response is null");
            throw new GCloudException("::更新镜像失败");
        }

        return newImage;

    }

    public Image updateImage(String imageId, List<PatchOperation> patchOperations){

        Image newImage = null;
        try{
            newImage = getClient().imagesV2().update(imageId, Builders.imageUpdateV2().ops(patchOperations).build());
        }catch (Exception ex){
            log.error("更新镜像失败" + ex, ex);
            throw new GCloudException("::更新镜像失败");
        }

        if(newImage == null){
            log.error("::更新镜像失败, response is null");
            throw new GCloudException("::更新镜像失败");
        }

        return newImage;

    }

    public Image updateImage(String imageId, PatchOperation patchOperation){

        Image newImage = null;
        try{
            newImage = getClient().imagesV2().update(imageId, Builders.imageUpdateV2().ops(patchOperation).build());
        }catch (Exception ex){
            log.error("更新镜像失败" + ex, ex);
            throw new GCloudException("::更新镜像失败");
        }

        if(newImage == null){
            log.error("::更新镜像失败, response is null");
            throw new GCloudException("::更新镜像失败");
        }

        return newImage;

    }

    public List<Image> listImage(Map<String, String> filters) {
	    List<Image> imageList = (List<Image>)getClient().imagesV2().list(filters);
	    return imageList;
    }
    
}