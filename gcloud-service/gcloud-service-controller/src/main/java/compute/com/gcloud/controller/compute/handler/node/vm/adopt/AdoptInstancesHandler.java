package com.gcloud.controller.compute.handler.node.vm.adopt;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.gcloud.core.handle.AsyncMessageHandler;
import com.gcloud.core.handle.Handler;
import com.gcloud.header.compute.msg.node.vm.adopt.AdoptInstanceMsg;
import lombok.extern.slf4j.Slf4j;

@Handler
@Slf4j
public class AdoptInstancesHandler extends AsyncMessageHandler<AdoptInstanceMsg>{



	@Override
	public void handle(AdoptInstanceMsg msg) {

		log.debug("===adopt==", JSONObject.toJSONString(msg, SerializerFeature.WriteMapNullValue, SerializerFeature.PrettyFormat));

	}
}
