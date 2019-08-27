package com.gcloud.controller;


import com.gcloud.common.util.StringUtils;
import com.gcloud.controller.log.model.LogFeedbackParams;
import com.gcloud.controller.log.service.ILogService;
import com.gcloud.core.annotations.CustomAnnotations.GcLog;
import com.gcloud.core.currentUser.policy.service.IResourceIsolationCheck;
import com.gcloud.core.exception.GCloudException;
import com.gcloud.core.handle.AsyncMessageHandler;
import com.gcloud.core.handle.MessageHandler;
import com.gcloud.core.handle.MessageHandlerKeeper;
import com.gcloud.core.service.SpringUtil;
import com.gcloud.header.ApiMessage;
import com.gcloud.header.GMessage;
import com.gcloud.header.NeedReplyMessage;
import com.gcloud.header.NodeMessage;
import com.gcloud.header.ReplyMessage;
import com.rabbitmq.client.Channel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.util.Date;
import java.util.List;

@Component
@Slf4j
public class MessageReceiver {
    @Autowired
    ILogService logService;

    @Value("${spring.rabbitmq.listener.simple.acknowledge-mode:}")
    private String ackMode;

    @RabbitListener(queues = "${gcloud.service.controller}")
    @RabbitHandler
    public ReplyMessage process(GMessage message, Message msg, Channel channel) {
    	Date startTime = new Date();
        MessageHandler handler = null;
        try {
            if(ackMode != null && "manual".equals(ackMode)){
                channel.basicAck(msg.getMessageProperties().getDeliveryTag(), false);
            }
            handler = MessageHandlerKeeper.get(message.getClass().getName());
            if(handler == null){
                throw new GCloudException("handler not found");
            }
            
            //租户资源隔离处理
            if(message instanceof ApiMessage) {
	            ResourceIsolationCheck[] checks = handler.getClass().getAnnotationsByType(ResourceIsolationCheck.class);
	            for(ResourceIsolationCheck check:checks) {
	            	Method method = message.getClass().getMethod(StringUtils.makeGetMethod(check.resourceIdField())); // 父类对象调用子类方法(反射原理)
	            	Object obj = method.invoke(message);
	            	if(obj != null) {
		            	IResourceIsolationCheck checkService = (IResourceIsolationCheck)SpringUtil.getBean(check.resourceIsolationCheckType().getCheckClazz());
		            	//只支持List<String> 和 String
		            	if(obj instanceof List) {
		            		List<String> ids = (List<String>)obj;
		            		for(String id:ids) {
		            			checkService.check(id, ((ApiMessage) message).getCurrentUser());
		            		}
		            	} else {
		            		checkService.check((String)obj, ((ApiMessage) message).getCurrentUser());
		            	}
	            	}
	            }
            }

            ReplyMessage reply = handler.handle(message);
            GcLog gcLog = (GcLog)handler.getClass().getAnnotation(GcLog.class);
            if (null != gcLog) {
            	if (gcLog.isMultiLog()) {
                    //线程池异步执行
            		logService.recordMultiLog(message, handler, null, startTime, reply);
                } else {
                    logService.recordLog(message, handler, null, startTime);
                }    
            }

            return reply;
        } catch (Throwable e) {
            log.error(String.format("message handle error, ex=%s, message=%s, msgId=%s, serviceId=%s, messageClass=%s", e.toString(), e.getMessage(), message.getMsgId(), message.getServiceId(), message.getClass()), e);

            if(message instanceof NeedReplyMessage){

                Class replyClass = ((NeedReplyMessage)message).replyClazz();
                if(replyClass != null && ReplyMessage.class.isAssignableFrom(replyClass)){

                    try{
                        String errorMsg = "api_controller_error::system error";
                        if(e instanceof GCloudException){
                            errorMsg = e.getMessage();
                        }

                        ReplyMessage replyMessage = (ReplyMessage)replyClass.newInstance();
                        replyMessage.setSuccess(false);
                        replyMessage.setErrorMsg(errorMsg);
                        if(e instanceof GCloudException && ((GCloudException) e).getParams() != null){
                            replyMessage.setErrorParam(((GCloudException) e).getParams());
                        }
                        return replyMessage;

                    }catch (Exception nex){
                        log.error("new api reply instance error", nex);
                    }
                }
            }
        }
        return null;
    }

    @RabbitListener(queues = "${gcloud.service.controller}" + "_async")
    @RabbitHandler
    public void proccessAsync(GMessage message, Message msg, Channel channel) {
        try{
            if(ackMode != null && "manual".equals(ackMode)){
                channel.basicAck(msg.getMessageProperties().getDeliveryTag(), false);
            }
            AsyncMessageHandler handler = MessageHandlerKeeper.getAsyncHandler(message.getClass().getName());
            if(handler != null){
                handler.handle(message);
            }else{
                log.error(String.format("handler not found msgId=%s, serviceId=%s, messageClass=%s", message.getMsgId(), message.getServiceId(), message.getClass()));
            }
        }catch (Throwable ex){
            log.error(String.format("message handle error, ex=%s, message=%s, msgId=%s, serviceId=%s, messageClass=%s", ex.toString(), ex.getMessage(), message.getMsgId(), message.getServiceId(), message.getClass()), ex);
        }

        try{
            if(message instanceof NodeMessage && StringUtils.isNotBlank(message.getTaskId())) {
                NodeMessage nodemsg = (NodeMessage)message;
                LogFeedbackParams params = new LogFeedbackParams();
                params.setTaskId(nodemsg.getTaskId());
                params.setStatus(nodemsg.getSuccess()?"COMPLETE":"FAILED");
                params.setCode(nodemsg.getErrorCode());
                logService.feedback(params);
            }
        }catch (Exception ex){
            log.error("::feedback error", ex);
        }

    }


}