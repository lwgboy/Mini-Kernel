package com.gcloud.controller.network.handler.node.bridge;

import com.gcloud.controller.network.dao.OvsBridgeDao;
import com.gcloud.controller.network.entity.OvsBridge;
import com.gcloud.controller.network.enums.OvsBridgeState;
import com.gcloud.core.handle.AsyncMessageHandler;
import com.gcloud.core.handle.Handler;
import com.gcloud.header.network.msg.node.bridge.CreateOvsBridgeReplyMsg;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;

@Handler
@Slf4j
public class CreateOvsBridgeReplyHandler extends AsyncMessageHandler<CreateOvsBridgeReplyMsg> {

    @Autowired
    private OvsBridgeDao ovsBridgeDao;

    @Override
    public void handle(CreateOvsBridgeReplyMsg msg) {

        OvsBridge ovsBridge = new OvsBridge();
        ovsBridge.setId(msg.getId());

        List<String> fields = new ArrayList<>();

        if(msg.getSuccess()){
            fields.add(ovsBridge.updateState(OvsBridgeState.AVAILABLE.value()));
            ovsBridgeDao.update(ovsBridge, fields);
        }else{
            ovsBridgeDao.deleteById(msg.getId());
        }

    }
}
