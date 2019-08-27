package com.gcloud.controller.monitor.service.impl;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.gcloud.controller.monitor.enums.MonitorMeters;
import com.gcloud.controller.monitor.model.Statistics;
import com.gcloud.controller.monitor.model.StatisticsPoint;
import com.gcloud.controller.monitor.service.IMonitorService;
import com.gcloud.controller.provider.MonitorProviderProxy;
import com.gcloud.controller.storage.dao.VolumeAttachmentDao;
import com.gcloud.controller.storage.entity.VolumeAttachment;
import com.gcloud.core.condition.ConditionalMonitor;
import com.gcloud.core.exception.GCloudException;
import com.gcloud.header.monitor.model.DescribeDiskMonitorDataResponse;
import com.gcloud.header.monitor.model.DescribeInstanceMonitorDataResponse;
import com.gcloud.header.monitor.model.DiskMonitorDataType;
import com.gcloud.header.monitor.model.InstanceMonitorDataType;
import com.gcloud.header.monitor.msg.api.ApiDescribeDiskMonitorDataHandlerMsg;
import com.gcloud.header.monitor.msg.api.ApiDescribeDiskMonitorDataHandlerReplyMsg;
import com.gcloud.header.monitor.msg.api.ApiDescribeInstanceMonitorDataHandlerMsg;
import com.gcloud.header.monitor.msg.api.ApiDescribeInstanceMonitorDataHandlerReplyMsg;


@ConditionalMonitor
@Transactional(propagation = Propagation.REQUIRES_NEW)
public class MonitorServiceImpl implements IMonitorService {

	@Autowired
	private MonitorProviderProxy monitorProviderProxy;
	
	@Autowired
	private VolumeAttachmentDao volumeAttachmentDao;
	
	private static final SimpleDateFormat monitorSdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	private static final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
	
	@Override
	public ApiDescribeInstanceMonitorDataHandlerReplyMsg describeInstanceMonitorData(ApiDescribeInstanceMonitorDataHandlerMsg msg) throws GCloudException, Exception {
		ApiDescribeInstanceMonitorDataHandlerReplyMsg reply = new ApiDescribeInstanceMonitorDataHandlerReplyMsg();
		
		Date startTime = sdf.parse(msg.getStartTime());
		Date endTime = sdf.parse(msg.getEndTime());
		
		String monitorBeginTime = monitorSdf.format(startTime);
		String monitorEndTime = monitorSdf.format(endTime);
		
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("resourceId", msg.getInstanceId());
		params.put("beginTime", monitorBeginTime);
		params.put("endTime", monitorEndTime);
		String interval = "1m";
		if (msg.getPeriod() != null) {
			if (Integer.valueOf(msg.getPeriod()) < 600) {
				interval = "1m";
			} else if (Integer.valueOf(msg.getPeriod()) < 3600) {
				interval = "10m";
			} else {
				interval = "1h";
			}
		}
		params.put("interval", interval);
		
		//cpu使用率
		params.put("meter", MonitorMeters.VM_CPU_UTIL.getValue());
		Statistics cpu = monitorProviderProxy.statistics(params);
		Map<String, Double> cpuStatByDate = new HashMap<String, Double>();
		if(cpu.getList() != null && cpu.getList().size() > 0){
			List<StatisticsPoint> cpuPoints = cpu.getList().get(0).getPoints();
			if(cpuPoints != null && cpuPoints.size() > 0){
				for(StatisticsPoint point : cpuPoints){
					cpuStatByDate.put(point.getTime(), point.getValue());
				}
			}
		}
		
		//磁盘读速度
		params.put("meter", MonitorMeters.VM_DISK_READ_RATE.getValue());
		Statistics diskRead = monitorProviderProxy.statistics(params);
		Map<String, Double> diskReadByDate = new HashMap<String, Double>();
		if(diskRead.getList() != null && diskRead.getList().size() > 0){
			List<StatisticsPoint> diskReadPoints = diskRead.getList().get(0).getPoints();
			if(diskReadPoints != null && diskReadPoints.size() > 0){
				for(StatisticsPoint point : diskReadPoints){
					diskReadByDate.put(point.getTime(), point.getValue());
				}
			}
		}
		
		//磁盘写速度
		params.put("meter", MonitorMeters.VM_DISK_WRITE_RATE.getValue());
		Statistics diskWrite = monitorProviderProxy.statistics(params);
		Map<String, Double> diskWriteByDate = new HashMap<String, Double>();
		if(diskWrite.getList() != null && diskWrite.getList().size() > 0){
			List<StatisticsPoint> diskWritePoints = diskWrite.getList().get(0).getPoints();
			if(diskWritePoints != null && diskWritePoints.size() > 0){
				for(StatisticsPoint point : diskWritePoints){
					diskWriteByDate.put(point.getTime(), point.getValue());
				}
			}
		}
		
		//网卡上行流量
		params.put("meter", MonitorMeters.VM_NIC_TX_RATE.getValue());
		Statistics intranetTX = monitorProviderProxy.statistics(params);
		Map<String, Double>intranetTXByDate = new HashMap<String, Double>();
		if(intranetTX.getList() != null && intranetTX.getList().size() > 0){
			List<StatisticsPoint> intranetTXPoints = intranetTX.getList().get(0).getPoints();
			if(intranetTXPoints != null && intranetTXPoints.size() > 0){
				for(StatisticsPoint point : intranetTXPoints){
					intranetTXByDate.put(point.getTime(), point.getValue());
				}
			}
		}
		
		//网卡下行流量
		params.put("meter", MonitorMeters.VM_NIC_RX_RATE.getValue());
		Statistics intranetRX = monitorProviderProxy.statistics(params);
		Map<String, Double> intranetRXByDate = new HashMap<String, Double>();
		if(intranetRX.getList() != null && intranetRX.getList().size() > 0){
			List<StatisticsPoint> intranetRXPoints = intranetRX.getList().get(0).getPoints();
			if(intranetRXPoints != null && intranetRXPoints.size() > 0){
				for(StatisticsPoint point : intranetRXPoints){
					intranetRXByDate.put(point.getTime(), point.getValue());
				}
			}
		}
		
		List<InstanceMonitorDataType> instanceMonitorDataTypeList = new ArrayList<>();
		for (String timeStamp : cpuStatByDate.keySet()) {
			InstanceMonitorDataType instanceMonitorData = new InstanceMonitorDataType();
			instanceMonitorData.setTimeStamp(timeStamp);
			instanceMonitorData.setInstanceId(msg.getInstanceId());
			
			if(cpuStatByDate.get(timeStamp) != null){
				instanceMonitorData.setCpu(cpuStatByDate.get(timeStamp).intValue());
			}
			if(intranetRXByDate.get(timeStamp) != null){
				instanceMonitorData.setIntranetRX(intranetRXByDate.get(timeStamp).intValue());
			}
			if(intranetTXByDate.get(timeStamp) != null){
				instanceMonitorData.setIntranetTX(intranetTXByDate.get(timeStamp).intValue());
			}
			if(diskWriteByDate.get(timeStamp) != null){
				instanceMonitorData.setBpsRead(diskWriteByDate.get(timeStamp).intValue());
			}
			if(diskReadByDate.get(timeStamp) != null){
				instanceMonitorData.setBpsWrite(diskReadByDate.get(timeStamp).intValue());
			}
			
			instanceMonitorDataTypeList.add(instanceMonitorData);
		}
		
		DescribeInstanceMonitorDataResponse monitorData = new DescribeInstanceMonitorDataResponse();
		monitorData.setInstanceMonitorData(instanceMonitorDataTypeList);
		reply.setMonitorData(monitorData);
		
		return reply;
	}

	@Override
	public ApiDescribeDiskMonitorDataHandlerReplyMsg describeDiskMonitorData(ApiDescribeDiskMonitorDataHandlerMsg msg) throws GCloudException, Exception{
		
		Date startTime = sdf.parse(msg.getStartTime());
		Date endTime = sdf.parse(msg.getEndTime());
		
		String monitorBeginTime = monitorSdf.format(startTime);
		String monitorEndTime = monitorSdf.format(endTime);
		
		List<VolumeAttachment> volumeAttachment = volumeAttachmentDao.findByProperty("volume_id", msg.getDiskId());
		
		String vmId = volumeAttachment.get(0).getInstanceUuid();
		
		String device = volumeAttachment.get(0).getMountpoint();
		String[] res = StringUtils.split(device, "/");
		device = res[1];
		
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("resourceId", vmId);
		params.put("instance", device);
		params.put("beginTime", monitorBeginTime);
		params.put("endTime", monitorEndTime);
		
		String interval = "1m";
		if (msg.getPeriod() != null) {
			if (Integer.valueOf(msg.getPeriod()) < 600) {
				interval = "1m";
			} else if (Integer.valueOf(msg.getPeriod()) < 3600) {
				interval = "10m";
			} else {
				interval = "1h";
			}
		}
		params.put("interval", interval);
		
		//磁盘写速度
		params.put("meter", MonitorMeters.VM_DISK_WRITE_RATE.getValue());
		Statistics diskWrite = monitorProviderProxy.statistics(params);
		Map<String, Double> diskWriteByDate = new HashMap<String, Double>();
		if(diskWrite.getList() != null && diskWrite.getList().size() >0){
			List<StatisticsPoint> diskWritePoints = diskWrite.getList().get(0).getPoints();
			if(diskWritePoints != null &&diskWritePoints.size() > 0){
				for(StatisticsPoint point : diskWritePoints){
					diskWriteByDate.put(point.getTime(), point.getValue());
				}
			}
		}
		
		//磁盘读速度
		params.put("meter", MonitorMeters.VM_DISK_READ_RATE.getValue());
		Statistics diskRead = monitorProviderProxy.statistics(params);
		Map<String, Double> diskReadByDate = new HashMap<String, Double>();
		if(diskRead.getList() != null && diskRead.getList().size() >0){
			List<StatisticsPoint> diskReadPoints = diskRead.getList().get(0).getPoints();
			if(diskReadPoints != null && diskReadPoints.size() > 0){
				for(StatisticsPoint point : diskReadPoints){
					diskReadByDate.put(point.getTime(), point.getValue());
				}
			}
		}
		
		ApiDescribeDiskMonitorDataHandlerReplyMsg reply = new ApiDescribeDiskMonitorDataHandlerReplyMsg();

		List<DiskMonitorDataType> diskMonitorDataTypeList = new ArrayList<>();
		for (String timeStamp : diskWriteByDate.keySet()) {
			DiskMonitorDataType diskMonitorData = new DiskMonitorDataType();
			diskMonitorData.setTimeStamp(timeStamp);
			
			if(diskReadByDate.get(timeStamp) != null){
				diskMonitorData.setBpsRead(diskReadByDate.get(timeStamp).intValue());
			}
			if(diskWriteByDate.get(timeStamp) != null){
				diskMonitorData.setBpsWrite(diskWriteByDate.get(timeStamp).intValue());
			}
			if(diskReadByDate.get(timeStamp) != null && diskWriteByDate.get(timeStamp) != null){
				diskMonitorData.setBpsTotal(diskReadByDate.get(timeStamp).intValue() + diskWriteByDate.get(timeStamp).intValue());
			}
			diskMonitorData.setDiskId(msg.getDiskId());
			
			diskMonitorDataTypeList.add(diskMonitorData);
		}
		
		DescribeDiskMonitorDataResponse monitorData = new DescribeDiskMonitorDataResponse();
		
		monitorData.setDiskMonitorDataType(diskMonitorDataTypeList);
		
		reply.setMonitorData(monitorData);
		
		return reply;
	}


}
