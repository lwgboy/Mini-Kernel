package com.gcloud.header.slb.msg.api;

import com.gcloud.header.ApiReplyMessage;
import com.gcloud.header.api.ApiModel;

public class ApiDescribeHealthCheckAttributeReplyMsg extends ApiReplyMessage {
	
	@ApiModel(description = "健康检查连续失败次数")
	private Integer unhealthyThreshold;
	@ApiModel(description = "健康检查等待后端服务器响应时间")
	private Integer healthCheckTimeout;
	@ApiModel(description = "健康检查的时间间隔")
	private Integer healthCheckInterval;
	@ApiModel(description = "健康检查开关：on、off")
	private String healthCheck;
	@ApiModel(description = "用于健康检查的URI")
	private String healthCheckURI;
	@ApiModel(description = "健康检查连续成功次数")
	private Integer healthyThreshold;
	@ApiModel(description = "健康检查类型,取值：tcp | http")
	private String healthCheckType;
	
	public Integer getUnhealthyThreshold() {
		return unhealthyThreshold;
	}
	public void setUnhealthyThreshold(Integer unhealthyThreshold) {
		this.unhealthyThreshold = unhealthyThreshold;
	}
	public Integer getHealthCheckTimeout() {
		return healthCheckTimeout;
	}
	public void setHealthCheckTimeout(Integer healthCheckTimeout) {
		this.healthCheckTimeout = healthCheckTimeout;
	}
	public Integer getHealthCheckInterval() {
		return healthCheckInterval;
	}
	public void setHealthCheckInterval(Integer healthCheckInterval) {
		this.healthCheckInterval = healthCheckInterval;
	}
	public String getHealthCheck() {
		return healthCheck;
	}
	public void setHealthCheck(String healthCheck) {
		this.healthCheck = healthCheck;
	}
	public String getHealthCheckURI() {
		return healthCheckURI;
	}
	public void setHealthCheckURI(String healthCheckURI) {
		this.healthCheckURI = healthCheckURI;
	}
	public Integer getHealthyThreshold() {
		return healthyThreshold;
	}
	public void setHealthyThreshold(Integer healthyThreshold) {
		this.healthyThreshold = healthyThreshold;
	}
	public String getHealthCheckType() {
		return healthCheckType;
	}
	public void setHealthCheckType(String healthCheckType) {
		this.healthCheckType = healthCheckType;
	}
}
