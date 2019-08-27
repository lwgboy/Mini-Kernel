package com.gcloud.controller.image.distribute;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.gcloud.common.util.StringUtils;
import com.gcloud.common.util.SystemUtil;
import com.gcloud.controller.image.prop.ImageProp;
import com.gcloud.core.exception.GCloudException;
import com.gcloud.core.workflow.engine.WorkFlowEngine;
import com.gcloud.core.workflow.enums.FeedbackState;

@Component
public class FileToRbdDistributeImageImpl implements IDistributeImage {
	
	@Autowired
	ImageProp prop;

	@Override
	public void distributeImage(String imageId, String target, String taskId) {
		//将qcow2镜像文件转化为raw格式的临时文件
		//qemu-img convert -f qcow2 -O raw windows2003.qcow2 windows2003.raw
		String[] formatCmd = new String[]{"qemu-img", "convert", "-f", "qcow2", "-O", "raw", prop.getImageFilesystemStoreDir() + imageId, prop.getImageFilesystemStoreDir() + imageId + ".raw"};
        int res = SystemUtil.runAndGetCode(formatCmd);
		//将raw文件copy到rbd上
		//rbd -p images import windows2003.raw --image ${imageId}
        String[] copyCmd = new String[]{"rbd", "-p", "images", "import", prop.getImageFilesystemStoreDir() + imageId + ".raw", "--image", imageId};
        int copyRes = SystemUtil.runAndGetCode(copyCmd);
        //删除临时文件
        String[] deleteCmd = new String[]{"rm", "-f", prop.getImageFilesystemStoreDir() + imageId + ".raw"};
        int deleteRes = SystemUtil.runAndGetCode(deleteCmd);
		//feedback task
        if(StringUtils.isNotBlank(taskId)) {
        	WorkFlowEngine.feedbackHandler(taskId, FeedbackState.SUCCESS.name(), "");
        }
	}

	/*@Override
	public void deleteImageCache(String imageId, String target,String storeType, String taskId) {
		//删掉images池上对应的image cache , rbd rm  -p images  imageId 
		String[] deleteCmd = new String[]{"rbd", "rm", "-p", "images" , imageId};
        int deleteRes = SystemUtil.runAndGetCode(deleteCmd);
        if(deleteRes != 0) {
        	throw new GCloudException("::删除images池上的镜像" + imageId + "失败");
        }
	}*/

}
