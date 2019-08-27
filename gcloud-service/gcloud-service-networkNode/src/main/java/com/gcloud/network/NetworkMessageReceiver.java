package com.gcloud.network;

import com.gcloud.core.exception.GCloudException;
import com.gcloud.core.handle.AsyncMessageHandler;
import com.gcloud.core.handle.MessageHandler;
import com.gcloud.core.handle.MessageHandlerKeeper;
import com.gcloud.header.GMessage;
import com.gcloud.header.NeedReplyMessage;
import com.gcloud.header.ReplyMessage;
import com.rabbitmq.client.Channel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class NetworkMessageReceiver {

    @Value("${spring.rabbitmq.listener.simple.acknowledge-mode:}")
    private String ackMode;

    @RabbitListener(queues = "${gcloud.service.networkNode}")
    @RabbitHandler
    public ReplyMessage process(GMessage message, Message msg, Channel channel) {
        MessageHandler handler = null;
        try {
            if(ackMode != null && "manual".equals(ackMode)){
                channel.basicAck(msg.getMessageProperties().getDeliveryTag(), false);
            }
            handler = MessageHandlerKeeper.get(message.getClass().getName());
            if(handler == null){
                throw new GCloudException("handler not found");
            }

            return handler.handle(message);
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

    @RabbitListener(queues = "${gcloud.service.networkNode}" + "_async")
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
    }


}