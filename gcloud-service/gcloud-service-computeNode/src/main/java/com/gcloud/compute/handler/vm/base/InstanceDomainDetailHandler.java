package com.gcloud.compute.handler.vm.base;

import com.gcloud.compute.virtual.IVmVirtual;
import com.gcloud.core.exception.GCloudException;
import com.gcloud.core.handle.Handler;
import com.gcloud.core.handle.MessageHandler;
import com.gcloud.header.compute.msg.node.vm.base.InstanceDomainDetailMsg;
import com.gcloud.header.compute.msg.node.vm.base.InstanceDomainDetailReplyMsg;
import com.gcloud.service.common.compute.model.DomainDetail;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

@Handler
@Slf4j
public class InstanceDomainDetailHandler extends MessageHandler<InstanceDomainDetailMsg, InstanceDomainDetailReplyMsg> {

	@Autowired
	private IVmVirtual vmVirtual;

	@Override
	public InstanceDomainDetailReplyMsg handle(InstanceDomainDetailMsg msg) throws GCloudException {
		InstanceDomainDetailReplyMsg reply = msg.deriveMsg(InstanceDomainDetailReplyMsg.class);
		DomainDetail detail = vmVirtual.getVmDetail(msg.getInstanceId());
		if(detail != null){
			reply.setCurrentMemory(detail.getCurrentMemory());
			reply.setMemory(detail.getMemory());
			reply.setVcpu(detail.getVcpu());
		}
		return reply;
	}
}
