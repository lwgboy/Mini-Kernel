package com.gcloud.header.slb.msg.api;

import org.hibernate.validator.constraints.NotBlank;

import com.gcloud.header.ApiMessage;
import com.gcloud.header.api.ApiModel;

public class ApiSetHealthCheckAttributeMsg extends ApiMessage {
	
	@ApiModel(description = "监听器ID或后端服务器组ID", require = true)
	@NotBlank(message = "0150201::监听器ID或后端服务器组ID不能为空")
	private String resourceId;
	@ApiModel(description = "监听器协议，取值：TCP| HTTP | HTTPS")
	private String protocol;
	@ApiModel(description = "健康检查连续失败次数")
	private Integer unhealthyThreshold;
	@ApiModel(description = "健康检查等待后端服务器响应时间")
	private Integer healthCheckTimeout;
	@ApiModel(description = "健康检查的时间间隔")
	private Integer healthCheckInterval;
	@NotBlank(message = "0150202::健康检查开关不能为空")
	@ApiModel(description = "健康检查开关：on、off", require = true)
	private String healthCheck;
	@ApiModel(description = "用于健康检查的URI")
	private String healthCheckURI;
	@ApiModel(description = "健康检查连续成功次数")
	private Integer healthyThreshold;
	@ApiModel(description = "健康检查类型,取值：tcp | http")
	private String healthCheckType;
	@Override
	public Class replyClazz() {
		// TODO Auto-generated method stub
		return ApiSetHealthCheckAttributeReplyMsg.class;
	}
	public String getResourceId() {
		return resourceId;
	}
	public void setResourceId(String resourceId) {
		this.resourceId = resourceId;
	}
	public String getProtocol() {
		return protocol;
	}
	public void setProtocol(String protocol) {
		this.protocol = protocol;
	}
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
