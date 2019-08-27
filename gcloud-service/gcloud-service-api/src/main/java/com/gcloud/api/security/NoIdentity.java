package com.gcloud.api.security;

import org.springframework.stereotype.Service;

import com.gcloud.header.identity.user.GetUserReplyMsg;
@Service
public class NoIdentity implements IApiIdentity {

	@Override
	public TokenUser checkToken(String token) {
		// TODO Auto-generated method stub
		TokenUser tokenUser=new TokenUser();
		tokenUser.setExpressTime(1877156686000l);//2029年
		tokenUser.setUserId("cc81efe719bf4b05a3c21524ccf6c1ea");
		return tokenUser;
	}

	@Override
	public SignUser getUserByAccessKey(String accessKey) {
		// TODO Auto-generated method stub
		SignUser signUser=new SignUser();
		signUser.setSecretKey("rgXooMaQAns9ki6SFlHps3O8flgY4TcPpQWwit0G");
		signUser.setUserId("cc81efe719bf4b05a3c21524ccf6c1ea");
		return signUser;
	}

	@Override
	public GetUserReplyMsg getUserById(String userId) {
		// TODO Auto-generated method stub
		GetUserReplyMsg reply=new GetUserReplyMsg();
		reply.setId("cc81efe719bf4b05a3c21524ccf6c1ea");
		reply.setDisable(false);
		reply.setEmail("gcloudtest01@gdeii.com.cn");
		reply.setLoginName("admin");
		reply.setRealName("admin");
		reply.setRoleId("superadmin");
		reply.setMobile("189xxxxxxxx");
		reply.setSuccess(true);
		return reply;
	}

}
