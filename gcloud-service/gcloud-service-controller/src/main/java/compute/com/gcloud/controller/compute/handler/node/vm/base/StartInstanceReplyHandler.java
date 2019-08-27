package com.gcloud.controller.compute.handler.node.vm.base;

import com.gcloud.controller.compute.dao.InstanceDao;
import com.gcloud.controller.compute.dispatcher.Dispatcher;
import com.gcloud.controller.compute.entity.VmInstance;
import com.gcloud.core.handle.AsyncMessageHandler;
import com.gcloud.core.handle.Handler;
import com.gcloud.header.compute.enums.VmState;
import com.gcloud.header.compute.msg.node.vm.base.StartInstanceReplyMsg;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;

@Handler
@Slf4j
public class StartInstanceReplyHandler extends AsyncMessageHandler<StartInstanceReplyMsg>{

	@Autowired
	private InstanceDao vmInstanceDao;


	@Override
	public void handle(StartInstanceReplyMsg msg) {
		VmInstance vm = vmInstanceDao.getById(msg.getInstanceId());
		List<String> fields = new ArrayList<>();
		fields.add(vm.updateStepState(null));
		if(msg.getSuccess()){
			fields.add(vm.updateState(VmState.RUNNING.value()));
			vmInstanceDao.update(vm, fields);
		}else{

		    //开机失败，并且状态不是开机状态，则释放资源
		    if(!VmState.RUNNING.value().equals(msg.getCurrentState()) && msg.getHandleResource() != null && msg.getHandleResource()){
                try{
                    Dispatcher.dispatcher().release(vm.getHostname(), vm.getCore(), vm.getMemory());
                }catch (Exception ex){
                    log.error("开机失败，释放资源失败", ex);
                }
            }

			fields.add(vm.updateState(msg.getCurrentState()));
			vmInstanceDao.update(vm, fields);
		}

	}
}
