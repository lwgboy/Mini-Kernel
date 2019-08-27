package com.gcloud.controller.monitor.service;

import com.gcloud.core.exception.GCloudException;
import com.gcloud.header.monitor.msg.api.ApiDescribeDiskMonitorDataHandlerMsg;
import com.gcloud.header.monitor.msg.api.ApiDescribeDiskMonitorDataHandlerReplyMsg;
import com.gcloud.header.monitor.msg.api.ApiDescribeInstanceMonitorDataHandlerMsg;
import com.gcloud.header.monitor.msg.api.ApiDescribeInstanceMonitorDataHandlerReplyMsg;

public interface IMonitorService {

	ApiDescribeInstanceMonitorDataHandlerReplyMsg describeInstanceMonitorData(ApiDescribeInstanceMonitorDataHandlerMsg msg) throws GCloudException, Exception;
	
	ApiDescribeDiskMonitorDataHandlerReplyMsg describeDiskMonitorData(ApiDescribeDiskMonitorDataHandlerMsg msg) throws GCloudException, Exception;
}
