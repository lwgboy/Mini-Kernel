package com.gcloud.api.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gcloud.api.ApiIdentityConfig;
import com.gcloud.core.cache.container.CacheContainer;
import com.gcloud.core.cache.enums.CacheType;
import com.gcloud.core.messagebus.MessageBus;
import com.gcloud.core.service.ServiceName;
import com.gcloud.header.identity.user.CheckTokenMsg;
import com.gcloud.header.identity.user.CheckTokenReplyMsg;
import com.gcloud.header.identity.user.GetUserByAccessKeyMsg;
import com.gcloud.header.identity.user.GetUserByAccessKeyReplyMsg;
import com.gcloud.header.identity.user.GetUserMsg;
import com.gcloud.header.identity.user.GetUserReplyMsg;

public class MqIdentity implements IApiIdentity {
	@Autowired
	MessageBus bus;
	@Autowired
	ServiceName serviceName;
	@Autowired
	ApiIdentityConfig identityConfig;
	public TokenUser checkToken(String token) {
		CheckTokenMsg msg=new CheckTokenMsg();
		msg.setToken(token);
		msg.setServiceId(serviceName.getIdentity());
		CheckTokenReplyMsg reply=bus.call(msg,CheckTokenReplyMsg.class);
		if(reply.getUserId()==null)
			return null;
		TokenUser tokenUser=new TokenUser();
		tokenUser.setExpressTime(reply.getExpressTime());
		tokenUser.setUserId(reply.getUserId());
		CacheContainer.getInstance().put(CacheType.TOKEN_USER,token,tokenUser);
		return tokenUser;
	}
	
	public SignUser getUserByAccessKey(String accessKey) {
		GetUserByAccessKeyMsg msg=new GetUserByAccessKeyMsg();
		msg.setAccessKey(accessKey);
		msg.setServiceId(serviceName.getIdentity());
		GetUserByAccessKeyReplyMsg reply=  bus.call(msg,GetUserByAccessKeyReplyMsg.class);
		if(reply.getUserId()==null)
			return null;
		SignUser signUser=new SignUser();
		signUser.setSecretKey(reply.getSecretKey());
		signUser.setUserId(reply.getUserId());
		CacheContainer.getInstance().put(CacheType.SIGN_USER,accessKey,signUser);
		return signUser;
	}
	
	public GetUserReplyMsg getUserById(String userId) {
		GetUserMsg msg=new GetUserMsg();
		msg.setId(userId);
		msg.setServiceId(serviceName.getIdentity());
		GetUserReplyMsg reply=bus.call(msg,GetUserReplyMsg.class);
		return reply;
	}
}
