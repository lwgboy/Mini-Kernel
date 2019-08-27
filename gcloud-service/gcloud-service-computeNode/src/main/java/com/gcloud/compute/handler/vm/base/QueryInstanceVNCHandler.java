package com.gcloud.compute.handler.vm.base;

import com.gcloud.compute.prop.ComputeNodeProp;
import com.gcloud.compute.service.vm.base.IVmBaseNodeService;
import com.gcloud.core.exception.GCloudException;
import com.gcloud.core.handle.Handler;
import com.gcloud.core.handle.MessageHandler;
import com.gcloud.header.compute.msg.node.vm.base.QueryInstanceVNCMsg;
import com.gcloud.header.compute.msg.node.vm.base.QueryInstanceVNCMsgReply;
import org.springframework.beans.factory.annotation.Autowired;

@Handler
public class QueryInstanceVNCHandler extends MessageHandler<QueryInstanceVNCMsg, QueryInstanceVNCMsgReply> {

    @Autowired
    private IVmBaseNodeService vmBaseNodeService;

    @Autowired
    private ComputeNodeProp prop;

    @Override
    public QueryInstanceVNCMsgReply handle(QueryInstanceVNCMsg msg) throws GCloudException {
        QueryInstanceVNCMsgReply reply = new QueryInstanceVNCMsgReply();
        String port = vmBaseNodeService.queryVnc(msg.getInstanceId());

        reply.setSuccess(true);
        reply.setPort(port);
        reply.setHostIp(prop.getNodeIp());
        return reply;
    }
}
