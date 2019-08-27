package com.gcloud.boot.config;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.context.annotation.Configuration;

import com.gcloud.core.handle.ApiHandler;
import com.gcloud.core.handle.ApiHandlerKeeper;
import com.gcloud.core.handle.AsyncMessageHandler;
import com.gcloud.core.handle.Handler;
import com.gcloud.core.handle.MessageHandler;
import com.gcloud.core.handle.MessageHandlerKeeper;
import com.gcloud.header.ApiVersion;
@Configuration
public class HandlerBeanPostProcessor implements BeanPostProcessor {

	@Override
	public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
		// TODO Auto-generated method stub
		Handler handler=bean.getClass().getAnnotation(Handler.class);
		ApiHandler apiHandler=bean.getClass().getAnnotation(ApiHandler.class);
		if(handler!=null||apiHandler!=null){
			String className= bean.getClass().getName().substring(0, bean.getClass().getName().indexOf("$$"));
			Class clazz = null;
			try {
				clazz = Class.forName(className);
			} catch (ClassNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			Type types = clazz.getGenericSuperclass();
	        Type[] genericType = ((ParameterizedType) types).getActualTypeArguments();
	        if(genericType.length>0){
	        	if(bean instanceof MessageHandler){
	        		MessageHandlerKeeper.put(genericType[0].getTypeName(), (MessageHandler)bean);
		        	System.out.println("register sync handler: "+ bean.getClass().getName());
	        	}else if(bean instanceof AsyncMessageHandler){
	        		MessageHandlerKeeper.put(genericType[0].getTypeName(), (AsyncMessageHandler)bean);
		        	System.out.println("register async handler: "+ bean.getClass().getName());
	        	}
	        	
	        } 
		}
		if(apiHandler!=null){
			for(ApiVersion version:apiHandler.versions()){
				ApiHandlerKeeper.put(version, apiHandler.module(), apiHandler.action(), (MessageHandler)bean);
			}
			
		}
		return bean;
	}

	@Override
	public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
		// TODO Auto-generated method stub
		return bean;
	}

}
