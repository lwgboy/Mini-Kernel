package com.gcloud.controller.compute.handler.node.vm.base;

import com.gcloud.controller.compute.dao.InstanceDao;
import com.gcloud.controller.compute.dispatcher.Dispatcher;
import com.gcloud.controller.compute.entity.VmInstance;
import com.gcloud.core.handle.AsyncMessageHandler;
import com.gcloud.core.handle.Handler;
import com.gcloud.header.compute.enums.VmState;
import com.gcloud.header.compute.msg.node.vm.base.DestroyInstanceReplyMsg;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;

@Handler
@Slf4j
public class DestroyInstanceReplyHandler extends AsyncMessageHandler<DestroyInstanceReplyMsg>{

	@Autowired
	private InstanceDao vmInstanceDao;


	@Override
	public void handle(DestroyInstanceReplyMsg msg) {
		VmInstance vm = vmInstanceDao.getById(msg.getInstanceId());
		List<String> fields = new ArrayList<>();
		fields.add(vm.updateStepState(null));
		if(msg.getSuccess()){
			fields.add(vm.updateState(VmState.STOPPED.value()));
			vmInstanceDao.update(vm, fields);

			//关机成功，释放资源
			Dispatcher.dispatcher().release(vm.getHostname(), vm.getCore(), vm.getMemory());

		}else{

            //已经不是开机，也需要释放资源
            if(!VmState.RUNNING.value().equals(msg.getCurrentState()) && msg.getHandleResource() != null && msg.getHandleResource()){
                try{
                    Dispatcher.dispatcher().release(vm.getHostname(), vm.getCore(), vm.getMemory());
                }catch (Exception ex){
                    log.error("释放资源是吧", ex);
                }
            }

			fields.add(vm.updateState(msg.getCurrentState()));
			vmInstanceDao.update(vm, fields);
		}

	}
}
