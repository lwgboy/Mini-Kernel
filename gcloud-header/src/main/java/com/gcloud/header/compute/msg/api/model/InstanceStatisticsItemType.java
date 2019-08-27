package com.gcloud.header.compute.msg.api.model;

import java.io.Serializable;

import com.gcloud.framework.db.jdbc.annotation.TableField;
import com.gcloud.header.api.ApiModel;

public class InstanceStatisticsItemType  implements Serializable{
	@ApiModel(description = "实例状态")
    @TableField("state")
    private String status;
	@ApiModel(description = "数量")
    private int countNum;
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public int getCountNum() {
		return countNum;
	}
	public void setCountNum(int countNum) {
		this.countNum = countNum;
	}
	
}
