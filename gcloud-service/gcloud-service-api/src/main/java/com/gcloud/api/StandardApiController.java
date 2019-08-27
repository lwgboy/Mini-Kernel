package com.gcloud.api;

import com.gcloud.api.log.LogComponent;
import com.gcloud.api.security.HttpRequestConstant;
import com.gcloud.common.util.StringUtils;
import com.gcloud.core.exception.GCloudException;
import com.gcloud.core.handle.MessageHandler;
import com.gcloud.core.handle.MessageHandlerKeeper;
import com.gcloud.core.handle.RequestHandler;
import com.gcloud.core.messagebus.MessageBus;
import com.gcloud.core.service.ServiceModuleMapping;
import com.gcloud.header.ApiMessage;
import com.gcloud.header.ApiReplyMessage;
import com.gcloud.header.Module;
import com.google.common.collect.Sets;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.Set;
import java.util.UUID;

@RestController
@RequestMapping("/")
@ResponseBody
@Slf4j
public class StandardApiController {
	//private static Set<String> localModules = Sets.newHashSet(Module.USER.toString());
	
	//@Value("${gcloud.service.controller}")
	//private String serviceId;
	
	@Autowired
	ServiceModuleMapping serviceModuleMapping;
	@Autowired
	MessageBus bus;
	
	@Autowired
	LogComponent logComponent;

	@RequestMapping(value = "/{module}/{action}.do")
	public ResponseEntity api(@PathVariable String module, @PathVariable String action, @Validated ApiMessage message, HttpServletRequest request) throws GCloudException{

		if(message!=null) {
			if (StringUtils.isBlank(message.getTaskId())) {
				message.setTaskId(UUID.randomUUID().toString());
	        }
			ApiReplyMessage replyMsg = null;
			/*if(localModules.contains(module.toUpperCase())) {
				log.debug("ApiController api ,module=" + module + ",action=" + action + ", currentThread:" + Thread.currentThread().getId());
				MessageHandler handler = null;
			    handler = MessageHandlerKeeper.get(message.getClass().getName());
			    if(handler instanceof RequestHandler) {
			    	handler = (RequestHandler)handler;
			    	((RequestHandler) handler).setRequest(request);
			    }
			    try {
			    	replyMsg = (ApiReplyMessage)handler.handle(message);
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
				replyMsg = (ApiReplyMessage) bus.call(message);
			}
			*/
			message.setServiceId(serviceModuleMapping.getService(module.toUpperCase()));
			replyMsg = (ApiReplyMessage) bus.call(message);
			if(replyMsg == null){
				throw new GCloudException("::error");
			}else{
				if(replyMsg.getSuccess() != null && !replyMsg.getSuccess()){
					throw new GCloudException(replyMsg.getErrorMsg(), replyMsg.getErrorParam());
				}else if(StringUtils.isBlank(replyMsg.getRequestId())){
					String requestId = ObjectUtils.toString(request.getAttribute(HttpRequestConstant.ATTR_REQUEST_ID), null);
					replyMsg.setRequestId(requestId);
				}

				return new ResponseEntity(replyMsg, HttpStatus.OK);
			}

		}else{
			throw new GCloudException("::api not found");
		}
	}
}
