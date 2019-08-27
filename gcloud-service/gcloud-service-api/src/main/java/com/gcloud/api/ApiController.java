package com.gcloud.api;

import java.util.Set;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.gcloud.api.log.LogComponent;
import com.gcloud.common.util.StringUtils;
import com.gcloud.core.exception.GCloudException;
import com.gcloud.core.handle.MessageHandler;
import com.gcloud.core.handle.MessageHandlerKeeper;
import com.gcloud.core.handle.RequestHandler;
import com.gcloud.core.messagebus.MessageBus;
import com.gcloud.header.ApiMessage;
import com.gcloud.header.Module;
import com.gcloud.header.ReplyMessage;
import com.gcloud.header.log.LogRecordMsg;
import com.google.common.collect.Sets;

import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/")
@ResponseBody
@Slf4j
public class ApiController {
	private static Set<String> localModules = Sets.newHashSet(Module.USER.toString());
	
	@Value("${gcloud.service.controller}")
	private String serviceId;
	
	@Autowired
	MessageBus bus;
	
	@Autowired
	LogComponent logComponent;
	
	@RequestMapping(value = "/{module}/{action}")
	public JsonResult api(@PathVariable String module, @PathVariable String action, @Validated ApiMessage message, HttpServletRequest request) throws GCloudException{
		if(message!=null) {
			if (StringUtils.isBlank(message.getTaskId())) {
				message.setTaskId(UUID.randomUUID().toString());
	        }
			ReplyMessage replyMsg = null;
			if(localModules.contains(module)) {
				log.debug("ApiController api ,module=" + module + ",action=" + action + ", currentThread:" + Thread.currentThread().getId());
				MessageHandler handler = null;
			    handler = MessageHandlerKeeper.get(message.getClass().getName());
			    if(handler instanceof RequestHandler) {
			    	handler = (RequestHandler)handler;
			    	((RequestHandler) handler).setRequest(request);
			    }
		    	
			    try {
			    	replyMsg = handler.handle(message);
			    } catch(GCloudException ge) {
			    	logComponent.logRecord(serviceId, message, handler, ge);
			    	throw ge;
			    } catch(Exception e) {
			    	GCloudException gex = new GCloudException("api-local-handler-0001::系统异常，请联系管理员");
			    	logComponent.logRecord(serviceId, message, handler, gex);
			    	throw gex;
			    }
			    logComponent.logRecord(serviceId, message, handler, null);
			   
			} else {
				message.setServiceId(serviceId);
				replyMsg= bus.call(message, message.replyClazz());
			}
			return new JsonResult(replyMsg);
			
		}
		JsonResult result = new JsonResult();
		result.setSuccess(true);
		return result;
	}
}
