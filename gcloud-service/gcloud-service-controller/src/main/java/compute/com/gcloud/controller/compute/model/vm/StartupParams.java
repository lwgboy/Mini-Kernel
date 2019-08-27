/*
 * @Date 2015-4-14
 * 
 * @Author chenyu1@g-cloud.com.cn
 * 
 * @Copyright 2015 www.g-cloud.com.cn Inc. All rights reserved.
 * 
 * @Description 开机参数类
 */
package com.gcloud.controller.compute.model.vm;
import java.util.List;

public class StartupParams {
    private List<String> instanceIds;
    private String gTaskId;
    private Boolean isPublicCloud;
    public List<String> getInstanceIds() {
        return instanceIds;
    }

    public void setInstanceIds(List<String> instanceIds) {
        this.instanceIds = instanceIds;
    }

	public String getGTaskId() {
		return gTaskId;
	}

	public void setGTaskId(String gTaskId) {
		this.gTaskId = gTaskId;
	}

    public Boolean getIsPublicCloud() {
        return isPublicCloud;
    }

    public void setIsPublicCloud(Boolean isPublicCloud) {
        this.isPublicCloud = isPublicCloud;
    }

}
